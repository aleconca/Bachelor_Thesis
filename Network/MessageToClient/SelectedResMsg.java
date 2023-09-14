package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;
import java.util.ArrayList;

public class SelectedResMsg extends MessageToClient {
    private ArrayList<Resource> resources;
    private String action;
    private boolean whiteUsed;

    /**
     * Constructor
     */
    public SelectedResMsg(ArrayList<Resource> resources, String action, boolean whiteUsed){
        this.resources = resources;
        this.action = action;
        this.whiteUsed = whiteUsed;
    }

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.insertRes(resources, action, whiteUsed);
    }
}
