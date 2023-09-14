package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.PapalFavourCard;
import org.junit.Test;

import static org.junit.Assert.*;

public class PapalFavourCardTest {

    @Test
    public void testPapalFavourCard(){
        PapalFavourCard p = new PapalFavourCard(1);

        assertEquals(1, p.getValue());

        p.setValue(2);
        assertEquals(2, p.getValue());
    }
}