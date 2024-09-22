package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.awt.Toolkit;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import main.App;
import main.Arena;
import main.Interval;
import main.KeyBehaviorPair;



// change cycleArenas to loop based on video time
// currently creates new interval when played from pause
// should just continue current interval until next time interval in video duration

public class RecordingController implements Initializable {
    @FXML
    private MediaView videoMediaView;
    @FXML
    private TableView<KeyBehaviorPair> keyBindingTable;
    @FXML 
    private Label curArenaLabel;
    @FXML
    private Button playPause;
    @FXML
    private Slider timeSlider;
    @FXML
    private Label totalDurationLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Button finishButton;
    @FXML 
    private Button volumeButton;
    @FXML
    private Slider volumeSlider;

    private ScheduledExecutorService executorService;
    private AtomicInteger arenaIndex;
    private Integer intervalLength = App.seconds;

    private Media video;
    private MediaPlayer videoPlayer;
    private List<Arena> arenas = new ArrayList<Arena>(App.arenaList);
    private ObservableList<KeyBehaviorPair> keyBindings = FXCollections.observableArrayList(App.selectedExperiment.getBehaviorPairs());
    private List<KeyCode> keys;
    private Interval currentInterval;
    private boolean isPlaying = false;

    private String nextScene = "endscreen.fxml";
    private Stage stage;
    private Scene scene;
    private Parent root;

    // toolkit for beeping
    Toolkit tool = Toolkit.getDefaultToolkit();


    // rearrange this, separate initialize() into better methods


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // populate key bindings table 
        TableColumn<KeyBehaviorPair, String> behaviorCol = new TableColumn<>("Behavior");
        behaviorCol.setCellValueFactory(new PropertyValueFactory<>("behavior"));
        TableColumn<KeyBehaviorPair, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        // set column widths
        behaviorCol.prefWidthProperty().bind(keyBindingTable.widthProperty().multiply(0.69));
        behaviorCol.setResizable(false);
        keyCol.prefWidthProperty().bind(keyBindingTable.widthProperty().multiply(0.29));
        keyCol.setResizable(false);
        keyBindingTable.getColumns().addAll(behaviorCol, keyCol);
        keyBindingTable.setItems(keyBindings);
        // set up selection model for visual indication when key pressed
        keyBindingTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        keyBindingTable.getSelectionModel().clearSelection();

        // get behavior keys as list of keycodes
        keys = new ArrayList<KeyCode>();
        for (KeyBehaviorPair p : keyBindings) {
            keys.add(p.getKeyCode());
        }

        // load and display video
        video = new Media(App.selectedVideo);
        videoPlayer = new MediaPlayer(video);
        videoMediaView.setMediaPlayer(videoPlayer);
        // set event handler after initialization because if not then NullPointerException because scene isnt displayed yet
        Platform.runLater(() -> setEventHandlers(videoMediaView.getScene()));

        // set up schedule thing
        executorService = Executors.newSingleThreadScheduledExecutor();
        arenaIndex = new AtomicInteger(0);

        // set up volume controls
        // FontAwesomeIconView volOff = new FontAwesomeIconView(FontAwesomeIcon.VOLUME_OFF);
        // FontAwesomeIconView volOn = new FontAwesomeIconView(FontAwesomeIcon.VOLUME_UP);
        // // fix this, should be the opposite way - vidplayer volume property is set by slider/volume button action
        // volumeSlider.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
        //     if (volumeSlider.getValue() > 0) {
        //         volumeButton.setGraphic(volOn);
        //         videoPlayer.setVolume(volumeSlider.getValue());       // need to be *100?
        //     }
        //     else {
        //         volumeButton.setGraphic(volOff);
        //         videoPlayer.setVolume(0);
        //     }
        // }));
        if (App.settingsMan.boolSettings.get("videoMute").get() == true) {
            videoPlayer.setVolume(0);
        }

        // set up timestamps
        videoPlayer.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
            Duration curTime = videoPlayer.getCurrentTime();
            currentTimeLabel.setText(formatTime(curTime));
            timeSlider.setValue(curTime.toSeconds());
        }));

        // whenReady sets the slider and labels to show time/duration
        videoPlayer.setOnReady(whenReady);
        videoPlayer.setOnEndOfMedia(setEndActions);
        videoPlayer.setOnError(() -> {
            Throwable error = videoPlayer.getError();
            if (error != null) {
                error.printStackTrace();
            }
        });
        System.out.println("OnReady, OnEndOfMedia, OnError set");
    }

    private void setEventHandlers(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, spaceFilter);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, behaviorInput);
    }

    // event filter to capture space bar presses - pause/resume main loop
    EventHandler<KeyEvent> spaceFilter = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.SPACE) {
                toggleLoop();
                event.consume();
            }
        }
    };

    // event handler for key bindings - recording data
    // Interval.recordBehavior sets the value for the key in its scores map to true (does nothing if value already true)
    EventHandler<KeyEvent> behaviorInput = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            KeyCode key = event.getCode();
            if (keys.contains(key)) {
                    // call recordbehavior, highlight row of keybindingtable if true to give visual indication of input being recorded
                    if (currentInterval.recordBehavior(key)) {
                        // get index of key in list, should be same as index of corresponding keybehaviorpair and row in table
                        int idx = keys.indexOf(key);
                        // select row of keybinding table to highlight, pausetransition to deselect after short time
                        // selected row highlighting styled in css 
                        keyBindingTable.getSelectionModel().select(idx);
                        PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // 0.5 sec delay
                        pause.setOnFinished(e -> keyBindingTable.getSelectionModel().clearSelection());
                        pause.play();
                    }

                //     // use stream to find corresponding keybehaviorpair of keycode
                //     KeyBehaviorPair findPair = keyBindings.stream()
                //     .filter(kbp -> kbp.getKeyCode().equals(key))
                //     .findFirst().orElse(null);
                // if (findPair != null) {
                //     // get index of keybehaviorpair in keybinding list
                //     // select row of keybinding table by index to highlight
                //     int idx = keyBindings.indexOf(findPair);
                //     keyBindingTable.getSelectionModel().select(idx);
                //     PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // 0.5 sec delay
                //     pause.setOnFinished(e -> keyBindingTable.getSelectionModel().clearSelection());
                //     pause.play();
                // }
            }
        }
    };

    // to start or resume 
    private void runLoop() {
        if (executorService.isShutdown()) {
            executorService = Executors.newSingleThreadScheduledExecutor();
        }
        videoPlayer.play();
        executorService.scheduleAtFixedRate(cycleArenas, 0, intervalLength, TimeUnit.SECONDS); 
    }
    // to pause
    private void pauseLoop() {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                System.err.println("executor did not terminate in 1 sec");
            }
        }
        catch (InterruptedException e) {
            System.err.println("loop termination interrupted");
            Thread.currentThread().interrupt();
        }
        videoPlayer.pause();
    }
    // handles space bar input and updates isPlaying
    private void toggleLoop() {
        if (isPlaying) {
            pauseLoop();
        }
        else {
            runLoop();
        }
        isPlaying = !isPlaying;
    }

    // task to cycle through arenas
    Runnable cycleArenas = () -> {
        Platform.runLater(() -> {
            if (arenaIndex.get() >= arenas.size()) {
                arenaIndex.set(0);  // reset to first index to loop continuously
            }
            // beep
            tool.beep();
            // show current arena number
            Arena currentArena = arenas.get(arenaIndex.getAndIncrement());
            curArenaLabel.setText(currentArena.getNum().toString());
            // create new interval and set to current
            currentInterval = currentArena.addInterval(formatTime(videoPlayer.getCurrentTime()));
        });
    };

    // these properties are loaded asynchronously according to the documentation, so getting them here
    Runnable whenReady = () -> {
        Duration totalDuration = videoPlayer.getTotalDuration();
        timeSlider.setMax(totalDuration.toSeconds());
        totalDurationLabel.setText(formatTime(totalDuration));
    };

    // disable keys/other actions and show finish button
    // they have no choice
    Runnable setEndActions = () -> {
        pauseLoop();
        videoMediaView.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, spaceFilter);
        videoMediaView.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, behaviorInput);
        Platform.runLater(() -> {
            // this is probably a bad way to do this but its just here for now
            finishButton.setDisable(false);
            finishButton.setVisible(true);
        });
    };

    public void next(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource(nextScene));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(App.stylesheet);
        stage.setScene(scene);
        stage.show();
    }

    // action when pressing volume button to toggle muted/unmuted
    // sets slider value that has a changelistener that will set the mediaplayer volume and volume button icon
    public void toggleMute() {
        if (volumeSlider.getValue() > 0) {
            volumeSlider.setValue(100);
        }
        else {
            volumeSlider.setValue(0);
        }
    }

    // to convert the Durations from media player time properties to normal readable format
    // why is there not a built in method for this
    private String formatTime(Duration time) {
        String showTime;
        // format time based on ms on/off setting
        if (App.settingsMan.boolSettings.get("videoMillisec").get() == false) {
            // min:sec format
            int totalsec = (int) time.toSeconds();
            int min = totalsec / 60;
            int sec = totalsec % 60;
            showTime = String.format("%d:%02d", min, sec);
        }
        else {
            // min:sec:ms format
            long totalMillis = (long) time.toMillis();  // probably large
            int min = (int) totalMillis / 60000; 
            int sec = (int) (totalMillis / 1000) % 60; 
            int ms = (int) totalMillis % 1000;       
            showTime = String.format("%d:%02d:%03d", min, sec, ms);
        }
        return showTime;
    }
}


