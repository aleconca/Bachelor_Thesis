package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Market;

import java.io.IOException;

public class MarketActionMsg extends MessageToClient {
    Market m;

    /**
     * Constructor
     */
    public MarketActionMsg(Market m) {
        this.m = m;
    }

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.goToMarket(m);
    }
}
