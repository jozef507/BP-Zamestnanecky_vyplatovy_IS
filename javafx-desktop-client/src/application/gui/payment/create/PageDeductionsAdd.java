package application.gui.payment.create;

import application.models.PaymentDeductionD;
import application.models.PaymentOtherComponentD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PageDeductionsAdd {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PageDeductions pageDeductions;

    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/
    public PageDeductionsAdd(PageDeductions pageDeductions) {
        this.pageDeductions = pageDeductions;

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

        PaymentDeductionD paymentDeductionD = new PaymentDeductionD();
        paymentDeductionD.setName(name.getText());
        paymentDeductionD.setSum(sum.getText());
        pageDeductions.addDeduction(paymentDeductionD);

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
