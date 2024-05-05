package main;

// Experiments are the sets of behaviors to be scored, premade experiments can be selected from (and modified?)
// can make new experiments with custom behavior sets and save them to be available to select in future runs, info stored in json file

import java.util.HashMap;
import java.util.Map;

public class Experiment {
    private String name;
    private Map<Integer, String> behaviors;

    // constructors
    public Experiment(String name, Map<Integer, String> behaviors) throws MaxBehaviorsExceededException {
        if (behaviors.size() <= 9) {
            this.name = name;
            this.behaviors = behaviors;
        }
        else {
            throw new MaxBehaviorsExceededException("Max behaviors exceeded, experiment not created");
        }
    }
    public Experiment(String name) {
        this.name = name;
        this.behaviors = new HashMap<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
 
    public void addBehavior(String behav) throws MaxBehaviorsExceededException {
        int num = this.behaviors.size() + 1;
        if (num <= 9) {
            behaviors.put(num, behav);
        }
        else {   
            throw new MaxBehaviorsExceededException("Max behaviors exceeded, behavior not added");
        }
    }

    public void setBehaviors(Map<Integer, String> behavs) throws MaxBehaviorsExceededException {
        if (behavs.size() <= 9) {
            this.behaviors = behavs;
        }
        else {    
            throw new MaxBehaviorsExceededException("Max behaviors exceeded, no behaviors added");
        }
    }

    public Map<Integer, String> getBehaviors() {
        return behaviors;
    }

}
