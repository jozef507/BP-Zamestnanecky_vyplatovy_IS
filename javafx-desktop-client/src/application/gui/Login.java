package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class ControllerLogin /*implements Initializable*/
{
    @FXML
    public PasswordField passwordT;
    @FXML
    private Label label;
    @FXML
    private TextField usernameT;
    @FXML
    private Button btn;
    @FXML
    private AnchorPane ap;

    @FXML
    public void initialize() {
        this.changeFocus();
        this.onEnterClick(usernameT);
        this.onEnterClick(passwordT);
    }

    @FXML
    public void loginButton(MouseEvent actionEvent)
    {
        login();
    }

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        usernameT.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                ap.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    private void login()
    {
        setLabel(Color.BLACK,"Prihlasujem...");
        if (serverLogin().equals("Success")) {
            try {
                Stage stage = (Stage) btn.getScene().getWindow();
                stage.close();

                Stage primaryStage = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main_pane.fxml"));
                Parent root1 = loader.load();

                primaryStage.setTitle("Hlavné okno");
                primaryStage.setScene(new Scene(root1, 985, 602));
                primaryStage.setMinHeight(500);
                primaryStage.setMinWidth(600);
                primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        System.out.println("Stage is closing");
                        LoggedInUser.logout();
                    }
                });
                primaryStage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    private String serverLogin()
    {
        String status = "Success";
        String email = usernameT.getText();
        String password = getMd5(passwordT.getText());

        if(email.isEmpty() || password.isEmpty()) {
            setLabel(Color.TOMATO,"Nevyplnené prihlasovacie údaje!");
            status = "Error";
        } else {
            HttpClientClass ht = new HttpClientClass();
            try {
                ht.sendLoginPost(email, password);
            } catch (IOException e) {
                e.printStackTrace();
                CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                        "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
                setLabel(Color.TOMATO,"Prihlásenie neúspešné!");
                return "Error";
            } catch (InterruptedException e) {
                e.printStackTrace();
                CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                        "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
                setLabel(Color.TOMATO,"Prihlásenie neúspešné!");

                return "Error";
            } catch (CommunicationException e) {
                e.printStackTrace();
                CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                        "\nKontaktujte administrátora systému!", e.toString());
                setLabel(Color.TOMATO,"Prihlásenie neúspešné!");
                return "Error"                        ;
            }

            if(ht.getRespnseStatus()==403)
            {
                JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
                String message = json.getElement(0, "message");
                setLabel(Color.TOMATO, message);
                return "Error";
            }
            setLabel(Color.GREEN,"Prihlásenie prebehlo úspešne!");


            JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
            LoggedInUser.setId(json.getElement(0, "id"));
            LoggedInUser.setToken(json.getElement( 0,"token"));
            LoggedInUser.setEmail(email);
            LoggedInUser.setRole(json.getElement( 0,"role"));
        }
        return status;
    }

    private String getMd5(String input)
    { //geeksforgeeks
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void setLabel(Color color, String message)
    {
        label.setTextFill(color);
        label.setText(message);
        System.out.println(message);
    }

    private void onEnterClick(TextField textField)
    {
        textField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    login();
                }
            }
        });
    }

    private void onEnterClick(PasswordField textField)
    {
        textField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    login();
                }
            }
        });
    }
}
