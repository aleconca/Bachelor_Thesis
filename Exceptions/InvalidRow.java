package it.polimi.ingsw.Exceptions;

public class InvalidRow extends Exception{

    /**
     * Exception for invalid row
     */
    public InvalidRow(){
        super ("Wrong row! Type a number in range 1 to 3");
    }
}
