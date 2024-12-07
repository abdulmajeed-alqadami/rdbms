package com.cse4508.todolist;




import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public class Task {
    private String comp ;
    private String Task_title;
    private String Task_Details;
    private LocalDate  Date;
    private int  id;
    
    static String basePath = Paths.get("src", "main", "java", "com", "cse4508", "todolist").toAbsolutePath().toString();
    String PATH = Paths.get(basePath, "task.txt").toString();
    static String back_up_delete = Paths.get(basePath, "back_up_delete_task.txt").toString();
    static String back_up_edit = Paths.get(basePath, "back_up_edit_task.txt").toString();
    String _PATH = Paths.get(basePath, "tempFile.txt").toString();
    
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_db";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "abdulmajeed72";


    private void load_data_db() throws IOException, SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS task_db.tasks_back_up ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "msg VARCHAR(255) NOT NULL);";

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(createTableSQL);
            }

            String selectLastMsgSQL = "SELECT id, msg FROM task_db.tasks_back_up ORDER BY id DESC LIMIT 1;";
            try (PreparedStatement statement = connection.prepareStatement(selectLastMsgSQL)) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String lastMessage = resultSet.getString("msg");
                    Path filePath = Paths.get("E:\\todolist\\javafx_to_do_list\\todolist\\src\\main\\java\\com\\cse4508\\todolist\\task.txt");
                    Files.write(filePath, lastMessage.getBytes());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void save_data(String data) throws SQLException{
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "CREATE TABLE IF NOT EXISTS  task_db.tasks_back_up ("
                    +"id INT AUTO_INCREMENT PRIMARY KEY,"+"msg VARCHAR(255) NOT NULL" + ");";

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sql);
            }
            String sql_ = "INSERT INTO tasks_back_up(msg) VALUES (?);";
            PreparedStatement statement = connection.prepareStatement(sql_);
            statement.setString(1, data);
            statement.executeUpdate();
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    Task(LocalDate Date_,String id,String Task_title_,String Task_Details_) throws IOException,SQLException {
    this.Date = Date_;
    this.Task_Details = Task_Details_;
    this.Task_title = Task_title_;

        this.comp = "false";
    if(!(isIdAvailable(Integer.parseInt(id)))){
        this.id = Integer.parseInt(id);
    }else{
        System.exit(1);
    }
    }

    Task(){}


    public String extract_text() throws SQLException {
        String fileContents = null;
        try {
        load_data_db();
        FileReader fileReader = new FileReader(PATH);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        bufferedReader.close();
         fileContents = stringBuilder.toString();
        }
        catch (IOException | SQLException e) {
        e.printStackTrace();
    }
        return fileContents;
    }




    public void sort_tasks() throws IOException,SQLException {
       Path sourcePath = Paths.get("E:\\todolist\\javafx_to_do_list\\todolist\\src\\main\\java\\com\\cse4508\\todolist\\tempFile.txt");
        if(sourcePath.toFile().exists()){
       Path destinationPath = Paths.get("E:\\todolist\\javafx_to_do_list\\todolist\\src\\main\\java\\com\\cse4508\\todolist\\task.txt");
        try (InputStream inputStream = Files.newInputStream(sourcePath);
             OutputStream outputStream = Files.newOutputStream(destinationPath)) {

            int byteRead;
            while ((byteRead = inputStream.read()) != -1) {
                outputStream.write(byteRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(new File(PATH));
        while (scanner.hasNextLine()) {
                    lines.add(scanner.nextLine());
        }
                scanner.close();
                Collections.sort(lines);
                FileWriter writer = new FileWriter(PATH);
                for (String line : lines) {
                    writer.write(line + "\n");
                }
                writer.close();
                save_data(extract_text());}
        }

public void sort_add() throws IOException,SQLException {
    ArrayList<String> lines = new ArrayList<>();
    Scanner scanner = new Scanner(new File(PATH));
    while (scanner.hasNextLine()) {
        lines.add(scanner.nextLine());
    }
    scanner.close();
    Collections.sort(lines);
    FileWriter writer = new FileWriter(PATH);
    for (String line : lines) {
        writer.write(line + "\n");
    }
    writer.close();
    save_data(extract_text());}

    public boolean AppendToFileFiles() throws IOException,SQLException {
        String textToAppend = Date.toString() + " " + String.valueOf(id) + " " + String.valueOf(false) + " " + Task_title + " "  + Task_Details + "\n";
        Files.write(Paths.get(PATH), textToAppend.getBytes(), StandardOpenOption.APPEND);
        sort_add();
        return true;
    }



    public boolean isIdAvailable(int id) throws IOException,SQLException {
        File filePath = new File(PATH);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            String[] parts = currentLine.split(" ");
            if (parts.length >= 2 && parts[1].equals(String.valueOf(id))) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    public String searchInfo(String id) throws SQLException {
        String info = null;
        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String taskId = parts[1];
                if (taskId.equals(id)) {
                    info = line;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }




    public boolean markComplete(String taskId) throws IOException,SQLException {
        boolean taskFound = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH));
             BufferedWriter writer = new BufferedWriter(new FileWriter(_PATH))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(" ");
                if (parts.length >= 4 && parts[1].equals(taskId) && parts[2].equals("false")) {
                    parts[2] = "true";
                    currentLine = String.join(" ", parts);
                    taskFound = true;
                }
                writer.write(currentLine + "\n");
            }
        }
        if (!taskFound) {
            FileWriter fileWriter = new FileWriter(_PATH);
            fileWriter.write("");
            fileWriter.close();
            return false;
        }
        sort_tasks();
        FileWriter fileWriter = new FileWriter(_PATH);
        fileWriter.write("");
        fileWriter.close();
        return true;
    }



    public boolean deleteTaskToFile(String taskInfo) throws IOException,SQLException {
        boolean deleted = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH));
             BufferedWriter writer = new BufferedWriter(new FileWriter(_PATH))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.equals(taskInfo)) {
                    writer.write(currentLine + "\n");
                } else {
                    deleted = true;
                    append_delete_file_back_up(taskInfo+"\n");
                }
            }
        }
        if (deleted) {
            sort_tasks();
            FileWriter fileWriter = new FileWriter(_PATH);
            fileWriter.write("");
            fileWriter.close();
            return true;
        } else {
            FileWriter fileWriter = new FileWriter(_PATH);
            fileWriter.write("");
            fileWriter.close();
            return false;
        }
    }


    public void append_edit_file_back_up(String lineToedit) throws IOException {
        Files.write(Paths.get(back_up_edit), lineToedit.getBytes(), StandardOpenOption.APPEND);
    }
    public void return_edit_file_back_up(String str_new_edit) throws IOException, SQLException {
        delete(str_new_edit,back_up_edit);
        String[] parts = str_new_edit.split(" ");
        delete(searchInfo(parts[1]),PATH);
        Files.write(Paths.get(PATH), str_new_edit.getBytes(), StandardOpenOption.APPEND);
        sort_add();
    }
    public void append_delete_file_back_up(String lineToDelete) throws IOException {
        Files.write(Paths.get(back_up_delete), lineToDelete.getBytes(), StandardOpenOption.APPEND);
    }
    public void return_delete_file_back_up(String str_new_delete) throws IOException, SQLException {
        delete(str_new_delete,back_up_delete);
        String[] parts = str_new_delete.split(" ");
        delete(searchInfo(parts[1]),PATH);
        Files.write(Paths.get(PATH), str_new_delete.getBytes(), StandardOpenOption.APPEND);
        sort_add();
    }


    public void delete(String lineToDelete,String path) throws IOException,SQLException

    {try (BufferedReader reader = new BufferedReader(new FileReader(path));
    BufferedWriter writer = new BufferedWriter(new FileWriter(_PATH))) {
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            if (!currentLine.equals(lineToDelete)) {
                writer.write(currentLine + "\n");
            }
        }
    }
        try (BufferedReader reader = new BufferedReader(new FileReader(_PATH));
             BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                    writer.write(currentLine + "\n");

            }
        }

    }

    public boolean editTask(String lineToDelete,String str_new) throws IOException,SQLException {
        boolean x = false;
        File filePath = new File(PATH);
        File tempFile = new File(_PATH);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            if (!currentLine.equals(lineToDelete)) {
                writer.write(currentLine + "\n");
            } else {
                x = true;
                append_edit_file_back_up(lineToDelete+"\n");
                writer.write(str_new + "\n");
            }
        }
        reader.close();
        writer.close();
        if (!x) {
            FileWriter fileWriter = new FileWriter(_PATH);
            fileWriter.write("");
            fileWriter.close();
            return false;
        }
        sort_tasks();
        FileWriter fileWriter = new FileWriter(_PATH);
        fileWriter.write("");
        fileWriter.close();

        return true;
    }
}