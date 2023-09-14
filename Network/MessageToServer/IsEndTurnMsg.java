package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.server.Controller.Controller;

import java.io.IOException;

public class IsEndTurnMsg extends MessageToServer {

    /**
     * To process the message in cotroller
     */
    @Override
    public void process(Controller controller) throws IOException, InterruptedException {
        controller.isEndTurn(false);
    }
}
