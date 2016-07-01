package gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import util.Exercise;
import xml.Sink;

public class WorkshopControl implements Initializable {
    
    private static WorkshopControl activeObject;
    
    private static List<Exercise> exercises;
    
    private Timer timer;
    
    @FXML
    private BorderPane root;
    
    @FXML
    private GridPane catalogGrid;
    private TextArea textArea;
    
    @FXML
    private MenuItem about;
    
    @FXML
    private Label timeLabel;
    
    @FXML
    private CheckBox babysteps, attd;

    
    @FXML 
    protected void handleOnAction(Event e){
        setExtensions();
        System.out.println("Ready Button pressed");
    }            
    
    @FXML
    protected void startNewExerciseOnAction(ActionEvent event) throws IOException{
        // stop Time and reset
        timer.stop();
        timeLabel.setText("0:0");
        
        // load scroll pane with catalog
        ScrollPane center = FXMLLoader.load(WorkshopControl.class.getResource("scrollPaneKatalog.fxml"));
        root.setCenter(center);
        
        // aktiviere Checkboxen
        babysteps.setDisable(false);
        attd.setDisable(false);
    }
    
    void readCatalog()
    {
        try
        {   
            InputSource is = new InputSource(
                new InputStreamReader(
                    new FileInputStream("Katalog.xml") , "UTF-8"
                )
            );
            is.setEncoding("UTF-8");
            SAXParserFactory.newInstance().
                    newSAXParser().parse(is, new Sink());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
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
        WorkshopControl.activeObject = this;
        WorkshopControl.exercises = new ArrayList();
        this.readCatalog();
    }
    
    public static void addExercise(Exercise exercise)
    {
        System.out.println(exercise.getName());
        
        WorkshopControl.exercises.add(exercise);
        GridPane grid = WorkshopControl.activeObject.catalogGrid;
        
        WSCButton btn = new WSCButton(exercise.getName(), exercise);
        btn.setMaxWidth(Double.MAX_VALUE);
        grid.add(btn, 0, grid.getChildren().size()-1);
        btn.setOnAction((ActionEvent event) -> {            
            WorkshopControl.activeObject.changeToTest(btn.getExercise());
        });
    }

    private static class WSCButton extends Button {
        Exercise exercise;
        public WSCButton(String name, Exercise exercise) {
            super(name);
            this.exercise = exercise;
        }
        
        public Exercise getExercise()
        {
            return this.exercise;
        }
    }
    
    private void setExtensions(){
        // lese Status ein
        if(babysteps.isSelected()) timer = new Timer();
        //attd noch ausstehend
        
        // deaktiviere Checkboxen
        babysteps.setDisable(true);
        attd.setDisable(true);
    }
    
    private void changeToTest(Exercise exercise) {
        this.textArea = new TextArea();
        this.root.setCenter(this.textArea);
        List<String> tests = new ArrayList();
        
        exercise.getTests().values().stream().forEach((ls) -> {
            this.textArea.appendText(String.join(System.lineSeparator(), ls));
        });              
    }
    
    private class Timer{
    
        //Attribute
        private int seconds;
        private String time;
        private final Timeline timeline;
        
        //Konstruktor
        public Timer(){
            seconds = 0;
            
            timeline = new Timeline(new KeyFrame (Duration.seconds(1), (ActionEvent event) ->{
                seconds += 1;
                time = seconds/60 + ":" + seconds%60;
                timeLabel.setText(time);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
        
        public void stop(){
            timeline.stop();
        }
        
        //Getter
        public int getSeconds(){
            return seconds;
        }
        public String getTime(){
            return time;
        }
    }
    
    
}