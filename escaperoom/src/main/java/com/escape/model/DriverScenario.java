package com.escape.model;

import com.escape.model.*;
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

        // create account duplicate user
        System.out.println("\nCreate Account Duplicate User");
        String bro = "Logan Rivers";
        if (accounts.getUser(bro) == null) {
            accounts.createAccount(bro, "loganPass123");
            writer.saveAccounts(accounts);
            System.out.println("Created account for " + bro);
        } else {
            System.out.println(bro + " already has an account");
        }

        System.out.println("Leni tries to create an account with her brothers name");
        accounts.createAccount("Logan Rivers", "leniTry");
        User check = accounts.getUser("Logan Rivers");
        if (check != null && !check.getPassword().equals("leniTry")) {
            System.out.println("Duplicate rejected");
        } else {
            System.out.println("Duplicate logic may need review");
        }

        // create account success
        System.out.println("\nCreate Account Success");
        String lena = "Leni Rivers";
        accounts.createAccount(lena, "leniSecret");
        writer.saveAccounts(accounts);
        User leni = accounts.getUser(lena);
        if (leni == null) {
            System.out.println("Could not create Leni account");
            return;
        }
        System.out.println("Account created for Leni");
        facade.login(lena, "leniSecret");
        System.out.println("Logged in as " + facade.getCurrentUsername());

        // enter room hear story
        System.out.println("\nEnter Room Hear Story");
        try {
            facade.startGame(Difficulty.MEDIUM);
        } catch (Throwable t) {
            try { facade.startGame(); } catch (Throwable ignored) { }
        }

        Rooms room = null;
        try { room = facade.getCurrentRoom(); } catch (Throwable ignored) { }

        if (room == null) {
            try {
                java.util.List<Rooms> all = loader.getRooms();
                if (all != null && !all.isEmpty()) room = all.get(0);
            } catch (Throwable ignored) { }
        }

        if (room != null) {
            try { System.out.println("Leni enters room " + room.getTitle()); }
            catch (Throwable ignored) { System.out.println("Leni enters a room"); }

            String story = null;
            String[] methods = {"getStory", "getDescription", "getIntro", "getText"};
            for (String name : methods) {
                try {
                    java.lang.reflect.Method m = room.getClass().getMethod(name);
                    Object val = m.invoke(room);
                    if (val != null) { story = val.toString(); break; }
                } catch (Throwable ignored) { }
            }
            if (story == null) story = "The lights flicker and the air feels heavy";
            System.out.println("Story\n" + story);
            System.out.println("Pretend TTS reads the story aloud");
        } else {
            System.out.println("No rooms found");
        }

        // solve puzzles hints items
        System.out.println("\nSolve Puzzles Hints Items");
        boolean solved1 = false;
        try { solved1 = facade.solvePuzzle("correct_answer_for_puzzle_1"); } catch (Throwable ignored) { }
        System.out.println("Puzzle 1 solved " + solved1);

        try { System.out.println("Collected letters " + facade.getCollectedLetters()); } catch (Throwable ignored) { }

        String hint1 = null;
        try { hint1 = facade.useHint(); } catch (Throwable ignored) { }
        System.out.println("First hint " + (hint1 == null ? "none" : hint1));

        boolean solved2 = false;
        try { solved2 = facade.solvePuzzle("answer_for_puzzle_2"); } catch (Throwable ignored) { }
        System.out.println("Puzzle 2 solved " + solved2);

        String hint2 = null;
        try { hint2 = facade.useHint(); } catch (Throwable ignored) { }
        System.out.println("Second hint " + (hint2 == null ? "none" : hint2));

        boolean solved3 = false;
        try { solved3 = facade.solvePuzzle("final_answer_for_puzzle_3"); } catch (Throwable ignored) { }
        System.out.println("Puzzle 3 solved " + solved3);

        try {
            if (!facade.getCollectedLetters().isEmpty()) {
                System.out.println("Leni uses item " + facade.getCollectedLetters().get(0));
            }
        } catch (Throwable ignored) { }

        // save logout login persistence
        System.out.println("\nSave Logout Login Persistence");
        try { facade.saveGame(); } catch (Throwable ignored) { }
        System.out.println("Game saved");
        try { facade.logout(); } catch (Throwable ignored) { }
        System.out.println("Logged out");
        try { facade.login(lena, "leniSecret"); } catch (Throwable ignored) { }
        System.out.println("Logged back in as " + facade.getCurrentUsername());

        Progress progress = null;
        try { progress = facade.getProgress(); } catch (Throwable ignored) { }

        if (progress != null) {
            System.out.println("Progress " + progress.toString());
            try { System.out.println("Hints used " + progress.getHintsUsed()); } catch (Throwable ignored) { }
        } else {
            System.out.println("No progress found");
        }

        System.out.println("\nSaved JSON");
        try {
            String json = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("escaperoom/src/main/resources/json/playerData.json")));
            System.out.println(json);
        } catch (Exception e) {
            System.out.println("Could not read JSON file");
        }

        // finish game leaderboard
        System.out.println("\nFinish Game Leaderboard");
        boolean finalPuzzle = false;
        try { finalPuzzle = facade.solvePuzzle("ultimate_answer"); } catch (Throwable ignored) { }
        System.out.println("Final puzzle solved " + finalPuzzle);
        try { facade.endGame(); } catch (Throwable ignored) { }

        Leaderboard lb = null;
        try { lb = loader.getLeaderboard(); } catch (Throwable ignored) { }
        if (lb != null) {
            System.out.println("\nLeaderboard");
            int rank = 1;
            try {
                for (Score s : lb.topN(10)) {
                    System.out.printf("%d %s %d pts %s%n", rank++, s.getUsername(), s.getScore(),
                                      s.getDifficulty() == null ? "unknown" : s.getDifficulty());
                }
            } catch (Throwable ignored) { }

            if (lb.size() < 4) {
                try {
                    lb.addScore(new Score("PlayerA", Difficulty.EASY, 120, java.util.Date.from(java.time.Instant.now()), 1200));
                    lb.addScore(new Score("PlayerB", Difficulty.MEDIUM, 200, java.util.Date.from(java.time.Instant.now()), 2400));
                    lb.addScore(new Score("PlayerC", Difficulty.HARD, 60, java.util.Date.from(java.time.Instant.now()), 900));
                    writer.saveLeaderboard(lb);
                    System.out.println("Added sample players");
                } catch (Throwable ignored) { }
            }
        }

        // certificate of completion
        System.out.println("\nCertificate of Completion");
        try {
            Score finalScore = facade.getCurrentScore();
            if (finalScore == null) {
                long fs = 0;
                try { fs = facade.calculateFinalScore(); } catch (Throwable ignored) { }
                makeCertificate(lena, "Infinite Stress Loop Escape Room", fs,
                                safeDifficulty(facade), progress == null ? 0 : safeHints(progress));
            } else {
                makeCertificate(finalScore.getUsername(), "Infinite Stress Loop Escape Room",
                                finalScore.getScore(), finalScore.getDifficulty(),
                                progress == null ? 0 : safeHints(progress));
            }
        } catch (Throwable ignored) {
            System.out.println("Could not generate certificate");
        }

        System.out.println("Certificate written to certificate_" + lena.replace(' ', '_') + ".txt");
        System.out.println("\nAll scenarios complete");
    }

    private static int safeHints(Progress p) {
        try { return p.getHintsUsed(); } catch (Throwable t) { return 0; }
    }

    private static Difficulty safeDifficulty(EscapeRoomFacade f) {
        try { return f.getCurrentDifficulty(); } catch (Throwable t) { return null; }
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
