package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Controller.Game;
import it.polimi.ingsw.server.Model.CardSlot;
import it.polimi.ingsw.server.Model.DevelopCard;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Resource;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardSlotTest {

    @Test
    public void returnVisibleCard() {
        Resource coin = new Resource("coin");
        Resource servant = new Resource("servant");
        Resource shield = new Resource("shield");

        ArrayList<Resource> cost= new ArrayList<>();
        cost.add(coin);
        cost.add(servant);
        cost.add(shield);

        ArrayList<Resource> requires= new ArrayList<>();
        requires.add(coin);
        requires.add(servant);
        requires.add(shield);

        ArrayList<Resource> produces= new ArrayList<>();
        produces.add(coin);
        produces.add(servant);
        produces.add(shield);

        DevelopCard card1 = new DevelopCard(1,cost,7,requires,produces);
        DevelopCard card2 = new DevelopCard(2,cost,2,requires,produces);
        DevelopCard card3 = new DevelopCard(3,cost,6,requires,produces);

        ArrayList<DevelopCard> slot = new ArrayList<>();
        slot.add(card1);
        slot.add(card2);
        slot.add(card3);
        CardSlot cardSlot = new CardSlot(slot);

    }
}