package com.abby;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.abby.main.Arena;
import com.abby.main.Interval;

public class ArenaTest {

    @Test
    public void constructorTest() {
        Arena arena = new Arena("test description");
        assertNotNull(arena);
        assertEquals("test description", arena.getDescription());
        assertEquals("test description", arena.getDescriptionProperty().get());
        assertTrue(arena.getIntervals().isEmpty());
    }

    @Test 
    public void setDescriptionTest() {
        Arena arena = new Arena("initial");
        arena.setDescription("updated");
        assertEquals("updated", arena.getDescription());
        assertEquals("updated", arena.getDescriptionProperty().get());
    }
    @Test 
    public void setEmptyDescriptionTest() {
        Arena arena = new Arena("initial");
        arena.setDescription("");
        assertEquals("", arena.getDescription());
        assertEquals("", arena.getDescriptionProperty().get());
    }
    @Test 
    public void setNullDescriptionTest() {
        Arena arena = new Arena("initial");
        arena.setDescription(null);
        assertNull(arena.getDescription());
        assertNull(arena.getDescriptionProperty().get());
    }

    @Test 
    public void getDescriptionTest() {
        Arena arena = new Arena("test description");
        assertEquals("test description", arena.getDescription());
        assertEquals("test description", arena.getDescriptionProperty().get());
    }
    @Test
    public void getEmptyDescriptionTest() {
        Arena arena = new Arena("");
        assertEquals("", arena.getDescription());
        assertEquals("", arena.getDescriptionProperty().get());
    }
    @Test
    public void getNullDescriptionTest() {
        Arena arena = new Arena();
        arena.setDescription(null);
        assertNull(arena.getDescription());
        assertNull(arena.getDescriptionProperty().get());
    }

    @Test 
    public void getIntervalsTest() {
        Arena arena = new Arena("test description");
        assertTrue(arena.getIntervals().isEmpty());
        arena.addInterval("10:00");
        assertEquals(1, arena.getIntervals().size());
    }
    
    @Test 
    public void addIntervalTest() {
        Arena arena = new Arena("test description");
        assertTrue(arena.getIntervals().isEmpty());
        arena.addInterval("10:00");
        assertEquals(1, arena.getIntervals().size());
    }

    @Test 
    public void isEmptyTest_NotEmpty() {
        Arena arena = new Arena("test description");
        assertFalse(arena.isEmpty());
    }
    @Test 
    public void isEmptyTest_Empty() {
        Arena arena = new Arena("");
        assertTrue(arena.isEmpty());
    }
    @Test 
    public void isEmptyTest_Null() {
        Arena arena = new Arena();
        arena.setDescription(null);
        assertTrue(arena.isEmpty());
    }

    @Test 
    public void setNumTest() {
        Arena arena = new Arena("test description");
        arena.setNum(10);
        assertEquals(Integer.valueOf(10), arena.getNum());
        assertEquals("10", arena.getNumProperty().get());
    }
    @Test 
    public void getNumTest() {
        Arena arena = new Arena(10, "test description");
        assertEquals(Integer.valueOf(10), arena.getNum());
        assertEquals("10", arena.getNumProperty().get());
    }
    @Test 
    public void constructorTest_NullDescription() {
        Arena arena = new Arena((String)null);
        assertNotNull(arena);
        assertNull(arena.getDescription());
        assertTrue(arena.getIntervals().isEmpty());
    }
    @Test 
    public void constructorTest_NullNum() {
        Arena arena = new Arena((Integer)null, "test description");
        assertNotNull(arena);
        assertEquals("test description", arena.getDescription());
        assertTrue(arena.getIntervals().isEmpty());
        assertNull(arena.getNum());
    }
    @Test 
    public void setNumTest_Null() {
        Arena arena = new Arena("test description");
        arena.setNum(null);
        assertNull(arena.getNum());
        assertEquals("null", arena.getNumProperty().get());
    }
    @Test 
    public void addIntervalTest_Null() {
        Arena arena = new Arena("test description");
        assertTrue(arena.getIntervals().isEmpty());
        arena.addInterval(null);
        assertEquals(1, arena.getIntervals().size());
        // Assuming Interval class can handle null times
    }
    @Test 
    public void addIntervalTest_EmptyString() {
        Arena arena = new Arena("test description");
        assertTrue(arena.getIntervals().isEmpty());
        arena.addInterval("");
        assertEquals(1, arena.getIntervals().size());
        // Assuming Interval class can handle empty strings
    }
    @Test 
    public void getIntervalsTest_ModifyReturnedCollection() {
        Arena arena = new Arena("test description");
        arena.addInterval("10:00");
        ArrayList<Interval> intervals = arena.getIntervals();
        intervals.add(new Interval("11:00"));
        assertEquals(1, arena.getIntervals().size());
        // Ensuring that modifying the returned collection does not affect the arena's intervals
    }
}

// public class ArenaTest {

//     @Test
//     public void constructorTest() {
//         Arena arena = new Arena("test description");
//         assertNotNull(arena);
//         assertEquals("test description", arena.getDescription());
//         assertEquals("test description", arena.getDescriptionProperty().get());
//         assertTrue(arena.getIntervals().isEmpty());
//     }

//     @Test 
//     public void setDescriptionTest() {
//         Arena arena = new Arena("initial");
//         arena.setDescription("updated");
//         assertEquals("updated", arena.getDescription());
//         assertEquals("updated", arena.getDescriptionProperty().get());
//     }
//     @Test 
//     public void setEmptyDescriptionTest() {
//         Arena arena = new Arena("initial");
//         arena.setDescription("");
//         assertEquals("", arena.getDescription());
//         assertEquals("", arena.getDescriptionProperty().get());
//     }
//     @Test 
//     public void setNullDescriptionTest() {
//         Arena arena = new Arena("initial");
//         arena.setDescription(null);
//         assertNull(arena.getDescription());
//         assertNull(arena.getDescriptionProperty().get());
//     }

//     @Test 
//     public void getDescriptionTest() {
//         Arena arena = new Arena("test description");
//         assertEquals("test description", arena.getDescription());
//         assertEquals("test description", arena.getDescriptionProperty().get());
//     }
//     @Test
//     public void getEmptyDescriptionTest() {
//         Arena arena = new Arena("");
//         assertEquals("", arena.getDescription());
//         assertEquals("", arena.getDescriptionProperty().get());
//     }
//     @Test
//     public void getNullDescriptionTest() {
//         Arena arena = new Arena();
//         arena.setDescription(null);
//         assertNull(arena.getDescription());
//         assertNull(arena.getDescriptionProperty().get());
//     }

//     @Test 
//     public void getIntervalsTest() {
//         Arena arena = new Arena("test description");
//         assertTrue(arena.getIntervals().isEmpty());
//         arena.addInterval("10:00");
//         assertEquals(1, arena.getIntervals().size());
//     }
    
//     @Test 
//     public void addIntervalTest() {
//         Arena arena = new Arena("test description");
//         assertTrue(arena.getIntervals().isEmpty());
//         arena.addInterval("10:00");
//         assertEquals(1, arena.getIntervals().size());
//     }

//     @Test 
//     public void isEmptyTest_NotEmpty() {
//         Arena arena = new Arena("test description");
//         assertFalse(arena.isEmpty());
//     }
//     @Test 
//     public void isEmptyTest_Empty() {
//         Arena arena = new Arena("");
//         assertTrue(arena.isEmpty());
//     }
//     @Test 
//     public void isEmptyTest_Null() {
//         Arena arena = new Arena();
//         arena.setDescription(null);
//         assertTrue(arena.isEmpty());
//     }

//     @Test 
//     public void setNumTest() {
//         Arena arena = new Arena("test description");
//         arena.setNum(10);
//         assertEquals(Integer.valueOf(10), arena.getNum());
//         assertEquals("10", arena.getNumProperty().get());
//     }
//     @Test 
//     public void getNumTest() {
//         Arena arena = new Arena(10, "test description");
//         assertEquals(Integer.valueOf(10), arena.getNum());
//         assertEquals("10", arena.getNumProperty().get());
//     }
// }

// public class ArenaTest {

//     @Test
//     public void constructorTest() {
//         Arena arena = new Arena("test description");
//         assertNotNull(arena);
//         assertEquals("test description", arena.getDescription());
//         assertTrue(arena.getIntervals().isEmpty());
//     }

//     @Test 
//     public void setDescriptionTest() {
//         Arena arena = new Arena("initial");
//         arena.setDescription("updated");
//         assertEquals("updated", arena.getDescription());
//     }
//     @Test 
//     public void setEmptyDescriptionTest() {
//         Arena arena = new Arena("initial");
//         arena.setDescription("");
//         assertEquals("", arena.getDescription());
//     }
//     @Test 
//     public void setNullDescriptionTest() {
//         Arena arena = new Arena("initial");
//         arena.setDescription(null);
//         assertNull(arena.getDescription());
//     }

//     @Test 
//     public void getDescriptionTest() {
//         Arena arena = new Arena("test description");
//         assertEquals("test description", arena.getDescription());
//     }
//     @Test
//     public void getEmptyDescriptionTest() {
//         Arena arena = new Arena("");
//         assertEquals("", arena.getDescription());
//     }
//     @Test
//     public void getNullDescriptionTest() {
//         Arena arena = new Arena(null);
//         assertNull(arena.getDescription());
//     }

//     @Test 
//     public void getIntervalsTest() {

//     }
    
// }
