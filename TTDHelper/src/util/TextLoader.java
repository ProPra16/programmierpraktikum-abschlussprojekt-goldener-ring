/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Lars
 */
public class TextLoader {

    public static String load(File f) {
        String out = "";
        if (f.exists()) {            
            BufferedReader br = null;
            try {

                br = new BufferedReader(new FileReader(f));
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.equals("")) {
                        out += line;
                    }

                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return out;
    }
}
