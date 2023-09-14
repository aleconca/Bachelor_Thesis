package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.DevelopCard;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;
import java.util.ArrayList;

public class BuyDevelopCardMsg extends MessageToClient {

    //levels assigned server side
    private ArrayList<DevelopCard> lvl1 ;
    private ArrayList<DevelopCard> lvl2 ;
    private ArrayList<DevelopCard> lvl3 ;

    private Player player;

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException {
        view.ShowDevCards(lvl1,lvl2,lvl3,player);
    }

    /**
     * Constructor
     */
    public BuyDevelopCardMsg(ArrayList<DevelopCard> lvl1, ArrayList<DevelopCard> lvl2, ArrayList<DevelopCard> lvl3, Player player) {
        this.lvl1 = lvl1;
        this.lvl2 = lvl2;
        this.lvl3 = lvl3;
        this.player= player;

    }

    //getters and setters
    public ArrayList<DevelopCard> getLvl1() {
        return lvl1;
    }

    public ArrayList<DevelopCard> getLvl2() {
        return lvl2;
    }

    public ArrayList<DevelopCard> getLvl3() {
        return lvl3;
    }

}
