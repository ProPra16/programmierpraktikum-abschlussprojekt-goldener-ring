package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Lars
 */
public class WorkshopControl implements Initializable {

    private Stage stage;
    private Scene scene;
    private Pane root;

    public WorkshopControl() {

    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init(Stage stage) throws IOException {
        // stage importieren
        this.stage = stage;
        this.root = FXMLLoader.load(getClass().getResource("workshop.fxml"));
        this.scene = new Scene(root);

        // Scene erstellen lassen
        //this.createWorkshopScene();
        // Import von Style
        URL stylesheet = getClass().getResource("workshop.css");
        this.root.getStylesheets().add(stylesheet.toExternalForm());

        // Scene auf Stage bringen               
        this.stage.setScene(scene);
        this.stage.show();
    }
    
    /*
    private void createWorkshopScene() {
        // root Pane erstellen
        this.root = new Pane();

        // Erstellen von Anchor Panes
        AnchorPane a1 = new AnchorPane();

        // Erstelle Inhalt für Anchor 1
        MenuBar menubar = new MenuBar();
        menubar.setId("MenuBar");

        Menu datei = new Menu("Datei");
        Menu about = new Menu("About");
        Menu help = new Menu("Help");

        MenuItem item1 = new MenuItem("");

        menubar.getMenus().addAll(datei, about, help);
        a1.getChildren().add(menubar);

        // Anchor to root
        root.getChildren().addAll(a1);

        this.scene = new Scene(root);
        this.scene.setFill(Color.BLACK);
    }*/
    
    /*
    Popup aboutPopup;
    
    @FXML
    void showAbout()
    {
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
        }
    }*/
}