package com.abby.main;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.Parent;

import com.abby.persistence.ExperimentManager;
import com.abby.persistence.SettingsManager;
import com.abby.ui.AppController;


// fix:
// add temp backup file situation in datamanager.appendcumfile()
// going back to scene1 the selected expmnt doesnt show, back to scene2 the seconds is reset
// scene2 seconds spinner sometimes goes down when up button clicked
// make so behavior name cant be null in experimentdialog
// fix nonexistent file/parse fail in experimentmanager and settingsmanager
// check/fix resultConverter use in dialogs
// settingscontroller - change bidirectional binding of settings to changelistener maybe?


public class App extends Application {

    // should these be static? 
    public static String dataDirectory = Util.getDataDirectory();
    public static SettingsManager settingsMan = new SettingsManager();
    public static ExperimentManager experimentMan = new ExperimentManager(); 
    public static AppController ctrl = new AppController();
    
    public static ObservableList<Arena> arenaList = FXCollections.observableArrayList(new ArrayList<Arena>());
    public static Experiment selectedExperiment;            // the experiment configuration to use
    public static String selectedVideo;                     // URI of the video to use
    public static Integer seconds;                          // number of seconds to observe each arena in a cycle

    public static String winTitle = "Flytime (I still don't have an actual name) - [beta version or whatever]";
    public static String winIcon = "/fruit-fly.png";
    public static String stylesheet;
    private String initialScene = "/screen1.fxml"; 

    @Override
    public void start(Stage primaryStage) throws Exception { 
        if (App.settingsMan.boolSettings.get("appearanceMode").get()) {
            stylesheet = "/darkstyle.css";
        }
        else {
            stylesheet = "/style.css";
        }
        Parent root = FXMLLoader.load(getClass().getResource(initialScene));
        primaryStage.setTitle(winTitle);
        primaryStage.getIcons().add(new Image(winIcon));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(stylesheet);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { 
        launch(args); 
    }
}
