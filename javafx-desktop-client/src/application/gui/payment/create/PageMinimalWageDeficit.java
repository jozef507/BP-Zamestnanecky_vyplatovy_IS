package application.gui.payment.create;

import application.models.PaymentOtherComponentD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PageMinimalWageDeficit {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;

    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/
    public PageMinimalWageDeficit(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
        this.paneCreate.getPaymentManager().getGrossWageManager().processMinimalWageDeficitManager();
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------SETTERS/GETTERS------------------------------------------------*/

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
    private Text sum;


    
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
       sum.setText(paneCreate.getPaymentManager().getGrossWageManager().getMinimalWageDeficitManager().getPaymentOtherComponentD().getWage() + " €");
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
        paneCreate.getDeficit().setText(paneCreate.getPaymentManager().getGrossWageManager().getMinimalWageDeficitManager()
                .getPaymentOtherComponentD().getWage() + " €");
        paneCreate.getPaymentManager().getGrossWageManager().calculateGrossWageTotal();
        paneCreate.getGrosswage().setText(paneCreate.getPaymentManager().getGrossWageManager().getGrossWage().toPlainString() + " €");
        paneCreate.getAssessmentbasis().setText(paneCreate.getPaymentManager().getGrossWageManager().getGrossWage().toPlainString() + " €");
        setNextPage();
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/
    private void setBackPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageOtherComponents.fxml"));
        l.setControllerFactory(c -> {
            return new PageOtherComponents(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

    private void setNextPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageLevies.fxml"));
        l.setControllerFactory(c -> {
            return new PageLevies(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

}
