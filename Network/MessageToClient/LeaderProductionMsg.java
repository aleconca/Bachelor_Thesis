package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;

public class LeaderProductionMsg extends MessageToClient{

    Resource resource;//risorsa necessaria
    Player player;

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.ActivateLeaderProduction(player,resource);
    }

    /**
     * Constructor
     */
    public LeaderProductionMsg(Resource resource, Player player) {
        this.resource = resource;
        this.player = player;
    }
}
