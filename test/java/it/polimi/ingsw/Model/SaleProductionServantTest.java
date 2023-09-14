package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.SaleProductionServant;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaleProductionServantTest {

    @Test
    public void testSaleProductionServant(){
        SaleProductionServant s = new SaleProductionServant(1,null, true,null);
        assertTrue(s.ability());
        SaleProductionServant s1 = new SaleProductionServant(1,null, false,null);
        assertFalse(s1.ability());
    }

}