module com.cse4508.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens com.cse4508.todolist to javafx.fxml;
    exports com.cse4508.todolist;
}