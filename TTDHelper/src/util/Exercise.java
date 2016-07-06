package util;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author erdelfion
 */
public class Exercise {
    private String name;
    private String description;
    private final HashMap<String,List<String>> classes;
    private final HashMap<String,List<String>> tests;
    private LocalTime babysteps;
    private boolean timetrack;

    public Exercise() {
        this.name = "";
        this.description = "";
        this.classes = new HashMap();
        this.tests = new HashMap();
        this.babysteps = LocalTime.MIN;
        this.timetrack = false;
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
    
    public LocalTime getBabysteps()
    {
        return this.babysteps;
    }
    
    public void setBabysteps(LocalTime babysteps)
    {
        this.babysteps = babysteps;
    }
    
    public boolean getTimetrack()
    {
        return this.timetrack;
    }
    
    public void setTimetrack(boolean timetrack)
    {
        this.timetrack = timetrack;
    }
}