package application.gui.user;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.gui.employee.PageEmployeeDetailsBox;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;

public class PageUsersDetails
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private String employeeID;
    private EmployeeD employeeD;
    private ArrayList<PageEmployeeDetailsBox> cBox;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageUsersDetails(String employeeID){

        this.employeeID = employeeID;
        try {
            this.setEmployee();
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
        MainPaneManager.getC().setBackPage("PageUsers");

    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    public void updateInfo()
    {
        try {
            this.setEmployee();
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
        setTextFields();
        setButtons();
    }

    private void setEmployee() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("employee/emp_usr/"+this.employeeID, LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        EmployeeD employeeD = new EmployeeD();
        employeeD.setId(json.getElement(0, "p_id"));
        employeeD.setName(json.getElement(0, "meno"));
        employeeD.setLastname(json.getElement(0, "priezvisko"));
        employeeD.setPhone(json.getElement(0, "telefon"));
        employeeD.setBornNum(json.getElement(0, "rodne_cislo"));
        employeeD.setBornDate(json.getElement(0, "datum_narodenia"));
        employeeD.setPkID(json.getElement(0, "pk_id"));
        employeeD.setEmail(json.getElement(0, "email"));
        employeeD.setUserType(json.getElement(0, "typ_prav"));
        employeeD.setIsCurrent(json.getElement(0, "aktualne"));
        employeeD.setLastLogIn(json.getElement(0, "posledne_prihlasenie"));
        employeeD.setCreated(json.getElement(0, "vytvorene_v"));
        this.employeeD = employeeD;

    }

    public EmployeeD getEmployeeD()
    {
        return this.employeeD;
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public Button create;
    public Button update;
    public Button change;
    public Text id;
    public Text created;
    public Text lastlogin;
    public Text current;
    @FXML
    private Text name, phone, email, born_num, born_date, username, role;
    @FXML
    private VBox vb;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setTextFields();
        setButtons();
    }

    private void setTextFields()
    {
        name.setText(this.employeeD.getName()+" "+ this.employeeD.getLastname());
        phone.setText(this.employeeD.getPhone());
        email.setText(this.employeeD.getEmail());
        born_num.setText(this.employeeD.getBornNum());
        born_date.setText(this.employeeD.getBornDate());

        id.setText(this.employeeD.getPkID());
        username.setText(this.employeeD.getEmail());
        role.setText(this.employeeD.getUserType());
        lastlogin.setText(this.employeeD.getLastLogIn());
        created.setText(this.employeeD.getCreated());
        if(this.employeeD.getIsCurrent()!=null)
        {
            if(this.employeeD.getIsCurrent().equals("1"))
                current.setText("áno");
            else
                current.setText("nie");
        }
    }

    private void setButtons()
    {
        if(employeeD.getPkID()==null)
        {
            create.setDisable(false);
            update.setDisable(true);
            change.setDisable(true);
        }
        else
        {
            update.setDisable(false);
            change.setDisable(false);
            create.setDisable(true);
        }
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void createClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAccount.fxml"));
        loader.setControllerFactory(c -> {
            return new AddAccount(this, this.employeeD);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie prihlasovacieho konta");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void updateClick(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateAccount.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdateAccount(this, this.employeeD);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Zmena údajov prihlasovacieho konta");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void changeClick(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateAccountPassword.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdateAccountPassword(this, this.employeeD);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Zmena hesla prihlasovacieho konta");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

}
