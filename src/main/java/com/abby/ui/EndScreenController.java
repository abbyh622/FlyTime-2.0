package com.abby.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.abby.main.*;


public class EndScreenController implements Initializable {
    // should datamanager be static ?
    private DataManager dm;

    @FXML
    private TextArea dataTextArea;
    @FXML
    private VBox outputVbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.ctrl.prevScene = AppController.endScene;
        dm = new DataManager();

        // set text area to monospaced font so spacing works right
        // dataTextArea.setStyle("-fx-font-family : 'Monospaced'");

        // display data on screen
        String dataDisplay = dm.getSessionTableString();
        dataTextArea.setText(dataDisplay);
    }

    public void exportSession() {
        // get video name without ext and create path, if file already exists show dialog asking to overwrite or make new file
        // String fileName = dm.getVideoName().split(".")[0] + ".txt"; 
        String fileName = dm.getVideoName() + ".txt"; 
        dm.setOutputPath(App.settingsMan.outputDirectory.get() + "\\" + fileName);
        // show message in side panel
        Label msg = new Label(dm.writeSessionFile());
        msg.setWrapText(true);
        outputVbox.getChildren().add(msg);
    }

    public void newCumFile() {
        // create file name as [experiment name]Cumulative or something and create path, if file already exists just make a new one
        // does not set the new file to be used as the experiment's cumulative file in settings, must be set manually
        String fileName = App.selectedExperiment.getName() + "_cumulative.txt";
        dm.setOutputPath(App.settingsMan.outputDirectory.get() + "\\" + fileName);
        // show message in side panel
        Label msg = new Label(dm.createCumulativeFile());
        msg.setWrapText(true);
        outputVbox.getChildren().add(msg);
    }

    public void appendCumFile() { 
        FileChooser fc = new FileChooser();
        Stage fcStage = new Stage();
        File file = fc.showOpenDialog(fcStage);
        // show message in side panel
        Label msg = new Label(dm.appendCumulativeFile(file));
        msg.setWrapText(true);
        outputVbox.getChildren().add(msg);
    }

    // thoughts: idk if its that useful to have cumulative files set for each experiment, would it be better to just use filechooser each time to select which file to append
    // if keeping the cumulative files setting i think delete the confirmation dialog for appending and show text on screen or dialog saying that data has been appended to [filename] or [filepath]
    // when appending, should probably make a temporary copy of the cumulative file before reading/modifying so its preserved if anything goes wrong right? what is standard procedure for this?
    // should session data file be automatically exported? i think probably yes  

    public void settingsScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.settingsScene);
    }

    // maybe put a confirmation dialog in this method like "exit to home screen?"
    public void done(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.scene1);
    }

    public void helpScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.instructionsScene);
    }
}
