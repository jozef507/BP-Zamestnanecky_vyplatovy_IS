package application.gui.payment.create;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.math.BigDecimal;

public class PageAverage {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;



    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/

    public PageAverage(PaneCreate paneCreate) {
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
    private TextField probableEarning;
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
        probableEarning.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                probableEarning.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    private void setElementsWithInitialData()
    {
        if(paneCreate.getPaymentManager().getGrossWageManager().getAverageWage()!=null)
            probableEarning.setText(paneCreate.getPaymentManager().getGrossWageManager().getAverageWage().toPlainString());
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
        if((probableEarning.getText() == null || probableEarning.getText().trim().isEmpty()))
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

        if(!probableEarning.isDisable() && !probableEarning.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
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
        paneCreate.getPaymentManager().getGrossWageManager().setAverageWage(new BigDecimal(probableEarning.getText()));
    }

    private void setElementsFromModel()
    {
        paneCreate.getAverageWage().setText(paneCreate.getPaymentManager().getGrossWageManager().getAverageWage() + " €");
    }

    private void setBackPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageBasDynComp.fxml"));
        l.setControllerFactory(c -> {
            return new PageBasDynComp(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

    private void setNextPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageSurcharges.fxml"));
        l.setControllerFactory(c -> {
            return new PageSurcharges(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }


}
