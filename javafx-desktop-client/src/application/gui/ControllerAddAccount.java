package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControllerAddAccount
{
    public Button cancel;
    public Button create;
    public Label label;
    public TextField email;
    public PasswordField password1, password2;
    public ComboBox role;

    private ControllerPageUsersDetails controllerPageUsersDetails;
    private EmployeeD employeeD;


    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.setComboBoxes();
        this.changeFocus();
    }

    public ControllerAddAccount(ControllerPageUsersDetails controllerPageUsersDetails, EmployeeD employeeD) {
        this.controllerPageUsersDetails = controllerPageUsersDetails;
        this.employeeD = employeeD;
    }

    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Vytvorenie užívateľského konta", "Ste si istý že chcete vytvoriť pre pracujúceho nové užívateľské konto?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        this.setModelsFromInputs();
        try {
            this.createAccount();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return;
        }

        this.controllerPageUsersDetails.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(email.getText() == null || email.getText().trim().isEmpty())
            flag=false;
        else if(password1.getText() == null || password1.getText().trim().isEmpty())
            flag=false;
        else if(password2.getText() == null || password2.getText().trim().isEmpty())
            flag=false;

        if(!password1.getText().equals(password2.getText()))
            flag = false;

        if(role.getSelectionModel().isEmpty())
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené alebo nesprávne vyplnené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        if(!email.getText().matches(regex)) flag=false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return flag;
        }
        return flag;
    }


    private void setModelsFromInputs()
    {
        this.employeeD.setPkID(null);
        this.employeeD.setEmail(email.getText());
        this.employeeD.setPassword(getMd5(password1.getText()));
        this.employeeD.setUserType(role.getValue().toString());
    }

    private void createAccount() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("employeeid", this.employeeD.getId());
        ht.addParam("email", this.employeeD.getEmail());
        ht.addParam("password", this.employeeD.getPassword());
        ht.addParam("role", this.employeeD.getUserType());

        ht.sendPost("employee/crt_usr", LoggedInUser.getToken(), LoggedInUser.getId());
    }

    private void setComboBoxes()
    {
        role.getItems().addAll(
                "admin",
                "riaditeľ",
                "účtovník",
                "zamestnanec"
        );
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

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        email.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                email.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }


}
