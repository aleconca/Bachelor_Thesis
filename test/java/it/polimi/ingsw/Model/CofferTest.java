package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.Coffer;
import it.polimi.ingsw.server.Model.Resource;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CofferTest {

    @Test
    public void testCoffer(){
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");
        ArrayList<Resource> ris = new ArrayList<>();
        ris.add(coin);
        ris.add(stone);

        Coffer c = new Coffer();
        c.addResources(ris);

        assertEquals(2, c.getResources().size());
        Coffer c1 = new Coffer();
        ArrayList<Resource> ris1 = new ArrayList<>();
        ris1.add(new Resource("coin"));
        ris1.add(new Resource("coin"));
        ris1.add(new Resource("stone"));
        ris1.add(new Resource("coin"));
        c1.addResources(ris1);
        assertEquals(3, c1.numberOf("coin"));
    }

}