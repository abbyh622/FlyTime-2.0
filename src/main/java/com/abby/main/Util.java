package com.abby.main;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;


public class Util {
    // valid characters for behavior key bindings
    private static List<Character> validChars = new ArrayList<Character>(List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'));

    // generic popup error message for dialogs
    // want to change this so its not an alert window
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
}
