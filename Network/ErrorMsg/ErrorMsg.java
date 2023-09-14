package it.polimi.ingsw.Network.ErrorMsg;

import it.polimi.ingsw.Network.MessageToClient.MessageToClient;
import it.polimi.ingsw.client.View.ViewCLI;

/**
 * Message to communicate an error to the Client;
 */
public class ErrorMsg extends MessageToClient {
    /**
     * The error String to print on CLI
     */
    private String error = "Error";

    public ErrorMsg(String error){
        this.error = error;
    }

    @Override
    public void process(ViewCLI view) {

        view.printError(error);

    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
