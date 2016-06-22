package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WorkshopControl implements Initializable {

    private Stage stage;
    private Scene scene;
    private Pane root;
    private GridPane catalogGrid;

    public WorkshopControl() {

    }
    
    @FXML 
    protected void handleOnAction(Event e){
        System.out.println("Ready Button pressed");
    }

    public void init(Stage stage) throws IOException {

    }
    
    
    //Popup aboutPopup;    
    @FXML
    void showAbout()
    {/*
        if(aboutPopup == null)
        {
            aboutPopup = new Popup();
            aboutPopup.setAutoHide( true );
            aboutPopup.setHideOnEscape( true );
            aboutPopup.setAutoFix( true );
            
            Label aboutLabel = new Label("The Members of the Golden Ring send their regards" + System.lineSeparator() + "For now at least.");
            aboutPopup.getContent().add(aboutLabel);
            aboutPopup.show(stage);
            aboutPopup.setOnCloseRequest((WindowEvent event) -> {
                aboutPopup = null;
            });
        }*/
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}