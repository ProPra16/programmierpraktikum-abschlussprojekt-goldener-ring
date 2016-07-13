/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ttdhelper;
import gui.WorkshopControl;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Lars
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("TTDHelper");
        primaryStage.setScene(new Scene(new StackPane()));
        primaryStage.centerOnScreen();       
        //WorkshopControl wc = new WorkshopControl();
        //wc.init(primaryStage);
        
        Scene scene;
        Pane root = FXMLLoader.load(WorkshopControl.class.getResource("workshop.fxml"));
        
        // Import von Style
        URL stylesheet = WorkshopControl.class.getResource("workshopDark.css");
        root.getStylesheets().add(stylesheet.toExternalForm());
        

        // Scene auf Stage bringen
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
