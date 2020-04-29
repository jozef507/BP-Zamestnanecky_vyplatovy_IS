package application.gui.payment.create;

import application.alerts.CustomAlert;
import application.models.AbsenceD;
import application.models.PaymentOtherComponentD;
import application.models.PaymentWageCompensationD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PageOtherComponentsAdd {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PageOtherComponents pageWageCompensations;

    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/
    public PageOtherComponentsAdd(PageOtherComponents pageWageCompensations) {
        this.pageWageCompensations = pageWageCompensations;

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
    private TextField name;
    @FXML
    private TextField sum;

    
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
       setTextfieldLimit(name, 30);
    }

    private void setTextfieldLimit(TextField textArea, int limit)
    {
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= limit ? change : null));
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/

    @FXML
    private void onBackClick(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNextClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        PaymentOtherComponentD paymentOtherComponentD = new PaymentOtherComponentD();
        paymentOtherComponentD.setName(name.getText());
        paymentOtherComponentD.setWage(sum.getText());
        pageWageCompensations.addOtherComponent(paymentOtherComponentD);

        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;

        if((name.getText() == null || name.getText().trim().isEmpty()))
            flag=false;
        if((sum.getText() == null || sum.getText().trim().isEmpty()))
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

        if(!sum.isDisable() && !sum.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
        }

        return flag;
    }
}
