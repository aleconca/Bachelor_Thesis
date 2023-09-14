package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;

import java.io.IOException;

public class LeaderCardNumberMsg extends MessageToServer{

    private int x;

    /**
     * Constructor
     */
    public LeaderCardNumberMsg(int x) {
        this.x = x;
    }

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException, InterruptedException {
        controller.ActivateLeaderCard(x);
    }
}
