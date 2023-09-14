package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Market;
import it.polimi.ingsw.server.Model.Player;

import java.util.ArrayList;

/**
 * Message to print DevCardGrid, Market and Dashboard at the start of each turn
 */
public class PrintEveryThingMsg extends MessageToClient{

    private Market market;
    private Player player;
    private ArrayList<Player> players = new ArrayList<>();

    /**
     * Constructor
     */
    public PrintEveryThingMsg(Market market, Player player, ArrayList<Player> players) {
        this.market = market;
        this.player = player;
        this.players = players;
    }

    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) {
        view.ShowMarket(market);
        if (players.size()>1) {
            view.ShowDashboardMulti(player, players);
        }
        else view.ShowDashboardSingle(player);
    }
}
