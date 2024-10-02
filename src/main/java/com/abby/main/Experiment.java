package com.abby.main;

// Experiments are the sets of behaviors to be scored, premade experiments can be selected from (and modified?)
// can make new experiments with custom behavior sets and save them to be available to select in future runs, info stored in json file

// make option to change key bindings, can use 1-9 as default when creating new experiments
// hack: use ascii values of the keys pressed as the behavior map key so u dont have to change the map params hehe
// should be fine bc addBehavior and setBehavior use the size of map not the key values

// currently limited to 9 behaviors, can think about changing that later if needed

import java.util.HashMap;
import java.util.Map;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

// import exceptions.InvalidCharException;
import com.abby.exceptions.MaxBehaviorsExceededException;

// change behaviors to be keybehaviorpairs or something
// should have the keycode in there 

public class Experiment {
    private SimpleStringProperty name;
    private List<KeyBehaviorPair> behaviors;

    // constructors
    public Experiment(String name, List<KeyBehaviorPair> behaviors) throws MaxBehaviorsExceededException {
        if (behaviors.size() <= 9) {
            this.name = new SimpleStringProperty(name);
            this.behaviors = new ArrayList<KeyBehaviorPair>(behaviors);
        }
        else {
            throw new MaxBehaviorsExceededException("Max behaviors exceeded, experiment not created");
        }
    }
    public Experiment(String name) {
        this.name = new SimpleStringProperty(name);
        this.behaviors = new ArrayList<KeyBehaviorPair>();
    }
    public Experiment() {
        this.name = new SimpleStringProperty();
        this.behaviors = new ArrayList<KeyBehaviorPair>();
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() {
        return name.get();
    }
    public SimpleStringProperty getNameProperty() {
        return name;
    }

    public void addBehavior(String behav) throws MaxBehaviorsExceededException {
        // setting the keys to numbers by default
        Integer num = this.behaviors.size() + 1;
        if (num <= 9) {
            behaviors.add(new KeyBehaviorPair(num.toString().charAt(0), behav));
        }
        else {   
            throw new MaxBehaviorsExceededException("Max behaviors exceeded, behavior not added");
        }
    }

    public void setBehaviors(List<KeyBehaviorPair> behavs) throws MaxBehaviorsExceededException {
        if (behavs.size() <= 9) {
            this.behaviors = behavs;
        }
        else {    
            throw new MaxBehaviorsExceededException("Max behaviors exceeded, no behaviors added");
        }
    }
    public List<KeyBehaviorPair> getBehaviorPairs() {
        return behaviors;
    }

    public boolean isEmpty() {
        return (this.getName() == null || this.getName() == "");
    }
    
    // // constructors
    // public Experiment(String name, Map<Character, String> behaviors) throws MaxBehaviorsExceededException {
    //     if (behaviors.size() <= 9) {
    //         this.name = new SimpleStringProperty(name);
    //         this.behaviors = behaviors;
    //     }
    //     else {
    //         throw new MaxBehaviorsExceededException("Max behaviors exceeded, experiment not created");
    //     }
    // }
    // public Experiment(String name) {
    //     this.name = new SimpleStringProperty(name);
    //     this.behaviors = new HashMap<>();
    // }
    // public Experiment() {
    //     this.name = new SimpleStringProperty();
    //     this.behaviors = new HashMap<>();
    // }

    // public void setName(String name) {
    //     this.name.set(name);
    // }
    // public String getName() {
    //     return name.get();
    // }
    // public SimpleStringProperty getNameProperty() {
    //     return name;
    // }

    // public void addBehavior(String behav) throws MaxBehaviorsExceededException {
    //     Integer num = this.behaviors.size() + 1;
    //     if (num <= 9) {
    //         behaviors.put(num.toString().charAt(0), behav);
    //     }
    //     else {   
    //         throw new MaxBehaviorsExceededException("Max behaviors exceeded, behavior not added");
    //     }
    // }

    // public void setBehaviors(Map<Character, String> behavs) throws MaxBehaviorsExceededException {
    //     if (behavs.size() <= 9) {
    //         this.behaviors = behavs;
    //     }
    //     else {    
    //         throw new MaxBehaviorsExceededException("Max behaviors exceeded, no behaviors added");
    //     }
    // }
    // public Map<Character, String> getBehaviors() {
    //     return behaviors;
    // }

    // // should be list or arraylist? 
    // public List<KeyBehaviorPair> getBehaviorPairs() {
    //     List<KeyBehaviorPair> keyBehaviorPairs = new ArrayList<KeyBehaviorPair>();
    //     behaviors.forEach((k, b) -> {
    //         keyBehaviorPairs.add(new KeyBehaviorPair(k, b));
    //     });
    //     return keyBehaviorPairs;
    // }

    // public boolean isEmpty() {
    //     return (this.getName() == null || this.getName() == "");
    // }
}
