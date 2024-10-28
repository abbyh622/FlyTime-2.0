package com.abby.ui;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import com.abby.main.App;
import com.abby.main.Experiment;
import com.abby.main.ExperimentItem;
import com.abby.main.KeyBehaviorPair;
import com.abby.main.Util;

// restore default settings button, dialog to confirm 
// sections:
// experiments: dropdown menu/treeview, show just names, maybe "edit" button on side of each name, bring up experimentdialog
// data output: convert session scores, directory to output to, auto export session files, set cumulative file to use for each experiment (file chooser?), can only have one file per experiment and file can only be used for one experiment, output csv or txt (future)
// video: default muted/unmuted, show milliseconds (if showing ms, show ms in data output too?) (future)
// appearance: dark/light mode (future)


public class SettingsController implements Initializable {
    private Stage stage;

    private Map<String, SimpleBooleanProperty> boolSettings;
    private SimpleStringProperty outputPath = App.settingsMan.outputDirectory;

    @FXML
    private TreeView<TreeDisplayable> experimentTree;
    @FXML
    private GridPane dataOutputGridpane;
    @FXML
    private GridPane videoGridpane;
    @FXML
    private GridPane appearanceGridpane;
    @FXML
    private TextField outputPathField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolSettings = App.settingsMan.boolSettings;

        // debugging/tracing statements
        // System.out.println("in initialize(), SettingsController");
        // System.out.println("String keys:");
        // for (String s : boolSettings.keySet()) {
        //     System.out.println(s);
        // }
        // System.out.println("SimpleBooleanProperty values:");
        // for (SimpleBooleanProperty b : boolSettings.values()) {
        //     System.out.println(b.getName() + " = " + b.getValue());
        // }
        // // System.out.println("Checking object ref of dataVisual: " + boolSettings.get("dataVisual").getName() + ", Object reference: " + System.identityHashCode(boolSettings.get("dataVisual")));
        // System.out.println("----------");

        populateExperiments();
        Platform.runLater(() -> createToggleGroups());
        outputPathField.textProperty().bindBidirectional(outputPath);
        outputPathField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            getDirectory(newValue);
        }));
    }

    private void createToggleGroups() {
        // data output section
        ToggleGroup dataVisualGroup = new ToggleGroup();
        createToggleButtons(boolSettings.get("dataVisual"), dataVisualGroup, dataOutputGridpane, 0);
        ToggleGroup dataAutoExportGroup = new ToggleGroup();
        createToggleButtons(boolSettings.get("dataAutoExport"), dataAutoExportGroup, dataOutputGridpane, 1);

        // video section
        ToggleGroup videoMillisecGroup = new ToggleGroup();
        createToggleButtons(boolSettings.get("videoMillisec"), videoMillisecGroup, videoGridpane, 0);
        ToggleGroup videoMuteGroup = new ToggleGroup();
        createToggleButtons(boolSettings.get("videoMute"), videoMuteGroup, videoGridpane, 1);

        // appearance section
        ToggleGroup appearanceModeGroup = new ToggleGroup();
        createToggleButtons(boolSettings.get("appearanceMode"), appearanceModeGroup, appearanceGridpane, 0);
    }

    private void createToggleButtons(SimpleBooleanProperty setting, ToggleGroup group, GridPane grid, int row) {
        // debugging/tracing statements
        if (setting == null) {
            System.out.println("setting is null");
        }
        // System.out.println("Setting: " + setting.getName() + ", Object reference: " + System.identityHashCode(setting));

        // create on and off buttons
        // using radio buttons for functionality (not deselectable) but want them to look like regular buttons
        // i dont really like this want to change it to a switch slider thing
        RadioButton b1 = new RadioButton("On");
        b1.getStyleClass().remove("radio-button");
        b1.getStyleClass().addAll("toggle-button", "button");
        RadioButton b2 = new RadioButton("Off");
        b2.getStyleClass().remove("radio-button");
        b2.getStyleClass().addAll("toggle-button", "button");

        // add to toggle group
        b1.setToggleGroup(group);
        b2.setToggleGroup(group);
        // bind to setting property
        // or use a listener to call settingsMan.setBoolSetting when changed ?
        b1.selectedProperty().bindBidirectional(setting);
        b2.setSelected(!setting.get());
        // add to gridpane 
        grid.add(b1, 1, row);
        grid.add(b2, 2, row);
        grid.setHalignment(b1, HPos.RIGHT);

        
        // disable dark mode buttons bc i havent done the css yet lol
        if (setting.getName().equals("appearanceMode")) {
            b1.setDisable(true);
            b2.setDisable(true);
            b1.setTooltip(Util.getTooltip());
            b2.setTooltip(Util.getTooltip());
        }
    }

    public void openFiles(ActionEvent e) {
        // directorychooser to select output directory
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Select Output Directory");
        Stage folderStage = new Stage();

        // set outputpath to the selected directory
        try {
            outputPath.set(dc.showDialog(folderStage).getAbsolutePath());
        }
        catch (NullPointerException n) {
            System.out.println("file chooser closed w/o selecting folder");
        }
    }

    // validate and set output directory setting
    private void getDirectory(String path) {
        if (path != null && path.length() > 0) {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                System.out.println(path + "destination directory accepted");
                outputPath.set(path);
            } else if (file.exists() && file.isFile()) {
                System.out.println(path + " is a file.");
            } else {
                System.out.println(path + " does not exist.");
            }
        }
    }

    private void populateExperiments() {
        // cell factory for displaying the correct text in tree
        experimentTree.setCellFactory(tv -> new TreeCell<TreeDisplayable>() {
            @Override
            protected void updateItem(TreeDisplayable item, boolean empty) {
                super.updateItem(item, empty); 
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } 
                else { 
                    // set graphic to the custom hbox to show edit/delete buttons                                 
                    if (item instanceof ExperimentItem) {
                        setText(null);
                        CustomItemHBox hbox = new CustomItemHBox((ExperimentItem)item);
                        setGraphic(hbox);
                        // set button onAction methods
                        hbox.getEditButton().setOnAction(event -> editExperiment((ExperimentItem)item));
                        hbox.getDeleteButton().setOnAction(event -> deleteExperiment((ExperimentItem)item));
                    }
                    else {                              // if behavior
                        setText(item.getText());        // use getText() for the text to display
                    }
                }
            }
        });

        // root of tree view
        TreeItem<TreeDisplayable> rootItem = new TreeItem<>(new TreeString("Experiments"));
        rootItem.setExpanded(true);
        // add experiments as children
        for (Experiment e : App.experimentMan.experiments) {
            ExperimentItem expmnt = new ExperimentItem(e);
            TreeItem<TreeDisplayable> treeExpmnt = new TreeItem<>(expmnt);
            // add behaviors as children of experiment
            for (KeyBehaviorPair p : expmnt.getBehaviors()) {
                TreeItem<TreeDisplayable> behavItem = new TreeItem<>(new TreeString(p.getBehavior()));
                treeExpmnt.getChildren().add(behavItem);
            }
            rootItem.getChildren().add(treeExpmnt);
        }
        // set root to display
        experimentTree.setRoot(rootItem);
    }

    // edit button action in experiment treeview
    public void editExperiment(ExperimentItem exp) {
        // show ExperimentDialog for the experiment containing the button
        Dialog<Experiment> dialog = new ExperimentDialog(exp.getExperiment());
        Optional<Experiment> result = dialog.showAndWait(); 
        if (result.isPresent() && result.get() instanceof Experiment) {
            // repopulate experiments treeview to show changes
            populateExperiments();
        } 
    }

    // delete button action in experiment treeview
    public void deleteExperiment(ExperimentItem exp) {
        // show confirmation dialog to delete experiment containing the button
        Alert confirm = Util.showConfirmDialog(stage, "Are you sure you want to delete experiment '" + exp.getText() + "'?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                // delete from stored experiments, repopulate treeview, save experiments to json file
                App.experimentMan.experiments.remove(exp.getExperiment());
                populateExperiments();
                App.experimentMan.saveExperiments();
            }
        }
    }

    public void createExperiment() {
        Dialog<Experiment> dialog = new ExperimentDialog(new Experiment());
        Optional<Experiment> result = dialog.showAndWait();
        // add new experiment to treeview
        if (result.isPresent()) {
            if (result.get() instanceof Experiment) {
                populateExperiments();
            } 
        }
    }

    public void back(ActionEvent e) throws Exception {
        App.settingsMan.saveSettings();       // save settings when exiting
        App.ctrl.switchScene(e, App.ctrl.prevScene);
    }
    
    public void helpScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.instructionsScene);
    }
}