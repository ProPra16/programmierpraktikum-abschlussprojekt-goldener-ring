package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads Files and converts the lines to Strings
 */
public class TextLoader {

    /**
     * Loads the given File and returns a String representation
     * @param p the given File
     * @return string representation of the file
     */
    public static String load(Path p) {
        if(p!=null)
            try {
                return String.join(System.lineSeparator(), Files.readAllLines(p));
            } catch (IOException ex) {
                Logger.getLogger(TextLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        return "";
    }
}