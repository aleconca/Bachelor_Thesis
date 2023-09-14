package it.polimi.ingsw.Exceptions;

public class InvalidColumn extends Exception{
    /**
     * Exception for invalid column
     */
    public InvalidColumn(){
        super("Invalid column! Type a number in range 1 to 4");
    }
}
