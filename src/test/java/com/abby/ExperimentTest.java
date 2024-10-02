package com.abby;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import com.abby.main.Experiment;
import com.abby.exceptions.MaxBehaviorsExceededException;

// add more edge case tests

public class ExperimentTest {

    // private Map<Character, String> testMap = new HashMap<>();

    // @Before
    // public void setup() {
    //     testMap.put('1', "one");
    //     testMap.put('2', "two");
    //     testMap.put('3', "three");
    // }
    
    // @Test
    // public void oneParamConstructorTest() {
    //     Experiment experiment = new Experiment("test");
    //     assertEquals("test", experiment.getName());
    //     assertNotNull(experiment.getBehaviors());
    //     assertTrue(experiment.getBehaviors().isEmpty());
    // }
    // @Test 
    // public void bothParamConstructorTest() throws MaxBehaviorsExceededException {
    //     Experiment experiment = new Experiment("test", testMap);
    //     assertEquals("test", experiment.getName());
    //     assertNotNull(experiment.getBehaviors());
    //     assertEquals(testMap, experiment.getBehaviors());
    // }
    // @Test (expected = MaxBehaviorsExceededException.class)
    // public void bothParamConstructorLimitTest() throws MaxBehaviorsExceededException {
    //     Map<Character, String> overflowMap = new HashMap<>();
    //     for (Integer i = 1; i <= 10; i++) {
    //         overflowMap.put(i.toString().charAt(0), "behavior " + i);
    //     }
    //     Experiment experiment = new Experiment("test", overflowMap);
    // }

    // @Test 
    // public void getNameTest() {
    //     Experiment experiment = new Experiment("test");
    //     assertEquals("test", experiment.getName());
    // }

    // @Test
    // public void setNameTest() {
    //     Experiment experiment = new Experiment("test");
    //     experiment.setName("new");
    //     assertEquals("new", experiment.getName());
    // }

    // @Test
    // public void addBehaviorTest() throws MaxBehaviorsExceededException {
    //     Experiment experiment = new Experiment("test");
    //     experiment.addBehavior("one");
    //     assertEquals(1, experiment.getBehaviors().size());
    //     assertEquals("one", experiment.getBehaviors().get('1'));
    //     experiment.addBehavior("two");
    //     assertEquals(2, experiment.getBehaviors().size());
    //     assertEquals("two", experiment.getBehaviors().get('2'));
    // }
    // @Test 
    // public void addMoreBehaviorTest() throws MaxBehaviorsExceededException {
    //     Experiment experiment = new Experiment("test", testMap);
    //     experiment.addBehavior("four");
    //     assertEquals(4, experiment.getBehaviors().size());
    //     assertEquals("four", experiment.getBehaviors().get('4'));
    // }
    // @Test (expected = MaxBehaviorsExceededException.class)
    // public void addBehaviorLimitTest() throws MaxBehaviorsExceededException {
    //     Experiment experiment = new Experiment("test");
    //     for (int i = 1; i <= 10; i++) {
    //         experiment.addBehavior("behavior " + i);
    //     }
    // }

    // @Test
    // public void getBehaviorsTest() throws MaxBehaviorsExceededException {
    //     Experiment experiment = new Experiment("test", testMap);
    //     assertEquals(testMap, experiment.getBehaviors());
    // }

    // @Test
    // public void setBehaviorsTest() throws MaxBehaviorsExceededException {
    //     Experiment experiment = new Experiment("test");
    //     experiment.setBehaviors(testMap);
    //     assertEquals(testMap, experiment.getBehaviors());
    // }
    // @Test
    // public void setNewBehaviorsTest()  throws MaxBehaviorsExceededException {
    //     HashMap<Character, String> newMap = new HashMap<>();
    //     newMap.put('4', "four");
    //     newMap.put('5', "five");
    //     Experiment experiment = new Experiment("test", testMap);
    //     experiment.setBehaviors(newMap);
    //     assertEquals(2, experiment.getBehaviors().size());
    //     assertEquals(newMap, experiment.getBehaviors());
    // }
    // @Test (expected = MaxBehaviorsExceededException.class)
    // public void setBehaviorsLimitTest() throws MaxBehaviorsExceededException {
    //     HashMap<Character, String> newMap = new HashMap<>();
    //     for (Integer i = 1; i <= 10; i++) {
    //         String behav = "behavior " + (i + 1);
    //         newMap.put(i.toString().charAt(0), behav);
    //     }
    //     Experiment experiment = new Experiment("test", testMap);
    //     experiment.setBehaviors(newMap);
    // }
}
