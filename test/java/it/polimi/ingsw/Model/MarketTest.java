package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidColumn;
import it.polimi.ingsw.Exceptions.InvalidInput;
import it.polimi.ingsw.Exceptions.InvalidRow;
import it.polimi.ingsw.server.Model.Marble;
import it.polimi.ingsw.server.Model.Market;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarketTest {

    private Marble.color[] colors1, colors2;

    @Test
    public void testMarket(){
        Market market = new Market();
        Market market1 = new Market();
        assertNotEquals(market,market1);
    }

    @Test
    public void testChooseRow() throws InvalidInput, InvalidRow {
        int i=2;
        Market market = new Market();
        colors1 = market.getColorsRow(i,market.getMat());
        market.chooseRow(i);
        colors2 = market.getColorsRow(i,market.getMat());
        assertNotEquals(colors1, colors2 );  //Confronto riga prima della chiamata con riga dopo chiamata
    }

    @Test
    public void testChooseColumn() throws InvalidInput, InvalidColumn {
        int j=2;
        Market market = new Market();
        colors1 = market.getColorsColumn(j,market.getMat());
        market.chooseColumn(j);
        colors2 = market.getColorsColumn(j,market.getMat());
        assertNotEquals(colors1, colors2 );
    }//Confronto colonna prima della chiamata con colonna dopo chiamata


    @Test
    public void testgetRow() throws InvalidRow, InvalidColumn {
        Market m = new Market();
        m.getRow(2);
        m.getColumn(2);
    }


}