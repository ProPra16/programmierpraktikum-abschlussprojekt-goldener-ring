package util;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author erdelfion
 */
public class Exercise {
    private String name;
    private String description;
    private HashMap<String,List<String>> classes;
    private HashMap<String,List<String>> tests;
    double[] config;

    public Exercise() {
        this.name = "";
        this.description = "";
        this.classes = new HashMap();
        this.tests = new HashMap();
        this.config = new double[3];
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String,List<String>> getClasses() {
        return classes;
    }

    public void addClass(String name, List<String> lines) {
        this.classes.put(name, lines);
    }

    public HashMap<String,List<String>> getTests() {
        return tests;
    }

    public void addTest(String name, List<String> lines) {
        this.tests.put(name, lines);
    }
}