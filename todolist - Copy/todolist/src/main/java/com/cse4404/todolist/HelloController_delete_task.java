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
import java.util.Objects;


public class HelloController_delete_task {

    @FXML
    private TextField task_id;


    @FXML
    private Label alter;



    @FXML
    public void onbackclick(ActionEvent event) throws IOException, SQLException {
        Button Button = (Button) event.getSource();
        Scene scene = Button.getParent().getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void ondeleteclick(ActionEvent event) throws IOException,SQLException {
        String  TASKID = task_id.getText().trim();
        String str;
        if (!(TASKID.isEmpty())) {
            Task task = new Task();
            str = task.searchInfo(TASKID);
            if(task.deleteTaskToFile(str)){onbackclick(event);}else{alter.setText("Check the correctness Id !!");}
        } else {
            alter.setText("Check the nullity of Id !!");
        }
    }
}