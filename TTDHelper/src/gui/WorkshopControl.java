package gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import util.Exercise;
import xml.Sink;
import util.CodeCompiler;

public class WorkshopControl implements Initializable {
    
    private static WorkshopControl activeObject;
    
    private static List<Exercise> exercises;
    
    private List<CheckBox> checkboxes;
    
    private Timer timer;
    
    private Phase phase;
    
    @FXML
    private BorderPane root;
    
    @FXML
    private GridPane catalogGrid;
    private TextArea textArea;
    
    @FXML
    private MenuItem about;
    
    @FXML
    private Label timeLabel, phaseLabel;
    
    @FXML
    private CheckBox babysteps, attd;
    
    @FXML
    private Button phaseButton, readyButton;

    
    @FXML 
    protected void handleReadyButtonOnAction(ActionEvent event){
        if(isExcerciseSelected()){
            int exerciseNr = this.getSelectedExercise(); 
            if(exerciseNr == -1) return;            
            else this.changeToTest(exercises.get(exerciseNr));   
            System.out.println(exercises.get(exerciseNr));
            setExtensions();
            phase = new Phase();
            
            System.out.println("Hier");
            readyButton.setVisible(false);
            phaseButton.setVisible(true);
        }else{
            System.out.println("No Exercise chosen");
        } 
        System.out.println("Ready Button pressed");
    }    
    
    @FXML
    protected void handlePhaseButtonOnAction(ActionEvent event){
        // code nehmen und checke
        if(CodeCompiler.isCorrect(((TextArea)root.getCenter()).getText(), phase.getState())){
            phase.change();
            // lade neuen Code für entsprechende Phase
            // evtl. Methode in class Phase
        }
    }
    
    private class Phase{
        
        private String state;
        
        public Phase(){
            state = "red";
        }
        
        public void change(){
            if(state.equals("red")) state = "green";
            if(state.equals("green")) state = "refactor";
            if(state.equals("refactor")) state = "red";
        }
        
        public String getState(){
            return this.state;
        }
    }
    
    @FXML
    protected void startNewExerciseOnAction(ActionEvent event) throws IOException{
        // stop Time and reset
        if(timer != null)timer.stop();        
        timeLabel.setText("0:0");
        
        // load scroll pane with catalog
        ScrollPane center = FXMLLoader.load(WorkshopControl.class.getResource("scrollPaneKatalog.fxml"));
        root.setCenter(center);
        
        // aktiviere Checkboxen
        babysteps.setDisable(false);
        attd.setDisable(false);
        
        // setzte richtige Button
        readyButton.setVisible(true);
        phaseButton.setVisible(false);
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
        int tmp = catalogGrid.getRowConstraints().size();
        checkboxes = new ArrayList();
        for(int i=0; i<(tmp-1); i++){
            CheckBox cb = new CheckBox();
            cb.setVisible(true);
            checkboxes.add(cb);
            GridPane.setRowIndex(cb, i);
            GridPane.setColumnIndex(cb, catalogGrid.getColumnConstraints().size()-1);
            catalogGrid.getChildren().add(cb);            
        }   
    }
    
    public static void addExercise(Exercise exercise) 
    {
        System.out.println(exercise.getName());
        
        WorkshopControl.exercises.add(exercise);
        GridPane grid = WorkshopControl.activeObject.catalogGrid;        
        WSCButton btn = new WSCButton(exercise.getName(), exercise);
        btn.setMaxWidth(Double.MAX_VALUE);
        grid.add(btn, 0, grid.getChildren().size()-1);
        /*
        btn.setOnAction((ActionEvent event) -> {            
            WorkshopControl.activeObject.changeToTest(btn.getExercise());
        });
        */
    }

    
    // inner class WSCButton
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
    
    private void changeToTest(Exercise exercise) {
        this.textArea = new TextArea();
        this.root.setCenter(this.textArea);
        List<String> tests = new ArrayList();
        
        exercise.getTests().values().stream().forEach((ls) -> {
            this.textArea.appendText(String.join(System.lineSeparator(), ls));
        });              
    }
    
    // Hilfsmethoden
    private int getSelectedExercise(){
        for(int i=0; i<checkboxes.size(); i++){
            if(((CheckBox)checkboxes.get(i)).isSelected()) return i;
        }
        return -1;
    }
    
    private boolean isExcerciseSelected(){
        for(int i=0; i<checkboxes.size(); i++){
            if(((CheckBox)checkboxes.get(i)).isSelected()) return true;
        }
        return false;
    }
    
    private void setExtensions(){
        // ändere phase
        phaseLabel.setText("writing a Test");
        
        // lese Status ein
        if(babysteps.isSelected()) timer = new Timer();
        //attd noch ausstehend
        
        // deaktiviere Checkboxen
        babysteps.setDisable(true);
        attd.setDisable(true);
    }
    
    
    // inner class Timer
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