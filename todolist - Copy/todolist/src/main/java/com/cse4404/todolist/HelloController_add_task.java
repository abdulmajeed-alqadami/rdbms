package com.cse4404.todolist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;


public class HelloController_add_task {

    @FXML

    private TextField details;

    @FXML
    private TextField title1;
    @FXML
    private TextField TASKID;

    @FXML
    private DatePicker date;
    @FXML

    private Label alert;

    @FXML
    public void on_back_click(ActionEvent event) throws IOException, SQLException {
        Button Button = (Button) event.getSource();
        Scene scene = Button.getParent().getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    public void on_create_click(ActionEvent event) throws IOException,SQLException {
        String taskTitle = title1.getText().trim();
        String taskDetails = details.getText().trim();
        LocalDate taskDate = date.getValue();
        String taskid = TASKID.getText().trim();
        LocalDate today = LocalDate.now();
        if (!taskTitle.isEmpty() && !taskDetails.isEmpty() && taskDate != null && ((taskDate.isAfter(today)) || (taskDate.isEqual(today)))) {
            Task task = new Task(taskDate,taskid,taskTitle, taskDetails);
            if(task.AppendToFileFiles()){on_back_click(event);}else{alert.setText("Check the I/O files !!");}
        } else {
            alert.setText("Check the nullity and realability content enteries !!");
        }
    }

}