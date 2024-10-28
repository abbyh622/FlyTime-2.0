package com.abby.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import com.abby.main.App;
import com.abby.main.Arena;
import com.abby.main.Util;

// number field restrictions not working

public class ArenaDialog extends Dialog<Arena> {
    private Arena arena;

    private TextField numberField = new TextField();
    private TextField descriptionField = new TextField();

    // constructor
    // if adding new arena, a is an empty arena created in controller
    // if editing existing arena, a is the selected arena
    public ArenaDialog(Arena a) {
        super();
        this.arena = a;
        if (arena.isEmpty()) {
            setTitle("Add Arena");
        }
        else {
            setTitle("Edit Arena");
        }

        buildWindow();
        setPropertyBindings();
    }

    private void buildWindow() {
        DialogPane pane = getDialogPane();
        pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        // numberField.setPromptText("");
        numberField.setPrefWidth(40);
        descriptionField.setPromptText("Description");

        HBox content = new HBox(10);
        content.getChildren().addAll(numberField, descriptionField);
        pane.setContent(content);

        // configure OK button
        Node okButton = pane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            // prevent default button action
            event.consume();
            // validate input and close dialog
            if (validateInput()) {
                setResult(arena);
            }
        });

        // restrict numberField to integer input
        numberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  
                numberField.setText(newValue.replaceAll("[^\\d]", ""));     // ^ = not
            }
        });
    }

    private void setPropertyBindings() {
        numberField.textProperty().bindBidirectional(arena.getNumProperty());
        descriptionField.textProperty().bindBidirectional(arena.getDescriptionProperty());
    }

    private boolean validateInput() {
        String num = numberField.getText();
        String desc = descriptionField.getText();

        // check that fields are filled
        if (num == null || num.isEmpty() || desc == null || desc.isEmpty()) {
            Util.showError(getDialogPane().getScene().getWindow(), "All fields must be filled");
            return false;
        }

        // check for duplicate number assignment
        if (getArenaNums().contains(num)) {
            Util.showError(getDialogPane().getScene().getWindow(), "An arena with this number already exists");
            return false;
        }

        return true;
    }

    // move to util class ?
    // makes list of numbers assigned to arenas - for checking for attempted duplicates 
    public List<String> getArenaNums() {
        List<String> arenaNums = new ArrayList<String>();
        for (Arena a : App.arenaList) {
            // exclude current arena (will be in arenaList if editing)
            if (a != this.arena) { 
                arenaNums.add(a.getNum().toString());
            }
        }
        return arenaNums;
    }
}
