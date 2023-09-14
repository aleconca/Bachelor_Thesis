package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Marble;

import java.io.IOException;

public class WhiteMarbleMsg extends MessageToClient {
    String[] resources;
    Marble[] marbles;
    int numWhite;
    /**
     * Constructor
     */
    public WhiteMarbleMsg(String[] resources, Marble[] marbles, int numWhite) {
        this.resources = resources;
        this.marbles = marbles;
        this.numWhite = numWhite;
    }

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.chooseWhiteMarble(resources, marbles, numWhite);
    }
}
