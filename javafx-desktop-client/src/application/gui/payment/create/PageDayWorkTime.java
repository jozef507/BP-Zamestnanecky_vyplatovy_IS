package application.gui.payment.create;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.math.BigDecimal;

public class PageDayWorkTime {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;



    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/

    public PageDayWorkTime(PaneCreate paneCreate) {
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
    private TextField dayTime;
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
        dayTime.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                dayTime.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    private void setElementsWithInitialData()
    {
        if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getNextConditionsD().getDayTime()!=null)
            dayTime.setText(paneCreate.getPaymentManager().getEmployeeConditionsManager().getNextConditionsD().getDayTime());
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/

    @FXML
    private void onBackClick(MouseEvent mouseEvent)
    {
        setBackPage();
    }

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
        if((dayTime.getText() == null || dayTime.getText().trim().isEmpty()))
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

        if(!dayTime.isDisable() && !dayTime.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
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
        paneCreate.getPaymentManager().getEmployeeConditionsManager().getNextConditionsD().setDayTime(dayTime.getText());
    }

    private void setElementsFromModel()
    {
        paneCreate.getDaytime().setText(paneCreate.getPaymentManager().getEmployeeConditionsManager().getNextConditionsD().getDayTime() + " hod");
    }

    private void setBackPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageSurcharges.fxml"));
        l.setControllerFactory(c -> {
            return new PageSurcharges(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

    private void setNextPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageWageCompensations.fxml"));
        l.setControllerFactory(c -> {
            return new PageWageCompensations(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }


}
