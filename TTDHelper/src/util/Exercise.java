package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Is a wrapper for Exercise values.
 */
public class Exercise {
    private String name;
    private String description;
    private String className;
    private List<String> classCode;
    private final List<List<String>> tests;
    private int availableTestCount;
    private int babystepsSec;
    private boolean timetrack;

    /**
     * Initiates the variables
     */
    public Exercise() {
        this.name = "";
        this.description = "";
        this.className = "";
        this.classCode = new ArrayList();
        this.tests = new ArrayList();
        this.availableTestCount = 0;
        this.babystepsSec = 0;
        this.timetrack = false;
    }
    
    /*
     * Getters and Setters only. 
     */
    
    
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

    
    public String getClassName() {
        return this.className;
    }
    
    public String getClassCode()
    {
        StringBuilder builder = new StringBuilder();
        this.classCode.stream().map((s) -> {
            builder.append(s);
            return s;
        }).forEach((_item) -> {
            builder.append(System.lineSeparator());
        });
        if(builder.length()>0)
            builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
    
    public void addClass(String name, List<String> lines) {
        this.className = name;
        this.classCode = lines;
    }
    
    public void setClassCode(String code)
    {
        this.classCode = Arrays.asList(code.split("\n"));
    }
    
    public List<List<String>> getTests() {
        return tests;
    }
    
    public void addTest(List<String> lines) {
        this.tests.add(lines);
    }
    
    
    public int getBabysteps()
    {
        return babystepsSec;
    }

    
    public void setBabysteps(int babysteps)
    {
        this.babystepsSec = babysteps;
    }
        
    public boolean getTimetrack()
    {
        return this.timetrack;
    }
    
    public void setTimetrack(boolean timetrack)
    {
        this.timetrack = timetrack;
    }
    
    public boolean raiseAvailableTestsCount()
    {
        this.availableTestCount++;
        return this.availableTestCount <= this.tests.size();
    }

    public int getAvailableTestsCount() {
        return this.availableTestCount;
    }
    
    public String getAvailableTestName(int index)
    {
        if(index > this.getAvailableTestsCount())
            return null;
        return this.tests.get(index).get(0);
    }
    
    public String getAvailableTestCode(int index)
    {
        if(this.getAvailableTestsCount() <= index)
            return null;
        
        StringBuilder builder = new StringBuilder();
        this.tests.get(index).subList(1, this.tests.get(index).size()).stream().map((s) -> {
            builder.append(s);
            return s;
        }).forEach((_item) -> {
            builder.append(System.lineSeparator());
        });
        return builder.toString();
    }
    
    public void setCurrentTestCode(String code)
    {
        List<String> codes = new ArrayList();
        codes.add(this.getAvailableTestName(this.availableTestCount-1));
        codes.addAll(Arrays.asList(code.split("\n")));
        this.tests.set(this.availableTestCount-1, codes);
    }
}