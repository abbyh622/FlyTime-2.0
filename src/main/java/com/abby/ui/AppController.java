package com.abby.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;

import com.abby.main.App;

// scene switch method
// error and confirm dialog methods from util

public class AppController {
    public static String scene1 = "/screen1.fxml"; 
    public static String scene2 = "/screen2.fxml";
    public static String recordingScene = "/recordingscreen.fxml";
    public static String endScene = "/endscreen.fxml";
    public static String settingsScene = "/settingsscreen.fxml";
    public String prevScene;           // previous scene visited, updated each scene change except recording (for returning from settings scene)
    private String stylesheet;
    private double curWidth;           // current window dimensions, to preserve size when switching scenes
    private double curHeight;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public AppController() {
        // appearancemode true = dark mode on
        if (App.settingsMan.boolSettings.get("appearanceMode").get()) {
            stylesheet = "/darkstyle.css";
        }
        else {
            stylesheet = "/style.css";
        }
    }

    public void switchScene(ActionEvent e, String newScene) throws Exception {
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        curWidth = stage.getWidth();
        curHeight = stage.getHeight();

        root = FXMLLoader.load(getClass().getResource(newScene));
        scene = new Scene(root);
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);

        if (newScene == recordingScene && (curWidth < 900 || curHeight < 700)) {
            stage.setWidth(900);
            stage.setHeight(700);
        }
        else {
            stage.setWidth(curWidth);
            stage.setHeight(curHeight);
        }

        stage.show();
    }
}
