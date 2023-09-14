package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.SaleProductionShield;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaleProductionShieldTest {

    @Test
    public void testSaleProductionShield(){
        SaleProductionShield s = new SaleProductionShield(1,null, true,null);
        assertTrue(s.ability());
        SaleProductionShield s1 = new SaleProductionShield(1,null, false,null);
        assertFalse(s1.ability());
    }

}