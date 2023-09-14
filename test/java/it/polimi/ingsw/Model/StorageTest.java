package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.server.Model.Resource;
import it.polimi.ingsw.server.Model.Storage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class StorageTest {
    private Storage storage = new Storage();
    private Resource coin = new Resource("coin");
    private Resource stone = new Resource("stone");
    private Resource shield = new Resource("shield");
    private Resource servant = new Resource("servant");

    private static ArrayList<Resource> coins = new ArrayList<>(); //x1
    private static ArrayList<Resource> stones = new ArrayList<>(); //x2
    private static ArrayList<Resource> shields = new ArrayList<>(); //x3
    private static ArrayList<Resource> servants = new ArrayList<>(); //x4
    private static ArrayList<Resource> servants2 = new ArrayList<>(); //x2
    private static ArrayList<Resource> shields1 = new ArrayList<>(); //x1

    /*stones.add(new Resource("stone"));
    stones.add(new Resource("stone")); //x2
    shields.add(new Resource("shield"));
    shields.add(new Resource("shield"));
    shields.add(new Resource("shield")); //x3
    servants.add(new Resource("servant"));
    servants.add(new Resource("servant"));
    servants.add(new Resource("servant"));
    servants.add(new Resource("servant")); //x4*/

    @Before
    public void ArraySetter() {
        coins.add(0, new Resource("coin")); //x1
        stones.add(0, new Resource("stone"));
        stones.add(1, new Resource("stone")); //x2
        shields.add(0, new Resource("shield"));
        shields.add(1, new Resource("shield"));
        shields.add(2, new Resource("shield")); //x3
        servants.add(0, new Resource("servant"));
        servants.add(1, new Resource("servant"));
        servants.add(2, new Resource("servant"));
        servants.add(3, new Resource("servant")); //x4
        servants2.add(0, new Resource("servant"));
        servants2.add(1, new Resource("servant")); //x2
        shields1.add(0, new Resource("shield")); //x1

    }

    @Test
    public void setLvlTest() {
        try {
            storage.setLvl(1, coins);
            assertEquals("coin", storage.getLvl1().get(0).getResourcename());
            storage.setLvl(2, stones);
            assertEquals("stone", storage.getLvl2().get(0).getResourcename());
            storage.setLvl(3, shields);
            assertEquals("shield", storage.getLvl3().get(0).getResourcename());
        } catch (LevelDoNotExistsException | ArrayDontFitException e) {
            System.out.println("Level typed do not exists!");
        }
    }

    @Test
    public void InsertResourcesCorrectAndFullTest() {
        try {
            storage.insertResources(coins, 1);
            assertEquals("coin", storage.getLvl1().get(0).getResourcename());
            storage.insertResources(stones, 2);
            assertEquals("stone", storage.getLvl2().get(0).getResourcename());
            assertEquals("stone", storage.getLvl2().get(1).getResourcename());
            storage.insertResources(shields, 3);
            for(int i=0; i<3; i++) {
                assertEquals("shield", storage.getLvl3().get(i).getResourcename());
            }
        } catch (ArrayDontFitException | IncorrectMoveException | LevelDoNotExistsException e) {
            System.out.println("troppe risorse, o nei posti sbagliati!");
        }

    }

    @Test
    public void insertResourcesCorrectButNotFullTest() {
        ArrayList<Resource> coins2  = new ArrayList<>();
        coins2.add(new Resource("coin"));
        assertEquals(0, storage.getLvl1().size());
        try {
            storage.insertResources(coins2, 2);
            assertEquals("coin", storage.getLvl2().get(0).getResourcename());
            assertEquals(1, storage.getLvl2().size());
            storage.insertResources(servants2, 3);
            assertEquals("servant", storage.getLvl3().get(0).getResourcename());
            assertEquals("servant", storage.getLvl3().get(1).getResourcename());
            assertEquals(2, storage.getLvl3().size());
        } catch (ArrayDontFitException | IncorrectMoveException | LevelDoNotExistsException e) {
            System.out.println("Error");
        }
    }

    @Test
    public void insertResourcesToCheckArrayDontFitExceptionTest() {
        boolean exception = false;
        try {
            storage.insertResources(shields, 2);
        } catch (ArrayDontFitException | IncorrectMoveException | LevelDoNotExistsException e) {
            exception = true;
        }
        assertTrue(exception);
        assertEquals(0, storage.getLvl1().size());
        assertEquals(0, storage.getLvl3().size());
        assertEquals(0, storage.getLvl2().size());
    }

    @Test
    public void shouldNotBeSameResourcesInDifferentLevelsTest() {
        ArrayList<Resource> shields3 = new ArrayList<>();
        ArrayList<Resource> shields4 = new ArrayList<>();
        shields3.add(new Resource("shield"));
        shields3.add(new Resource("shield"));
        shields3.add(new Resource("shield"));
        shields4.add(new Resource("shield"));
        try{
            storage.insertResources(shields3, 3);
            storage.insertResources(shields4, 2);
        } catch (ArrayDontFitException | LevelDoNotExistsException e) {
            System.out.println("Test KO: Array don't fit or level don't exists");
        } catch (IncorrectMoveException e) {
            System.out.println("Test OK: Incorrect move");
        } finally {
            assertEquals("shield", storage.getLvl3().get(0).getResourcename());
            assertTrue(storage.getLvl1().isEmpty());
            assertTrue(storage.getLvl2().isEmpty());
            assertFalse(storage.getLvl3().isEmpty());
        }
    }

    @Test
    public void switchLevelTest() {
        try {
            storage.insertResources(coins, 2);
            storage.insertResources(stones, 3);
        } catch (ArrayDontFitException | IncorrectMoveException | LevelDoNotExistsException e) {
            System.out.println("Error in insert");
        }
        try {
            storage.switchLevels(2, 3);
        } catch (CannotSwitchLevelsException | LevelDoNotExistsException e) {
            System.out.println("Error in switch");
            System.out.println(e.getMessage());
        }
        assertEquals("coin", storage.getLvl3().get(0).getResourcename());
        assertEquals("stone", storage.getLvl2().get(0).getResourcename());
        assertTrue(storage.getLvl1().isEmpty());
    }

    @Test
    public void checkExceptionsInSwitchLevelsTest() {
        ArrayList<Resource> shields5 = new ArrayList<>();
        shields5.add(new Resource("shield"));
        shields5.add(new Resource("shield"));
        shields5.add(new Resource("shield"));
        ArrayList<Resource> coins3 = new ArrayList<>();
        coins3.add(new Resource("coin"));
        ArrayList<Resource> stones3 = new ArrayList<>();
        stones3.add(new Resource("stone"));
        stones3.add(new Resource("stone"));
        boolean exceptionC = false;
        boolean exceptionL = false;
        try {
            storage.insertResources(coins3, 1);
            storage.insertResources(stones3, 2);
            storage.insertResources(shields5, 3);
        } catch (ArrayDontFitException | IncorrectMoveException | LevelDoNotExistsException e) {
            e.printStackTrace();
            System.out.println("Error in insert");
        }

        try {
            storage.switchLevels(1,2);
        } catch (CannotSwitchLevelsException e) {
            exceptionC = true;
        } catch (LevelDoNotExistsException e) {
            System.out.println("Error in levels");
        }
        assertTrue(exceptionC);

        try {
            storage.switchLevels(1,4);
        } catch (CannotSwitchLevelsException e) {
            System.out.println("Error in switching");
        } catch (LevelDoNotExistsException e) {
            exceptionL = true;
        }
        assertTrue(exceptionL);
    }

    @Test
    public void freeLvlTest() {
        //freelvl2 in questo caso, le altre due sono identiche
        ArrayList<Resource> stones5 = new ArrayList<>();
        stones5.add(new Resource("stone"));
        stones5.add(new Resource("stone"));
        try {
            storage.insertResources(stones5, 2);
        } catch (ArrayDontFitException | IncorrectMoveException | LevelDoNotExistsException e) {
            System.out.println("eccezione");
        }
        assertTrue(storage.freeLvl2());
        assertEquals("stone", storage.getLvl3().get(0).getResourcename());
        assertEquals("stone", storage.getLvl3().get(1).getResourcename());
        assertTrue(storage.getLvl1().isEmpty());
        assertTrue(storage.getLvl2().isEmpty());
    }

    @Test
    public void testGetLvl() throws LevelDoNotExistsException, IncorrectMoveException, ArrayDontFitException, CannotSwitchLevelsException {
        Storage s = new Storage();
        assertNotNull(s.getLvl(1));
        assertNotNull(s.getLvl(2));
        assertNotNull(s.getLvl(3));

        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");
        ArrayList<Resource> coins = new ArrayList<>();
        coins.add(coin);
        ArrayList<Resource> stones = new ArrayList<>();
        stones.add(stone);
        ArrayList<Resource> shields = new ArrayList<>();
        shields.add(shield);

        s.insertResources(coins,1);
        s.insertResources(stones,2);
        s.insertResources(shields,3);

        s.switchLevels(3,1);
        s.switchLevels(3,2);
        try {
            s.switchLevels(3,3);
        } catch (CannotSwitchLevelsException | LevelDoNotExistsException e) {
        }
        try {
            s.switchLevels(2,2);
        } catch (CannotSwitchLevelsException | LevelDoNotExistsException e) {
        }

        try {
            s.switchLvls(1,2);
        } catch (CannotSwitchLevelsException | LevelDoNotExistsException e) {
        }

        try {
            s.switchLvls(2,1);
        } catch (CannotSwitchLevelsException | LevelDoNotExistsException e) {
        }
        try {
            s.switchLvls(2,2);
        } catch (CannotSwitchLevelsException | LevelDoNotExistsException e) {
        }


        try {
            s.moveResources(coins, 4);
        } catch (ArrayDontFitException | LevelDoNotExistsException ignored) {
        }

        s.getLvl2().remove(0);
        s.freeLvl1();


        s.freeLvl3();
        s.getLvl1().add(coin);
        s.getLvl3().add(coin);
        s.freeLvl3();

        s.toString();
    }
}