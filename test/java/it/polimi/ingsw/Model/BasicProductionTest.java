package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.Exceptions.LevelDoNotExistsException;
import it.polimi.ingsw.server.Model.BasicProduction;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Resource;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BasicProductionTest {

    @Test
    public void testRemove() throws IncorrectMoveException, ArrayDontFitException, LevelDoNotExistsException {
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");
        ArrayList<Resource> coins = new ArrayList<>();
        ArrayList<Resource> stones = new ArrayList<>();
        ArrayList<Resource> servants = new ArrayList<>();
        ArrayList<Resource> shields = new ArrayList<>();
        BasicProduction b = new BasicProduction();
        Player p = new Player("c", 1, true);
        coins.add(coin);
        servants.add(servant);
        shields.add(shield);
        stones.add(stone);
        p.getDashboard().getCoffer().addResources(coins);
        p.getDashboard().getStorage().insertResources(servants,1);
        p.getDashboard().getStorage().insertResources(stones,2);
        p.getDashboard().getStorage().insertResources(shields,3);
        b.remove(p,coin);
        assertEquals(0, p.getDashboard().getCoffer().getResources().size());
        b.remove(p,servant);
        assertEquals(0, p.getDashboard().getStorage().getLvl1().size());
        b.remove(p,stone);
        assertEquals(0, p.getDashboard().getStorage().getLvl2().size());
        b.remove(p,shield);
        assertEquals(0,p.getDashboard().getStorage().getLvl3().size());
    }

    @Test
    public void testChooseRes() throws IncorrectMoveException, ArrayDontFitException, LevelDoNotExistsException {
        Resource coin = new Resource("coin");
        Resource stone = new Resource("stone");
        Resource servant = new Resource("servant");
        Resource shield = new Resource("shield");

        Player p = new Player("c", 1, true);
        BasicProduction b =new BasicProduction();
        b.chooseRes("coin", p);
        b.chooseRes("servant", p);
        b.chooseRes("shield", p);
        b.chooseRes("stone", p);
        b.chooseRes("co", p);
        assertEquals(4, p.getDashboard().getCoffer().getResources().size());
        p.getDashboard().getCoffer().getResources().clear();

        ArrayList<Resource> coins = new ArrayList<>();
        ArrayList<Resource> stones = new ArrayList<>();
        ArrayList<Resource> servants = new ArrayList<>();
        ArrayList<Resource> shields = new ArrayList<>();
        coins.add(coin);
        servants.add(servant);
        shields.add(shield);
        stones.add(stone);
        p.getDashboard().getCoffer().addResources(coins);
        p.getDashboard().getStorage().insertResources(servants,1);
        p.getDashboard().getStorage().insertResources(stones,2);
        p.getDashboard().getStorage().insertResources(shields,3);

        assertTrue(b.checkWithoutRemovingRes(p, servant));
        assertTrue(b.checkWithoutRemovingRes(p, stone));
        assertTrue(b.checkWithoutRemovingRes(p, shield));
        assertTrue(b.checkWithoutRemovingRes(p, coin));
        p.getDashboard().getCoffer().getResources().clear();
        assertFalse(b.checkWithoutRemovingRes(p, coin));

        assertEquals(coin, b.chooseResForActivated("coin"));
        assertEquals(stone, b.chooseResForActivated("stone"));
        assertEquals(servant, b.chooseResForActivated("servant"));
        assertEquals(shield, b.chooseResForActivated("shield"));
        assertNotEquals(stone, b.chooseResForActivated("coin"));
    }

}