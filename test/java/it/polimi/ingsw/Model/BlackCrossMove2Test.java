package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Controller.Game;
import it.polimi.ingsw.server.Model.BlackCross1Reset;
import it.polimi.ingsw.server.Model.BlackCrossMove2;
import it.polimi.ingsw.server.Model.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlackCrossMove2Test {

    @Test
    public void testEffect(){
        Game game = new Game();
        Player p = new Player("c", 1, true);
        game.getPlayers().add(p);

        BlackCrossMove2 b = new BlackCrossMove2();
        b.effect(game);
        assertEquals(2,game.getPlayers().get(0).getDashboard().getFaithRoute().getFaithIndicatorLorenzo());
    }

}