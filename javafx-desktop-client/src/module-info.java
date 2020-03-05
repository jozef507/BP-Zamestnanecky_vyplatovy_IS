module BP {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires mysql.connector.java;
    requires java.net.http;
    requires json.simple;

    opens application;
    opens application.gui;
    opens application.models;
    opens application.gui.fxml;
}