package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import statistics.StatsManager;
import util.CodeCompiler;
import util.Exercise;
import xml.DOMReader;

public class WorkshopControl implements Initializable {
    private boolean first = true;
    private String code = "";
    
    private static WorkshopControl activeObject;

    private List<Exercise> exercises;

    private ToggleGroup radioGroup;

    private Timer timer;

    private Phase phase;

    private StatsManager statsmanager;

    @FXML
    private BorderPane root;
    @FXML
    private GridPane catalogGrid;
    @FXML
    private TextArea textArea;
    @FXML
    private MenuItem about, newExcercise, options;
    @FXML
    private Label timeLabel, phaseLabel;
    @FXML
    private CheckBox babysteps, track;
    @FXML
    private Button phaseButton, readyButton, backButton;
    @FXML
    private PieChart visuellPhase;
    @FXML
    protected void handleReadyButtonOnAction(ActionEvent event) {
        if (isExcerciseSelected()) {
            int exerciseNr = this.getSelectedExercise();
            if (exerciseNr == -1) {
                return;
            } else {
                this.changeToTest(exercises.get(exerciseNr));
            }

            // Einstellungen
            phaseLabel.setText("Writing a failing Test");
            phase = new Phase();

            // babysteps
            // sehr unschöne if abfrage...
            if (exercises.get(getSelectedExercise()).getBabysteps() != null) {
                setBabysteps();
            }

            // tracking
            if (exercises.get(getSelectedExercise()).getTimetrack()) {
                setTracking();
            }

            // PieChart
            this.createPieChart();
            
            backButton.setVisible(true);
            newExcercise.setDisable(false);
            readyButton.setVisible(false);
            phaseButton.setVisible(true);
        } else {
            System.out.println("No Exercise chosen");
        }
    }

    @FXML
    protected void handleBackButtonOnAction(ActionEvent event){
        goBack();
    }
    
    /*
    WICHTIG!!!!!
    "RomanNumbersTest ist Hardcoded, Simon wird ihn auf softCoded ändern.
     */
    @FXML
    protected void handlePhaseButtonOnAction(ActionEvent event) {
        // code nehmen und checke
        if (CodeCompiler.isCorrect("RomanNumbersTest", ((TextArea) root.getCenter()).getText(), phase.getState())) {
            // prüfe ob Excercise vorbei ist
            boolean fill = false;
            if (!fill) {

                // Ändere Phase
                //Sichern des Codes fehlt noch von Tests und normaler Klasse
                phase.changeForward();
                System.out.println(phase.getState());
                code = textArea.getText();
                textArea.clear();
                first = false;
                int exerciseNr = this.getSelectedExercise();
                switch(phase.getState()) {
                    case "red":
                        exercises.get(exerciseNr).getTests().values().stream().forEach((ls) -> {
                            this.textArea.appendText(String.join(System.lineSeparator(), ls));
                        });
                        break;
                    case "green":
                        exercises.get(exerciseNr).getClasses().values().stream().forEach((ls) -> {
                            this.textArea.appendText(String.join(System.lineSeparator(), ls));
                        });
                        break;
                    case "refactor":
                        exercises.get(exerciseNr).getClasses().values().stream().forEach((ls) -> {
                            this.textArea.appendText(String.join(System.lineSeparator(), ls));
                        });
                        break;
                }
                

                // babysteps
                // if abfrage unschön...
                if (exercises.get(getSelectedExercise()).getBabysteps() != null) {
                    timer.reset();
                }

                // tracking
                // für den fall das die excercise noch weiter geht
                // if abfrage unschön...
                if (exercises.get(getSelectedExercise()).getTimetrack()) {
                    statsmanager.stopTimer(false);
                }
                
                
                // lade neuen Code für entsprechende Phase
                // evtl. Methode in class Phase
            } else {
                // stoppe Zeit
                timer.stop();
                // stoppe tracking Zeit
                // if abfrage unschön...
                if (exercises.get(getSelectedExercise()).getTimetrack()) {
                    statsmanager.stopTimer(true);
                }
            }
        }
    }

    @FXML
    protected void startNewExerciseOnAction(ActionEvent event) throws IOException {
        // stop Time and reset
        if (timer != null) {
            timer.stop();
        }
        timeLabel.setText("0:0");

        // load scroll pane with catalog       
        //ScrollPane center = FXMLLoader.load(WorkshopControl.class.getResource("scrollPane.fxml"));
        //root.setCenter(center);
        
        Pane pane = FXMLLoader.load(WorkshopControl.class.getResource("workshop.fxml"));
        root.getScene().setRoot(pane);
        
        // Import von Style
        URL stylesheet = WorkshopControl.class.getResource("workshop.css");
        pane.getStylesheets().add(stylesheet.toExternalForm());
        // Scene auf Stage bringen
        
        
        

        //
        visuellPhase.getData().clear();
        
        // aktiviere Checkboxen
        babysteps.setDisable(false);
        track.setDisable(false);

        // setzte richtige Button
        readyButton.setVisible(true);
        phaseButton.setVisible(false);
    }

    void readCatalog() {
        DOMReader.parseCatalog();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WorkshopControl.activeObject = this;
        this.exercises = new ArrayList();
        this.radioGroup = new ToggleGroup();
        this.readCatalog();
        
    }

    public static void addExercise(Exercise exercise) {

        WorkshopControl.activeObject.exercises.add(exercise);
        GridPane grid = WorkshopControl.activeObject.catalogGrid;
        GridPane lblGrid = new GridPane();
        lblGrid.setMaxWidth(Double.MAX_VALUE);
        grid.add(lblGrid, 0, grid.getChildren().size() / 2);

        lblGrid.add(new Label(exercise.getName()), 0, 0);
        lblGrid.add(new Label(exercise.getDescription()), 0, 1);

        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(70);
        lblGrid.getColumnConstraints().add(column);

        lblGrid.add(new Label("Babysteps: " + (exercise.getBabysteps().equals(LocalTime.MIN)
                ? "False" : exercise.getBabysteps().toSecondOfDay() + "s")), 1, 0);
        lblGrid.add(new Label("Timetrack: " + exercise.getTimetrack()), 1, 1);

        RadioButton cb = new RadioButton();
        cb.setToggleGroup(WorkshopControl.activeObject.radioGroup);
        grid.add(cb, 1, grid.getChildren().size() / 2);
    }

    private void changeToTest(Exercise exercise) {
        this.textArea = new TextArea();
        this.root.setCenter(this.textArea);

        exercise.getTests().values().stream().forEach((ls) -> {
            this.textArea.appendText(String.join(System.lineSeparator(), ls));
        });
    }

    // Hilfsmethoden
    // Code zum Teil von http://docs.oracle.com/javafx/2/charts/pie-chart.htm
    private void createPieChart() {
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Make the test pass", 30),
                        new PieChart.Data("Refactor", 30),
                        new PieChart.Data("Write a failing test", 30));        
        visuellPhase.setData(pieChartData);

        URL stylesheet = WorkshopControl.class.getResource("piechart.css");
        visuellPhase.getStylesheets().add(stylesheet.toExternalForm());
    }

    private int getSelectedExercise() {
        RadioButton btn = (RadioButton) radioGroup.getSelectedToggle();
        if (btn != null) {
            return GridPane.getRowIndex(btn);
        }

        return -1;
    }
    private boolean isExcerciseSelected() {
        return (RadioButton) radioGroup.getSelectedToggle() != null;

    }
    private void setBabysteps() {
        timer = new Timer();
    }
    private void setTracking() {
        statsmanager = new StatsManager();
        statsmanager.startTimer(phase.getState(), exercises.get(getSelectedExercise()).getName());
    }
    public void goBack(){
        if (first) {

        } else {
            int exerciseNr = this.getSelectedExercise();
            
            switch (phase.getState()) {
                case "green":
                    phase.changeBackward();
                    textArea.clear();
                    textArea.setText(code);
                    break;
                case "red":
                    phase.changeBackward();
                    textArea.clear();
                    textArea.setText(code);
                    break;
            }
        }
    }

    // inner class Phase
    private class Phase {

        private String state;

        public Phase() {
            state = "red";
        }

        public void changeForward() {
            switch (state) {
                case "red":
                    state = "green";
                    phaseLabel.setText("Make the Test pass");
                    break;
                case "green":
                    state = "refactor";
                    phaseLabel.setText("Refactor");
                    break;
                case "refactor":
                    state = "red";
                    phaseLabel.setText("Write a failing Test");
                    break;
                default:
                    break;
            }
        }
        
        public void changeBackward() {
            switch (state) {
                case "red":
                    state = "refactor";
                    phaseLabel.setText("Refactor");
                    break;
                case "green":
                    state = "red";
                    phaseLabel.setText("Write a failing Test");
                    break;
                default:
                    break;
            }
        }

        public String getState() {
            return this.state;
        }
    }

    // inner class Timer
    private class Timer {

        //Attribute
        private int seconds;
        private String time;
        private final Timeline timeline;
        private String maxTime;

        //Konstruktor
        public Timer() {
            seconds = 0;
            System.out.println(maxTime);
            
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
                seconds += 1;
                time = seconds / 60 + ":" + seconds % 60;
                timeLabel.setText(time);
                if(time.equals("0:10")){
                    reset();
                    goBack();
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }

        public void stop() {
            timeline.stop();
        }
        public void reset() {
            seconds = 0;
        }

        //Getter
        public int getSeconds() {
            return seconds;
        }
        public String getTime() {
            return time;
        }
    }

    // ist da aber mehr auch nicht...
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
        URL stylesheet = WorkshopControl.class.getResource("menu.css");
        rootTmp.getStylesheets().add(stylesheet.toExternalForm());

        //anzeigen lassen
        Stage stage = new Stage();
        stage.setTitle("About Us");
        stage.setScene(new Scene(rootTmp, 400, 200));
        stage.centerOnScreen();
        stage.show();
    }
    
    //Just a little Idea and not finished, feel free to delete.
    
    @FXML
    protected void openOptions() throws IOException{
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Optios");        
        optionsStage.centerOnScreen();       
        
        Scene scene;
        Pane oproot = FXMLLoader.load(getClass().getResource("options.fxml"));
   
        // Import von Style
        URL stylesheet = getClass().getResource("menu.css");
        oproot.getStylesheets().add(stylesheet.toExternalForm());

        // Scene auf Stage bringen
        scene = new Scene(oproot);
        optionsStage.setScene(scene);
        optionsStage.show();

    }
    
}
