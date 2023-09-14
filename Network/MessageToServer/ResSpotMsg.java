package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;
import java.util.ArrayList;

/**
 * For each resource took from the market will be sent a message
 * in order to control every move before doing the next one
 */
public class ResSpotMsg extends MessageToServer {
    ArrayList<Resource> res;
    int level;

    public ResSpotMsg(){}

    @Override
    public void process(Controller controller) throws IOException {
        controller.insertRes(res, level);
    }


    public ArrayList<Resource> getRes() {
        return res;
    }

    public void setRes(ArrayList<Resource> res) {
        this.res = res;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
