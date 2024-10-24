module org.example.werwirdmillionaer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.werwirdmillionaer to javafx.fxml;
    exports org.example.werwirdmillionaer;
}
