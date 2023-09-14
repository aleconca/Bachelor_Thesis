package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.SaleProductionStone;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaleProductionStoneTest {

    @Test
    public void testSaleProductionStone(){
        SaleProductionStone s = new SaleProductionStone(1,null, true,null);
        assertTrue(s.ability());
        SaleProductionStone s1 = new SaleProductionStone(1,null, false,null);
        assertFalse(s1.ability());
    }

}