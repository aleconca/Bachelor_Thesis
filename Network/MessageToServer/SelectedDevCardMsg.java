package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.DevelopCard;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;
import java.util.ArrayList;

public class SelectedDevCardMsg extends MessageToServer {

    //stessi array inviati dal server rimandati indietro per la successiva rimozione della carta dal mazzo
    ArrayList<DevelopCard> lvl1 ;
    ArrayList<DevelopCard> lvl2 ;
    ArrayList<DevelopCard> lvl3 ;

    //attributo player assegnato nel server prima di invocare process per prendere gli sòot del player
    Player player;

    //carta scelta mandata al server
    int chosenIndex;
    int chosenSlot;

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException {
        controller.ErrorDevCard(lvl1,lvl2,lvl3,chosenIndex,chosenSlot);
    }

    /**
     * Constructor
     */
    public SelectedDevCardMsg(ArrayList<DevelopCard> lvl1, ArrayList<DevelopCard> lvl2, ArrayList<DevelopCard> lvl3, int chosenIndex,
                              int chosenSlot) {
        this.lvl1 = lvl1;
        this.lvl2 = lvl2;
        this.lvl3 = lvl3;
        this.chosenIndex = chosenIndex;
        this.chosenSlot = chosenSlot;
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
