package it.polimi.ingsw.Network.ErrorMsg;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.Network.MessageToClient.MessageToClient;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;
import java.util.ArrayList;

public class InsertResErrorMsg extends MessageToClient {
    String error="";
    String action;
    ArrayList<Resource> resources;

    public InsertResErrorMsg(String error) {
        this.error=error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    /**
     * Error message for inserting a resorce
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.printError(error);
        view.insertRes(resources, action, false);
    }
}
