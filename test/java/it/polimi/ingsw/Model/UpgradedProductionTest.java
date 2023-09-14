package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.Exceptions.LevelDoNotExistsException;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Resource;
import it.polimi.ingsw.server.Model.UpgradedProduction;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UpgradedProductionTest {

    @Test
    public void testUpgradedProduction() throws IncorrectMoveException, ArrayDontFitException, LevelDoNotExistsException {
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");
        ArrayList<Resource> coins1 = new ArrayList<>();
        coins1.add(coin);
        ArrayList<Resource> stones1 = new ArrayList<>();
        stones1.add(stone);
        ArrayList<Resource> shields1 = new ArrayList<>();
        shields1.add(shield);
        ArrayList<Resource> servants1 = new ArrayList<>();
        servants1.add(servant);
        UpgradedProduction u = new UpgradedProduction(1,null,1,false,null,coin);
        UpgradedProduction u1 = new UpgradedProduction(1,null,1,false,null,stone);
        UpgradedProduction u2= new UpgradedProduction(1,null,1,false,null,shield);
        UpgradedProduction u3= new UpgradedProduction(1,null,1,false,null,servant);
        UpgradedProduction u4= new UpgradedProduction(1,null,1,false,null,servant);

        Player p = new Player("c", 1 , true);
        assertEquals(1, u.getLevel());
        assertEquals(coin, u.getResourceForProduction());
        p.getDashboard().getStorage().insertResources(coins1,1);
        p.getDashboard().getStorage().insertResources(stones1,2);
        p.getDashboard().getStorage().insertResources(shields1,3);
        u4.ability("coin", p);
        p.getDashboard().getCoffer().addResources(servants1);

        u.ability("coin", p);
        assertEquals(1, p.getDashboard().getFaithRoute().getFaithIndicator());
        u1.ability("stone", p);
        assertEquals(2, p.getDashboard().getFaithRoute().getFaithIndicator());
        u2.ability("shield", p);
        assertEquals(3, p.getDashboard().getFaithRoute().getFaithIndicator());
        u3.ability("servant", p);
        assertEquals(4, p.getDashboard().getFaithRoute().getFaithIndicator());
        u2.ability("se", p);



    }

}