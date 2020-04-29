module BP {
    requires javafx.fxml;
    requires javafx.controls;
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
    opens application.gui.payment;
    opens application.gui.payment.create;
    opens application.models;
}