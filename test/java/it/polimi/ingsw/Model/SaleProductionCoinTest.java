package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.SaleProductionCoin;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaleProductionCoinTest {

    @Test
    public void testSaleProductionCoin(){
        SaleProductionCoin s = new SaleProductionCoin(1,null, true,null);
        assertTrue(s.ability());
        SaleProductionCoin s1 = new SaleProductionCoin(1,null, false,null);
        assertFalse(s1.ability());
    }

}