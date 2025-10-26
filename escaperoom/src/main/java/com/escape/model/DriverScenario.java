package com.escape.model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Runs through each of the scenarios given by the instructor
 * @author Dylan Diaz
 */
public class DriverScenario {

/**
 * Main driver for the Escape Room project.
 * Runs through each of the scenarios given by the instructor to demonstrate the capabilities of the Escape Room system.
 * 
 * @author Dylan Diaz
 */
    public static void main(String[] args) throws Exception {

        GameDataLoader loader = new GameDataLoader();
        GameDataWriter writer = new GameDataWriter();
        EscapeRoomFacade facade = new EscapeRoomFacade();
        Accounts accounts = Accounts.getInstance();

        // duplicate account attempt
        System.out.println("\nCreate Account Duplicate User");
        String bro = "Logan Rivers";
        if (accounts.getUser(bro) == null) {
            accounts.createAccount(bro, "loganPass123");
            writer.saveAccounts(accounts);
            System.out.println("Account created for username " + bro);
        } else {
            System.out.println("Account already exists for username " + bro);
        }

        System.out.println("Leni tries to create an account with her brothers name");
        if (accounts.getUser("Logan Rivers") == null) {
            accounts.createAccount("Logan Rivers", "leniTry");
            writer.saveAccounts(accounts);
        } else {
            System.out.println("Account already exists for username Logan Rivers");
        }
        System.out.println("Duplicate rejected");

        // create and log in Leni
        System.out.println("\nCreate Account Success");
        String lena = "Leni Rivers";
        if (accounts.getUser(lena) == null) {
            accounts.createAccount(lena, "leniSecret");
            writer.saveAccounts(accounts);
            System.out.println("Account created for username " + lena);
        } else {
            System.out.println("Account already exists for username " + lena);
        }

        try {
            facade.login(lena, "leniSecret");
        } catch (Throwable ignored) { }
        try { System.out.println("Logged in as " + facade.getCurrentUsername()); } catch (Throwable ignored) { System.out.println("Logged in"); }

        // --- Interactive play: enter room, solve a few, then CHOOSE 8 to Save & Logout (Scenario) ---
        System.out.println("\nEnter Room • Play a bit • Choose '8' to Save & Logout (Scenario)");
        facade.startGame(Difficulty.EASY); // Rooms.startGame(facade) is called inside

        // When Rooms returns, the scenario option 8 saved progress and logged out for us.
        if (facade.getCurrentUser() == null) {
            System.out.println("Detected scenario logout from Rooms. Progress was saved to:");
            System.out.println("  escaperoom/src/main/resources/json/playerData.json");
        } else {
            try { facade.logout(); } catch (Throwable ignored) {}
        }

        // Log back in and restore
        try { facade.login(lena, "leniSecret"); } catch (Throwable ignored) {}
        try { facade.restoreProgressForCurrentUser(); } catch (Throwable ignored) {}

        Progress progress = null;
        try { progress = facade.getProgress(); } catch (Throwable ignored) {}
        System.out.println("\nRestored progress:");
        System.out.println(progress == null ? "No progress found" : progress.toString());

        // Resume to finish
        System.out.println("\n--- RESUMING PLAY ---");
        facade.startGame(Difficulty.EASY);


        // show JSON snapshot
        System.out.println("\nSaved JSON snapshot");
        try {
            String json = new String(java.nio.file.Files.readAllBytes(
                    java.nio.file.Paths.get("escaperoom/src/main/resources/json/playerData.json")));
            System.out.println(json);
        } catch (Throwable ignored) { System.out.println("Could not read playerData.json"); }

        // finish game and update leaderboard
        System.out.println("\nFinish Game Leaderboard");
        try { facade.endGame(); System.out.println("End game requested"); } catch (Throwable ignored) { System.out.println("End game failed"); }
        Leaderboard lb = null;
        try { lb = loader.getLeaderboard(); } catch (Throwable ignored) { }
        if (lb != null) {
            int rank = 1;
            try {
                for (Score s : lb.topN(10)) {
                    System.out.printf("%d %s %d pts %s%n", rank++, s.getUsername(), s.getScore(),
                            s.getDifficulty() == null ? "unknown" : s.getDifficulty());
                }
            } catch (Throwable ignored) { System.out.println("Could not print leaderboard entries"); }
        } else {
            System.out.println("No leaderboard available");
        }

        // certificate of completion
        System.out.println("\nCertificate of Completion");
        long finalScore = 0;
        try { finalScore = facade.calculateFinalScore(); } catch (Throwable ignored) { }
        try { makeCertificate(lena, "Infinite Stress Loop Escape Room", finalScore, facade.getCurrentDifficulty(), progress == null ? 0 : progress.getHintsUsed()); System.out.println("Certificate written to certificate_" + lena.replace(' ', '_') + ".txt"); } catch (Throwable ignored) { System.out.println("Certificate creation failed"); }

        System.out.println("\nAll scenarios complete");
    }

    /**
     * Creates a certificate of completion for the given user in the given game.
     * The certificate contains the user's name, the game name, the date of completion,
     * the difficulty level, the number of hints used, and the final score.
     * The certificate is written to a file named "certificate_<user>.txt"
     * @param user the username of the player
     * @param game the name of the game
     * @param score the final score of the player
     * @param diff the difficulty level of the game
     * @param hints the number of hints used by the player
     */
    private static void makeCertificate(String user, String game, long score, Difficulty diff, int hints) {
        String filename = "certificate_" + user.replace(' ', '_') + ".txt";
        String header = "*******************************************\n" +
                        "       ESCAPE ROOM COMPLETION AWARD\n" +
                        "*******************************************\n";
        String body = String.format("Player %s%nGame %s%nDate %s%n%n", user, game, LocalDate.now());
        body += String.format("Difficulty %s%nHints used %d%nFinal Score %d%n%n",
                diff == null ? "Unknown" : diff, hints, score);
        body += "Message\nYou survived the Infinite Stress Loop\n";
        body += "Enjoy your freedom until the next puzzle\n\n";
        body += "Signed\nThe Infinite Stress Loop Dev Team\n";

        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(header);
            fw.write(body);
        } catch (IOException e) {
            System.out.println("Could not write certificate");
        }
    }
}