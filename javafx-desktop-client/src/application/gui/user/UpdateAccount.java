package application.gui.user;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateAccount
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private PageUsersDetails pageUsersDetails;
    private EmployeeD employeeD;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public UpdateAccount(PageUsersDetails pageUsersDetails, EmployeeD employeeD) {
        this.pageUsersDetails = pageUsersDetails;
        this.employeeD = employeeD;
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private void createAccount() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("userid", this.employeeD.getPkID());
        ht.addParam("role", this.employeeD.getUserType());
        ht.addParam("current", this.employeeD.getIsCurrent());

        ht.sendPost("employee/upd_usr", LoggedInUser.getToken(), LoggedInUser.getId());
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public Button cancel;
    public Button create;
    public Label label;
    public ComboBox role;
    public ComboBox current;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.setComboBoxes();
        this.setInputs();
        this.changeFocus();
    }

    private void setComboBoxes()
    {
        role.getItems().addAll(
                "admin",
                "riaditeľ",
                "účtovník",
                "zamestnanec"
        );

        current.getItems().addAll(
                "áno",
                "nie"
        );
    }

    private void setInputs()
    {
        if(this.employeeD.getIsCurrent().equals("1"))
            current.getSelectionModel().select(0);
        else
            current.getSelectionModel().select(1);

        if(this.employeeD.getUserType().equals("admin"))
            role.getSelectionModel().select(0);
        else if(this.employeeD.getUserType().equals("riaditeľ"))
            role.getSelectionModel().select(1);
        else if(this.employeeD.getUserType().equals("účtovník"))
            role.getSelectionModel().select(2);
        else if(this.employeeD.getUserType().equals("zamestnanec"))
            role.getSelectionModel().select(3);

    }

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        role.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                role.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
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

        CustomAlert al = new CustomAlert("Zmena informácii užívateľského konta", "Ste si istý že chcete zmeniť informácie užívateľského konta?", "", "Áno", "Nie");
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

        if(role.getSelectionModel().isEmpty())
            flag=false;
        else if(current.getSelectionModel().isEmpty())
            flag=false;

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
        this.employeeD.setUserType(role.getValue().toString());
        if(current.getValue().toString().equals("áno"))
            this.employeeD.setIsCurrent("1");
        else
            this.employeeD.setIsCurrent("0");
    }



}
