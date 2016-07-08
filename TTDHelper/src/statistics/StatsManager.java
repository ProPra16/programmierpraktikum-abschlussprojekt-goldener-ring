/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

/**
 *
 * @author Lars
 */
public class StatsManager {

    private final List<String> red = new ArrayList(); // index 0
    private final List<String> green = new ArrayList(); // index 1
    private final List<String> refactor = new ArrayList(); // index 2
    private final List[] stats;

    private String name; // Titel der Aufgabe
    private final Timer timer;
    private int index;

    public StatsManager() {
        this.stats = new List[3];
        stats[0] = red;
        stats[1] = green;
        stats[2] = refactor;
        timer = new Timer();
    }

    // nur aufrufen, wenn eine neue Aufgabe beginnt
    public void startTimer(String phase, String nameOfExcercise) {
        timer.start();
        this.setIndex(phase);
        this.name = nameOfExcercise;
    }

    public void stopTimer(boolean completly) {
        timer.stop();
        stats[index].add(timer.getTime());
        if (!completly) {
            timer.reset();
            timer.start();
        }
    }

    public void resetTimer() {
        timer.reset();
    }

    // Verweis auf Workshop Control createPieChart Methode 
    public LineChart getGui() throws IOException {
        LineChart root = FXMLLoader.load(getClass().getResource("linechart.fxml"));        
        if (red.size() == green.size() && green.size() == refactor.size()) {
            XYChart.Series seriRed = new XYChart.Series();
            seriRed = getSeries(seriRed, 0);
            seriRed.setName("Phase Red");
            
            XYChart.Series seriGreen = new XYChart.Series();
            seriGreen = getSeries(seriGreen, 1);
            seriRed.setName("Phase Green");
            
            XYChart.Series seriRefactor = new XYChart.Series();
            seriRefactor = getSeries(seriRefactor, 2);
            seriRed.setName("Phase Refactor");            
            
            root.getData().addAll(seriRed, seriGreen, seriRefactor);
            root.setTitle(this.name);
        } else {
            System.out.println("Range Check fail");
        }

        // evtl. import von css Datei 
        return root;
    }

    private XYChart.Series getSeries(XYChart.Series series, int index) {
        for (int i = 0; i < stats[index].size(); i++) {
            series.getData().add(new XYChart.Data(i, stats[index].get(i)));
        }
        return series;
    }

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

    private class Timer {

        //Attribute
        private int seconds;
        private String time;
        private Timeline timeline;

        //Konstruktor
        public Timer() {
        }

        public void start() {
            seconds = 0;
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
                seconds += 1;
                time = seconds / 60 + ":" + seconds % 60;
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
}