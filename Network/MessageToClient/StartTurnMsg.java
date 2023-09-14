package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.DevelopCardDeck;
import it.polimi.ingsw.server.Model.Market;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Message to notify to the client to start the turn
 * */
public class StartTurnMsg extends MessageToClient {

    private Market market;
    private DevelopCardDeck devDeck;
    private Player player;
    private ArrayList<Player> players = new ArrayList<>();

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException {
        view.ShowMarket(market);
        if (players.size()>1) {
            view.ShowDashboardMulti(player, players);
        }
        else if (players.size()==1)
            view.ShowDashboardSingle(player);
        view.startTurn(market, devDeck,player);
    }

    /**
     * Constructor
     */
    public StartTurnMsg(Market market, DevelopCardDeck devDeck, Player player, ArrayList<Player> players) {
        this.market = market;
        this.devDeck = devDeck;
        this.player = player;
        this.players = players;
    }

    /**
     * Getter and Setter
     */
    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

}
