package com.cse4508.todolist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Objects;

public class backup {

    static String basePath = Paths.get("src", "main", "java", "com", "cse4508", "todolist").toAbsolutePath().toString();
     static String PATH = Paths.get(basePath, "task.txt").toString();
     static String back_up_delete = Paths.get(basePath, "back_up_delete_task.txt").toString();
     static String back_up_edit = Paths.get(basePath, "back_up_edit_task.txt").toString();
    boolean edit = false;
    boolean delete = false;
    @FXML
    private ListView<String> listview;

    @FXML
    public void Readfromfile_deleted() throws IOException {
        listview.getItems().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(back_up_delete))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                line = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4];
                    listview.getItems().add(line);
                }
            }
        }


    @FXML
    public void Readfromfile_eddited() throws IOException {
        listview.getItems().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(back_up_edit))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                    line = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4];
                    listview.getItems().add(line);
                }
            }
        }




    @FXML
    public void listviewonmouseclicked() throws NullPointerException, SQLException, IOException {
            if(!(listview.getSelectionModel().getSelectedItem()).isEmpty()){
                String garpah = listview.getSelectionModel().getSelectedItem();
                Task T1 = new Task();
                if(delete==true && edit==false){T1.return_delete_file_back_up(garpah);}
                if(delete==false && edit==true){T1.return_edit_file_back_up(garpah);}
            }else{
                System.out.println("No Selection Type ! ");
            }
    }

    @FXML
    public void click_eddited(ActionEvent event) throws IOException {
        Readfromfile_eddited();
        delete = false;
        edit = true;
    }

    @FXML
    public void click_deleted(ActionEvent event) throws IOException {
        Readfromfile_deleted();
        delete = true;
        edit = false;
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
}
