package application;

import application.alerts.CustomAlert;
import application.gui.Login;
import application.httpcomunication.HttpClientClass;
import application.pdf.PaymentPDF;
import application.pdf.PaymnetTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/Login.fxml"));
        Parent root = loader.load();
        Login login = loader.getController();
        if (!login.setProperties()) return;

        primaryStage.setTitle("Prihlasovacie okno");
        primaryStage.setScene(new Scene(root, 464, 294));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
