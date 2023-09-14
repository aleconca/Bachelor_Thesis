package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;
import java.util.ArrayList;

public class DoubleLeaderProdmsg extends MessageToClient{

    Player player;
    ArrayList<Resource> req;

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.ActivateDoubleLeaderProd(player,req);
    }

    /**
     * Constructor
     */
    public DoubleLeaderProdmsg(Player player, ArrayList<Resource> req) {
        this.player = player;
        this.req = req;
    }
}
