package application.gui.payment.create;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PageFund {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;



    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/

    public PageFund(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
    }



    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------METHODS----------------------------------------------------*/



    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI FIELDS---------------------------------------------------*/
    @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private TextField days;
    @FXML
    private TextField hours;
    @FXML
    private Label label;
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        setElementsWithInitialData();
        this.changeFocus();
    }

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        days.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                days.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    private void setElementsWithInitialData()
    {
        days.setText(paneCreate.getPaymentManager().getPaymentD().getDaysFund());
        hours.setText(paneCreate.getPaymentManager().getPaymentD().getHoursFund());
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/

    @FXML
    private void onNextClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;
        setModelsFromInputs();
        setElementsFromModel();
        setNextPage();
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;
        if((hours.getText() == null || hours.getText().trim().isEmpty()))
            flag=false;
        else if((days.getText() == null || days.getText().trim().isEmpty()))
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené údaje.");
            label.setText("Nevyplené údaje.");
            label.setVisible(true);
        }

        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        if(!days.isDisable() && !days.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(!hours.isDisable() && !hours.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
        }

        return flag;
    }

    private void setModelsFromInputs()
    {
        paneCreate.getPaymentManager().getPaymentD().setDaysFund(days.getText());
        paneCreate.getPaymentManager().getPaymentD().setHoursFund(hours.getText());
    }

    private void setElementsFromModel()
    {
        paneCreate.getFund().setText(paneCreate.getPaymentManager().getPaymentD().getHoursFund() + " hod / " +
                paneCreate.getPaymentManager().getPaymentD().getDaysFund() + " dní");

    }

    private void setNextPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageBasDynComp.fxml"));
        l.setControllerFactory(c -> {
            return new PageBasDynComp(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }


}
