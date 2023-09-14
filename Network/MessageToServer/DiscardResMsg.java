package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;
import java.util.ArrayList;

public class DiscardResMsg extends MessageToServer {
    ArrayList<Resource> resources;

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException, InterruptedException {
        controller.discardRes(resources, true);
    }

}
