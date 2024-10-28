package com.abby.main;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.stage.Window;


public class Util {
    // valid characters for behavior key bindings
    private static List<Character> validChars = new ArrayList<Character>(List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'));

    // generic popup error message for dialogs
    // want to change this so its not an alert window maybe
    // possible to have a label in the dialog that is set to the error message if occurs and prevent dialog from closing?
    public static void showError(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    // generic popup dialog to get confirmation
    public static Alert showConfirmDialog(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        return alert;
    }

    public static boolean validateKeyChar(Character c) {
        c = Character.toUpperCase(c);
        if (validChars.contains(c)) {
            return true;
        }
        return false;
    }

    // tooltip for indicating unfinished features that are visible in the ui
    public static Tooltip getTooltip() {
        return new Tooltip("Coming soon");
    }

    // get directory where app data is stored (settings.json, experiments.json)
    public static String getDataDirectory() {
        // use C:\Users\*user*\AppData\Local if exists
        String appData = System.getProperty("user.home") + "\\AppData\\Local";
        File appDataDir = new File(appData);
        if (appDataDir.exists()) {
            File flytimeData = new File(appData + "\\.flytime");
            if (flytimeData.exists()) {
                return flytimeData.getAbsolutePath();
            }
            else {
                flytimeData.mkdir();
                return flytimeData.getAbsolutePath();
            }
        }
        // if not for some reason use a folder in the app directory
        else {
            String appRoot = System.getenv("user.dir");
            File rootDataDir = new File(appRoot + "\\appdata");
            if (rootDataDir.exists()) {
                return rootDataDir.getAbsolutePath();
            }
            else {
                rootDataDir.mkdir();
                return rootDataDir.getAbsolutePath();
            }
        }
    }
}
