package com.escape.model;

/**
 * Class for the story elements of the Escape Room.
 * Data holder for all story elements.
 * 
 * @author Talan Kinard
 */
public class StoryElements 
{
    private String intro;
    private String outro;

    private String roomOneIntro;
    private String roomOneConc;

    private String roomTwoIntro;
    private String roomTwoBetween;
    private String roomTwoConc;

    private String roomThreeIntro;
    private String roomThreeBetween;
    private String roomThreeConc;

    private String finalPuzzle;
    private String conclusion;

    public StoryElements() {

    }

    /**
     * Getters
     * @return
     */
    public String getIntro() { return intro; }
    public String getRoomOneIntro() { return roomOneIntro; }
    public String getRoomOneConc() { return roomOneConc; }

    public String getRoomTwoIntro() { return roomTwoIntro; }
    public String getRoomTwoBetween() { return roomTwoBetween; }
    public String getRoomTwoConc() { return roomTwoConc; }

    public String getRoomThreeIntro() { return roomThreeIntro; }
    public String getRoomThreeBetween() { return roomThreeBetween; }
    public String getRoomThreeConc() { return roomThreeConc; }

    public String getFinalPuzzle() { return finalPuzzle; }
    public String getConclusion() { return conclusion; }

    /**
     * Setters
     */

    public void setIntro(String intro) { this.intro = intro; }

    public void setRoomOneIntro(String roomOneIntro) { this.roomOneIntro = roomOneIntro; }
    public void setRoomOneConc(String roomOneConc) { this.roomOneConc = roomOneConc; }

    public void setRoomTwoIntro(String roomTwoIntro) { this.roomTwoIntro = roomTwoIntro; }
    public void setRoomTwoBetween(String roomTwoBetween) { this.roomTwoBetween = roomTwoBetween; }
    public void setRoomTwoConc(String roomTwoConc) { this.roomTwoConc = roomTwoConc; }

    public void setRoomThreeIntro(String roomThreeIntro) { this.roomThreeIntro = roomThreeIntro; }
    public void setRoomThreeBetween(String roomThreeBetween) { this.roomThreeBetween = roomThreeBetween; }
    public void setRoomThreeConc(String roomThreeConc) { this.roomThreeConc = roomThreeConc; }

    public void setFinalPuzzle(String finalPuzzle) { this.finalPuzzle = finalPuzzle; }
    public void setConclusion(String conclusion) { this.conclusion = conclusion; }


}
