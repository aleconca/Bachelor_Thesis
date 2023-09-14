package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.Marble;
import it.polimi.ingsw.server.Model.WhiteMarble;
import org.junit.Test;

import static org.junit.Assert.*;

public class WhiteMarbleTest {

    @Test
    public void testWhiteMarble(){
        WhiteMarble w = new WhiteMarble(Marble.color.p,2,null,false,null);
        assertEquals(Marble.color.p , w.getColor());
        assertEquals(1 , w.whiteMarble(Marble.color.r));
        assertEquals(2 , w.whiteMarble(Marble.color.b));
        assertEquals(3 , w.whiteMarble(Marble.color.g));
        assertEquals(4 , w.whiteMarble(Marble.color.p));
        assertEquals(5 , w.whiteMarble(Marble.color.w));
        assertEquals(6 , w.whiteMarble(Marble.color.y));


        WhiteMarble w1 = new WhiteMarble(Marble.color.r,2,null,false,null);
        WhiteMarble w2 = new WhiteMarble(Marble.color.g,2,null,false,null);
        WhiteMarble w3 = new WhiteMarble(Marble.color.y,2,null,false,null);
        WhiteMarble w4 = new WhiteMarble(Marble.color.w,2,null,false,null);
        WhiteMarble w5 = new WhiteMarble(Marble.color.b,2,null,false,null);

        assertEquals("servant", w.whichResource());
        assertEquals("red", w1.whichResource());
        assertEquals("stone", w2.whichResource());
        assertEquals("coin", w3.whichResource());
        assertEquals("white", w4.whichResource());
        assertEquals("shield", w5.whichResource());

    }

}