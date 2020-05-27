package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.pdf.PaymentPDF;
import application.pdf.PaymnetTable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;


public class Login /*implements Initializable*/
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private String getMd5(String input)
    { //from geeksforgeeks.com
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

    public boolean setProperties()
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
        PaymnetTable.companyName= p.getProperty("company_name");
        PaymentPDF.path= p.getProperty("pdf_output_path");

        boolean flag = true;
        if(HttpClientClass.url==null || HttpClientClass.url.trim().isEmpty())
            flag = false;
        else if (PaymnetTable.companyName==null || PaymnetTable.companyName.trim().isEmpty())
            flag = false;
        else if (PaymentPDF.path==null || PaymentPDF.path.trim().isEmpty())
            flag = false;

        CustomAlert a;
        if (!flag)
            a = new CustomAlert("warning", "Chyba", "Nesprávne nastavený súbor config.properties!" );

        return flag;
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
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


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() {
        this.changeFocus();
        this.onEnterClick(usernameT);
        this.onEnterClick(passwordT);
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

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    @FXML
    public void loginButton(MouseEvent actionEvent)
    {
        login();
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

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    private void login()
    {
        setLabel(Color.BLACK,"Prihlasujem...");
        if (serverLogin().equals("Success")) {
            try {
                Stage stage = (Stage) btn.getScene().getWindow();
                stage.close();

                Stage primaryStage = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPane.fxml"));
                Parent root1 = loader.load();

                primaryStage.setTitle("Hlavné okno");
                primaryStage.setScene(new Scene(root1, 1200, 700));
                primaryStage.setMinHeight(500);
                primaryStage.setMinWidth(900);
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


    private void setLabel(Color color, String message)
    {
        label.setTextFill(color);
        label.setText(message);
        System.out.println(message);
    }


}
