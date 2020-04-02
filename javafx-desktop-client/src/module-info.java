module BP {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires mysql.connector.java;
    requires java.net.http;
    requires json.simple;

    opens application;
    opens application.gui;
    opens application.gui.absence;
    opens application.gui.employee;
    opens application.gui.firm;
    opens application.gui.hours;
    opens application.gui.legislation;
    opens application.gui.user;
    opens application.models;
}