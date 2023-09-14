package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.Marble;

import java.io.IOException;

public class WhiteMarbleResponseMsg extends MessageToServer {
    int[] cardChosen;
    Marble[] marbles;

    /**
     * Constructor
     */
    public WhiteMarbleResponseMsg(int[] cardChosen, Marble[] marbles) {
        this.cardChosen = cardChosen;
        this.marbles = marbles;
    }

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException, InterruptedException {
        controller.colorForWhite(marbles, cardChosen);
    }
}
