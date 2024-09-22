package ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.*;

// whats the best way to output bool data? 0/1, empty/X, ?
// should probably be a setting that can be changed 

// data columns - start time, behavior 1, behavior 2, behavior 3
// display/export data transposed by default?

//      Arena 1 - CS male 2-5 days x4
// | start time | wing ext | chase | chain |
// |  0:00:000  |     X    |       |       |
// |  0:05:000  |     X    |   X   |       |
// |  0:10:000  |          |       |       |  
// |  0:15:000  |     x    |       |       |
// |  0:20:000  |          |   x   |   x   |
// total: wing ext = 3, chase = 2, chain = 1

// | start time | 0:00:000 | 0:05:000 | 0:10:000 |
// |  wing ext  |     X    |     X    |          |
// |   chase    |          |     X    |          |
// |   chain    |          |          |          |

public class EndScreenController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String settingsScene = "settingsscreen.fxml";

    private DataManager dm;

    @FXML
    private TextArea dataTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dm = new DataManager();

        // set text area to monospaced font so spacing works right
        dataTextArea.setStyle("-fx-font-family : 'Monospaced'");

        // display data on screen
        String dataDisplay = dm.getSessionTableString();
        dataTextArea.setText(dataDisplay);
    }

    public void exportSession() {
        // get video name without ext and create path, if file already exists show dialog asking to overwrite or make new file
        String fileName = dm.getVideoName().split(".")[0] + ".txt"; 
        dm.setOutputPath(App.settingsMan.outputDirectory.get() + fileName);
        dm.writeSessionFile();
    }

    public void newCumFile() {
        // create file name as [experiment name]Cumulative or something and create path, if file already exists just make a new one
        // does not set the new file to be used as the experiment's cumulative file in settings, must be set manually
        String fileName = App.selectedExperiment.getName() + "_cumulative.txt";
        dm.setOutputPath(App.settingsMan.outputDirectory.get() + fileName);
        dm.createCumulativeFile();
    }

    public void appendCumFile() { 
        FileChooser fc = new FileChooser();
        Stage fcStage = new Stage();
        File file = fc.showOpenDialog(fcStage);
        dm.appendCumulativeFile(file);
    }

    // thoughts: idk if its that useful to have cumulative files set for each experiment, would it be better to just use filechooser each time to select which file to append
    // if keeping the cumulative files setting i think delete the confirmation dialog for appending and show text on screen or dialog saying that data has been appended to [filename] or [filepath]
    // when appending, should probably make a temporary copy of the cumulative file before reading/modifying so its preserved if anything goes wrong right? what is standard procedure for this?
    // should session data file be automatically exported? i think probably yes  

    public void settingsScreen(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource(settingsScene));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(App.stylesheet);
        stage.setScene(scene);
        stage.show();
    }
}
