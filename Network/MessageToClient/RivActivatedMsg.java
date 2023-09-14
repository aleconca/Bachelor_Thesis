package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.Colour;
import it.polimi.ingsw.client.View.ViewCLI;

import java.io.IOException;

public class RivActivatedMsg extends MessageToClient{

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        System.out.println("\n" + Colour.ANSI_BLUE + "RIV "+ Colour.RESET + "has been activated!\n");
    }
}
