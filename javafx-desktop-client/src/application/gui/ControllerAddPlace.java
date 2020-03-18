package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.PlaceD;
import application.models.WageFormD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerAddPlace
{
    public Button cancel;
    public Button create;
    public Label label;
    public TextField name, town, street, num;


    private ControllerPageFirmPlace controllerPageFirmPlace;
    private PlaceD placeD;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.changeFocus();
    }

    public ControllerAddPlace(ControllerPageFirmPlace controllerPageFirmPlace) {
        this.controllerPageFirmPlace = controllerPageFirmPlace;
        this.placeD = new PlaceD();
    }

    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Vytvorenie pracoviska firmy", "Ste si istý že chcete vytvoriť nové pracovisko firmy?", "", "Áno", "Nie");
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

        this.controllerPageFirmPlace.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(name.getText() == null || name.getText().trim().isEmpty())
            flag=false;
        else if(town.getText() == null || town.getText().trim().isEmpty())
            flag=false;
        else if(street.getText() == null || street.getText().trim().isEmpty())
            flag=false;
        else if(num.getText() == null || num.getText().trim().isEmpty())
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

        if(num.getText().matches("^\\+?-?\\d+$")) flag=false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return flag;
        }
        return flag;
        /*try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }*/
    }

    private void setModelsFromInputs()
    {
        this.placeD.setName(name.getText());
        this.placeD.setTown(town.getText());
        this.placeD.setStreet(street.getText());
        this.placeD.setNum(num.getText());
    }

    private void createWageForm() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("name", this.placeD.getName());
        ht.addParam("town", this.placeD.getTown());
        ht.addParam("street", this.placeD.getStreet());
        ht.addParam("num", this.placeD.getNum());

        ht.sendPost("place/crt_place", LoggedInUser.getToken(), LoggedInUser.getId());
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
