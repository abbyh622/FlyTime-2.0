package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;

// class for containing and displaying behavior key bindings 
public class KeyBehaviorPair {
    private SimpleStringProperty key;
    private SimpleStringProperty behavior;
    private KeyCode keyCode;

    // constructors
    public KeyBehaviorPair(char key, String behavior) {
        this.key = new SimpleStringProperty(String.valueOf(key).toUpperCase());
        this.behavior = new SimpleStringProperty(behavior);
        this.keyCode = KeyCode.getKeyCode(String.valueOf(key));
    }
    public KeyBehaviorPair(String behavior) {
        this.key = new SimpleStringProperty("");
        this.behavior = new SimpleStringProperty(behavior);
    }
    public KeyBehaviorPair() {
        this.key = new SimpleStringProperty("");
        this.behavior = new SimpleStringProperty("");
    }

    // listener to update keycode when key string property is changed through ui
    private ChangeListener<String> keyListener() {
        return (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            this.keyCode = KeyCode.getKeyCode(newValue.toUpperCase());
        };
    }

    // public methods to add and remove listener 
    public void addListener() {
        this.key.addListener(keyListener());
    }
    public void removeListener() {
        this.key.removeListener(keyListener());
    }

    public void setKey(String key) {
        this.key.set(key.toUpperCase());
        this.keyCode = KeyCode.getKeyCode(key);
    }
    public Character getKey() {
        if (!key.isEmpty().get()) {
            return key.get().charAt(0);
        }
        else {
            return null;
        }
    }
    public SimpleStringProperty getKeyProperty() {
        return key;
    }

    public KeyCode getKeyCode() {
        return this.keyCode;
    }

    public void setBehavior(String behavior) {
        this.behavior.set(behavior);
    }
    public String getBehavior() {
        return behavior.get();
    }
    public SimpleStringProperty getBehaviorProperty() {
        return behavior;
    }
}
