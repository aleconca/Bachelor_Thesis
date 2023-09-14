package it.polimi.ingsw.Exceptions;

public class CannotSwitchLevelsException extends Exception {
    /**
     * Exception for levels
     */
    public CannotSwitchLevelsException() {
        super("Cannot switch levels");
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

}
