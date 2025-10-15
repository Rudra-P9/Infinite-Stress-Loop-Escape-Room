package com.escape.model;

import java.util.ArrayList;

/**
 * Class for the story elements of the Escape Room.
 * Methods to get the story text and convert to String.
 * 
 * @author Talan Kinard
 */
public class StoryElements 
{
    private ArrayList<StoryElements> story;
    private String storyText;
    private static final int storyPos = 0;

    /**
     * @return story text
     */

    public String getStory()
    {
        return storyText;
    }

    /**
     * String representation of story elements
     */

    @Override
    public String toString()
    {
        return "Story Elements: Story Position = "+storyPos+" | Text = "+storyText;
    }
}
