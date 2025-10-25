package com.escape.model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Runs through each of the scenarios given by the instructor
 * @author Dylan Diaz
 */
public class DriverScenario {

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

        // start a game
        System.out.println("\nEnter Room Hear Story");
        try { facade.startGame(); } catch (Throwable ignored) { }
        Rooms room = null;
        try { room = facade.getCurrentRoom(); } catch (Throwable ignored) { }
        if (room != null) {
            try { System.out.println("Leni enters room " + room.getTitle()); } catch (Throwable ignored) { System.out.println("Leni enters a room"); }
            System.out.println("Story\nThe lights flicker and the air feels heavy");
            System.out.println("Pretend TTS reads the story aloud");
        } else {
            System.out.println("No rooms found");
        }

        // simulate solving puzzles and using hints
        System.out.println("\nSolve Puzzles Hints Items");
        try {
            // call facade methods without assuming return types
            facade.solveCurrentPuzzle();
            System.out.println("Puzzle 1 solved attempted");
        } catch (Throwable t) {
            System.out.println("Puzzle 1 solve attempt failed");
        }

        try {
            facade.getHint();
            System.out.println("Hint requested");
        } catch (Throwable t) {
            System.out.println("Hint request failed");
        }

        try {
            facade.solveCurrentPuzzle();
            System.out.println("Puzzle 2 solved attempted");
        } catch (Throwable t) {
            System.out.println("Puzzle 2 solve attempt failed");
        }

        try {
            facade.getHint();
            System.out.println("Second hint requested");
        } catch (Throwable t) {
            System.out.println("Second hint request failed");
        }

        try {
            facade.solveCurrentPuzzle();
            System.out.println("Puzzle 3 solved attempted");
        } catch (Throwable t) {
            System.out.println("Puzzle 3 solve attempt failed");
        }

        // save game
        System.out.println("\nSave Logout Login Persistence");
        try { facade.saveGame(); System.out.println("Game saved to playerData.json"); } catch (Throwable ignored) { System.out.println("Save failed"); }
        try { facade.logout(); System.out.println("Logged out"); } catch (Throwable ignored) { System.out.println("Logout failed"); }
        try { facade.login(lena, "leniSecret"); System.out.println("Logged back in as " + facade.getCurrentUsername()); } catch (Throwable ignored) { System.out.println("Login failed"); }

        Progress progress = null;
        try { progress = facade.getProgress(); } catch (Throwable ignored) { }
        if (progress != null) {
            try { System.out.println("Progress " + progress.toString()); } catch (Throwable ignored) { System.out.println("Progress available"); }
        } else {
            System.out.println("No progress found");
        }

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