package main;
// Interval contains the record of whether each behavior happened in the given roi during the given time interval
// scores maps the input key for each behavior to a bool for whether it happened
// every time the timer beeps a new interval object is created and its scores values are initialized to false
// if a key is pressed corresponding to a behavior (1 for wing ext etc) then in that Interval scores[key] is updated to true
// when the timer beeps again the Interval object is added to the list of Intervals for that roi and a new Interval is created for the next roi

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.KeyCode;

public class Interval {
    private String startTime;   // timestamp when interval begins, not sure if should be string 
    private HashMap<KeyCode, Boolean> scores;

    // hashmap with all values initialized to false to create initial scores map for each interval
    private static Map<KeyCode, Boolean> defaultScores = new HashMap<>();
    static {
        for (KeyBehaviorPair p : App.selectedExperiment.getBehaviorPairs()) {
            defaultScores.put(p.getKeyCode(), false);
        }
    }

    // constructor 
    // time passed from main -> arena -> interval ?
    public Interval(String time) {
        this.startTime = time;
        this.scores = new HashMap<>(defaultScores);
    }

    // no setter because start time shouldnt be changed after creating the interval right 
    public String getStartTime() {
        return startTime;
    }

    public HashMap<KeyCode, Boolean> getScores() {
        return this.scores;
    }

    // if current value for the behavior is false, make true
    // return true if replacement was made to update ui in RecordingController
    public boolean recordBehavior(KeyCode b) {
        if (scores.replace(b, false, true)) {
            System.out.println(b + " recorded");
            return true;
        }
        return false;
    }
}
