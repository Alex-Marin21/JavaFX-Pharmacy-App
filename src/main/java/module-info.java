module com.example.farmaciebdproiect {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.farmaciebdproiect to javafx.fxml;
    exports com.example.farmaciebdproiect;
}