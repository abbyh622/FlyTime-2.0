package com.abby.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.abby.main.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InstructionsController implements Initializable {
    @FXML
    private VBox aboutVbox;
    @FXML
    private VBox experimentsVbox;
    @FXML
    private VBox arenasVbox;
    @FXML
    private VBox scoringVbox;
    @FXML
    private VBox outputVbox;

    private String aboutText = "ummmmmmmmmmmm......";
    private String experimentsText = "Experiments are sets of behaviors used for scoring. Each behavior has a unique key (a single letter or number) that is used to record occurrances of that behavior during scoring; up to nine behaviors are allowed in an experiment. \n\nYou can create custom experiments and define your own behaviors and corresponding keys through the 'Create New' buttons on the first setup screen and in settings. You can also edit or delete existing experiments in settings. \n\nExperiments are stored locally, so any experiments you create or edit will be available to use in future sessions (and deleted experiments will not).";
    private String arenasText = "Arenas are separate regions or groups (ROIs) in a video and are scored independently from each other. \n\nYou can add arenas to score on the second setup screen one by one by clicking 'Add Arena' and entering a description. It is suggested to use a description that is meaningful for your experiment, such as the genotype, sex, drug condition, etc. of the flies in the arena. \n\nThe arena numbers define the order that the arenas will be iterated through during scoring, and are set automatically but can be changed. After an arena has been created, you can right-click on it to edit or delete.";
    private String scoringText = "Press the space bar to start, pause, and resume scoring. \n\nWhen the video is playing, pressing a key on the keyboard that is linked to a behavior will record that the behavior occurred in the currently active arena during the current time interval. The arenas are iterated through, repeating for the length of the video, each remaining active for the specified duration (set as 'Interval length' on the second setup screen). \n\nWhen the active arena changes, a beep sound is played and the arena number shown on the lower right is updated. \n\nDuring each time interval that an arena is active, each behavior can only be recorded once, pressing the same key again will have no effect.";
    private String outputText = "The data output screen shows the recorded behavior scores for each arena in each time interval, along with the video name, date scored, and experiment details. Each row in the tables shows the details for one time interval, with the timestamp at the interval's start in the leftmost column followed by values indicating whether each behavior was present. \n\nDisplay mode: \nThe 'visual data display' setting (in the settings screen) controls whether the session data is shown (and exported) with behavior scores represented by 'true'/'false' or 'X'/'-' indicating whether a behavior occurred in an interval or not. Visual display is turned on by default (showing 'X'/'-') as the on-screen display and session files are intended to present a simple view of the raw score data. \n\nSession files:\nThe 'export session file' button exports the scoring data shown on the screen as a text file. The default output location is your local downloads folder; this can be changed in the settings. \n\nCumulative files: \nCumulative files are intended for programmatic analysis, databases, or spreadsheets and to aggregate score data from multiple videos using the same experiment behavior set. 'Create cumulative file' will create and export a new cumulative file for the experiment type containing the scored data from the session; 'append cumulative file' will allow you to select an existing file to append the current session data to. [Note: there are currently no checks for the experiment type/behavior set when appending a file, if you modify an experiment's behaviors and try to append data to a previous file for that experiment it may result in incomplete data or errors, same with trying to append data to a file with a different experiment type] \nThese files contain a single table, with each row being the data from one arena. Instead of showing behavior scores for each time interval, the total occurrences of each behavior are totaled for each arena (the number of intervals scored and interval duration are also included). Cumulative files are exported as tab-delimited text files and should be able to be opened in Excel and easily read into databases in R, SQL, etc.";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // using Text wasnt working right so using Label instead
        Label aboutLabel = new Label(aboutText);
        aboutLabel.setWrapText(true);
        aboutLabel.setPadding(new Insets(6, 6, 12, 6));    // seems to match the titledpane content
        aboutLabel.getStyleClass().add("instructionsText");
        aboutVbox.getChildren().add(aboutLabel);

        Label experimentsLabel = new Label(experimentsText);
        experimentsLabel.setWrapText(true);
        experimentsLabel.getStyleClass().add("instructionsText");
        experimentsVbox.getChildren().add(experimentsLabel);

        Label arenasLabel = new Label(arenasText);
        arenasLabel.setWrapText(true);
        arenasLabel.getStyleClass().add("instructionsText");
        arenasVbox.getChildren().add(arenasLabel);

        Label scoringLabel = new Label(scoringText);
        scoringLabel.setWrapText(true);
        scoringLabel.getStyleClass().add("instructionsText");
        scoringVbox.getChildren().add(scoringLabel);

        Label outputLabel = new Label(outputText);
        outputLabel.setWrapText(true);
        outputLabel.getStyleClass().add("instructionsText");
        outputVbox.getChildren().add(outputLabel);
        // outputVbox.setVgrow(outputLabel, Priority.ALWAYS);
    }

    public void back(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.prevScene);
    }

    public void settingsScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.settingsScene);
    }
}


