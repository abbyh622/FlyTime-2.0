package com.abby.ui;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

// dialog for showing specific instructions
// currently just for recordingscreen help

public class HelpDialog extends Dialog<Object> {    // dont need to return anything so just making it object so it stops yelling about parameterizing
    // move these from instructionscontroller to util
    private String scoringInstructions = "Press the space bar to start, pause, and resume scoring. \n\nWhen the video is playing, pressing a key on the keyboard that is linked to a behavior will record that the behavior occurred in the currently active arena during the current time interval. The arenas are iterated through, repeating for the length of the video, each remaining active for the specified duration (set as 'Interval length' on the second setup screen). \n\nWhen the active arena changes, a beep sound is played and the arena number shown on the lower right is updated. \n\nDuring each time interval that an arena is active, each behavior can only be recorded once, pressing the same key again will have no effect."; 

    public HelpDialog() {
        super();
        setTitle("Scoring instructions");
        buildWindow();
    }
    
    private void buildWindow() {
        DialogPane pane = getDialogPane();
        pane.getButtonTypes().add(ButtonType.CLOSE);

        Label text = new Label(scoringInstructions);
        text.setWrapText(true);
        text.setMaxWidth(400);
        
        VBox content = new VBox(10);
        content.getChildren().add(text);
        pane.setContent(content);
    }
}