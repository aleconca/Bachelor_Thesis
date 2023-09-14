package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Controller.Game;
import it.polimi.ingsw.server.Model.DevelopCard;
import it.polimi.ingsw.server.Model.DiscardPurpleSignal;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscardPurpleSignalTest {

    @Test
    public void testEffect(){
        Game game = new Game();
        remove(game);
        DiscardPurpleSignal d = new DiscardPurpleSignal();
        d.effect(game);
        assertEquals(1, numberlvl1(game));
        d.effect(game);
        assertEquals(0, numberlvl1(game));
        d.effect(game);
        assertEquals(0, numberlvl1(game));
        assertEquals(1, numberlvl2(game));
        d.effect(game);
        assertEquals(0, numberlvl2(game));
        d.effect(game);
        assertEquals(0, numberlvl2(game));
        assertEquals(1, numberlvl3(game));
        d.effect(game);
        assertEquals(0, numberlvl3(game));
    }

    private int numberlvl1(Game game){
        int c=0;
        for (int i=0; i<game.getDevelopCardsDeck().getLvl1().size(); i++){
            if (game.getDevelopCardsDeck().getLvl1().get(i).getColour() == DevelopCard.Colour.P)
                c++;
        }
        return c;
    }

    private int numberlvl2(Game game){
        int c=0;
        for (int i=0; i<game.getDevelopCardsDeck().getLvl2().size(); i++){
            if (game.getDevelopCardsDeck().getLvl2().get(i).getColour() == DevelopCard.Colour.P)
                c++;
        }
        return c;
    }

    private int numberlvl3(Game game){
        int c=0;
        for (int i=0; i<game.getDevelopCardsDeck().getLvl3().size(); i++){
            if (game.getDevelopCardsDeck().getLvl3().get(i).getColour() == DevelopCard.Colour.P)
                c++;
        }
        return c;
    }

    private void remove(Game game){
        for (int i=0; i<game.getDevelopCardsDeck().getLvl1().size(); i++){
            if (game.getDevelopCardsDeck().getLvl1().get(i).getColour() == DevelopCard.Colour.P) {
                game.getDevelopCardsDeck().getLvl1().remove(i);
                break;
            }
        }
    }


}