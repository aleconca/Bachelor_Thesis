package it.polimi.ingsw.Network.ErrorMsg;

import it.polimi.ingsw.Network.MessageToClient.MessageToClient;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Market;


public class MarketErrorMsg extends MessageToClient {

    private Market m;
    private String errorType;

    /**
     * Constructor
     */
    public MarketErrorMsg(Market m, String errorType) {
        this.m = m;
        this.errorType = errorType;
    }

    /**
     * Print market error
     */
    @Override
    public void process(ViewCLI view) {
        view.marketError(m, errorType);
    }

}
