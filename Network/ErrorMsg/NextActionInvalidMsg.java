package it.polimi.ingsw.Network.ErrorMsg;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.Network.MessageToClient.MessageToClient;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.DevelopCardDeck;
import it.polimi.ingsw.server.Model.Market;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;

public class NextActionInvalidMsg extends MessageToClient {
    String type="";
    Market m;
    DevelopCardDeck dev;
    Player p;

    /**
     * Constructor
     */
    public NextActionInvalidMsg(Market m, DevelopCardDeck dev, Player p) {
        this.m = m;
        this.dev = dev;
        this.p = p;
    }

    /**
     * Error of action turn
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.printError("This " + type + " action cannot be done! ");
        view.startTurn(m, dev, p);
    }

    /**
     * Getter
     */
    public String getType() {
        return type;
    }

    /**
     * Setter
     */
    public void setType(String type) {
        this.type = type;
    }
}
