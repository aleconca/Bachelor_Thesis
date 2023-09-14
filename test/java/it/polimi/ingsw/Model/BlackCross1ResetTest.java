package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Controller.Game;
import it.polimi.ingsw.server.Model.BlackCross1Reset;
import it.polimi.ingsw.server.Model.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlackCross1ResetTest {

    @Test
    public void testEffect(){
        Game game = new Game();
        Player p = new Player("c", 1, true);
        game.getPlayers().add(p);

        BlackCross1Reset b = new BlackCross1Reset();
        b.effect(game);
        assertEquals(1,game.getPlayers().get(0).getDashboard().getFaithRoute().getFaithIndicatorLorenzo());
        assertEquals(7, game.getActionSignalStack().getActionSignals().size());
    }

}