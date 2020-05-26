package application;

import application.httpcomunication.HttpClientClass;
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

        Parent root = FXMLLoader.load(getClass().getResource("gui/Login.fxml"));
        primaryStage.setTitle("Prihlasovacie okno");
        primaryStage.setScene(new Scene(root, 464, 294));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        setServerUrl();
        launch(args);
    }

    private static void setServerUrl()
    {
        Properties p = new Properties();

        InputStream is = null;
        try {
            is = new FileInputStream("config.properties");
            p.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpClientClass.url= p.getProperty("server_url");
    }
}
