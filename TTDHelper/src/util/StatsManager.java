package util;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

/**
 * Provides the Stats for the Tracking.
 */
public class StatsManager {

    private final List<Integer> red = new ArrayList(); // index 0
    private final List<Integer> green = new ArrayList(); // index 1
    private final List<Integer> refactor = new ArrayList(); // index 2
    private final List[] stats;

    private String name; // Titel der Aufgabe
    private final StatsTimer timer;
    private int index;

    /**
     * Initializes the variables.
     */
    public StatsManager() {
        this.stats = new List[3];
        stats[0] = red;
        stats[1] = green;
        stats[2] = refactor;
        timer = new StatsTimer();
    }

    /**
     * Starts timer. Phase String is converted to int and set to Index. Name set
     * to name of current excersise.
     *
     * @param phase name of Phase
     * @param nameOfExcercise Name of the Excercise the user is currently
     * working on.
     */
    public void startTimer(String phase, String nameOfExcercise) {
        timer.start();
        this.setIndex(phase);
        this.name = nameOfExcercise;
    }

    /**
     * Stops timer if completey is true, else creates splits and continues
     * counting.
     *
     * @param phase The current phase
     * @param completly Indicates if excercise is done.    
     */
    public void stopTimer(String phase, boolean completly) {
        timer.stop();
        this.setIndex(phase);
        stats[index].add(timer.getSeconds());        
        if (!completly) {
            timer.reset();
            timer.start();
        }
        System.out.println("timer: " + phase);
    }
    
    /**
     * Removes the last Timer Data.
     * 
     * Used when the phase is changed backwards.
     */
    public void removeLastTimerData()
    {
        this.timer.stop();
        this.timer.reset();
        this.timer.start();
        this.stats[index].remove(this.stats.length-1);
    }

    /**
     * errechnet die maximale Zeit die erreicht wurd in einer phase plus 10; 
     * @return out maxTime
     */
    private double getMaxTime() {
        if (red.size() == green.size() && green.size() == refactor.size()) {
            int out = 0;
            for (int i = 0; i < red.size(); i++) {
                if(red.get(i)> out) out =red.get(i);
                if(green.get(i)> out) out =green.get(i);
                if(refactor.get(i)> out) out =refactor.get(i);
            }
            return out + 10.0;
        }
        return 180.0;
    }

    /**
     * Creates Linechart for Phases, using linechart.fxml.
     *
     * @return root LineChart
     */
    public LineChart getGui() {
        NumberAxis X_Axis = new NumberAxis("Crossings", 0.0, (double) red.size(), 1.0);
        NumberAxis Y_Axis = new NumberAxis("Time [sec]", 0.0, this.getMaxTime(), 10.0);
        LineChart<Number, Number> root = new LineChart<>(X_Axis, Y_Axis);
        if (red.size() == green.size() && green.size() == refactor.size()) {
            XYChart.Series<Number, Number> seriRed = new XYChart.Series<>();
            seriRed = getSeries(seriRed, 0);
            seriRed.setName("Phase Red");

            XYChart.Series<Number, Number> seriGreen = new XYChart.Series();
            seriGreen = getSeries(seriGreen, 1);
            seriGreen.setName("Phase Green");

            XYChart.Series<Number, Number> seriRefactor = new XYChart.Series();
            seriRefactor = getSeries(seriRefactor, 2);
            seriRefactor.setName("Phase Refactor");

            root.getData().addAll(seriRed, seriGreen, seriRefactor);
            root.setTitle(this.name);
        } else {
            System.out.println("Range Check fail");
        }

        // evtl. import von css Datei 
        return root;
    }

    /**
     * Adds new Data to the given series, based on given index.
     *
     * @param series
     * @param index
     * @return series
     */
    private XYChart.Series getSeries(XYChart.Series series, int index) {
        for (int i = 0; i < stats[index].size(); i++) {
            series.getData().add(new XYChart.Data<>(i, stats[index].get(i)));
        }
        return series;
    }

    /**
     * Converts phase from String to int.
     *
     * @param phase Phase the user is currently in.
     */
    private void setIndex(String phase) {
        index = -1;
        switch (phase) {
            case "red":
                index = 0;
                break;
            case "green":
                index = 1;
                break;
            case "refactor":
                index = 2;
                break;
        }
        if (index == -1) {
            System.out.println("Index set is failing");
        }
    }

    private class StatsTimer {

        //Attribute
        private int seconds;
        private String time;
        private final Timeline timeline;

        //Konstruktor
        public StatsTimer() {
            seconds = 0;
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
                seconds += 1;
                time = seconds / 60 + ":" + seconds % 60;
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
        }

        /**
         * Creates new timer and starts it.
         */
        public void start() {
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
}