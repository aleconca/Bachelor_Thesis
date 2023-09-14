package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.Colour;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;

public class WaitForTurnMsg extends MessageToClient{

    private Player player;
    /**
     * Constructor
     */
    public WaitForTurnMsg(Player player) {
        this.player = player;
    }

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        System.out.println("\nTURN OF " + Colour.ANSI_CIANO + player.getName() + Colour.RESET + " ! WAIT FOR YOUR TURN...\n");
    }
}
