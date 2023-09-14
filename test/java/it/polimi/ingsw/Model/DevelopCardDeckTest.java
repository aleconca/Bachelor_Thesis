package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Controller.Game;
import it.polimi.ingsw.server.Model.DevelopCardDeck;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevelopCardDeckTest {

    @Test
    public void testDevelopCardDeck(){
        DevelopCardDeck d = DevelopCardDeck.getInstance();

        assertEquals(16, d.getLvl1().size());
        assertEquals(16, d.getLvl2().size());
        assertEquals(16, d.getLvl3().size());

    }
}