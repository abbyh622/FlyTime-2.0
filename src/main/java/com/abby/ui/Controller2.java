package com.abby.ui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import com.abby.main.App;
import com.abby.main.Arena;
import com.abby.main.KeyBehaviorPair;
import com.abby.main.Util;

// seconds input field not restricted, arrows dont work

public class Controller2 implements Initializable {
    private Stage stage;

    @FXML 
    private HBox experimentHbox;
    @FXML 
    private TableView<Arena> arenaTable;    // context menu is in the fxml file
    @FXML
    private TableView<KeyBehaviorPair> behaviorTable;
    @FXML
    private Spinner<Integer> secondSpinner;
    private Integer sec;
    // should keybehaviorpairs list be in app instead
    private ObservableList<KeyBehaviorPair> keyBindings = FXCollections.observableArrayList(App.selectedExperiment.getBehaviorPairs());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.ctrl.prevScene = AppController.scene2;
        // set up arena table 
        TableColumn<Arena, Number> numberCol = new TableColumn<>("Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("num"));
        TableColumn<Arena, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        // set column widths
        numberCol.prefWidthProperty().bind(arenaTable.widthProperty().multiply(0.29));
        descCol.prefWidthProperty().bind(arenaTable.widthProperty().multiply(0.69));
        arenaTable.getColumns().addAll(numberCol, descCol);
        arenaTable.setItems(App.arenaList);

        // show experiment name and behavior key bindings
        Label experimentLabel = new Label(App.selectedExperiment.getName());  
        experimentLabel.setFont(Font.font("System", FontWeight.BOLD, 18.0));
        experimentLabel.setTextFill(Color.CADETBLUE);
        experimentHbox.getChildren().add(experimentLabel);

        // populate tableview with behavior names and keys
        TableColumn<KeyBehaviorPair, String> behaviorCol = new TableColumn<>("Behavior");
        behaviorCol.setCellValueFactory(new PropertyValueFactory<>("behavior"));
        TableColumn<KeyBehaviorPair, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        // set column widths
        behaviorCol.prefWidthProperty().bind(behaviorTable.widthProperty().multiply(0.69));
        behaviorCol.setResizable(false);
        keyCol.prefWidthProperty().bind(behaviorTable.widthProperty().multiply(0.29));
        keyCol.setResizable(false);
        behaviorTable.getColumns().addAll(behaviorCol, keyCol);
        behaviorTable.setItems(keyBindings);
        behaviorTable.setSelectionModel(null);

        // set up spinner
        if (App.seconds != null) {
            sec = App.seconds;
        }
        else {
            // default if not set yet
            sec = 5;    
        }
        SpinnerValueFactory<Integer> valFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, sec);
        secondSpinner.setValueFactory(valFactory);
    }

    // dialog for adding new arena
    public void showAddDialog() {
        // create new arena with number initialized 
        Dialog<Arena> dialog = new ArenaDialog(new Arena(App.arenaList.size() + 1, null));
        Optional<Arena> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get() instanceof Arena) {
                App.arenaList.add(result.get());
                arenaTable.refresh(); 
            }
        } 
    }

    // dialog for editing existing arena
    public void showEditDialog() {
        Arena selected = arenaTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
    
        Dialog<Arena> dialog = new ArenaDialog(selected);
        Optional<Arena> result = dialog.showAndWait();
        if (result.isPresent()) {
            arenaTable.refresh(); 
        } 
    }

    public void deleteArena() {
        Arena selected = arenaTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = Util.showConfirmDialog(stage, "Are you sure you want to delete arena " + selected.getNum() + " (" + selected.getDescription() + ")?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                App.arenaList.remove(selected);
                // reset arena numbers after deleting
                for (int i = 0; i < App.arenaList.size(); i++) {
                    App.arenaList.get(i).setNum(i + 1);
                }
            }
        }
    }

    // go to next screen
    public void next(ActionEvent e) throws Exception {
        // check that everything is valid first
        if (App.arenaList.isEmpty()) {
            Util.showError(stage, "Enter at least one arena");
            return;
        }
        App.seconds = secondSpinner.getValue();
        App.ctrl.switchScene(e, App.ctrl.recordingScene);
    }
    
    public void back(ActionEvent e) throws Exception {
        // go to previous screen
        App.ctrl.switchScene(e, App.ctrl.scene1);
    }

    public void settingsScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.settingsScene);
    }
}