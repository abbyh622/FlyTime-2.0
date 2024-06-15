package main;
// Arena (roi) contains a description of the flies (user input) and a list of intervals that will be added to

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class Arena {
    // using javafx propterties for binding to ui elements
    // num is simplestringproperty because you cant bind simpleintegerproperty to text fields
    private SimpleStringProperty num;
    private SimpleStringProperty description;
    private ArrayList<Interval> intervals;

    // constructors
    public Arena(Integer n, String desc) {
        this.num = new SimpleStringProperty(Integer.toString(n));
        this.description = new SimpleStringProperty(desc);
        this.intervals = new ArrayList<Interval>();
    }
    public Arena(String desc) {
        this.description = new SimpleStringProperty(desc);
        this.num = new SimpleStringProperty();
        this.intervals = new ArrayList<Interval>();
    }
    public Arena() {
        this.description = new SimpleStringProperty();
        this.num = new SimpleStringProperty();
        this.intervals = new ArrayList<Interval>();
    }

    // time passed in from controller
    // return interval so controller can use it for recording 
    public Interval addInterval(String time) {
        Interval newInterval = new Interval(time);
        intervals.add(newInterval);
        System.out.println("interval created in arena " + this.num.get() + ", " + time);
        return newInterval;
    }

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setNum(Integer n)  {
        this.num.set(n.toString());
    }
    public Integer getNum() {
        return Integer.parseInt(num.get());
    }
    public SimpleStringProperty getNumProperty() {
        return num;
    }

    public void setDescription(String desc) {
        this.description.set(desc);
    }
    public String getDescription() {
        return description.get();
    }
    public SimpleStringProperty getDescriptionProperty() {
        return description;
    }

    public boolean isEmpty() {
        return (this.getDescription() == null || this.getDescription() == "");
    }
}
