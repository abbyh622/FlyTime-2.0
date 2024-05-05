package main;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

// class to manage experiments
// experiment info is stored in a json file, this class has methods to load the experiments and write new experiments to the file (also delete experiments)
// for saving new experiments, this class should deal with assigning numbers to the behaviors before writing to file 

public class ExperimentManager {

    // this is the list of experiments available during runtime
    public static ArrayList<Experiment> experiments; 

    // parses json file of stored experiments and converts them to Experiment objects
    public void loadExperiments() {
        experiments = new ArrayList<Experiment>();

        try {
            FileReader in = new FileReader("experiments.json");
            JSONParser parse = new JSONParser();
            ContainerFactory containers = new CoolContainerFactory();
            try {
                ArrayList<Object> parsed = (ArrayList<Object>) parse.parse(in, containers);
                // System.out.println(parsed);
                // System.out.println(parsed.size());

                for (Object o : parsed) {
                    // putting the info into Experiment objects
                    // i hate all of the casting happening here this sucks
                    if (o instanceof Map) {
                        Map<String, Object> exp = (Map) o;
                        // get experiment name as string
                        String name = (String) exp.get("name");
                        // get behavior map and convert to <Integer, String>
                        Map<String, String> behaviorStrings = (Map<String, String>) exp.get("behaviors");
                        Map<Integer, String> behaviorConverted = new HashMap<Integer, String>();
                        for (Map.Entry<String, String> item : behaviorStrings.entrySet()) {
                            behaviorConverted.put(Integer.parseInt(item.getKey()), item.getValue());
                        }
                        // make experiment and add to list
                        try {
                            Experiment newExp = new Experiment(name, behaviorConverted);
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
            System.out.println("parse didnt work");
            }
        }
        catch (IOException e) {
            System.out.println("file reader didnt work");
        }
    }

    public void saveExperiments() {
        // file writer exceptions need to be checked
        try {
            FileWriter out = new FileWriter("experiments.json");       // change this to the right file
            JSONArray expArray = new JSONArray();
            for (Experiment e : experiments) {
                JSONObject jsonExp = new JSONObject();
                jsonExp.put("name", e.getName());
                jsonExp.put("behaviors", e.getBehaviors());
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