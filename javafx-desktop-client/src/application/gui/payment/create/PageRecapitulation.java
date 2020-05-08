package application.gui.payment.create;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.RoundingMode;

public class PageRecapitulation {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;

    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/

    public PageRecapitulation(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
        paneCreate.getPaymentManager().getNetWageManager().calculateSumTotal();
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
    private Label label;
    @FXML
    private Text sum0;
    @FXML
    private Text sum1;
    @FXML
    private Text sum2;
    @FXML
    private Text sum3;
    @FXML
    private Text sum4;

    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        setElementsWithInitialData();
    }

    private void setElementsWithInitialData()
    {
        sum0.setText(paneCreate.getPaymentManager().getNetWageManager().getNetWage().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        sum1.setText("-"+paneCreate.getPaymentManager().getNetWageManager().getDeductionsManager().getDeductionsTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        sum2.setText(paneCreate.getPaymentManager().getNetWageManager().getSumTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        sum3.setText(paneCreate.getPaymentManager().getNetWageManager().getToBankAccountFromTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        sum4.setText(paneCreate.getPaymentManager().getNetWageManager().getInCashFromTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
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
        setElementsFromModel();
        if(!paneCreate.getPaymentManager().exportPayment())
            return;
        paneCreate.getPaymentManager().calculateAverageWage();
        paneCreate.getPagePayment().updateInfo();

        Stage stage = (Stage) next.getScene().getWindow();
        stage.close();
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/


    private void setElementsFromModel()
    {
        paneCreate.getTopay().setText(paneCreate.getPaymentManager().getNetWageManager().getSumTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
    }

    private void setBackPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageDeductions.fxml"));
        l.setControllerFactory(c -> {
            return new PageDeductions(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }




}
