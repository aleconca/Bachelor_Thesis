package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.ExtraDepositException;
import it.polimi.ingsw.server.Model.ExtraDeposit;
import it.polimi.ingsw.server.Model.Resource;
import static org.junit.Assert.*;
import org.junit.Test;

public class ExtraDepositTest {


    @Test
    public void testExtraDeposit() {
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");

        ExtraDeposit e1 = new ExtraDeposit(coin, 3, null,false,null);
        ExtraDeposit e2 = new ExtraDeposit(stone, 3, null,false,null);
        ExtraDeposit e3 = new ExtraDeposit(servant, 3, null,false,null);
        ExtraDeposit e4 = new ExtraDeposit(shield, 3, null,false,null);

        assertEquals(0, e1.getTwoExtraCoins().size());
        assertEquals(0, e2.getTwoExtraRocks().size());
        assertEquals(0, e3.getTwoExtraServants().size());
        assertEquals(0, e4.getTwoExtraShields().size());

        assertEquals(coin, e1.getType());

    }

    @Test
    public void testaddResource() throws ArrayDontFitException, ExtraDepositException {
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");


        ExtraDeposit e1 = new ExtraDeposit(coin, 3, null,false,null);
        ExtraDeposit e2 = new ExtraDeposit(stone, 3, null,false,null);
        ExtraDeposit e3 = new ExtraDeposit(servant, 3, null,false,null);
        ExtraDeposit e4 = new ExtraDeposit(shield, 3, null,false,null);

        e1.addExtraResource(coin);
        assertEquals(1, e1.getTwoExtraCoins().size());

        e2.addExtraResource(stone);
        assertEquals(1, e2.getTwoExtraRocks().size());

        e3.addExtraResource(servant);
        assertEquals(1, e3.getTwoExtraServants().size());

        e4.addExtraResource(shield);
        assertEquals(1, e4.getTwoExtraShields().size());

        e1.size();
        e2.size();
        e3.size();
        e4.size();

    }
}