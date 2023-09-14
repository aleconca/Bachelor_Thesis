package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;

import java.io.IOException;

/**
 * Message to communicate to the Controller what marbles the player took
 * so it has to change the Market
 * */
public class SelectedMarblesMsg extends MessageToServer {
    int RorC = 0;
    int number = 0;

    /**
     * To process the message in controller
     */
    @Override
    public void process(Controller controller) throws IOException {
        controller.updateMarket(RorC, number);
    }

    /**
     * Getter and Setter
     */
    public int getNumber() {
        return number;
    }

    public void setRorC(int rorC) {
        RorC = rorC;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
