package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.Box;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoxTest {

    @Test
    public void testBox(){
        Box b = new Box(1);

        assertEquals(1, b.getBoxNumber());

        b.setBoxNumber(2);
        assertEquals(2, b.getBoxNumber());
        b.setIndicatorIsHere(true);
        assertTrue(b.isIndicatorIsHere());
        b.setPopeBox(true);
        b.setReportGotActivated(true);
        assertTrue(b.isPopeBox());
        assertTrue(b.isReportGotActivated());
        b.setReportBox(1);
        assertEquals(1, b.getReportBox());
        b.setVictoryPoints(3);
        assertEquals(3, b.getVictoryPoints());
    }

}