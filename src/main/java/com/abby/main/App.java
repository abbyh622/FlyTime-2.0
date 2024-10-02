package com.abby.main;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import com.abby.persistence.ExperimentManager;
import com.abby.persistence.SettingsManager;

public class App extends Application {

    // should these be static? 
    public static String dataDirectory = Util.getDataDirectory();
    public static ExperimentManager experimentMan = new ExperimentManager();        // shitty superhero names
    public static SettingsManager settingsMan = new SettingsManager();
    public static ObservableList<Arena> arenaList = FXCollections.observableArrayList(new ArrayList<Arena>());
    public static Experiment selectedExperiment;            // the experiment configuration to use
    public static String selectedVideo;                     // URI of the video to use
    public static Integer seconds;                          // number of seconds to observe each arena in a cycle

    public static String prevScene;                         // the previous scene visited, updated each scene change
    public static String stylesheet = "/style.css";
    private String initialScene = "/screen1.fxml"; 

    @Override
    public void start(Stage primaryStage) throws Exception { 
        Parent root = FXMLLoader.load(getClass().getResource(initialScene));
        primaryStage.setTitle("flytime :)");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(stylesheet);
        primaryStage.setScene(scene);
        primaryStage.show();

        // fix nonexistent file/parse fail in experimentmanager and settingsmanager
        // check/fix resultConverter use in dialogs
        // put scene loader methods in one class and call them from each?
        // recordingcontroller - make width of volume button constant
        // settingscontroller - change bidirectional binding of settings to changelistener maybe?

    }

    public static void main(String[] args) { 
        launch(args); 
    }
}
