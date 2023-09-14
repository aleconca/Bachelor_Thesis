package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInput;
import it.polimi.ingsw.server.Model.Marble;
import it.polimi.ingsw.server.Model.Resource;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarbleTest {

    @Test
    public void testMarble() throws InvalidInput {
        Marble m = new Marble(Marble.color.b);
        Marble m2 = new Marble(Marble.color.g);
        Marble m3 = new Marble(Marble.color.p);
        Marble m4 = new Marble(Marble.color.y);
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");

        assertEquals(Marble.color.b, m.getCol());
        assertEquals(coin, m4.transformInRes());
        assertEquals(shield, m.transformInRes());
        assertEquals(stone, m2.transformInRes());
        assertEquals(servant, m3.transformInRes());
    }

}