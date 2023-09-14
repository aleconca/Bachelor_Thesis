package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.Player;

import java.io.IOException;
import java.util.ArrayList;

public class EndGameMsg extends MessageToClient{

    private ArrayList<Integer> sumFinalPoints = new ArrayList<>(4) ;
    private ArrayList<Player> players;
    private boolean lorenzowin;
    private int n;
    private String winner;


    /**
     * To process the message in view
     */
    @Override
    public void process(ViewCLI view) throws IOException {
        if (n>1)
            view.EndGame(players,sumFinalPoints, winner);
        else if (n==1)
            view.EndGameSinglePlayer(players.get(0), sumFinalPoints.get(0), lorenzowin);
    }

    /**
     * Constructor
     */
    public EndGameMsg(ArrayList<Integer> sumFinalPoints, ArrayList<Player> players, boolean lorenzowin, int n, String winner) {
        this.sumFinalPoints = sumFinalPoints;
        this.players = players;
        this.lorenzowin = lorenzowin;
        this.n = n;
        this.winner = winner;
    }

}
