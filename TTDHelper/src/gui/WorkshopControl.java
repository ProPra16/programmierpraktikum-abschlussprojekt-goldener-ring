package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import util.Exercise;
import xml.XML;
import xml.Sink;

public class WorkshopControl implements Initializable {
    
    @FXML private Pane root;
    @FXML private GridPane catalogGrid;
    private static List<Exercise> exercises;
    
    @FXML 
    protected void handleOnAction(Event e){
        System.out.println("Ready Button pressed");
    }
    
    void readCatalog()
    {
        try {
            XMLReader reader = XML.makeXMLReader();
            reader.setContentHandler(new Sink());
            reader.parse( new InputSource("Katalog.xml"));
        } catch (IOException | SAXException ex) {
            Logger.getLogger(WorkshopControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WorkshopControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        readCatalog();
    }
    
    public static void addExercise(Exercise exercise)
    {
        WorkshopControl.exercises.add(exercise);
    }
}