package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.FaithRoute;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithRouteTest {

    @Test
    public void testFaithRoute(){
        FaithRoute f = new FaithRoute();

        assertEquals(25, f.getRoute().size());
        assertEquals(3, f.getPapalFavourCards().size());
        f.moveIndicatorLorenzo(1);
        f.moveIndicator(1);
        assertEquals(1, f.getFaithIndicatorLorenzo());
        assertEquals(1,f.getFaithIndicator());
        f.moveIndicator(5);
        f.moveIndicatorLorenzo(5);
        assertEquals(1, f.isInReportSlot());
        assertEquals(1, f.isInReportSlotLorenzo());
    }

}