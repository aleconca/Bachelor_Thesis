package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelectedLeaderProductionMsg extends    MessageToServer {

    ArrayList<String> leaderProduction;


    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException, InterruptedException {
        controller.VerifyLeaderProduction(leaderProduction);
    }
    /**
     * Constructor
     */
    public SelectedLeaderProductionMsg(ArrayList<String> leaderProduction) {
        this.leaderProduction = leaderProduction;
    }

}
