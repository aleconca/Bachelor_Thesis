package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;

public class SelectedTurnMsg extends MessageToServer {

    private String action;

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException, InterruptedException {

        controller.action(action);

    }
    /**
     * Constructor
     */
    public SelectedTurnMsg(String action, Player player) {
        this.action = action;
    }


    //getters and setters

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
