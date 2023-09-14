package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.LeaderCard;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This message sends to Clients the Leader Cards Deck and communicates to choose two of them;
 * */
public class InitGameMsg extends MessageToClient {
    private Player player;
    private ArrayList<LeaderCard> cards;


    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException, IncorrectMoveException, ArrayDontFitException {
        view.initGame(player,cards);
    }

    /**
     * Constructor
     */
    public InitGameMsg(Player player, ArrayList<LeaderCard> cards) {
        this.player = player;
        this.cards = cards;
    }
}
