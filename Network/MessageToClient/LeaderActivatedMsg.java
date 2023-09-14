package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;

import java.io.IOException;

public class LeaderActivatedMsg extends MessageToClient{

    private int n;

    /**
     * Constructor
     */
    public LeaderActivatedMsg(int n) {
        this.n = n;
    }

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        if (n==3) {
            System.out.println("\nLeaderCard HAS NOT been activated!");
            System.out.println("\nAction canceled\n");
        }
        else if (n==1 || n==2) {
            System.out.println("\nLeaderCard has been activated!");
        }
        else if (n==5){
            System.out.println("\nLeaderCard has been ALREADY activated ....");
        }
        else
            System.out.println("\nLeaderCard HAS NOT been activated because you don't have the requirements!\n");
    }
}
