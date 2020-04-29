package application.gui.payment.create;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.firm.PageFirmPlace;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.PaymentDynamicComponentD;
import application.models.PlaceD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PageBasDynCompAddDynComp
{

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private PageBasDynComp pageBasDynComp;
    private PaymentDynamicComponentD paymentDynamicComponentD;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageBasDynCompAddDynComp(PageBasDynComp pageBasDynComp) {
        this.pageBasDynComp = pageBasDynComp;
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public Button cancel;
    public Button create;
    public Label label;
    public ComboBox<String> type;
    public TextField characteristic, value;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.setTextfieldLimit(characteristic, 50);
        this.setComboBoxes();
    }

    private void setComboBoxes()
    {
        type.getItems().addAll(
                "prémia",
                "odmena",
                "bonus",
                "iné"
        );
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
        if(!this.checkFormularTypes()) return;

        this.setModelsFromInputs();
        this.pageBasDynComp.addDynamicComponent(this.paymentDynamicComponentD);

        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(characteristic.getText() == null || characteristic.getText().trim().isEmpty())
            flag=false;
        else if(value.getText() == null || value.getText().trim().isEmpty())
            flag=false;
        else if(type.getSelectionModel().isEmpty())
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

        if (!value.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag = false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return flag;
        }
        return true;
    }

    private void setModelsFromInputs()
    {
        PaymentDynamicComponentD paymentDynamicComponentD = new PaymentDynamicComponentD();
        paymentDynamicComponentD.setType(type.getSelectionModel().getSelectedItem());
        paymentDynamicComponentD.setCharacteristic(characteristic.getText());
        paymentDynamicComponentD.setWage(value.getText());
        this.paymentDynamicComponentD = paymentDynamicComponentD;
    }




}
