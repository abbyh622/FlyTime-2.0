package test;

import static org.junit.Assert.*;
import org.junit.Test;
import main.Arena;

public class ArenaTest {

    @Test
    public void constructorTest() {
        Arena arena = new Arena("test description");
        assertNotNull(arena);
        assertEquals("test description", arena.getDescription());
        assertTrue(arena.getIntervals().isEmpty());
    }

    @Test 
    public void setDescriptionTest() {
        Arena arena = new Arena("initial");
        arena.setDescription("updated");
        assertEquals("updated", arena.getDescription());
    }
    @Test 
    public void setEmptyDescriptionTest() {
        Arena arena = new Arena("initial");
        arena.setDescription("");
        assertEquals("", arena.getDescription());
    }
    @Test 
    public void setNullDescriptionTest() {
        Arena arena = new Arena("initial");
        arena.setDescription(null);
        assertNull(arena.getDescription());
    }

    @Test 
    public void getDescriptionTest() {
        Arena arena = new Arena("test description");
        assertEquals("test description", arena.getDescription());
    }
    @Test
    public void getEmptyDescriptionTest() {
        Arena arena = new Arena("");
        assertEquals("", arena.getDescription());
    }
    @Test
    public void getNullDescriptionTest() {
        Arena arena = new Arena(null);
        assertNull(arena.getDescription());
    }

    @Test 
    public void getIntervalsTest() {

    }
    
}
