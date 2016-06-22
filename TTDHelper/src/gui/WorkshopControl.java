package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WorkshopControl implements Initializable {

    @FXML
    private Pane root;
    @FXML
    private GridPane catalogGrid;
    @FXML
    private MenuItem about;

    @FXML
    protected void handleOnAction(ActionEvent e) {
        System.out.println("Ready Button pressed");
    }

    @FXML
    protected void AboutOnAction(ActionEvent e) {
        String text = "Test";
        VBox rootTmp = new VBox();
        rootTmp.setId("vbox");

        Label topLabel = new Label("About Us");
        topLabel.setId("topLabel");
        Label bottomLabel = new Label(text);
        bottomLabel.setId("bottomLabel");

        VBox.setVgrow(bottomLabel, Priority.ALWAYS);
        VBox.setVgrow(topLabel, Priority.ALWAYS);
        
        rootTmp.getChildren().addAll(topLabel, bottomLabel);

        // Import von Styles
        URL stylesheet = WorkshopControl.class.getResource("about.css");
        rootTmp.getStylesheets().add(stylesheet.toExternalForm());

        //anzeigen lassen
        Stage stage = new Stage();
        stage.setScene(new Scene(rootTmp, 400, 200));
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
