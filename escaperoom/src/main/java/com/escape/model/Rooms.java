package com.escape.model;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

/**
 * Rooms of the EscapeRoom
 * Each room has an ID, puzzles, and a difficulty level.
 * 
 * @author Talan Kinard
 */

public class Rooms {

    /**
     * Unique identifier for the room.
     */
    private String roomID;
    private String title;
    private ArrayList<Puzzle> puzzles;

    /**
     * Default constructor
     */
    public Rooms() {
        puzzles = new ArrayList<>();
    }

    /**
     * Constructs a room with id, title, and puzzles
     * @param roomID the id for the room
     * @param title name of the room
     * @param puzzles the puzzles the room contains
     */
    public Rooms(String roomID, String title, ArrayList<Puzzle> puzzles) {
        this.roomID = roomID;
        this.title = title;
        this.puzzles = puzzles;
    }

    /**
     * Returns the list of puzzles associated with the room.
     * @return puzzles
     */
    public ArrayList<Puzzle> getPuzzles()
    {
        return puzzles;
    }

    /**
     * Sets the list of puzzles for this room.
     * @param puzzles
     */
    public void setPuzzles(ArrayList<Puzzle> puzzles) {
        this.puzzles = puzzles;
    }

    /**
     * Returns the id of the room.
     * @return
     */
    public String getRoomID() {
        return roomID;
    }

    /**
     * Sets the identifier for this room.
     * @param roomID
     */
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    /**
     * Returns the title of the room.
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the room.
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Provides a String description for the room and avoids a NullPointer with an incorrect load.
     */
    @Override
    public String toString() {
        return "Room: "+title+" ("+roomID+"), Puzzles: "+(puzzles != null ? puzzles.size() : 0);
    }

    /**
     * Puzzle Testing
     * @param args
     */

     public static void main(String[] args) {
        try {
            GameDataLoader loader = new GameDataLoader();
            ArrayList<Rooms> rooms = loader.getRooms();
            
            StoryElements story = loader.getStory();
            
            /** 
            StoryElements story = new StoryElements();
            story.setIntro("\nYou wake up in a bright white room, the light dazing you. A voice echoes, shaky yet familiar `Hey. Easy now. My name’s Varen. You’re safe. We’re going to get you out of here.'");

            story.setRoomOneIntro("A terminal flickers to life. Varen begins, 'This is a basic calibration to confirm your logic is intact.’");
            story.setRoomOneConc("The console chimes softly. A compartment opens, revealing a glowing tag labeled 'R'. Varen sounds pleased: 'Good. Neural patterns are stabilizing. Let’s continue then.’");

            story.setRoomTwoIntro("You step into a corridor lined with broken screens and scattered data fragments floating in the air. Each fragment flashes with jumbled text. Varen’s tone softens: 'These are memory shards... pieces of what was once mine. Try to put them back together.'");
            story.setRoomTwoBetween("The screens pulse as the first fragment stabilizes. Among the data, a symbol flashes bright, releasing a tag with the letter 'E'. The floor shifts slightly beneath your feet, reorienting itself. Varen murmurs: 'Spatial mapping is unstable… we’ll need to recalibrate your movement pathways next.'");
            story.setRoomTwoConc("When the second puzzle finishes, the corridor lights flicker, then stabilize. A faint hum echoes through the glass as another tag materializes, the letter 'A'. Varen whispers: 'Memory alignment, seventy-five percent. You’re restoring more than just data. Shall we continue…?'");

            story.setRoomThreeIntro("Moving to the next room, you step into the core chamber. The lights dim to a soft pulse that syncs with your heartbeat. Varen speaks calmly: 'We’re nearing full synchronization. This test measures how our thoughts align, patterns, logic, precision. Solve it exactly as I would.'");
            story.setRoomThreeBetween("As you complete the calculation, the console flickers. The number locks in perfectly, almost too perfect. The system hums in resonance with your pulse. A glowing mark forms on the terminal, a tag with the letter 'L' dispenses. Varen’s tone shifts, uncertain, 'That frequency… it’s echoing back. Something’s trying to communicate. Listen closely, it’s not me.'");
            story.setRoomThreeConc("Static floods the speakers. A voice overlays Varen’s own, distorted, layered, identical voice. 'Subject 03 synchronization nearing completion.' The lights flicker as the chamber shakes. From the distortion, a faint frequency plays, hidden within the noise, a message: the letter 'M' and an equal tag appears to grab. Varen’s voice breaks: 'It’s my voice… no… its yours. The system is waiting for the final command.'");

            story.setFinalPuzzle("The central terminal activates. Lines of code race across the glass. The prompt appears: 'ENTER MERGE COMMAND.' Varen pleads: 'Listen to me, you execute it, we won’t come back.'");
            story.setConclusion("You type REALM. The room floods with white light. The hum fades into silence, then darkness. You open your eyes in a sterile white room. A camera blinks red in the corner. An intercom crackles to life. You walk toward it, press the button, and speak `Hey. Easy now. My name’s Varen. You’re safe. We’re going to get you out of here.'");
            */

            if (rooms.isEmpty()) {
                System.out.println("No rooms found");
                return;
            }

            // Count total puzzles (optional debugging)
            int totalPuzzles = 0;
            for (Rooms r : rooms) {
                if (r.getPuzzles() != null) {
                    totalPuzzles += r.getPuzzles().size();
                }
            }

            Progress progress = new Progress(UUID.randomUUID(), UUID.randomUUID());
            Scanner scanner = new Scanner(System.in);

            System.out.println("--- ROOM & PUZZLE TESTING ---");
            System.out.println(story.getIntro());

            // Loop through each room
            for (int i = 0; i < rooms.size(); i++) {
                Rooms room = rooms.get(i);
                ArrayList<Puzzle> puzzles = room.getPuzzles();

                System.out.println("\n--- " + room.getTitle() + " ---");

                // Room intro
                switch (i) {
                    case 0:
                        System.out.println(story.getRoomOneIntro());
                        break;
                    case 1:
                        System.out.println(story.getRoomTwoIntro());
                        break;
                    case 2:
                        System.out.println(story.getRoomThreeIntro());
                        break;
                }

                // Puzzle loop
                for (int p = 0; p < puzzles.size(); p++) {
                    Puzzle puzzle = puzzles.get(p);
                    System.out.println("\nPuzzle: " + puzzle.getTitle());
                    System.out.println("Prompt: " + puzzle.getPrompt());
                    System.out.println("Objective: " + puzzle.getObjective());

                    boolean solved = false;
                    while (!solved) {
                        System.out.println(
                            "\nChoose an Option:\n" +
                            "1. Enter Answer\n" +
                            "2. Get a Hint\n" +
                            "3. Check Progress\n" +
                            "4. Quit\n"
                        );
                        System.out.print("Enter Choice: ");
                        String choice = scanner.nextLine().trim();

                        switch (choice) {
                            case "1":
                                System.out.print("Enter Answer: ");
                                String answer = scanner.nextLine().trim();
                                if (puzzle.checkAnswer(answer)) {
                                    progress.advanceStory();
                                    solved = true;

                                    if (puzzle.getTitle().equalsIgnoreCase("Merge Command")) 
                                    {

                                        // Wait for player input before finalizing
                                        System.out.println("\n(Press Enter to continue...)");
                                        scanner.nextLine();

                                        // Then show the final system merge / ending
                                        System.out.println("\n" + story.getConclusion());
                                        System.out.println("\n--- GAME COMPLETE ---");
                                        System.out.println(progress);
                                        return;
                                    }
                                } else {
                                    System.out.println("Not quite. Try again.");
                                }
                                break;
                            case "2":
                                System.out.println("Hint: " + puzzle.getHint());
                                break;
                            case "3":
                                System.out.println(progress);
                                break;
                            case "4":
                                System.out.println("Exiting Escape The Varen Project...");
                                scanner.close();
                                return;
                            default:
                                System.out.println("Invalid Choice, please select 1-4.");
                                break;
                        }
                    }

                    // Between-story transitions
                    if (i == 1 && p == 0) 
                    {
                        System.out.println("\n" + story.getRoomTwoBetween());
                        System.out.println("\n(Press Enter to continue...)");
                        scanner.nextLine();
                    }
                    if (i == 2 && p == 0) 
                    {
                        System.out.println("\n" + story.getRoomThreeBetween());
                        System.out.println("\n(Press Enter to continue...)");
                        scanner.nextLine();
                    }
                    if (i == 2 && p == 1) 
                    {
                        System.out.println("\n" + story.getRoomThreeConc());
                        System.out.println("\n(Press Enter to continue...)");
                        scanner.nextLine();
                    }
                }

                // Room conclusions
                switch (i) {
                    case 0:
                        System.out.println(story.getRoomOneConc());
                        break;
                    case 1:
                        System.out.println(story.getRoomTwoConc());
                        break;
                    case 2:
                        System.out.println(story.getRoomThreeConc());
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


