package com.abby.ui;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.abby.exceptions.MaxBehaviorsExceededException;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DefaultStringConverter;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome.FontAwesome;

import com.abby.main.App;
import com.abby.main.Experiment;
import com.abby.main.KeyBehaviorPair;
import com.abby.main.Util;


// should limit name input length?

public class ExperimentDialog extends Dialog<Experiment> {
    
    private Experiment experiment;
    private ObservableList<KeyBehaviorPair> behaviors = FXCollections.observableArrayList();
    private Map<Character, String> behaviorMap; 

    VBox mainLayout = new VBox(10);
    TextField nameField = new TextField();
    Button addButton = new Button("Add Behavior");

    // constructor
    public ExperimentDialog(Experiment e) {
        super();
        this.experiment = e;
        if (experiment.isEmpty()) {
            setTitle("New experiment type");
        }
        else {
            setTitle("Edit experiment");
        }
        buildWindow();
    }

    private void buildWindow() {
        DialogPane pane = getDialogPane();
        pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        pane.setPrefSize(400, 300);

        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));
        
        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER);
        nameBox.getChildren().addAll(new Label("Name"), nameField);

        mainLayout.setAlignment(Pos.CENTER);
        container.getChildren().addAll(nameBox, mainLayout, addButton);
        pane.setContent(container);

        if (!experiment.isEmpty()) {
            // fill observablelist with existing behaviors if editing experiment
            nameField.setText(experiment.getName());
            for (KeyBehaviorPair p : experiment.getBehaviorPairs()) {
                behaviors.add(p);
            }
        } else {
            // else add empty one for fields to display
            behaviors.add(new KeyBehaviorPair());
        }

        // display key behavior pairs and add listener to observable list
        updateFields();
        behaviors.addListener((ListChangeListener.Change<? extends KeyBehaviorPair> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    updateFields();
                }
            }
        });

        // behaviors.addListener((ListChangeListener.Change<? extends KeyBehaviorPair> change) -> updateFields());

        // configure add button
        addButton.setOnAction(e -> {
            if (behaviors.size() < 9) {
                behaviors.add(new KeyBehaviorPair());
                // expand pane window if needed
                pane.getScene().getWindow().sizeToScene();
            }
            else {
                Util.showError(pane.getScene().getWindow(), "Experiment already has maximum behaviors");
            }
        });

        // configure OK button
        Node okButton = pane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            // prevent default button action
            event.consume();
            // validate input and set attributes
            if (validateExperiment()) {
                updateExperiment();
                setResult(experiment);
                // add new experiment to list if creating, not if editing existing experiment
                if (this.getTitle() == "New experiment type") {
                    App.experimentMan.experiments.add(experiment);
                }
                // save experiments to json file after any changes/additions
                App.experimentMan.saveExperiments();
            }
        });
    }

    private void updateFields() {
        mainLayout.getChildren().clear();

        if (behaviors.size() >= 1) {
            HBox headerBox = new HBox(85);
            headerBox.setPadding(new Insets(10, 0, 0, 30));
            headerBox.getChildren().addAll(new Label("Key"), new Label("Behavior description"));
            mainLayout.getChildren().add(headerBox);
        }

        for (KeyBehaviorPair b : behaviors) {
            HBox hbox = new HBox(10);
            TextField keyField = new TextField(b.getKeyProperty().toString());
            keyField.setTextFormatter(getFormatter());
            TextField descField = new TextField(b.getBehavior());
            keyField.setPrefWidth(30);
            descField.setPrefWidth(250);
    
            keyField.textProperty().bindBidirectional(b.getKeyProperty());
            descField.textProperty().bindBidirectional(b.getBehaviorProperty());

            Button deleteButton = new Button();
            deleteButton.setGraphic(new FontIcon(FontAwesome.TRASH));
            deleteButton.setOnAction(event -> {
                // this exception shouldnt happen ever
                try { deleteBehavior(b); } 
                catch (MaxBehaviorsExceededException e) {
                    System.out.println("MaxBehaviorsExceededException occurred while deleting behavior");
                    e.printStackTrace(); }
            });
    
            hbox.getChildren().addAll(keyField, descField, deleteButton);
            hbox.setAlignment(Pos.CENTER);
            mainLayout.getChildren().add(hbox);
        }
    }

    public void deleteBehavior(KeyBehaviorPair behav) throws MaxBehaviorsExceededException {
        List<KeyBehaviorPair> behavPairs = this.experiment.getBehaviorPairs();
        behavPairs.remove(behav);
        this.experiment.setBehaviors(behavPairs);
        behaviors.setAll(this.experiment.getBehaviorPairs());
    }

    // should check that behavior descriptions are unique and not empty?
    private boolean validateExperiment() {
        // check that name isnt empty
        if (nameField.getText() == null || nameField.getText() == "") {
            Util.showError(getDialogPane().getScene().getWindow(), "Name field cannot be empty");
            return false;
        }
        // check that theres at least 1 behavior
        if (behaviors.isEmpty()) {
            Util.showError(getDialogPane().getScene().getWindow(), "Enter at least one behavior");
            return false;
        }
        // check that name isnt the same as a stored experiment's name
        for (Experiment e : App.experimentMan.experiments) {
            if (e.getName() == nameField.getText()) {
                Util.showError(getDialogPane().getScene().getWindow(), "An experiment with this name already exists");
                return false;
            }
        }
        // check that keys are unique and valid 
        Set<Character> keySet = new HashSet<Character>();
        for (KeyBehaviorPair p : behaviors) {
            if (p.getKey() == null) {
                Util.showError(getDialogPane().getScene().getWindow(), "All keys must be assigned");
                return false;
            }
            keySet.add(p.getKey());
            if (!Util.validateKeyChar(p.getKey())) {
                Util.showError(getDialogPane().getScene().getWindow(), "Key " + p.getKey() + " is not allowed \nKey must be a letter or number (A-Z, 0-9)");
                return false;
            }
        }

        // is this an okay way to check if keys are unique or is this stupid

        if (keySet.size() != behaviors.size()) {
            Util.showError(getDialogPane().getScene().getWindow(), "Duplicate keys found");
            return false;
        }
        return true;
    }

    private void updateExperiment() {
        // set name and behaviors
        try {
            if (experiment.isEmpty() || nameField.getText() != experiment.getName()) {
                experiment.setName(nameField.getText());
            }
            experiment.setBehaviors(behaviors);
        }
        catch (MaxBehaviorsExceededException e) {
            // this shouldn't happen if we limit the input fields to 9
            Util.showError(getDialogPane().getScene().getWindow(), "Error creating experiment; maximum behaviors exceeded");
        }
    }

    // generate text formatter to limit input of key fields to 1 char
    private TextFormatter<String> getFormatter() {
        TextFormatter<String> keyFormatter = new TextFormatter<String>(new DefaultStringConverter(), "", change -> {
            if (change.getControlNewText().length() > 1) {
                return null; 
            }
            return change; 
        });
        return keyFormatter;
    }
}