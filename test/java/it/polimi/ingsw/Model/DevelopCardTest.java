package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.DevelopCard;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Resource;
import org.junit.Test;
import java.util.ArrayList;


import static org.junit.Assert.*;

public class DevelopCardTest {


    @Test
    public void resourcestoBuy(){
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

        DevelopCard devCard= new DevelopCard(1,cost,7,requires,produces);

        ArrayList<Resource> input = new ArrayList<>();
        input.add(coin);
        input.add(servant);
        input.add(shield);


        //assertEquals(devCard.checkResourcestoBuy(input,false,false,false,false),true);

    }


     @Test
    public void checkforProduction(){
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

         DevelopCard devCard= new DevelopCard(1,cost,7,requires,produces);

         ArrayList<Resource> input = new ArrayList<>();
         input.add(coin);
         input.add(servant);
         input.add(shield);

         ArrayList<Resource> output = new ArrayList<>();
         output.add(coin);
         output.add(servant);
         output.add(shield);

         assertEquals(devCard.checkResourcesforProduction(input),output);

     }

     @Test
    public void testGetSet(){
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");
        DevelopCard d = new DevelopCard(1,null,3,null,null);
        d.setLevel(2);
        assertEquals(2, d.getLevel());
        d.setColour(DevelopCard.Colour.G);
        d.setColour(DevelopCard.Colour.P);
        d.setColour(DevelopCard.Colour.Y);
        d.setColour(DevelopCard.Colour.B);
        assertEquals(DevelopCard.Colour.B, d.getColour());
        ArrayList<Resource> ris = new ArrayList<>();
        ArrayList<Resource> ris1 = new ArrayList<>();
        ArrayList<Resource> ris2 = new ArrayList<>();
        ris.add(coin);
        ris1.add(stone);
        ris2.add(servant);
        d.setCost(ris);
        assertEquals(coin, d.getCost().get(0));
        d.setVictoryPoints(2);
        assertEquals(2, d.getVictoryPoints());
        d.setRequires(ris1);
        d.setProduces(ris2);
        assertEquals(stone, d.getRequires().get(0));
        assertEquals(servant, d.getProduces().get(0));

        ArrayList<Resource> res = new ArrayList<>();
        res.add(coin);
        res.add(servant);
        res.add(shield);
        res.add(stone);
        ArrayList<Resource> res1 = new ArrayList<>();
         ArrayList<Resource> res2 = new ArrayList<>();
         ArrayList<Resource> res3 = new ArrayList<>();
         ArrayList<Resource> res4 = new ArrayList<>();
        res1.add(stone);
        res2.add(servant);
        res3.add(coin);
        res4.add(shield);
        d.setCost(res1);

         d.setCost(res2);

         d.setCost(res3);

         d.setCost(res4);

     }


}