package main;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.Parent;

import persistence.ExperimentManager;

public class App extends Application {

    // should these be static? 
    public static ExperimentManager experimentMan = new ExperimentManager();        // shitty superhero name
    public static ObservableList<Arena> arenaList = FXCollections.observableArrayList(new ArrayList<Arena>());
    public static Experiment selectedExperiment;            // the experiment configuration to use
    public static String selectedVideo;                     // URI of the video to use
    public static Integer seconds;                          // number of seconds to observe each arena in a cycle

    // public static ObservableMap<String, Object> metadata;   // video metadata


    private String initialScene = "/ui/screen1.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        experimentMan.loadExperiments();

        Parent root = FXMLLoader.load(getClass().getResource(initialScene));
        primaryStage.setTitle("Abby's Cool Behavior Scoring App Slay");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) { 
        launch(args); 

    }
}
