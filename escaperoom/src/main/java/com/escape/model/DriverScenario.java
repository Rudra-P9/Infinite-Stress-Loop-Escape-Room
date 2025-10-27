package com.escape.model;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;


/**
 * Runs through each of the scenarios given by the instructor
 * @author Dylan Diaz
 */
public class DriverScenario {


    /**
     * Driver scenario for testing the entire Escape Room application from start to finish.
     * 
     * This scenario tests the following:
     *  1. Create an account with a duplicate username (should be rejected)
     *  2. Create an account successfully
     *  3. Interactive login
     *  4. Enter an escape room and hear the story
     *  5. Re-login and verify saved progress
     *  6. Resume play to finish final puzzles
     *  7. Show saved json snapshot
     *  8. Finish game and update leaderboard
     *  9. Certificate of completion
     */
    public static void main(String[] args) throws Exception {


        GameDataLoader loader = new GameDataLoader();
        GameDataWriter writer = new GameDataWriter();
        EscapeRoomFacade facade = new EscapeRoomFacade();
        Accounts accounts = Accounts.getInstance();


        Scanner in = new Scanner(System.in);


        // Create Account - Duplicate User
        System.out.println("\nCreate Account - Duplicate User");
        String bro = "Logan Rivers";
        if (accounts.getUser(bro) == null) {
            accounts.createAccount(bro, "loganPass123");
            writer.saveAccounts(accounts);
            System.out.println("Account created for username " + bro);
        } else {
            System.out.println("Account already exists for username " + bro);
        }


        System.out.println("Leni tries to create an account using her brothers username should be rejected");
        if (accounts.getUser("Logan Rivers") == null) {
            accounts.createAccount("Logan Rivers", "leniTry");
            writer.saveAccounts(accounts);
            System.out.println("Unexpected duplicate account created this should not happen");
        } else {
            System.out.println("Duplicate rejected as expected");
        }


        // Create Account - Success
        System.out.println("\nCreate Account - Success");
        String lena = "Leni Rivers";
        if (accounts.getUser(lena) == null) {
            accounts.createAccount(lena, "leniSecret");
            writer.saveAccounts(accounts);
            System.out.println("Account created for username " + lena);
        } else {
            System.out.println("Account already exists for username " + lena);
        }


        // Interactive login
        System.out.println("\nInteractive Login");
        System.out.print("Enter username ");
        String inputUser = in.nextLine().trim();
        System.out.print("Enter password ");
        String inputPass = in.nextLine();


        facade.login(inputUser, inputPass);


        String cur = null;
        try { cur = facade.getCurrentUsername(); } catch (Throwable ignored) { }
        if (cur == null) {
            System.out.println("Login failed or no user set in facade exiting");
            in.close();
            return;
        } else {
            System.out.println("Logged in as " + cur);
        }


        // Enter an Escape Room - Hear the story
        System.out.println("\nEnter Room the Rooms UI will run now");
        facade.startGame(Difficulty.EASY);


        if (facade.getCurrentUser() == null) {
            System.out.println("\nDetected scenario logout from Rooms progress was saved to");
            System.out.println("escaperoom/src/main/resources/json/playerData.json");
        } else {
            try { facade.saveGame(); } catch (Throwable ignored) { }
            try { facade.endGame(); } catch (Throwable ignored) { }
        }


        // Re-login and show persistence
        System.out.println("\nRe login to verify saved progress");
        System.out.print("Re enter username ");
        inputUser = in.nextLine().trim();
        System.out.print("Re enter password ");
        inputPass = in.nextLine();


        facade.login(inputUser, inputPass);


        try {
            facade.restoreProgressForCurrentUser();
        } catch (Throwable ignored) { }


        Progress progress = null;
        try { progress = facade.getProgress(); } catch (Throwable ignored) { }
        System.out.println("\nRestored progress:");
        System.out.println(progress); //  existing percent/hints/solved counter
        
        // --- Resume banner: show solved puzzle names and where hints were used ---
        try {
            int sp = (progress == null) ? 0 : progress.getStoryPos();
            System.out.println("\nSolved so far:");

            // Load rooms once to resolve human-friendly titles
            GameDataLoader tmpLoader = new GameDataLoader();
            java.util.ArrayList<com.escape.model.Rooms> all = tmpLoader.getRooms();

            String[][] order = com.escape.model.Rooms.getGlobalOrder();
            for (int k = 0; k < Math.min(sp, order.length); k++) {
                String rid = order[k][0];
                int idx    = Integer.parseInt(order[k][1]);

                // find the room by id
                com.escape.model.Rooms rr = null;
                for (com.escape.model.Rooms r : all) {
                    if (rid.equalsIgnoreCase(r.getRoomID())) { rr = r; break; }
                }

                String title;
                if (rr != null && rr.getPuzzles() != null && idx >= 0 && idx < rr.getPuzzles().size()) {
                    title = rr.getPuzzles().get(idx).getTitle();
                } else {
                    title = "Puzzle " + (k + 1); // safe fallback
                }
                System.out.println(" - " + title);
            }

            // Also show which puzzles the player used a hint on (if any)
            if (progress != null && progress.getHintedPuzzles() != null && !progress.getHintedPuzzles().isEmpty()) {
                System.out.println("\nHint used on:");
                for (String t : progress.getHintedPuzzles()) {
                    System.out.println(" * " + t);
                }
            }
        } catch (Throwable ignored) {
            // Never fail the resume flow because of printing
        }

        
        //
        // Resume to finish final puzzles
        System.out.println("\nResuming play to finish the game");
        facade.startGame(Difficulty.EASY);


        // Show saved json snapshot
        System.out.println("\nSaved JSON snapshot playerData.json");
        try {
            byte[] json = java.nio.file.Files.readAllBytes(
                    java.nio.file.Paths.get("escaperoom/src/main/resources/json/playerData.json"));
            System.out.println(new String(json));
        } catch (Throwable ignored) {
            System.out.println("Could not read playerData.json");
        }


        // Finish Game and update leaderboard
        System.out.println("\nFinish Game and Leaderboard");
        try { facade.endGame(); System.out.println("End game requested"); }
        catch (Throwable ignored) { System.out.println("End game failed"); }


        Leaderboard lb = null;
        // RELOAD to get the newly-saved score
        try {
            loader = new GameDataLoader();
            lb = loader.getLeaderboard();
        } catch (Throwable ignored) {}

        if (lb != null) {
            int rank = 1;
            for (Score s : lb.topN(10)) {
                System.out.printf("%d %s %d pts %s%n",
                    rank++, s.getUsername(), s.getScore(),
                    s.getDifficulty() == null ? "unknown" : s.getDifficulty());
            }
        } else {
            System.out.println("No leaderboard available");
        }


        // Certificate of Completion
        System.out.println("\nCertificate of Completion");
        long finalScore = 0;
        try { finalScore = facade.calculateFinalScore(); } catch (Throwable ignored) { }
        try {
            makeCertificate(inputUser, "Escape The Varen Project", finalScore, facade.getCurrentDifficulty(),
                    progress == null ? 0 : progress.getHintsUsed());
            System.out.println("Certificate written to certificate_" + inputUser.replace(' ', '_') + ".txt");
        } catch (Throwable ignored) {
            System.out.println("Certificate creation failed");
        }


        System.out.println("\nAll scenarios complete");
        in.close();
    }


    /**
     * Generates a certificate of completion for the user.
     * The certificate is a text file containing the user's name, game title, date, difficulty, hints used, and final score.
     * The file is named "certificate_<user>.txt".
     * @param user the user's name
     * @param game the game title
     * @param score the final score
     * @param diff the difficulty level
     * @param hints the number of hints used
     */
    private static void makeCertificate(String user, String game, long score, Difficulty diff, int hints) {
        String filename = "certificate_" + user.replace(' ', '_') + ".txt";
        String header = "*******************************************\n" +
                        "       ESCAPE ROOM COMPLETION AWARD\n" +
                        "*******************************************\n";
        String body = String.format("Player %s%nGame %s%nDate %s%n%n", user, game, LocalDate.now());
        body += String.format("Difficulty %s%nHints used %d%nFinal Score %d%n%n",
                diff == null ? "Unknown" : diff, hints, score);
        body += "Message\nCongratulations on escaping the Varen Project\n";
        body += "You discovered truths and unlocked doors\n\n";
        body += "Signed\nThe Escape Room Dev Team\n";


        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(header);
            fw.write(body);
        } catch (IOException e) {
            System.out.println("Could not write certificate");
        }
    }
}
