package com.abby.ui;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;

import com.abby.main.App;
import com.abby.main.Experiment;
import com.abby.main.ExperimentItem;
import com.abby.main.KeyBehaviorPair;
import com.abby.main.Util;


// add error styling to videopathfield (turn red if not valid)

 
public class Controller1 implements Initializable {
    private Stage stage;

    @FXML
    private TreeView<TreeDisplayable> experimentTree;
    @FXML
    private Button selectVideoBtn;
    @FXML 
    private TextField videoPathField;

    private Experiment selectedExperiment = App.selectedExperiment;
    private SimpleStringProperty videoPath = new SimpleStringProperty(App.selectedVideo);
    // MediaPlayer works with these types of video files, nobody will prob have .flv but just including it bc why not
    private List validFileTypes = Arrays.asList(new String[] {".mp4", ".m4a", ".m4v", ".flv"});

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.ctrl.prevScene = AppController.scene1;
        populateExperiments();
        // create experiment tree context menu since you cant put in in the fxml file
        // jk i decided were not doing that
        // ContextMenu treeMenu = new ContextMenu();
        // MenuItem edit = new MenuItem("Edit");
        // edit.setOnAction(event -> editExperiment());
        // treeMenu.getItems().add(edit);
        // experimentTree.setContextMenu(treeMenu);

        // bind videopath variable to text field and add listener
        videoPathField.textProperty().bindBidirectional(videoPath);
        videoPathField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            getPath(newValue);
        }));
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
                    setText(item.getText());  // use getText() for the text to display
                }
            }
        });

        // root of tree view
        TreeItem<TreeDisplayable> rootItem = new TreeItem<>(new TreeString("Experiments"));
        rootItem.setExpanded(true);
        // add experiments as children
        for (Experiment e : App.experimentMan.experiments) {
            ExperimentItem expmntItem = new ExperimentItem(e);
            TreeItem<TreeDisplayable> treeExpmnt = new TreeItem<>(expmntItem);  
            // add behaviors as children of experiment
            for (KeyBehaviorPair p : expmntItem.getBehaviors()) {
                TreeItem<TreeDisplayable> behavItem = new TreeItem<>(new TreeString(p.getBehavior()));
                treeExpmnt.getChildren().add(behavItem);
            }
            rootItem.getChildren().add(treeExpmnt);
        }
        // set root to display
        experimentTree.setRoot(rootItem);
    }

    // set selected experiment (onMouseClicked action in fxml)
    public void selectItem() {
        TreeItem<TreeDisplayable> selectedItem = experimentTree.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.getValue() != null) {
            if (selectedItem.getValue() instanceof ExperimentItem) {
                ExperimentItem experimentItem = (ExperimentItem) selectedItem.getValue();
                Experiment selectedExperiment = experimentItem.getExperiment();
                App.selectedExperiment = selectedExperiment;
            } 
            else {
                // select experiment if a behavior is selected
                experimentTree.getSelectionModel().select(selectedItem.getParent());
                TreeItem<TreeDisplayable> parentExp = experimentTree.getSelectionModel().getSelectedItem();
                if (parentExp.getValue() instanceof ExperimentItem) {
                    ExperimentItem experimentItem = (ExperimentItem) parentExp.getValue();
                    Experiment selectedExperiment = experimentItem.getExperiment();
                    App.selectedExperiment = selectedExperiment;
                }
            }
        }
    }

    public void createExperiment() {
        Dialog<Experiment> dialog = new ExperimentDialog(new Experiment());
        
        Optional<Experiment> result = dialog.showAndWait();
        // add new experiment to treeview
        if (result.isPresent() && result.get() instanceof Experiment) {
            ExperimentItem newExpmnt = new ExperimentItem(result.get());
            TreeItem<TreeDisplayable> newTreeExpmnt = new TreeItem<>(newExpmnt);
            for (KeyBehaviorPair p : newExpmnt.getBehaviors()) {
                TreeItem<TreeDisplayable> behav = new TreeItem<>(new TreeString(p.getBehavior()));
                newTreeExpmnt.getChildren().add(behav);
            }
            experimentTree.getRoot().getChildren().add(newTreeExpmnt);
            // make new experiment the selected item and selectedExperiment
            experimentTree.getSelectionModel().selectLast();
            selectItem();
        }
    }

    public void openFiles(ActionEvent e) {
        // do filechooser then call validateVideo then selectVideo
        FileChooser fc = new FileChooser();
        ExtensionFilter mp4Filter = new ExtensionFilter("MP4", "*.mp4");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All files", "*.*"), mp4Filter);
        fc.setSelectedExtensionFilter(mp4Filter);
        fc.setTitle("Select Video");
        Stage fileStage = new Stage();

        // set videopath to the selected video to trigger changelistener -> getPath
        try {
            videoPath.set(fc.showOpenDialog(fileStage).getAbsolutePath());
        }
        catch (NullPointerException n) {
            // System.out.println("file chooser closed w/o selecting video");
        }
    }

    private void getPath(String path) {
        if (path != null && path.length() > 0) {
            String videoURI = validateVideo(path);
            if (videoURI != null) {
                App.selectedVideo = videoURI.toString();
                // System.out.println("video accepted");
            }
        }
    }

    private String validateVideo(String path) {
        // validate video path as existing and right file type (.mp4 or .flv)
        // what conditions and errors do we need to check for?

        // replace quotation marks if exist
        String quote = "\"";
        path = path.replaceAll(quote, "");

        File vidFile = new File(path);
        if (vidFile.exists()) {
            // check extension
            int fourthFromEnd = path.length() - 4;
            String ext = path.substring(fourthFromEnd);
            if (validFileTypes.contains(ext)) {
                // can just do file.toUri();
                // videoPathField.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), false);
                return vidFile.toURI().toString();
            }
            else {
                // error message/tell user video invalid, no video selected
                // Util.showError(stage, "Error: invalid file type");
                // videoPathField.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), true);
                return null;
            }
        }
        else {
            // error message/tell user video invalid, no video selected
            // Util.showError(stage, "Error: File not found");
            // videoPathField.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), true);
            return null;
        }
    }    
        
    public void next(ActionEvent e) throws Exception {
        // go to next screen
        // check that selectedExperiment and selectedVideo != null before allowing next
        if (App.selectedExperiment == null) {
            Util.showError(stage, "Select an experiment type");
            return;
        }
        if (App.selectedVideo == null) {
            if (videoPathField.getText() != null && videoPathField.getText().length() > 0) {
                Util.showError(stage, "Video file invalid");
            }
            else {
                Util.showError(stage, "Select a video");
            }
            return;
        }
        App.ctrl.switchScene(e, App.ctrl.scene2);
    }

    public void settingsScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.settingsScene);
    }

    public void helpScreen(ActionEvent e) throws Exception {
        App.ctrl.switchScene(e, App.ctrl.instructionsScene);
    }
}