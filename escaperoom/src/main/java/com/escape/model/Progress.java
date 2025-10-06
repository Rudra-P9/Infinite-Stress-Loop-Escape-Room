package com.escape.model;
import java.util.UUID;

/**
 * Represents a user's saved or ongoing progress in the game.
 * @author Dylan Diaz
 */
public class Progress {

    /**
     * Identifier for this progress record.
     * Used to distinguish one saved progress instance from another.
     */
    private UUID progressUUID;

    /**
     * Identifier of the user associated with this progress.
     * Links this progress record to a specific user profile.
     */
    private UUID userUUID;

    /**
     * Constructs a new Progress object with the specified identifiers.
     *
     * @param progressUUID the identifier for this progress record
     * @param userUUID the identifier of the user associated with this progress
     */
    public Progress(UUID progressUUID, UUID userUUID) {
        this.progressUUID = progressUUID;
        this.userUUID = userUUID;
    }

    /**
     * Returns the unique identifier for this progress record.
     *
     * @return the progress record's unique identifier
     */
    public UUID getProgressUUID() {
        return progressUUID;
    }

    /**
     * Sets the unique identifier for this progress record.
     *
     * @param progressUUID the new unique identifier for this progress
     */
    public void setProgressUUID(UUID progressUUID) {
        this.progressUUID = progressUUID;
    }

    /**
     * Returns the unique identifier of the user associated with this progress.
     *
     * @return the user's unique identifier
     */
    public UUID getUserUUID() {
        return userUUID;
    }

    /**
     * Sets the unique identifier of the user associated with this progress.
     *
     * @param userUUID the new unique identifier for the user
     */
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }
}
