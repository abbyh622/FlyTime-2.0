package com.abby;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import com.abby.main.App;
import com.abby.main.Interval;

// add more recordBehavior tests and edge cases

public class IntervalTest {
    // private Interval interval;

    // @Before
    // public void setup() {
    //     App.behaviorNums = new Integer[]{1, 2, 3}; 
    //     interval = new Interval("10:00");
    // }

    // @Test
    // public void constructorTest() {
    //     assertEquals("10:00", interval.getStartTime());
    //     assertNotNull(interval.getScores());
    //     assertFalse(interval.getScores().isEmpty());
    // }

    // @Test
    // public void getStartTimeTest() {
    //     assertEquals("10:00", interval.getStartTime());
    // }

    // @Test
    // public void getScoresTest() {
    //     HashMap<Integer, Boolean> scores = interval.getScores();
    //     assertNotNull(scores);
    //     assertEquals(3, scores.size());
    //     assertFalse(scores.get(1));
    //     assertFalse(scores.get(2));
    //     assertFalse(scores.get(3));
    // }

    // @Test
    // public void recordBehaviorTest() {
    //     HashMap<Integer, Boolean> scores = interval.getScores();
    //     assertFalse(scores.get(1));
    //     assertFalse(scores.get(2));
    //     assertFalse(scores.get(3));
    //     interval.recordBehavior(1);
    //     assertTrue(scores.get(1));
    //     assertFalse(scores.get(2));
    //     assertFalse(scores.get(3));
    //     interval.recordBehavior(3);
    //     assertTrue(scores.get(1));
    //     assertFalse(scores.get(2));
    //     assertTrue(scores.get(3));
    // }
    // @Test
    // public void testRecordBehaviorInvalid() {
    //     HashMap<Integer, Boolean> scores = interval.getScores();
    //     assertFalse(scores.containsKey(4));
    //     interval.recordBehavior(4);
    //     assertFalse(scores.containsKey(4));
    // }
}
