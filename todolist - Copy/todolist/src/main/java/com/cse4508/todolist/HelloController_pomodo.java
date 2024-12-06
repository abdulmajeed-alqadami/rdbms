package com.cse4508.todolist;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HelloController_pomodo {
    @FXML
    private Label timerLabel;
    @FXML
    private Label instructionlabel;


    @FXML
    private Label information;
    @FXML
    private Button startButton;
    @FXML
    private Button shortBreakButton;
    @FXML
    private Button longBreakButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button stopButton;
    Font customFont = Font.loadFont(getClass().getResourceAsStream("/DS-DIGI.TTF"), 15);
    private Timeline timeline;
    private int minutes = 30;
    private int seconds = 0;
    private boolean isRunning = false;
    String PATH     = "E:/todolist/javafx_to_do_list/todolist/src/main/java/com/cse4404/todolist/task.txt";
    @FXML
    private Spinner<String> fxx;

    public void Readfromfile() throws IOException {
        final int[] x = {0};
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory<String>() {
            public void decrement(int steps) {
                if (x[0] >= 1) {
                    x[0]--;
                    setValue(lines.get(x[0]));
                }
            }

            public void increment(int steps) {
                if (x[0] < lines.size() - 1) {
                    x[0]++;
                    setValue(lines.get(x[0]));
                }
            }
        };
        valueFactory.setConverter(new StringConverter<String>() {
            public String toString(String object) {
                return object;
            }

            public String fromString(String string) {
                return string;
            }
        });
        valueFactory.setValue(lines.get(x[0]));
        fxx.setValueFactory(valueFactory);
    }

    @FXML
    public void initialize()throws IOException{
        startButton.setFont(customFont);
        stopButton.setFont(customFont);
        shortBreakButton.setFont(customFont);
        longBreakButton.setFont(customFont);
        continueButton.setFont(customFont);
        instructionlabel.setFont(customFont);
        instructionlabel.setOpacity(0);
        Readfromfile();
        fxx.getEditor().setOnMouseClicked(event -> {
            String selectedItem = fxx.getValue();
            String[] parts = selectedItem.split(" ");
            int id = Integer.parseInt(parts[1]);
            as.setText(Integer.toString(id));
        });
    }

    @FXML
    public void onbackclick(ActionEvent event) throws IOException {
        Button Button = (Button) event.getSource();
        Scene scene = Button.getParent().getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

     @FXML
    private TextField as;

@FXML
public void onchange() throws SQLException{
    if(as.getText() != null && !as.getText().isEmpty()) {
    Task t1 = new Task();
    information.setText(t1.searchInfo(as.getText().trim()));}
}


    @FXML
    public void oncomplete(ActionEvent event) throws IOException, SQLException {
        if(as.getText() != null && !as.getText().isEmpty()) {
            Task t1 = new Task();
            information.setText("Task Completed !!");
            if (t1.markComplete(as.getText().trim())) {
                onbackclick(event);
            }else{
                instructionlabel.setText("Check the ID !!");
                return;

            }
        }else{
            instructionlabel.setText("Check the ID !!");
        }
    }

    @FXML
    private void handleStartButton(ActionEvent event) {

        if (!isRunning) {

            instructionlabel.setOpacity(1);
            isRunning = true;
            startButton.setDisable(true);
            stopButton.setDisable(false);

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (seconds == 0) {
                    minutes--;
                    seconds = 59;
                } else {
                    seconds--;
                }

                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

                if (minutes == 0 && seconds == 0) {
                    timeline.stop();
                    isRunning = false;
                    shortBreakButton.setDisable(false);
                    longBreakButton.setDisable(false);
                    stopButton.setDisable(true);

                    instructionlabel.setOpacity(1);

                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    @FXML
    private void handleShortBreakButton(ActionEvent event) {
        minutes = 5;
        seconds = 0;
        timerLabel.setText("05:00");
        shortBreakButton.setDisable(true);
        longBreakButton.setDisable(true);
        continueButton.setDisable(false);

        instructionlabel.setOpacity(1);
        handleStartButton(null);
    }

    @FXML
    private void handleLongBreakButton(ActionEvent event) {
        minutes = 15;
        seconds = 0;
        timerLabel.setText("15:00");
        shortBreakButton.setDisable(true);
        longBreakButton.setDisable(true);
        continueButton.setDisable(false);
        handleStartButton(null);

        instructionlabel.setOpacity(1);
    }

    @FXML
    private void handleContinueButton(ActionEvent event) {
        minutes = 5;
        seconds = 0;
        timerLabel.setText("05:00");
        continueButton.setDisable(true);
        startButton.setDisable(true);

        instructionlabel.setOpacity(1);
    }

    @FXML
    private void handleStopButton(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }
        isRunning = false;
        minutes = 30;
        seconds = 0;
        timerLabel.setText("30:00");
        startButton.setDisable(false);
        stopButton.setDisable(true);
        shortBreakButton.setDisable(true);
        longBreakButton.setDisable(true);
        continueButton.setDisable(true);
        instructionlabel.setOpacity(0);
    }
}