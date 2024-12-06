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
import java.util.Objects;

public class view_completed {
    String PATH     = "E:\\todolist\\javafx_to_do_list\\todolist\\src\\main\\java\\com\\cse4508\\todolist\\task.txt";


    @FXML
    private ListView<String> listview;

    @FXML
    public void Readfromfile_sort_completed() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if(parts[2].equals("true")){
                 line = parts[0] + "\t" + parts[1] + "\t\t\t  " + parts[2] + "\t\t\t" + parts[3] + "\t   " + parts[4];
                listview.getItems().add(line);
                }
            }
        }
    }


    @FXML
    public void initialize() throws IOException{
        Readfromfile_sort_completed();
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
