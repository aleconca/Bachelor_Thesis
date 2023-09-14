package it.polimi.ingsw.Network.ErrorMsg;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.Network.MessageToClient.MessageToClient;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;
import java.util.ArrayList;

public class SwitchLvlsErrorMsg extends MessageToClient {
    ArrayList<Resource> resources;
    String error;

    /**
     * Constructor
     */
    public SwitchLvlsErrorMsg(ArrayList<Resource> resources, String error) {
        this.resources = resources;
        this.error = error;
    }

    /**
     * Error for switching levels
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.printError(error);
        view.insertRes(resources, "s", false);
    }
}
