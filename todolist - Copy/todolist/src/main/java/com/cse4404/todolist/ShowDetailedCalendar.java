package com.cse4404.todolist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class ShowDetailedCalendar {
    String PATH     = "E:\\todolist\\javafx_to_do_list\\todolist\\src\\main\\java\\com\\cse4404\\todolist\\task.txt";



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
    private Label label_title;
    @FXML
    private Label label_catagory;
    @FXML
    private Label label_date;
    @FXML
    private Label label_complete;
    @FXML
    private Label label_Description;
    @FXML
    private Button nextMonthButton;
    @FXML
    private Button previousMonthButton;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private DialogPane pane_controller;


    String temp;
    public Map<LocalDate, String> readTasksFromFile() {
        Map<LocalDate, String> tasks = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    LocalDate date = LocalDate.parse(parts[0].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String task = parts[1].trim();
                    tasks.put(date, task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private LocalDate currentDate = LocalDate.now();

    public void handleTaskClick_off() {
        pane_controller.setVisible(false);
    }
    public void click_month_plus(){
        currentDate = currentDate.plusMonths(1);
        updateCalendar();
    }

    public void click_month_mins(){
        currentDate = currentDate.minusMonths(1);
        updateCalendar();
    }


    public void initialize() {
        updateCalendar();

    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        Map<LocalDate, String> tasks = readTasksFromFile();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                LocalDate date = currentDate.withDayOfMonth(1).plusDays(row * 7 + col);
                Label dateLabel = new Label(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                StringBuilder tasksForDate = new StringBuilder();
                tasks.forEach((taskDate, taskDescription) -> {
                    if (taskDate.equals(date)) {
                        tasksForDate.append(taskDescription).append("\n\n");

                    }
                });
                Label taskLabel = new Label( tasksForDate.toString()+ "\n\n");
                calendarGrid.add(dateLabel, col, row);
                calendarGrid.add(taskLabel, col, row + 1);
                System.out.print(taskLabel.getText());
                taskLabel.setOnMouseClicked(event -> {
                    try {
                        handleTaskClick(taskLabel.getText().trim());
                    } catch (SQLException e) {
                        System.err.println("Error fetching task details: " + e.getMessage());
                    }
                });
            }
        }
    }

    public void handleTaskClick(String taskDescription) throws SQLException {
        pane_controller.setVisible(true);
        Task T1 = new Task();
        temp = T1.searchInfo(taskDescription);
        String[] parts = temp.split(" ");
        label_date.setText( " Task Date "+ " : " + parts[0]);
        label_complete.setText( " Task Complete "+ " : " + parts[2]);
        label_title.setText(" Task Title "+ " : " + parts[3]);
        label_Description.setText( " Task Description "+ " : " + parts[4]);
        }



}
