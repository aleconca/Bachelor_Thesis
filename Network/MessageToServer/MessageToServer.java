package it.polimi.ingsw.Network.MessageToServer;

import it.polimi.ingsw.Exceptions.LevelDoNotExistsException;
import it.polimi.ingsw.server.Controller.Controller;

import java.io.IOException;
import java.io.Serializable;

public abstract class MessageToServer implements Serializable {
    /**
     * To process the message in controller
     */
    public abstract void process(Controller controller) throws IOException, InterruptedException;

}
