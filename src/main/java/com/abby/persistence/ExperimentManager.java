package com.abby.persistence;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.abby.main.Experiment;
import com.abby.main.KeyBehaviorPair;
import com.abby.main.CoolContainerFactory;
import com.abby.exceptions.MaxBehaviorsExceededException;

// class to manage experiment configurations
// experiment info is stored in a json file, this class has methods to load the experiments and write new experiments to the file (also delete experiments)

// problem - if an experiment is added/changed and not saved then another is added/changed later and saved (in same session), the previous experiment will be saved also because its in the main list
// also unnecessary then to save after each experiment in the edit/config page because if ur editing/adding multiple then they can just all be saved at the end
// need to rework this part 

public class ExperimentManager {
    private String experimentFile = "/experiments.json";
    // this is the list of experiments available during runtime
    public static ArrayList<Experiment> experiments; 

    // static instantiated when app starts, so experiments are loaded right away
    public ExperimentManager() {
        loadExperiments();
    }

    // parses json file of stored experiments and converts them to Experiment objects
    public void loadExperiments() {
        experiments = new ArrayList<Experiment>();
        try {
            // FileReader in = new FileReader(experimentFile);
            InputStreamReader in = new InputStreamReader(getClass().getResourceAsStream(experimentFile));
            JSONParser parse = new JSONParser();
            ContainerFactory containers = new CoolContainerFactory();
            try {
                ArrayList<Object> parsed = (ArrayList<Object>) parse.parse(in, containers);
                for (Object o : parsed) {
                    // putting the info into Experiment objects
                    // i hate all of the casting happening here
                    if (o instanceof Map) {
                        Map<String, Object> exp = (Map) o;
                        // get experiment name as string
                        String name = (String) exp.get("name");

                        // get behaviors and convert to keybehaviorpairs
                        List<KeyBehaviorPair> behaviorsConverted = new ArrayList<KeyBehaviorPair>();
                        Map<String, String> behaviors = (Map) exp.get("behaviors");
                        for (Map.Entry<String, String> b : behaviors.entrySet()) {
                            behaviorsConverted.add(new KeyBehaviorPair(b.getKey().charAt(0), b.getValue()));
                        }
                        // Map<String, String> behaviorStrings = (Map<String, String>) exp.get("behaviors");
                        // Map<Character, String> behaviorConverted = new HashMap<Character, String>();
                        // for (Map.Entry<String, String> item : behaviorStrings.entrySet()) {
                        //     behaviorConverted.put((item.getKey().charAt(0)), item.getValue());
                        // }

                        // make experiment and add to list
                        try {
                            // need to make list of keyvaluepairs first
                            Experiment newExp = new Experiment(name, behaviorsConverted);
                            experiments.add(newExp);
                        }
                        catch (MaxBehaviorsExceededException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    else {          // what should happen here
                        System.out.println(o.toString() + "is not map");
                    }
                }
            }    
            catch (ParseException e) {
                System.out.println("No experiments found");
                System.err.println(e.getMessage());
            }
        }
        catch (IOException e) {
            System.out.println("file reader didnt work");
            System.err.println(e.getMessage());
        }
    }

    public void saveExperiments() {
        // file writer exceptions need to be checked
        try {
            FileWriter out = new FileWriter(experimentFile); 
            JSONArray expArray = new JSONArray();
            for (Experiment e : experiments) {
                JSONObject jsonExp = new JSONObject();
                jsonExp.put("name", e.getName());

                JSONObject jsonBehaviors = new JSONObject();
                for (KeyBehaviorPair p : e.getBehaviorPairs()) {
                    // JSONObject jsonBehavior = new JSONObject();
                    jsonBehaviors.put(Character.toString(p.getKey()), p.getBehavior());   // can delete this line i think
                } 

                jsonExp.put("behaviors", jsonBehaviors);
                expArray.add(jsonExp);
            }
            expArray.writeJSONString(out);
            out.close();
        }
        catch (IOException e) {
            System.out.println("file writer didnt work");
        }
    }
}    