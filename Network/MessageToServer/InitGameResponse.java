package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.LeaderCard;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;
import java.util.ArrayList;

public class InitGameResponse extends MessageToServer {

    private ArrayList<Integer> chosenleaderCrads;
    private String[] chosenResources;
    private ArrayList<LeaderCard> cards;
    private Player player;

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException {

        controller.initGameLeaderCards(chosenleaderCrads,cards,chosenResources);
    }
    /**
     * Constructor
     */
    public InitGameResponse(ArrayList<Integer> chosenleaderCrads, ArrayList<LeaderCard> cards,String[] chosenResources, Player player) {
        this.chosenleaderCrads = chosenleaderCrads;
        this.cards = cards;
        this.player = player;
        this.chosenResources= chosenResources;
    }

}
