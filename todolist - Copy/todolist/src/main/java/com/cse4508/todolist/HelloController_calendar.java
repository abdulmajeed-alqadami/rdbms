package com.cse4508.todolist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class HelloController_calendar {


    @FXML
    private ListView<String> listView;
    @FXML
    private DatePicker date;
    @FXML
    private TextField id;
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
    public void onsearchclick()  {
        listView.getItems().clear();
            if (date.getValue() != null || (id.getText() != null)) {
                LocalDate taskDate = date.getValue();
                String ID = id.getText().trim();

                String filePath = "E:\\todolist\\javafx_to_do_list\\todolist\\src\\main\\java\\com\\cse4508\\todolist\\task.txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        LocalDate _D = LocalDate.parse(line.substring(0, 10));
                        String[] parts = line.split(" ");
                        String id = parts[1];
                        if (ID.equals(id) ||( _D.equals(taskDate))) {
                            listView.getItems().add(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }



}