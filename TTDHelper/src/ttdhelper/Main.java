package ttdhelper;
import gui.WorkshopControl;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Is the Main Class and starts the gui.
 */
public class Main extends Application {
    
    /**
     * Starts the javafx portion of the program.
     * @param primaryStage the stage where the scene is displayed on.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
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
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The Main Method that starts the game.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}


