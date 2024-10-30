package com.abby.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

        // display data on screen
        String dataDisplay = dm.getSessionTableString();
        dataTextArea.setText(dataDisplay);
        // export if auto export setting is on
        if (App.settingsMan.boolSettings.get("dataAutoExport").get() == true) {
            exportSession();
        }
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

    public void settingsScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.settingsScene);
    }

    // maybe put a confirmation dialog in this method like "exit to home screen?"
    public void done(ActionEvent e) throws Exception {
        // clear global vars from the session
        App.selectedVideo = null;
        App.selectedExperiment = null;
        App.arenaList = null;
        App.seconds = null;

        App.ctrl.switchScene(e, App.ctrl.scene1);
    }

    public void helpScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.instructionsScene);
    }
}
