package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Resource;
import it.polimi.ingsw.server.Model.Storage;

import java.io.IOException;
import java.util.ArrayList;

public class SendStorageMsg extends MessageToClient {
    Storage storage;
    ArrayList<Resource> resources;

    public SendStorageMsg(Storage storage) {
        this.storage = storage;
    }

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.printStorage(storage);
        view.insertRes(resources, "s", false); //per un potenziale altro switch
    }

    /**
     * Constructor
     */
    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }
}
