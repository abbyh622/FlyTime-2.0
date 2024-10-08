package com.abby.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import com.abby.main.App;
import com.abby.main.CoolContainerFactory;

public class SettingsManager {

    private String settingsFile = App.dataDirectory + "\\settings.json";
    public static Map<String, SimpleBooleanProperty> boolSettings;
    public static SimpleStringProperty outputDirectory;

    // constructor
    // static instantiated when app starts, so settings are loaded right away
    public SettingsManager() {
        loadSettings();
    }


    public void loadSettings() {
        boolSettings = new HashMap<String, SimpleBooleanProperty>();
        try {
            FileReader in = new FileReader(settingsFile);
            // InputStreamReader in = new InputStreamReader(getClass().getResourceAsStream(settingsFile));
            JSONParser parse = new JSONParser();
            ContainerFactory containers = new CoolContainerFactory();
            try {
                ArrayList<Object> parsed = (ArrayList<Object>) parse.parse(in, containers);
                for (Object o : parsed) {
                    if (o instanceof Map) {
                        Map<String, Object> settings = (Map) o;
                
                        // get boolean settings
                        Object boolsObj = settings.get("booleans");
                        if (boolsObj instanceof Map) {
                            Map<String, Boolean> boolsMap = (Map) boolsObj;
                            Set<Entry<String, Boolean>> bools = boolsMap.entrySet();
                            // create simplebooleanproperties and map to names
                            for (Entry<String, Boolean> e : bools) {
                                SimpleBooleanProperty b = new SimpleBooleanProperty(boolSettings, e.getKey(), e.getValue());
                                boolSettings.put(b.getName(), b);
                            }
                        }

                        // get directory settings
                        Object strObj = settings.get("strings");
                        if (strObj instanceof Map) {
                            Map<String, String> strMap = (Map) strObj;
                            Set<Entry<String, String>> strs = strMap.entrySet();
                            for (Entry<String, String> e : strs) {
                                // ?
                                outputDirectory = new SimpleStringProperty(null, e.getKey(), e.getValue());
                            }
                        } 
                    }
                }
            }
            catch (ParseException e) {
                System.out.println("parse error from JSONParser");
                System.err.println(e.getMessage());
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Settings file not found, applying defaults");
            restoreDefaults();
        }
        catch (IOException e) {
            System.out.println("file reader didnt work (I/O exception)");
            System.err.println(e.getMessage());
        }
    }

    // write settings to json file
    public void saveSettings() {
        try {
            FileWriter out = new FileWriter(settingsFile);
            JSONArray settingsArray = new JSONArray();

            // add boolean settings
            JSONObject bools = new JSONObject();
            for (SimpleBooleanProperty s : boolSettings.values()) {
                bools.put(s.getName(), s.get());
            }
            JSONObject jsonBools = new JSONObject();
            jsonBools.put("booleans", bools);
            settingsArray.add(jsonBools);

            // add string settings (currently just outputdirectory)
            JSONObject jsonString = new JSONObject();
            jsonString.put("outputDirectory", outputDirectory.getValue());
            JSONObject jsonStrings = new JSONObject();
            jsonStrings.put("strings", jsonString);
            settingsArray.add(jsonStrings);

            settingsArray.writeJSONString(out);
            out.close();
            System.out.println("settings saved");
        }
        catch (IOException e) {
            System.out.println("file writer didnt work");
            System.out.println(e.getMessage());
        }
        
    }

    // set all settings to default values
    public void restoreDefaults() {
        boolSettings.clear();
        SimpleBooleanProperty dataVisual = new SimpleBooleanProperty(boolSettings, "dataVisual", false);
        boolSettings.put(dataVisual.getName(), dataVisual);
        SimpleBooleanProperty dataAutoExport = new SimpleBooleanProperty(boolSettings, "dataAutoExport", true);
        boolSettings.put(dataAutoExport.getName(), dataAutoExport);
        // SimpleBooleanProperty dataAutoAppend = new SimpleBooleanProperty(boolSettings, "dataAutoAppend", false);
        // boolSettings.put(dataAutoAppend.getName(), dataAutoAppend);
        SimpleBooleanProperty videoMillisec = new SimpleBooleanProperty(boolSettings, "videoMillisec", false);
        boolSettings.put(videoMillisec.getName(), videoMillisec);
        SimpleBooleanProperty videoMute = new SimpleBooleanProperty(boolSettings, "videoMute", true);
        boolSettings.put(videoMute.getName(), videoMute);
        SimpleBooleanProperty appearanceMode = new SimpleBooleanProperty(boolSettings, "appearanceMode", false);
        boolSettings.put(appearanceMode.getName(), appearanceMode);
        // set output directory to downloads folder
        outputDirectory = new SimpleStringProperty(null, "outputDirectory", System.getProperty("user.home") + "\\Downloads");
    }

    // this is kind of unnecessary i think
    // generic method to get boolean setting values
    // public SimpleBooleanProperty getBoolSetting(String name) {
    //     return boolSettings.get(name);
    // }
    // // generic method to set settings with boolean values
    // public void setBoolSetting(String name, boolean newValue) {
    //     boolSettings.replace(name, new SimpleBooleanProperty(boolSettings, name, newValue));
    // }

}
