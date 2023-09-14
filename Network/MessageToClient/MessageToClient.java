package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;

import java.io.IOException;
import java.io.Serializable;

public abstract  class MessageToClient implements Serializable {

    /**
     * To process the message in view
     */
    public abstract void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException;

}
