package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
import application.models.WageFormD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ControllerAddWageform
{
    public Button cancel;
    public Button create;
    public Label label;
    public TextField name, unit, unitShort;


    private ControllerPageFirmForm controllerPageFirmForm;
    private WageFormD wageFormD;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        changeFocus();
    }

    public ControllerAddWageform(ControllerPageFirmForm controllerPageFirmForm) {
        this.controllerPageFirmForm = controllerPageFirmForm;
        this.wageFormD = new WageFormD();
    }

    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;

        CustomAlert al = new CustomAlert("Vytvorenie formy mzdy", "Ste si istý že chcete vytvoriť novú formu mzdy?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        this.setModelsFromInputs();
        try {
            this.createWageForm();
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

        this.controllerPageFirmForm.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(name.getText() == null || name.getText().trim().isEmpty())
            flag=false;
        else if(unit.getText() == null || unit.getText().trim().isEmpty())
            flag=false;
        else if(unitShort.getText() == null || unitShort.getText().trim().isEmpty())
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
        this.wageFormD.setName(name.getText());
        this.wageFormD.setUnit(unit.getText());
        this.wageFormD.setUnitShort(unitShort.getText());
    }

    private void createWageForm() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("name", this.wageFormD.getName());
        ht.addParam("unit", this.wageFormD.getUnit());
        ht.addParam("unitshort", this.wageFormD.getUnitShort());

        ht.sendPost("wage/crt_form", LoggedInUser.getToken(), LoggedInUser.getId());
    }

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                name.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }

}
