module BP {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.base;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.fxml;

    requires java.net.http;
    requires json.simple;
    requires itextpdf;

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
    opens application.pdf;
}