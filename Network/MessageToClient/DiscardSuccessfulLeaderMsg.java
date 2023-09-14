package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;

import java.io.IOException;

public class DiscardSuccessfulLeaderMsg extends MessageToClient{

    int n;

    /**
     * Constructor
     */
    public DiscardSuccessfulLeaderMsg(int n) {
        this.n = n;
    }

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        if (n==1)
            System.out.println("\nLeader Card has been discarded successfully! You get +1 faith point\n");
        else if (n==3)
            System.out.println("\nAction canceled\n");
        else
            System.out.println("\nLeader Card HAS NOT been discarded!");
    }
}
