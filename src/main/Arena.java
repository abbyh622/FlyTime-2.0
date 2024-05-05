package main;
// Arena (roi) contains a description of the flies (user input) and a list of intervals that will be added to

import java.util.ArrayList;

public class Arena {
    private String description;
    private ArrayList<Interval> intervals;

    // constructor
    public Arena(String desc) {
        this.description = desc;
        this.intervals = new ArrayList<>();
    }

    // time passed in from main
    public void addInterval(String time) {
        intervals.add(new Interval(time));
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
    public String getDescription() {
        return description;
    }

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }
}
