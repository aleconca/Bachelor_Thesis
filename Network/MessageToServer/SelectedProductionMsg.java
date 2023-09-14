package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;

import java.io.IOException;
import java.util.ArrayList;

public class SelectedProductionMsg extends MessageToServer{

    ArrayList<String> basic;
    ArrayList<ArrayList<String>> cards;
    ArrayList<Integer> z;

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException {
        controller.ActivateProduction(basic,cards,z);
    }
    /**
     * Constructor
     */
    public SelectedProductionMsg(ArrayList<String> basic, ArrayList<ArrayList<String>> cards, ArrayList<Integer> z) {
        this.basic = basic;
        this.cards = cards;
        this.z = z;
    }
}
