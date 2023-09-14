package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.client.View.ViewCLI;

import java.io.IOException;

public class WaitForYourTurnMsg extends MessageToClient {

    private String string;

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException {
        view.WaitForYourTurn(string);
    }

    /**
     * Constructor
     */
    public WaitForYourTurnMsg(String string) {
        this.string = string;
    }
}
