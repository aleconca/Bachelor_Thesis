package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.LeaderCardsDeck;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeaderCardsDeckTest {

    @Test
    public void testLeaderCardDeck(){
        LeaderCardsDeck l = LeaderCardsDeck.getInstance();

        assertEquals(16, l.getLeaderCS().size());
    }

}