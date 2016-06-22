package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class WorkshopControl implements Initializable {

    @FXML private Pane root;
    @FXML private GridPane catalogGrid;
    
    @FXML 
    protected void handleOnAction(Event e){
        System.out.println("Ready Button pressed");
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}