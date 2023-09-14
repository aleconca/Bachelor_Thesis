package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.Exceptions.LevelDoNotExistsException;
import it.polimi.ingsw.server.Model.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testPlayer(){
        Player p = new Player("c" , 1 , true);
        assertEquals("c" , p.getName());
        assertEquals(1, p.getNumber());
        p.setName("ciccio");
        p.setNumber(2);
        assertEquals("ciccio", p.getName());
        assertEquals(2, p.getNumber());
        assertNotNull(p.getDashboard());

        ArrayList<DevelopCard.Colour> c1 = new ArrayList<>(2);
        c1.add(DevelopCard.Colour.Y);
        c1.add(DevelopCard.Colour.P);
        LeaderCard l1 = new SaleProductionCoin(2, c1, false, null);
        p.getLeaderCardsOfPlayer().add(l1);
        assertEquals(1, p.getLeaderCardsOfPlayer().size());

        p.addVictoryPoints(2);
        assertEquals(2, p.getVictorypoints());

        p.setHasActivatedRIV(true);
        assertTrue(p.getHasActivatedRIV());

        p.setLeaderCard1(true);
        p.setLeaderCard2(true);
        assertTrue(p.isLeaderCard1());
        assertTrue(p.isLeaderCard2());

        p.setBaseAction(false);
        assertFalse(p.isBaseAction());
    }

    @Test
    public void testPlayerColorsLvlRes() throws IncorrectMoveException, ArrayDontFitException, LevelDoNotExistsException {
        Player p = new Player("c" , 1 , true);
        Resource coin = new Resource("coin");
        Resource servant = new Resource("servant");
        Resource stone = new Resource("stone");
        Resource shield = new Resource("shield");
        Resource faith = new Resource("faith indicator");

        ArrayList<Resource> cost = new ArrayList<>();
        cost.add(shield);
        cost.add(shield);

        ArrayList<Resource> requires = new ArrayList<>();
        requires.add(coin);

        ArrayList<Resource> produces = new ArrayList<>();
        produces.add(faith);

        DevelopCard devCard = new DevelopCard(1,cost,1,requires,produces);
        devCard.setColour(DevelopCard.Colour.G);

        ArrayList<DevelopCard> dev = new ArrayList<>();
        dev.add(devCard);

        CardSlot c = new CardSlot(dev);
        p.getDashboard().getCardSlot().add(c);

        assertEquals(1, p.playerColors().size());
        assertEquals(1, p.playerLvl().size());

        ArrayList<Resource> stones = new ArrayList<>();
        stones.add(stone);
        stones.add(stone);
        ArrayList<Resource> coins = new ArrayList<>();
        coins.add(coin);
        ArrayList<Resource> ser = new ArrayList<>();
        ser.add(servant);
        ser.add(servant);
        ArrayList<Resource> shields = new ArrayList<>();
        shields.add(shield);
        shields.add(shield);
        shields.add(shield);

        p.getDashboard().getStorage().insertResources(coins,1);
        p.getDashboard().getStorage().insertResources(ser,2);
        p.getDashboard().getStorage().insertResources(shields,3);

        p.getDashboard().getCoffer().addResources(stones);

        assertEquals(8, p.playerRes().size());
    }

}