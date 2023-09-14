package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;
import java.util.ArrayList;

public class SwitchLvlsMsg extends MessageToServer {
    int firstLvl;
    int secondLvl;
    ArrayList<Resource> resources;

    /**
     * Constructor
     */
    public SwitchLvlsMsg(int firstLvl, int secondLvl, ArrayList<Resource> resources) {
        this.firstLvl = firstLvl;
        this.secondLvl = secondLvl;
        this.resources = resources;
    }

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException {
        controller.switchLvls(firstLvl, secondLvl, resources);
    }
}
