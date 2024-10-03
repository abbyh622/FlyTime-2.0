package com.abby.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;

public class DataManager {
    private String vidName;
    private Experiment exp;
    private List<KeyBehaviorPair> keyBindings;
    private List<Arena> arenas;
    private int sec;
    private Date date;
    private SimpleDateFormat format;

    // folder to output files to, can be set by user
    private String outputPath;
    // map for storing data tables for arenas
    private Map<Arena, List<List<String>>> dataTables;
    //
    private int[] columnWidths;

    // setting to convert scores to X/blank instead of true/false 
    // TEMPORARY this is just here for testing will be moved 
    // private boolean convertSessionScores = false;
    // public void setScoreDisplay(boolean b) {
    //     convertSessionScores = b;
    // }

    public DataManager() {
        exp = App.selectedExperiment;
        keyBindings = new ArrayList<KeyBehaviorPair>(exp.getBehaviorPairs());
        arenas = App.arenaList;
        sec = App.seconds; 
        vidName = getVideoName();
        date = new Date();
        format = new SimpleDateFormat("MM/dd/yyyy"); 

        // create score tables for display in ui
        createScoreTables();
    }

    public void setOutputPath(String path) {
        outputPath = path;
    }

    // get video file name minus extension
    public String getVideoName() {
        String[] vidSplit = App.selectedVideo.split("/"); 
        vidName = vidSplit[vidSplit.length - 1].split(".")[0];      
        return vidName;
    }

    private void createScoreTables() {
        // get headers
        dataTables = new HashMap<Arena, List<List<String>>>();
        ArrayList<String> headers = new ArrayList<>();
        headers.add("Interval Start");
        for (KeyBehaviorPair p : keyBindings) {
            headers.add(p.getBehavior());
        }

        // calculate column widths from header length
        columnWidths = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            columnWidths[i] = headers.get(i).length() + 4;  // just adding 4 for more spacing
        }

        // create tables for each arena 
        for (Arena a : arenas) {
            dataTables.put(a, createScoreTable(a, headers));
        }
    }

    // creates table of intervals/scores for an arena
    private List<List<String>> createScoreTable(Arena a, List<String> headers) {
        List<List<String>> data = new ArrayList<>();
        // add headers to table
        data.add(headers);

        // map to store behaviors w/ column indexes
        Map<String, Integer> behaviorCols = new HashMap<>();
        // start at 1 to skip time column
        for (int i = 1; i < headers.size(); i++) {
            behaviorCols.put(headers.get(i), i);
        }

        // make row for each interval
        for (Interval i : a.getIntervals()) {
            // initialize w/ correct number of elements set to null
            List<String> row = new ArrayList<>(Collections.nCopies(headers.size(), null));
            row.set(0, i.getStartTime());

            // add behavior scores
            for (KeyBehaviorPair p : keyBindings) {
                // put score in column index
                Integer index = behaviorCols.get(p.getBehavior());
                if (index != null) {
                    row.set(index, String.valueOf(i.getScores().get(p.getKeyCode())));
                }
                else {
                    System.out.println("Error creating table; column index for " + p.getBehavior() + " not found");
                }
            }
            data.add(row);
        }

        // convert scores to X/blank if set
        if (App.settingsMan.boolSettings.get("dataVisual").get() == true) {
            return convertScoreTable(data);
        }
        else {
            return data;
        }
    }

    // convert from true/false to X/blank
    // to print visual scores if setting is on
    private List<List<String>> convertScoreTable(List<List<String>> table) {
        for (int i = 1; i < table.size(); i++) {          // start at row 1 to skip headers 
            List<String> row = table.get(i);
            for (int j = 1; j < row.size(); j++) {        // start at column 1 to skip time
                if (row.get(j) == "true") {
                    row.set(j, "X");
                } 
                if (row.get(j) == "false") {
                    row.set(j, "-");
                }
            }
        }
        return table;
    }

    // format session data into one string to display in TextArea in the ui
    public String getSessionTableString() {
        StringBuilder sb = new StringBuilder();
        for (Arena a : arenas) {
            // add number and description
            sb.append("Arena " + a.getNum() + ": " + a.getDescription()); 
            sb.append("\n");
            List<List<String>> tableData = dataTables.get(a);
            // add data tables
            for (List<String> row : tableData) {
                for (int i = 0; i < row.size(); i++) {
                    sb.append(String.format("%-" + columnWidths[i] + "s", row.get(i)));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // write out the data from the current session to a new txt file
    // table-like display
    public void writeSessionFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // create file headers with session info
            // first row
            List<String> info1 = new ArrayList<String>();
            info1.add(vidName);
            info1.add("     ");
            info1.add("Date scored: ");
            info1.add(format.format(date));
            // second row
            List<String> info2 = new ArrayList<String>();
            info2.add("Experiment type: ");
            info2.add(exp.getName());
            // get list of behavior names 
            String behavs = "  (";
            for (int i = 0; i < keyBindings.size(); i++) {
                behavs += keyBindings.get(i).getBehavior();
                if (i < keyBindings.size() - 1) {
                    behavs += ", ";
                }
            }
            behavs += ")";
            info2.add(behavs);

            // write file header
            for (String s : info1) {
                writer.write(s);
            }
            writer.newLine();
            for (String s : info2) {
                writer.write(s);
            }
            writer.newLine();
            writer.newLine();
            writer.newLine();

            for (Arena a : arenas) {
                // write number and description
                writer.write("Arena " + a.getNum() + ": " + a.getDescription()); 
                writer.newLine();
                List<List<String>> tableData = dataTables.get(a);
                // write rows
                for (List<String> row : tableData) {
                    for (int i = 0; i < row.size(); i++) {
                        writer.write(String.format("%-" + columnWidths[i] + "s", row.get(i)));
                    }
                    writer.newLine();
                }
                writer.newLine();
            }
            System.out.println("session file made in " + outputPath);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write out the data from the current session to a new tab delimited txt file
    // public void writeSessionTab() {

    // }

    // make row of totaled/simplified scores for an arena
    // for adding to an existing file
    private List<String> createCumulativeRow(Arena a, List<String> behavs) {
        List<Interval> intervals = a.getIntervals();
        List<String> row = new ArrayList<String>(Arrays.asList(vidName, a.getNum().toString(), a.getDescription(), Integer.toString(sec), String.valueOf(intervals.size())));
        Map<String, Integer> behaviorTotals = new HashMap<String, Integer>();
        // add up true values for each behavior 
        for (KeyBehaviorPair p : keyBindings) {
            behaviorTotals.put(p.getBehavior(), 0);
            for (Interval i : intervals) {
                if (i.getScores().get(p.getKeyCode()) == true) {
                    int curVal = behaviorTotals.get(p.getBehavior());
                    behaviorTotals.replace(p.getBehavior(), curVal + 1);
                }
            }
        }
        // need to read in columns and add scores in the right order so it corresponds to headers
        for (String b : behavs) {
            row.add(behaviorTotals.get(b).toString());
        }
        return row;
    }

    // write out shortened data as new rows in an existing file
    public void appendCumulativeFile(File file) {
        // each row - video name, arena #, arena description, interval length, # intervals, # w/ behavior 1, # with behavior 2, etc
        ArrayList<List<String>> rows = new ArrayList<List<String>>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {        // handle filenotfoundexception
            // get column names from first line
            String[] columns = reader.readLine().split("\t");   
            // first behavior column is at index 5 so get 5 to end
            List<String> behaviorCols = Arrays.asList(Arrays.copyOfRange(columns, 5, columns.length));
            for (Arena a : arenas) {
                rows.add(createCumulativeRow(a, behaviorCols));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // write rows to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (List<String> r : rows) {
                for (String s : r) {
                    writer.write(s);
                    writer.write("\t");
                }
                writer.newLine();
            }
            System.out.println("cumulative file updated in " + outputPath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // create a cumulative file for the current experiment type
    public void createCumulativeFile() {
        List<String> columns = new ArrayList<String>(Arrays.asList("Video name", "Arena number", "Arena description", "Interval length (sec)", "Total intervals"));
        for (KeyBehaviorPair p : keyBindings) {
            columns.add(p.getBehavior());
        }
        // get rows for each arena
        ArrayList<List<String>> rows = new ArrayList<List<String>>();
        for (Arena a : arenas) {
            rows.add(createCumulativeRow(a, columns.subList(5, columns.size())));
        }
        // create file and write headers
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String c : columns) {
                writer.write(c);
                writer.write("\t");  // tab delimited
            }
            writer.newLine();
            // write rows for each arena
            for (List<String> r : rows) {
                for (String s : r) {
                    writer.write(s);
                    writer.write("\t");
                }
                writer.newLine();
            }
            System.out.println("cumulative file made in " + outputPath);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}
