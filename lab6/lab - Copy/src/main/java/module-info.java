module com.example.lab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.lab to javafx.fxml;
    opens com.example.lab.controller to javafx.fxml;

    exports com.example.lab;
    exports com.example.lab.domain;
    exports com.example.lab.controller;
}