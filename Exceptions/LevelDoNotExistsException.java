package it.polimi.ingsw.Exceptions;

public class LevelDoNotExistsException extends Exception {

    /**
     * Exception for inexistent level
     */
    public LevelDoNotExistsException(String message) {
        super(message);
    }
}
