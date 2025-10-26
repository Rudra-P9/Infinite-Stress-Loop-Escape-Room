package com.escape.model;

import java.util.ArrayList;
import java.util.Scanner;
// import java.util.UUID; // not needed (use fully-qualified UUID where required)

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
    /**  Set when the player chooses option 6 (Quit) in any puzzle/room.
     *startGame(...) will immediately return to the caller when this is true.
    **/
     private boolean quitRequested = false;

     // Set when the player chooses the scripted Save & Logout option.
    private boolean scenarioLogoutRequested = false;



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
     * Searches for a room with the given id in the given list of rooms.
     * @param rooms list of rooms to search
     * @param id id of the room to search for
     * @return the room with the given id, or null if not found
     */
    private static Rooms findRoomByID(ArrayList<Rooms> rooms, String id) {
    for (Rooms r : rooms) {
            if (r.getRoomID().equalsIgnoreCase(id)) {
                return r;
            }
        }
        return null;
    }

    
    private static boolean playRoom(
        Rooms room,
        StoryElements story,
        Scanner scanner,
        Progress progress,
        EscapeRoomFacade facade) {
   
    if (room == null) {
        System.out.println("Room not found.");
        Speek.speak("Room not found.");
        return false;
    }

        System.out.println("\n--- " + room.getTitle() + " ---");
        Speek.speak("Entering room: " + room.getTitle());

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
        int i = 0;
        while (i < puzzles.size() && puzzles.get(i).solved()) {
            // show what we’re skipping
            System.out.println("(Skipping solved) " + puzzles.get(i).getTitle());
        i++;
    }
    
        for (; i < puzzles.size(); i++) {
            Puzzle puzzle = puzzles.get(i);
            System.out.println("\nPuzzle: " + puzzle.getTitle());
            Speek.speak("Puzzle: " + puzzle.getTitle());
            System.out.println("Prompt: " + puzzle.getPrompt());
            Speek.speak(puzzle.getPrompt());
            System.out.println("Objective: " + puzzle.getObjective());

            if(puzzle instanceof AudioPuzzle) {
                AudioPuzzle ap =(AudioPuzzle) puzzle;
                ap.playAudio();
            }
            boolean allowScenarioSave = (progress != null && progress.getStoryPos() >= 3);
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
                    (puzzle instanceof AudioPuzzle ? "7. Replay Audio\n" : "")+
                    "8. Save & Logout (Scenario)\n" +
                    "9. Save Progress\n"
                );
                System.out.print("Enter Choice: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        System.out.print("Enter Answer: ");
                        String answer = scanner.nextLine().trim();
                        if (puzzle.checkAnswer(answer)) {
                            progress.advanceStory();
                            solved = true; // mark solved

                            // Story beats between puzzles/rooms
                            if (room.getRoomID().equalsIgnoreCase("room1")) {
                                System.out.println("\n" + story.getRoomOneConc());
                                Speek.speak(story.getRoomOneConc());
                            } else if (room.getRoomID().equalsIgnoreCase("room2") && i == 0) {
                                System.out.println("\n" + story.getRoomTwoBetween());
                                Speek.speak(story.getRoomTwoBetween());
                            } else if (room.getRoomID().equalsIgnoreCase("room2") && i == puzzles.size() - 1) {
                                System.out.println("\n" + story.getRoomTwoConc());
                                Speek.speak(story.getRoomTwoConc());
                            } else if (room.getRoomID().equalsIgnoreCase("room3") && i == 0) {
                                System.out.println("\n" + story.getRoomThreeBetween());
                                Speek.speak(story.getRoomThreeBetween());
                            } else if (room.getRoomID().equalsIgnoreCase("room3") && i == puzzles.size() - 1) {
                                System.out.println("\n" + story.getRoomThreeConc());
                                Speek.speak(story.getRoomThreeConc());
                            } else if (puzzle.getTitle().equalsIgnoreCase("Merge Command")) {
                                if (collectedLetters.size() < 5) {
                                    System.out.println("\nSystem locked. The merge command cannot execute yet.");
                                    Speek.speak("System locked. The merge command cannot execute yet.");
                                    System.out.println("You still sense incomplete data fragments...");
                                    Speek.speak("You still sense incomplete data fragments...");
                                    System.out.println("(Collect all letters before returning here.)");
                                    Speek.speak("Collect all letters before returning here.");
                                    System.out.println("Press enter to continue...");
                                    scanner.nextLine();
                                    // return to caller (room selection), not to keep looping this puzzle
                                    return false;
                                } else {
                                    System.out.println("\n" + story.getConclusion());
                                    Speek.speak(story.getConclusion());
                                    // final completion; return to caller
                                    return false;
                                }
                            }

                            // Award next letter tag after a correct solve
                            String rewardLetter = getNextLetter(collectedLetters);
                            if (rewardLetter != null && !rewardLetter.isEmpty() && !collectedLetters.contains(rewardLetter)) {
                                collectedLetters.add(rewardLetter);
                                System.out.println("\nA tag with the letter " + rewardLetter + " reveals itself!");
                                Speek.speak("A tag with the letter " + rewardLetter + " reveals itself!");
                            }

                            System.out.println("Press enter to continue...");
                            scanner.nextLine();

                            // leave the "while (!solved)" loop NOW,
                            // so the outer for-loop moves to the next puzzle (or the room selection)
                            break;
                        } else {
                            System.out.println("Not quite. Try again.");
                            Speek.speak("Not quite. Try again.");
                        }
                        break;
                    case "2":
                        System.out.println("Hint: " + puzzle.getHint());
                        progress.useHint();
                        break;
                    case "3":
                        System.out.println(progress);
                        // optional: list solved so far (by scanning puzzles in all rooms is more involved;
                        // instead, we infer from global order + storyPos)
                        int sp = progress.getStoryPos();
                        if (sp > 0) {
                            System.out.println("Solved so far:");
                            for (int k = 0; k < Math.min(sp, GLOBAL_ORDER.length); k++) {
                                String rid = GLOBAL_ORDER[k][0];
                                int idx    = Integer.parseInt(GLOBAL_ORDER[k][1]);
                                // show nicer names if it’s the current room
                                String title = (room.getRoomID().equals(rid)
                                            && idx < room.getPuzzles().size())
                                            ? room.getPuzzles().get(idx).getTitle()
                                            : ("Puzzle " + (k+1));
                                System.out.println(" - " + title);
                            }
                        }
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
                    
                        // Make Quit exit the entire Rooms flow, save, and log out.
                        System.out.println("Exiting Escape The Varen Project...");
                        return false; // not completed

                    case "7":
                        if (puzzle instanceof AudioPuzzle) {
                            System.out.println("\nThe terminal starts again...");
                            ((AudioPuzzle) puzzle).playAudio();
                        }
                        break;
                    case "9":
                        // Save progress only (no logout)
                        if (facade != null) {
                            try { facade.saveProgressForCurrentUser(progress); } catch (Throwable ignored) {}
                            System.out.println("Progress saved.");
                            Speek.speak("Progress saved.");
                        } else {
                            System.out.println("Unable to save: game facade not available.");
                            Speek.speak("Unable to save: game facade not available.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice, please select a valid option.");
                        break;

                    case "8": // Save & Logout (Scenario)
                        if (!allowScenarioSave) {
                            System.out.println("Keep playing: solve at least 3 puzzles to unlock this scenario step.");
                            break;
                        }

                        if (facade != null) {
                            try { facade.saveGame(); } catch (Throwable ignored) {}
                            try { facade.saveProgressForCurrentUser(progress); } catch (Throwable ignored) {}
                            try { facade.logout(); } catch (Throwable ignored) {}
                        }
                        System.out.println("Saved & logged out. Returning to DriverScenario...");
                        return false;   // return to DriverScenario
                    }
                }
            }
            // If we got here, all puzzles in this room were solved.
            return true;
        }
    

    /**
     * Given an ArrayList of Strings, this function returns the next letter in the order 
     * of R, E, A, L, M that is not already in the ArrayList. If all letters are 
     * already in the ArrayList, this function returns an empty string.
     * 
     * @param collectedLetters the ArrayList of Strings containing the letters already collected
     * @return the next letter in the order that is not already collected, or an empty string if all are collected
     */
    private static String getNextLetter(ArrayList<String> collectedLetters) {
        String [] order = {"R","E","A","L","M"};
        for(String letter : order) {
            if(!collectedLetters.contains(letter)) {
                return letter;
            }
        }
        return "";
    }
    /**
     * Sets the quit flag to true, which will cause the game loop to exit.
     */
    private void quit() {
        this.quitRequested = true;
    }

    /** Hard-coded global puzzle order (roomID, indexWithinRoom) used to fast-forward on resume. */
    private static final String[][] GLOBAL_ORDER = {
        {"room1", "0"},
        {"room2", "0"},
        {"room2", "1"},
        {"room3", "0"},
        {"room3", "1"},
        {"final", "0"}
    };

    /** Marks the first `count` puzzles (in global order) as solved on the provided rooms list. */
    private static void fastForwardSolved(ArrayList<Rooms> rooms, int count) {
        if (rooms == null || count <= 0) return;
        for (int i = 0; i < Math.min(count, GLOBAL_ORDER.length); i++) {
            String rid = GLOBAL_ORDER[i][0];
            int idx = Integer.parseInt(GLOBAL_ORDER[i][1]);
            Rooms r = findRoomByID(rooms, rid);
            if (r == null) continue;
            var ps = r.getPuzzles();
            if (ps == null || idx < 0 || idx >= ps.size()) continue;
            try { ps.get(idx).setSolved(true); } catch (Throwable ignored) {}
        }
    }

    /**
     * Puzzle Testing
     * @param args
     */

    /**
     * Starts a new game with the given facade.
     * 
     * The game will load all rooms and puzzles from the disk, and then play through
     * each room in order. The user can choose which room to play next between the
     * Fragment Corridor and the Synchronization Core. After all rooms are complete,
     * the final command will be played.
     * 
     * @param facade the facade for the Escape Room game
     */
     public void startGame(EscapeRoomFacade facade) {
        
        try {
            quitRequested = false;  // fresh run
            GameDataLoader loader = new GameDataLoader();
            ArrayList<Rooms> rooms = loader.getRooms();
            StoryElements story = loader.getStory();

            if (rooms.isEmpty()) {
                System.out.println("No rooms found");
                return;
            }
            
            scenarioLogoutRequested = false; // <<< reset this run
            Progress progress = (facade != null && facade.getProgress() != null)
                    ? facade.getProgress()
                    : new Progress(
                        java.util.UUID.randomUUID(),
                        (facade != null && facade.getCurrentUser() != null)
                            ? facade.getCurrentUser().userID
                            : java.util.UUID.randomUUID());

            if (facade != null && facade.getProgress() == null) {
                // expose it back to the facade so future calls use the same object
                // (add a setter if you don’t have one; otherwise skip this)
                try { // optional if you added setProgress on the facade
                    java.lang.reflect.Method m =
                        facade.getClass().getMethod("setProgress", Progress.class);
                    m.invoke(facade, progress);
                } catch (Throwable ignored) {}
            }
            // after rooms/story are loaded and 'progress' is resolved
            int alreadySolved = (progress == null) ? 0 : progress.getStoryPos();
            fastForwardSolved(rooms, alreadySolved);

            Scanner scanner = new Scanner(System.in);

            System.out.println("--- ROOM & PUZZLE TESTING ---\n");
            System.out.println(story.getIntro());

            boolean r1 = playRoom(findRoomByID(rooms, "room1"),
                      story, scanner, progress, facade);
            if (!r1) return; // player quit or Save&Logout; stop and return to DriverScenario


            boolean room2Completed = false;
            boolean room3Completed = false;

            while (!(room2Completed && room3Completed)) {
                System.out.println("\nVaren:'There's diverging rooms, which way will you choose?'");
                System.out.println("1. Fragment Corridor ");
                System.out.println("2. Synchronization Core");
                System.out.print("Enter Choice: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        if (!room2Completed) {
                            boolean done = playRoom(findRoomByID(rooms, "room2"),
                                                    story, scanner, progress, facade);
                            if (!done) return;           // user quit or Save&Logout -> bubble out
                            room2Completed = true;       // mark complete only on success
                        } else {
                            System.out.println("Fragment Corridor already stabilized!");
                        }
                        break;
                    case "2":
                        if (!room3Completed) {
                            boolean done = playRoom(findRoomByID(rooms, "room3"),
                                                    story, scanner, progress, facade);
                            if (!done) return;           // user quit or Save&Logout
                            room3Completed = true;       // only if truly finished
                        } else {
                            System.out.println("Full syncronization already reached!");
                        }
                        break;
                }
            }

            System.out.println("\nAll systemed aligned...proceeding to the final command.");
            boolean finalDone = playRoom(findRoomByID(rooms, "final"),
                             story, scanner, progress, facade);
            if (!finalDone) return; // if they saved/logged out or were blocked, bail out

            System.out.println("\n--- The Varen Project Complete ---");
            System.out.println(progress);
        } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }

