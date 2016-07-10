package util;

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
    
    
    /**
     * @return String
     */
    
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
     */
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return String
     */
    
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description 
     */
    
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return classes
     */
    
    public HashMap<String,List<String>> getClasses() {
        return classes;
    }

    /**
     * 
     * @param name
     * @param lines 
     */
    
    public void addClass(String name, List<String> lines) {
        this.classes.put(name, lines);
    }

    /**
     * 
     * @return tests
     */
    
    public HashMap<String,List<String>> getTests() {
        return tests;
    }

    /**
     * 
     * @param name
     * @param lines 
     */
    
    public void addTest(String name, List<String> lines) {
        this.tests.put(name, lines);
    }
    
    /**
     * 
     * @return LocalTime 
     */
    
    public LocalTime getBabysteps()
    {
        return this.babysteps!=LocalTime.MIN?this.babysteps:null;
    }
    
    /**
     * 
     * @param babysteps 
     */
    
    public void setBabysteps(LocalTime babysteps)
    {
        this.babysteps = babysteps;
    }
    
    /**
     * 
     * @return boolean
     */
    
    public boolean getTimetrack()
    {
        return this.timetrack;
    }
    
    /**
     * 
     * @param timetrack 
     */
    
    public void setTimetrack(boolean timetrack)
    {
        this.timetrack = timetrack;
    }
}