package com.cse4404.todolist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Quotes {



    @FXML
    private Label as;
    String PATH_qu     = "E:\\todolist\\javafx_to_do_list\\todolist\\src\\main\\java\\com\\cse4404\\todolist\\qu.txt";

    @FXML
    public void Readfromfile_sort_completed() throws IOException {
        List<String> quotes = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(PATH_qu))) {
            while (scanner.hasNextLine()) {
                quotes.add(scanner.nextLine());
            }
        }

        if (quotes.isEmpty()) {
            System.out.println("The file 'productivity_quotes.txt' is empty.");
            return;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(quotes.size());

        String randomQuote = quotes.get(randomIndex);
        as.setText(randomQuote);
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
