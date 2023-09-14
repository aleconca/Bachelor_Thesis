package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LeaderCardTest {

    @Test
    public void testLeaderCard(){
        Resource coin = new Resource("coin");
        ArrayList<Resource> ris = new ArrayList<>();
        ris.add(coin);
        LeaderCard l = new LeaderCard(3,null,false,ris);

        assertEquals(3, l.getVictory_points());
        l.setVictory_points(4);
        assertEquals(4, l.getVictory_points());
        l.setActive(true);
        assertTrue(l.isActive());
        ArrayList<DevelopCard.Colour> col = new ArrayList<>();
        col.add(DevelopCard.Colour.G);
        col.add(DevelopCard.Colour.P);
        l.setRequirements(col);
        assertEquals(2, l.getRequirements().size());
        assertEquals(1, l.getResourcesreq().size());

        ArrayList<DevelopCard.Colour> col1 = new ArrayList<>();
        col1.add(DevelopCard.Colour.P);
        col1.add(DevelopCard.Colour.G);
        ArrayList<DevelopCard.Colour> col2 = new ArrayList<>();
        col2.add(DevelopCard.Colour.Y);
        col2.add(DevelopCard.Colour.G);
    }

    @Test
    public void testActiveLeaderCard(){
        ArrayList<DevelopCard.Colour> col1 = new ArrayList<>();
        col1.add(DevelopCard.Colour.G);
        ArrayList<DevelopCard.Colour> col2 = new ArrayList<>();
        col2.add(DevelopCard.Colour.B);
        ArrayList<DevelopCard.Colour> col3 = new ArrayList<>();
        col3.add(DevelopCard.Colour.P);
        ArrayList<DevelopCard.Colour> col4 = new ArrayList<>();
        col4.add(DevelopCard.Colour.G);
        ArrayList<DevelopCard.Colour> col5 = new ArrayList<>();
        col5.add(DevelopCard.Colour.G);
        ArrayList<DevelopCard.Colour> col6 = new ArrayList<>();
        col6.add(DevelopCard.Colour.Y);
        UpgradedProduction u = new UpgradedProduction(3,col1,1,false,null,null);
        WhiteMarble w = new WhiteMarble(Marble.color.g,3,col2,false,null);
        SaleProductionStone s1 = new SaleProductionStone(1,col3, false,null);
        SaleProductionCoin s2 = new SaleProductionCoin(1,col4, false,null);
        SaleProductionServant s3 = new SaleProductionServant(1,col5, false,null);
        SaleProductionShield s4 = new SaleProductionShield(1,col6, false,null);

        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");
        ArrayList<Resource> res = new ArrayList<>();
        res.add(coin);
        res.add(servant);
        res.add(shield);
        res.add(stone);
        ArrayList<Resource> res2 = new ArrayList<>();
        res2.add(coin);
        res2.add(servant);
        res2.add(shield);
        res2.add(stone);
        ExtraDeposit e = new ExtraDeposit(stone, 3,null,false,res);

        Player p = new Player("c", 1, true);
        Player p1 = new Player("c", 1, true);
        p.getDashboard().getCoffer().addResources(res2);
        DevelopCard d = new DevelopCard(1,null,1,null,null);
        d.setColour(DevelopCard.Colour.G);
        ArrayList<DevelopCard> dev = new ArrayList<>();
        dev.add(d);
        CardSlot cardSlot = new CardSlot(dev);
        p.getDashboard().getCardSlot().add(cardSlot);

        assertTrue(u.ActiveLeaderCard(p, u));
        d.setColour(DevelopCard.Colour.B);
        assertTrue(w.ActiveLeaderCard(p, w));
        d.setColour(DevelopCard.Colour.P);
        assertTrue(s1.ActiveLeaderCard(p,s1));
        d.setColour(DevelopCard.Colour.G);
        assertTrue(s2.ActiveLeaderCard(p,s2));
        assertTrue(s3.ActiveLeaderCard(p,s3));
        assertFalse(s4.ActiveLeaderCard(p,s4));
        assertTrue(e.ActiveLeaderCard(p,e));
        assertFalse(e.ActiveLeaderCard(p1,e));
    }
}