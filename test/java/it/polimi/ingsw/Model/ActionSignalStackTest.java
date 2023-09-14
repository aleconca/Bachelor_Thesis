package it.polimi.ingsw.Model;

import it.polimi.ingsw.server.Model.ActionSignalStack;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActionSignalStackTest {

    @Test
    public void testActionSignalStack(){
        ActionSignalStack a = new ActionSignalStack();
        ActionSignalStack b = new ActionSignalStack();
        assertEquals(7, a.getActionSignals().size());
        assertNotEquals(b.getActionSignals(), a.getActionSignals());
    }

}