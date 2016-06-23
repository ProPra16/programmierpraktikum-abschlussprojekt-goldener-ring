package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import util.Exercise;
import xml.XML;
import xml.Sink;

public class WorkshopControl implements Initializable {
    
    private static List<Exercise> exercises;
    
    @FXML
    private Pane root;
    @FXML
    private GridPane catalogGrid;
    @FXML
    private MenuItem about;

    
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

    @FXML
    protected void AboutOnAction(ActionEvent e) {
        String text = "Baris, Kirill, Lars, Simon";
        BorderPane rootTmp = new BorderPane();
        rootTmp.setId("borderpane");

        Label topLabel = new Label("Created by:");
        topLabel.setId("topLabel");
        Label centerLabel = new Label(text);
        centerLabel.setId("centerLabel");
        
        rootTmp.setTop(topLabel);
        rootTmp.setCenter(centerLabel);

        // Import von Styles
        URL stylesheet = WorkshopControl.class.getResource("about.css");
        rootTmp.getStylesheets().add(stylesheet.toExternalForm());

        //anzeigen lassen
        Stage stage = new Stage();
        stage.setTitle("About Us");
        stage.setScene(new Scene(rootTmp, 400, 200));
        stage.centerOnScreen();
        stage.show();
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
