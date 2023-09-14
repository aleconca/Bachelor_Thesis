package it.polimi.ingsw.Exceptions;

public class ArrayDontFitException extends Exception {
    /**
     * Exception for too much resources
     */
    public ArrayDontFitException() {
        super("There are too much resources!");
    }
}
