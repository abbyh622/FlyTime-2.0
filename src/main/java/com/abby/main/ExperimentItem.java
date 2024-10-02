package com.abby.main;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import com.abby.ui.TreeDisplayable;


// wrapper class for Experiments to use in TreeView

public class ExperimentItem implements TreeDisplayable {
    private Experiment experiment;
    private String name;
    private List<KeyBehaviorPair> behaviors;

    public ExperimentItem(Experiment e) {
        this.experiment = e;
        this.name = e.getName();
        this.behaviors = this.experiment.getBehaviorPairs();
    }

    public String getText() {
        return this.name;
    }

    public Experiment getExperiment() {
        return this.experiment;
    }
    public List<KeyBehaviorPair> getBehaviors() {
        return this.behaviors;
    }
}
