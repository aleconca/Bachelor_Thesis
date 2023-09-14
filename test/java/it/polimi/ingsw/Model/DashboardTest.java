package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.Coffer;
import it.polimi.ingsw.server.Model.Dashboard;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Storage;
import org.junit.Test;

import static org.junit.Assert.*;

public class DashboardTest {

    @Test
    public void testDashboard(){
        Dashboard d = new Dashboard();

        assertNotNull(d.getFaithRoute());
        assertNotNull(d.getCardSlot());
        Storage s = new Storage();
        d.setStorage(s);
        assertNotNull(d.getStorage());
        Coffer c = new Coffer();
        d.setCoffer(c);
        assertNotNull(d.getCoffer());
        assertNotNull(d.getBasicProduction());

        Player p = new Player("p", 1, true);
        assertEquals(0, p.getDashboard().numberOfDevelopCard());
    }

}