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
    private static final ArrayList<String> collectedLetters = new ArrayList<>();
    private static boolean quit = false; 


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

    private static Rooms findRoomByID(ArrayList<Rooms> rooms, String id) {
    for (Rooms r : rooms) {
            if (r.getRoomID().equalsIgnoreCase(id)) {
                return r;
            }
        }
        return null;
    }

    
    private static void playRoom(
        Rooms room,
        StoryElements story,
        Scanner scanner,
        Progress progress,
        EscapeRoomFacade facade) {
   
    if (room == null) {
        System.out.println("Room not found.");
        return;
    }

        System.out.println("\n--- " + room.getTitle() + " ---");

        switch (room.getRoomID().toLowerCase()) {
            case "room1":
                System.out.println(story.getRoomOneIntro());
                Speek.speak(story.getRoomOneIntro());
                break;
            case "room2":
                System.out.println(story.getRoomTwoIntro());
                Speek.speak(story.getRoomTwoIntro());
                break;
            case "room3":
                System.out.println(story.getRoomThreeIntro());
                Speek.speak(story.getRoomThreeIntro());
                break;
            case "final":
                System.out.println(story.getFinalPuzzle());
                Speek.speak(story.getFinalPuzzle());
                break;
        }

        ArrayList<Puzzle> puzzles = room.getPuzzles();

        for (int i = 0; i < puzzles.size(); i++) {
            Puzzle puzzle = puzzles.get(i);
            System.out.println("\nPuzzle: " + puzzle.getTitle());
            System.out.println("Prompt: " + puzzle.getPrompt());
            Speek.speak(puzzle.getPrompt());
            System.out.println("Objective: " + puzzle.getObjective());

            if(puzzle instanceof AudioPuzzle) {
                AudioPuzzle ap =(AudioPuzzle) puzzle;
                ap.playAudio();
            }

            boolean solved = false;
            while (!solved) {
                System.out.println(
                    "\nChoose an Option:\n" +
                    "1. Enter Answer\n" +
                    "2. Get a Hint\n" +
                    "3. Check Progress\n" +
                    "4. Open Inventory\n" +
                    "5. See Time Remaining (Seconds)\n" +
                    "6. Quit\n"+
                    (puzzle instanceof AudioPuzzle ? "7. Replay Audio\n" : "")
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

                            if (room.getRoomID().equalsIgnoreCase("room1")) {
                                System.out.println("\n" + story.getRoomOneConc());
                            } else if (room.getRoomID().equalsIgnoreCase("room2") && i == 0) {
                                System.out.println("\n" + story.getRoomTwoBetween());
                            } else if (room.getRoomID().equalsIgnoreCase("room2") && i == puzzles.size() - 1) {
                                System.out.println("\n" + story.getRoomTwoConc());
                            } else if (room.getRoomID().equalsIgnoreCase("room3") && i == 0) {
                                System.out.println("\n" + story.getRoomThreeBetween());
                            } else if (room.getRoomID().equalsIgnoreCase("room3") && i == puzzles.size() - 1) {
                                System.out.println("\n" + story.getRoomThreeConc());
                            } else if (puzzle.getTitle().equalsIgnoreCase("Merge Command")) {
                                if (collectedLetters.size() < 5) {
                                    System.out.println("\nSystem locked. The merge command cannot execute yet.");
                                    System.out.println("You still sense incomplete data fragments...");
                                    System.out.println("(Collect all letters before returning here.)");
                                    System.out.println("Press enter to continue...");
                                    scanner.nextLine();
                                    return;
                                } else {
                                    System.out.println("\n" + story.getConclusion());
                                    return;
                                }
                            }

                            // After a puzzle is solved, award the letter
                            String rewardLetter = getNextLetter(collectedLetters);
                            if (rewardLetter != null && !rewardLetter.isEmpty() && !collectedLetters.contains(rewardLetter)) {
                                collectedLetters.add(rewardLetter);
                                System.out.println("\nA tag with the letter " + rewardLetter + " reveals itself!");
                            }
                            System.out.println("Press enter to continue...");
                            scanner.nextLine();
                        } else {
                            System.out.println("Not quite. Try again.");
                        }
                    break;
                    case "2":
                        System.out.println("Hint: " + puzzle.getHint());
                        progress.useHint();
                        break;
                    case "3":
                        System.out.println(progress);
                        break;
                    case "4":
                        System.out.println("Letter tags collected: "+collectedLetters);
                        break;
                    case "5":
                        if (facade != null) {
                            System.out.println("Time remaining (sec): " + facade.getTimeRemaining());
                        } else {
                            System.out.println("Timer not available (facade is null).");
                        }
                        break;

                    case "6":
                    
                        System.out.println("Exiting Escape The Varen Project...");
                        UI ui = new UI();
                        ui.run();
                        scanner.close();
                        quit();
                        return;

                    case "7":
                        if (puzzle instanceof AudioPuzzle) {
                            System.out.println("\nThe terminal starts again...");
                            ((AudioPuzzle) puzzle).playAudio();
                        }
                        break;
                    default:
                        System.out.println("Invalid Choice, please select 1–6.");
                        break;
                    
                    }
                }
            }
        }
    

    private static String getNextLetter(ArrayList<String> collectedLetters) {
        String [] order = {"R","E","A","L","M"};
        for(String letter : order) {
            if(!collectedLetters.contains(letter)) {
                return letter;
            }
        }
        return "";
    }
    private static void quit() {
        quit = true;
    }

    /**
     * Puzzle Testing
     * @param args
     */

     public void startGame(EscapeRoomFacade facade) {
    try {
        GameDataLoader loader = new GameDataLoader();
        ArrayList<Rooms> rooms = loader.getRooms();
        StoryElements story = loader.getStory();

        if (rooms.isEmpty()) {
            System.out.println("No rooms found");
            return;
        }

        Progress progress = new Progress(UUID.randomUUID(), UUID.randomUUID());
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- ROOM & PUZZLE TESTING ---\n");
        System.out.println(story.getIntro());

        playRoom(findRoomByID(rooms, "room1"), story, scanner, progress, facade);

        boolean room2Completed = false;
        boolean room3Completed = false;
        boolean quit = false;

        while (!(room2Completed && room3Completed && quit)) {
            System.out.println("\nVaren:'There's diverging rooms, which way will you choose?'");
            System.out.println("1. Fragment Corridor ");
            System.out.println("2. Synchronization Core");
            System.out.print("Enter Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (!room2Completed) {
                        playRoom(findRoomByID(rooms, "room2"), story, scanner, progress, facade);
                        room2Completed = true;
                    } else {
                        System.out.println("Fragment Corridor already stabilized!");
                    }
                    break;
                case "2":
                    if (!room3Completed) {
                        playRoom(findRoomByID(rooms, "room3"), story, scanner, progress, facade);
                        room3Completed = true;
                    } else {
                        System.out.println("Full syncronization already reached!");
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }

        System.out.println("\nAll systemed aligned...proceeding to the final command.");
        playRoom(findRoomByID(rooms, "final"), story, scanner, progress, facade);
        System.out.println("\n--- The Varen Project Complete ---");
        System.out.println(progress);
    } catch (Exception e) {
        e.printStackTrace();
        }
    }
}

