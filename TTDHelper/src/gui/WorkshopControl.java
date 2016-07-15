package gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.StatsManager;
import util.CodeCompiler;
import util.DOMReader;
import util.Exercise;
import util.TextLoader;

/**
 * Provides the main gui and basic control over the phases.
 */
public class WorkshopControl implements Initializable {

    private static WorkshopControl activeObject;
    private List<Exercise> exercises;
    private ToggleGroup radioGroup;
    private WorkshopTimer timer;
    private Phase phase;
    private StatsManager statsmanager;

    @FXML
    private BorderPane root;
    @FXML
    private GridPane catalogGrid;
    @FXML
    private TextArea textArea;
    @FXML
    private MenuItem newExcercise;
    @FXML
    private Label timeLabel, phaseLabel;
    @FXML
    private CheckBox babysteps, track;
    @FXML
    private Button phaseButton, readyButton, backButton;
    @FXML
    private PieChart visualPhase;

    /**
     * Starts chosen Excercise and modifiers like babysteps.
     * 
     * @param event click on Button.
     */
    
    @FXML
    protected void handleReadyButtonOnAction(ActionEvent event) {
        if (isExcerciseSelected()) {
            int exerciseNr = this.getSelectedExerciseIndex();
            if (exerciseNr == -1) {
                return;
            } else {
                this.changeToTest(exercises.get(exerciseNr));
            }
            // Einstellungen
            phaseLabel.setText("Writing a failing Test");
            phase = new Phase();

            if (phase.getState().equals("red")) {
                System.out.println("TextCode Sichern");
                this.getSelectedExercise().setCurrentTestCode(textArea.getText());
            }

            // babysteps
            if (babysteps.isSelected()) {
                setBabysteps();
            }
            // tracking
            if (track.isSelected()) {
                setTracking();
            }
            // PieChart
            this.createPieChart();

            backButton.setVisible(true);
            newExcercise.setDisable(false);
            readyButton.setVisible(false);
            phaseButton.setVisible(true);
            visualPhase.startAngleProperty().set(30);
        } else {
            System.out.println("No Exercise chosen");
        }
    }

    /**
     * Returns to previous phase.
     * @param event click on Button.
     */
    
    @FXML
    protected void handleBackButtonOnAction(ActionEvent event) {
        goBack();
    }
    
    /**
     * Switches to next Phase if button is clicked and requirements to switch are met.
     * 
     * @param event click on Button. 
     */
    
    @FXML
    protected void handlePhaseButtonOnAction(ActionEvent event) {
        //Code sichern
        if (phase.getState().equals("red")) {
            this.getSelectedExercise().setCurrentTestCode(this.textArea.getText());
        } else {
            this.getSelectedExercise().setClassCode(textArea.getText());
        }
        if (CodeCompiler.isCorrect(this.getSelectedExercise(), this.phase.getState())) {
            // prüfe ob Excercise vorbei ist
            boolean finished = 
                    this.getSelectedExercise().getAvailableTestsCount() == 
                    this.getSelectedExercise().getTests().size() && 
                    phase.getState().equals("refactor");
            if (!finished) {
                textArea.clear();
                Exercise exercise = this.getSelectedExercise();
                switch (phase.getState()) {
                    case "red":
                        this.textArea.appendText(exercise.getClassCode());
                        break;
                    case "green":
                        this.textArea.appendText(exercise.getClassCode());
                        break;
                    case "refactor":
                        this.changeToTest(exercise);
                        break;
                }
                // babysteps
                if (babysteps.isSelected()) {
                    timer.reset();
                }
                // tracking
                if (track.isSelected()) {
                    statsmanager.stopTimer(this.phase.getState(),false);
                }
                
                // Ändere Phase
                this.phase.change(true);
            } else {
                // stoppe Zeit
                if(babysteps.isSelected())
                    this.timer.stop();
                // stoppe tracking Zeit
                if (this.getSelectedExercise().getTimetrack()) {
                    this.statsmanager.stopTimer(this.phase.getState(),true);
                    this.root.setCenter(this.statsmanager.getGui());
                    this.phaseButton.setVisible(false);
                } else
                {
                    this.startNewExerciseOnAction(null);
                }
            }
        }
    }

    /**
     * Cancels current excercise and returns to startscreen.
     * 
     * 
     * @param event click on Button.
     */
    
    @FXML
    protected void startNewExerciseOnAction(ActionEvent event) {
        try {
            // stop Time and reset
            if (timer != null) {
                timer.stop();
            }
            timeLabel.setText("0:0");
            
            Pane pane = FXMLLoader.load(WorkshopControl.class.getResource("workshop.fxml"));
            root.getScene().setRoot(pane);
            
            // Import von Style
            URL stylesheet = WorkshopControl.class.getResource("workshopDark.css");
            pane.getStylesheets().add(stylesheet.toExternalForm());
            // Scene auf Stage bringen
            
            visualPhase.getData().clear();
            
            // aktiviere Checkboxen
            babysteps.setDisable(false);
            track.setDisable(false);
            
            // setzte richtige Button
            readyButton.setVisible(true);
            phaseButton.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(WorkshopControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void readCatalog() {
        DOMReader.parseCatalog();
    }

    /**
     * Initializes the variables exercises, radioGroup and Catalog.
     * Reads the catalog
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WorkshopControl.activeObject = this;
        this.exercises = new ArrayList();
        this.radioGroup = new ToggleGroup();
        this.readCatalog();
        this.catalogGrid.setMaxWidth(Double.MAX_VALUE);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.catalogGrid.setPrefWidth(screenBounds.getWidth()/3*2);
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        this.catalogGrid.getColumnConstraints().add(0,column);
    }

    /**
     * Adds exercises to screen from Catalog.
     * @param exercise The exercise to be added
     */
    
    public static void addExercise(Exercise exercise) {
        WorkshopControl.activeObject.exercises.add(exercise);
        GridPane grid = WorkshopControl.activeObject.catalogGrid;
        GridPane lblGrid = new GridPane();
        lblGrid.setMaxWidth(Double.MAX_VALUE);
        grid.add(lblGrid, 0, grid.getChildren().size() / 2);

        lblGrid.add(new Label(exercise.getName()), 0, 0);
        lblGrid.add(new Label(exercise.getDescription()), 0, 1);

        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(80);
        lblGrid.getColumnConstraints().add(column);

        lblGrid.add(new Label("Babysteps: " + exercise.getBabysteps() + "s"), 1, 0);
        lblGrid.add(new Label("Timetrack: " + exercise.getTimetrack()), 1, 1);

        RadioButton cb = new RadioButton();
        cb.setToggleGroup(WorkshopControl.activeObject.radioGroup);
        grid.add(cb, 1, grid.getChildren().size() / 2);
    }

    /**
     * Changes from Catalog to Test-writing phase.
     * @param exercise current worked on exercise.
     */
    
    private void changeToTest(Exercise exercise) {
        this.textArea = new TextArea();
        this.root.setCenter(this.textArea);
        this.getSelectedExercise().raiseAvailableTestsCount();
        this.textArea.appendText(String.join(
                    System.lineSeparator(), exercise.getAvailableTestCode(
                            exercise.getAvailableTestsCount()-1)));
    }

    // Hilfsmethoden
    private void createPieChart() {
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Make the test pass", 30),
                        new PieChart.Data("Refactor", 30),
                        new PieChart.Data("Write a failing test", 30));
        
        visualPhase.setData(pieChartData);
        //visualPhase.setEffect(new DropShadow());
        //visualPhase.legendVisibleProperty().set(false);
        //visualPhase.titleProperty().set("");
        //visualPhase.setRotate(330.0);

        URL stylesheet = WorkshopControl.class.getResource("piechart.css");
        visualPhase.getStylesheets().add(stylesheet.toExternalForm());
    }

    private int getSelectedExerciseIndex() {
        RadioButton btn = (RadioButton) radioGroup.getSelectedToggle();
        if (btn != null) {
            return GridPane.getRowIndex(btn);
        }
        return -1;
    }

    private boolean isExcerciseSelected() {
        return (RadioButton) this.radioGroup.getSelectedToggle() != null;
    }

    private void setBabysteps() {
        this.timer = new WorkshopTimer();
    }

    private void setTracking() {
        this.statsmanager = new StatsManager();
        this.statsmanager.startTimer(this.phase.getState(), this.exercises.get(getSelectedExerciseIndex()).getName());
    }

    /**
     * Goes back if the babystep timer ran out
     */
    public void makeBabyStep() {
        switch (this.phase.getState()) {
            case "green":
                this.phase.change(false);
                this.textArea.clear();
                this.textArea.setText(this.getSelectedExercise().getAvailableTestCode(
                        this.getSelectedExercise().getAvailableTestsCount()-1));
                break;
            case "red":
                this.textArea.clear();
                this.textArea.setText(this.getSelectedExercise().getAvailableTestCode(
                        this.getSelectedExercise().getAvailableTestsCount()-1));
                break;
        }
    }

    /**
     *  Goes back to the last Phase
     */
    public void goBack() {
        switch (phase.getState()) {
            case "red":
                this.startNewExerciseOnAction(null);
                phase.change(false);
                break;
            case "green":
                textArea.clear();
                textArea.setText(this.getSelectedExercise().getAvailableTestCode(
                        this.getSelectedExercise().getAvailableTestsCount()-1));
                phase.change(false);
                if(this.getSelectedExercise().getTimetrack())
                    this.statsmanager.removeLastTimerData();
                break;
            case "refactor":
                break;
        }
    }
    
    /**
     * Determines the current selected Exercise
     * @return the selected Exercise
     */
    public Exercise getSelectedExercise() {
        int exerciseNr = this.getSelectedExerciseIndex();
        return exercises.get(exerciseNr);
    }

    // inner class Phase
    private class Phase {

        private String state;

        public Phase() {
            state = "red";
        }
        
        public void change(boolean forward)
        {
            switch (state) {
                case "red":
                    if(forward)
                    {
                        state = "green";
                        phaseLabel.setText("Make the Test pass");
                        visualPhase.startAngleProperty().set(150);
                    }
                    break;
                case "green":
                    if(forward)
                    {
                        visualPhase.startAngleProperty().set(270);
                        state = "refactor";
                        phaseLabel.setText("Refactor");
                    } else
                    {
                        visualPhase.startAngleProperty().set(30);
                        state = "red";
                        phaseLabel.setText("Write a failing Test");
                    }
                    break;
                case "refactor":
                    if(forward)
                    {
                        visualPhase.startAngleProperty().set(30);
                        state = "red";
                        phaseLabel.setText("Write a failing Test");
                    }
                    break;
                default:
                    break;
            }
        }
        public String getState() {
            return this.state;
        }
    }

    // inner class WorkshopTimer
    private class WorkshopTimer {

        //Attribute
        private int seconds;
        private String time;
        private final Timeline timeline;

        //Konstruktor
        public WorkshopTimer() {
            seconds = 0;
            Exercise current = getSelectedExercise();

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
                seconds += 1;
                time = seconds / 60 + ":" + seconds % 60;
                timeLabel.setText(time);
                if (seconds == current.getBabysteps() && !phase.getState().equals("refactor")) {
                    reset();
                    makeBabyStep();
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

    /**
     * Creates new Windows that displays informations of the the creators.
     * 
     * @param e click on Button.
     */
    
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
    
    /**
     * When clicked creates new Window with 2 Buttons which change the startscreen style.
     */
    
    @FXML
    protected void handleViewMenuOnAction() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Optios");
        optionsStage.centerOnScreen();

        Button lightButton = new Button("Light");
        Button darkButton = new Button("Dark");

        lightButton.setId("button");
        darkButton.setId("button");

        lightButton.setPrefSize(114, 33);
        darkButton.setPrefSize(114, 33);

        lightButton.setLayoutX(168);
        lightButton.setLayoutY(158);
        darkButton.setLayoutX(331);
        darkButton.setLayoutY(158);

        AnchorPane oproot = new AnchorPane(lightButton, darkButton);
        oproot.setId("anchorpane");
        oproot.setPrefSize(600.0, 400.0);

        // Import von Style
        URL stylesheet = getClass().getResource("menu.css");
        oproot.getStylesheets().add(stylesheet.toExternalForm());

        // buttons on Action
        darkButton.setOnAction((ActionEvent event) -> {
            root.getStylesheets().clear();
            root.getStylesheets().add(WorkshopControl.class.getResource("workshopDark.css").toExternalForm());
        });

        lightButton.setOnAction((ActionEvent event) -> {
            root.getStylesheets().clear();
            root.getStylesheets().add(WorkshopControl.class.getResource("workshopLight.css").toExternalForm());
        });

        // Scene auf Stage bringen
        Scene scene = new Scene(oproot);
        optionsStage.setScene(scene);
        optionsStage.show();
    }
    
    /**
     * Opens a guide for the user
     * @param event The Button event
     */
    @FXML
    protected void handleUserGuideOnAction(ActionEvent event){
        TextArea guide = new TextArea();
        guide.setText(TextLoader.load(Paths.get("guide.txt")));
        Stage stage = new Stage();
        stage.setTitle("User Guide");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setScene(new Scene(guide, screenBounds.getWidth()/3*2, screenBounds.getHeight()/3*2));
        stage.centerOnScreen();
        stage.show();
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
}