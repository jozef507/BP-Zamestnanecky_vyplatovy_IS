package application.gui.user;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UpdateAccountPassword
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private PageUsersDetails pageUsersDetails;
    private EmployeeD employeeD;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public UpdateAccountPassword(PageUsersDetails pageUsersDetails, EmployeeD employeeD) {
        this.pageUsersDetails = pageUsersDetails;
        this.employeeD = employeeD;
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private void createAccount() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("userid", this.employeeD.getPkID());
        ht.addParam("password", this.employeeD.getPassword());

        ht.sendPost("employee/upd_usr_pas", LoggedInUser.getToken(), LoggedInUser.getId());
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



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public Button cancel;
    public Button create;
    public Label label;
    public PasswordField password1, password2;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.setTextfieldLimit(password1, 255);
        this.setTextfieldLimit(password2, 255);
    }

    private void setTextfieldLimit(TextField textArea, int limit)
    {
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= limit ? change : null));
    }
    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;

        CustomAlert al = new CustomAlert("Zmena hesla užívateľa", "Ste si istý že chcete zmeniť heslo užívateľa?", "", "Áno", "Nie");
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

        this.pageUsersDetails.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/


    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(password1.getText() == null || password1.getText().trim().isEmpty())
            flag=false;
        else if(password2.getText() == null || password2.getText().trim().isEmpty())
            flag=false;

        if(!password1.getText().equals(password2.getText()))
            flag = false;

        if(!flag)
        {
            System.out.println("Nevyplené alebo nesprávne vyplnené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private void setModelsFromInputs()
    {
        this.employeeD.setPassword(getMd5(password1.getText()));
    }



}
