package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.Resource;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {

    @Test
    public void testResource(){

        Resource testResource = new Resource("coin");
        Resource coin = new Resource("coin");
        Resource testResource1 = new Resource("shield");

        assertEquals("coin", testResource.getResourcename());
        assertEquals(Resource.Colour.y, testResource.getColor());


        assertFalse(testResource.equals(testResource1));
        assertTrue(testResource.equals(coin));


        assertEquals(testResource1.getResourcename(),"shield");
        assertEquals(testResource1.getColor(), Resource.Colour.b);

        Resource testResource2 = new Resource("stone");
        assertEquals("stone" , testResource2.getResourcename());
        assertEquals(testResource2.getColor(), Resource.Colour.g);

        Resource testResource3 = new Resource("servant");
        assertEquals(testResource3.getResourcename(),"servant");
        assertEquals(testResource3.getColor(), Resource.Colour.p);


        Resource res = new Resource("coin");
        res.setResourcename("stone");
        assertEquals("stone", res.getResourcename());

        res.setColor(Resource.Colour.g);
        assertEquals(Resource.Colour.g, res.getColor());
        res.equals("servant");
    }

    @Test
    public void testRes(){
        Resource r = new Resource("sdf");
    }


}