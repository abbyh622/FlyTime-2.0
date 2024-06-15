package ui;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import main.App;
import main.Arena;
import main.Experiment;
import main.KeyBehaviorPair;


// add error styling to videopathfield (turn red if not valid)


public class Controller1 implements Initializable {

    @FXML
    private TreeView<String> experimentTree;
    @FXML
    private Button selectVideoBtn;
    @FXML 
    private TextField videoPathField;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String nextScene = "screen2.fxml";
    private Experiment selectedExperiment;
    private SimpleStringProperty videoPath = new SimpleStringProperty();
    // MediaPlayer works with these types of video files, nobody will prob have .flv but just including it bc why not
    private List validFileTypes = Arrays.asList(new String[] {".mp4", ".m4a", ".m4v", ".flv"});

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateExperiments();
        videoPathField.textProperty().bindBidirectional(videoPath);
        videoPathField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            getPath(newValue);
        }));
    }

    public void populateExperiments() {
        // root of tree view
        TreeItem<String> rootItem = new TreeItem<>("experiments");
        rootItem.setExpanded(true);
        // add experiments as children
        for (Experiment e : App.experimentMan.experiments) {
            TreeItem<String> expmntItem = new TreeItem<>(e.getName());      // is no param in definition okay ?
            // for (String b : e.getBehaviors().values()) {
            //     TreeItem<String> behav = new TreeItem<>(b);
            //     expmntItem.getChildren().add(behav);
            // }
            for (KeyBehaviorPair p : e.getBehaviorPairs()) {
                TreeItem<String> behav = new TreeItem<>(p.getBehavior());
                expmntItem.getChildren().add(behav);
            }
            rootItem.getChildren().add(expmntItem);
        }
        // set root to display
        experimentTree.setRoot(rootItem);

        // make only experiments selectable (not behaviors)
        // if the new selected item's parent is not root, clear selection 
        experimentTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.getParent() != rootItem) {
                Platform.runLater(() -> experimentTree.getSelectionModel().clearSelection());
            }
        });
    }

    // can this be changed to run only when a treeitem is selected instead ?
    // set selected experiment 
    public void selectItem() {
        TreeItem<String> selectedItem = experimentTree.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String selectedExperiment = selectedItem.getValue();
            // find experiment from selected name and set App.selectedExperiment
            for (Experiment e : App.experimentMan.experiments) {
                if (selectedExperiment == e.getName() && (App.selectedExperiment == null || selectedExperiment != App.selectedExperiment.getName())) {
                    App.selectedExperiment = e;
                }
            }
        }
    }

    public void createExperiment() {
        Dialog<Experiment> dialog = new ExperimentDialog(new Experiment());
        Optional<Experiment> result = dialog.showAndWait();
        // add new experiment to treeview
        if (result.isPresent()) {
            if (result.get() instanceof Experiment) {
                TreeItem<String> newExpmntItem = new TreeItem<>(result.get().getName());
                for (KeyBehaviorPair p : result.get().getBehaviorPairs()) {
                    TreeItem<String> behav = new TreeItem<>(p.getBehavior());
                    newExpmntItem.getChildren().add(behav);
                }
                experimentTree.getRoot().getChildren().add(newExpmntItem);
                // make new experiment the selected item and selectedExperiment
                experimentTree.getSelectionModel().selectLast();
                selectItem();
            }
        } 
    }

    public void openFiles(ActionEvent e) {
        // do filechooser then call validateVideo then selectVideo
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All files", "*.*"), new FileChooser.ExtensionFilter("MP4", "*.mp4"));
        fc.setTitle("Select Video");
        Stage fileStage = new Stage();

        // set videopath to the selected video to trigger changelistener -> getPath
        // this isnt working
        try {
            videoPath.set(fc.showOpenDialog(fileStage).getAbsolutePath());
        }
        catch (NullPointerException n) {
            System.out.println("file chooser closed w/o selecting video");
        }

        // File video = fc.showOpenDialog(fileStage);
        // if (video != null) {
        //     String videoURI = validateVideo(video.getAbsolutePath());
        //     if (videoURI != null) {
        //         App.selectedVideo = videoURI.toString();
        //         System.out.println("video accepted");
        //     }
        // } 
    }

    public void getPath(String path) {
        // if (videoPathField.getText() != null && videoPathField.getText().length() > 0) {
        //    String videoURI = validateVideo(videoPathField.getText());
        if (path != null && path.length() > 0) {
            String videoURI = validateVideo(path);
            if (videoURI != null) {
                App.selectedVideo = videoURI.toString();
                System.out.println("video accepted");
            }
        }
    }

    public String validateVideo(String path) {
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
                return vidFile.toURI().toString();
            }
            else {
                // error message/tell user video invalid, no video selected
                System.out.println("Error: invalid file type");
                System.out.println(validFileTypes.toString());
                return null;
            }
        }
        else {
            // error message/tell user video invalid, no video selected
            System.out.println("Error: File not found");
            return null;
        }
    }    
        
    public void next(ActionEvent e) throws Exception {
        // go to next screen
        // check that selectedExperiment and selectedVideo != null before allowing next
        if (App.selectedExperiment != null && App.selectedVideo != null) {
            root = FXMLLoader.load(getClass().getResource(nextScene));
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}