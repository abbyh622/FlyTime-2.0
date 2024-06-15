package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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

    // should maybe change all behavior maps to keycode instead of character?

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // populate key bindings table 
        TableColumn<KeyBehaviorPair, String> behaviorCol = new TableColumn<>("Behavior");
        behaviorCol.setCellValueFactory(new PropertyValueFactory<>("behavior"));
        TableColumn<KeyBehaviorPair, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        keyBindingTable.getColumns().addAll(behaviorCol, keyCol);
        keyBindingTable.setItems(keyBindings);
        keyBindingTable.setSelectionModel(null);

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

        // whenReady sets the slider and labels to show time/duration
        videoPlayer.setOnReady(whenReady);
        videoPlayer.setVolume(0);    // just mute for now
        videoPlayer.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
            Duration curTime = videoPlayer.getCurrentTime();
            currentTimeLabel.setText(formatTime(curTime));
            timeSlider.setValue(curTime.toSeconds());
        }));

        videoPlayer.setOnEndOfMedia(setEndActions);

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
                currentInterval.recordBehavior(key);
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
            // show current arena number
            Arena currentArena = arenas.get(arenaIndex.getAndIncrement());
            curArenaLabel.setText(currentArena.getNum().toString());
            // play beep
            currentInterval = currentArena.addInterval(formatTime(videoPlayer.getCurrentTime()));
        });
    };

    // these properties are loaded asynchronously according to the documentation, so getting them here
    Runnable whenReady = () -> {
        Duration totalDuration = videoPlayer.getTotalDuration();
        timeSlider.setMax(totalDuration.toSeconds());
        totalDurationLabel.setText(formatTime(totalDuration));
        // App.metadata = video.getMetadata();     // to include some metadata in the data output
        // System.out.println("Metadata: " + App.metadata);
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
            // im just being annoying now
            BackgroundFill[] fill = { new BackgroundFill(Color.LIGHTGREEN, null, null) };
            finishButton.setBackground(new Background(fill));
        });
    };

    public void next(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource(nextScene));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // to convert the Durations from media player time properties to normal readable format
    // why is there not a built in method for this
    // want to have ms as an option to show maybe 
    private String formatTime(Duration time) {
        int totalsec = (int) time.toSeconds();
        int min = totalsec / 60;
        int sec = totalsec % 60;
        String showTime = String.format("%d:%02d", min, sec);
        return showTime;
    }
}


