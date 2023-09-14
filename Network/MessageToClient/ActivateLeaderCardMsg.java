package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;

public class ActivateLeaderCardMsg extends MessageToClient{

    private Player player;

    /**
     * Constructor
     */
    public ActivateLeaderCardMsg(Player player) {
        this.player = player;
    }

    /**
     * To process the message in the view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.activateLeaderCard();
    }
}
