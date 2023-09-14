package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;

public class ActivateProductionMsg extends MessageToClient{

    Player player;

    /**
     * To process the message in the view
     */
    @Override
    public void process(ViewCLI view) throws IOException {
        view.ActivateProduction(player);
    }

    /**
     * Constructor
     */
    public ActivateProductionMsg(Player player) {
        this.player = player;
    }
}
