package ui;

import java.net.URL;
import java.security.Key;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import main.App;
import main.Arena;
import main.KeyBehaviorPair;

// seconds input field not restricted, arrows dont work

public class Controller2 implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String prevScene = "screen1.fxml";
    private String nextScene = "recordingscreen.fxml";

    @FXML 
    private HBox experimentHbox;
    @FXML 
    private TableView<Arena> arenaTable;
    @FXML
    private TableView<KeyBehaviorPair> behaviorTable;
    @FXML
    private Spinner<Integer> secondSpinner;
    // should keybehaviorpairs list be in app instead
    private ObservableList<KeyBehaviorPair> keyBindings = FXCollections.observableArrayList(App.selectedExperiment.getBehaviorPairs());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set up arena table 
        TableColumn<Arena, Number> numberCol = new TableColumn<>("Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("num"));
        TableColumn<Arena, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        arenaTable.getColumns().addAll(numberCol, descCol);
        arenaTable.setItems(App.arenaList);

        // show experiment name and behavior key bindings
        Label experimentLabel = new Label(App.selectedExperiment.getName());  
        experimentHbox.getChildren().add(experimentLabel);

        TableColumn<KeyBehaviorPair, String> behaviorCol = new TableColumn<>("Behavior");
        behaviorCol.setCellValueFactory(new PropertyValueFactory<>("behavior"));
        TableColumn<KeyBehaviorPair, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        behaviorTable.getColumns().addAll(behaviorCol, keyCol);
        behaviorTable.setItems(keyBindings);
        behaviorTable.setSelectionModel(null);

        // set up spinner
        SpinnerValueFactory<Integer> valFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 5);
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

        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this arena?");
        confirmDelete.setHeaderText("Confirm Deletion");
        confirmDelete.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                App.arenaList.remove(selected);
            }
        });
    }

    public void next(ActionEvent e) throws Exception {
        // go to next screen
        // check that everything is valid first
        App.seconds = secondSpinner.getValue();

        root = FXMLLoader.load(getClass().getResource(nextScene));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void back(ActionEvent e) throws Exception {
        // go to previous screen
        root = FXMLLoader.load(getClass().getResource(prevScene));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}