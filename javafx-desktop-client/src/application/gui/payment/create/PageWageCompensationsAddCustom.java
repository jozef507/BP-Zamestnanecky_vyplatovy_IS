package application.gui.payment.create;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.models.AbsenceD;
import application.models.PaymentWageCompensationD;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class PageWageCompensationsAddCustom {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PageWageCompensations pageWageCompensations;

    private ObservableList<AbsenceD> absenceDS;
    private ObservableList<AbsenceD> choosenAbsenceDS;



    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/
    public PageWageCompensationsAddCustom(PageWageCompensations pageWageCompensations) {
        this.pageWageCompensations = pageWageCompensations;
        absenceDS = FXCollections.observableArrayList(pageWageCompensations.getPaneCreate().getPaymentManager().getGrossWageManager()
                .getWageCompensationManager().getAbsenceDS());
        choosenAbsenceDS = FXCollections.observableArrayList();
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
    private TableView<AbsenceD> tab0 = new TableView<>();
    @FXML
    private TableColumn fromCol0;
    @FXML
    private TableColumn toCol0;
    @FXML
    private TableColumn halfCol0;
    @FXML
    private TableColumn reasonCol0;
    @FXML
    private TableColumn descriptionCol0;
    @FXML
    private TableView<AbsenceD> tab1 = new TableView<>();
    @FXML
    private TableColumn fromCol1;
    @FXML
    private TableColumn toCol1;
    @FXML
    private TableColumn halfCol1;
    @FXML
    private TableColumn reasonCol1;
    @FXML
    private TableColumn descriptionCol1;
    @FXML
    private TextField part;
    @FXML
    private ComboBox base;

    
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        fromCol0.setCellValueFactory(new PropertyValueFactory<>("fromT"));
        toCol0.setCellValueFactory(new PropertyValueFactory<>("toT"));
        halfCol0.setCellValueFactory(new PropertyValueFactory<>("halfT"));
        reasonCol0.setCellValueFactory(new PropertyValueFactory<>("reason"));
        descriptionCol0.setCellValueFactory(new PropertyValueFactory<>("characteristic"));
        tab0.setItems(absenceDS);
        tab0.setPlaceholder(new Label("Pracujúci nemá v danom období žiadne absencie."));

        fromCol1.setCellValueFactory(new PropertyValueFactory<>("fromT"));
        toCol1.setCellValueFactory(new PropertyValueFactory<>("toT"));
        halfCol1.setCellValueFactory(new PropertyValueFactory<>("half"));
        reasonCol1.setCellValueFactory(new PropertyValueFactory<>("reason"));
        descriptionCol1.setCellValueFactory(new PropertyValueFactory<>("characteristic"));
        tab1.setItems(choosenAbsenceDS);
        tab1.setPlaceholder(new Label("Nevybrali ste žiadnú pracovnú neprítomnosť"));



        base.getItems().addAll("priemerná mzda", "minimálna mzda");
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/
    @FXML
    private void onRemoveClick1(MouseEvent mouseEvent) {
        AbsenceD absenceD = choosenAbsenceDS.get(0);
        choosenAbsenceDS.remove(absenceD);
        absenceDS.add(absenceD);
    }

    @FXML
    private void onAddClick1(MouseEvent mouseEvent) {
        AbsenceD a = tab0.getSelectionModel().getSelectedItem();
        if(a==null) {
            CustomAlert al = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        if(choosenAbsenceDS.size()>0)
        {
            AbsenceD absenceD = choosenAbsenceDS.get(0);
            choosenAbsenceDS.remove(absenceD);
            absenceDS.add(absenceD);
        }

        absenceDS.remove(a);
        choosenAbsenceDS.add(a);
    }

    @FXML
    private void onBackClick(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) base.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNextClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        PaymentWageCompensationD paymentWageCompensationD = new PaymentWageCompensationD();
        paymentWageCompensationD.setAbsenceD(choosenAbsenceDS.get(0));
        paymentWageCompensationD.setCalculatedFrom(base.getSelectionModel().getSelectedItem().toString());
        paymentWageCompensationD.setPartOfBase(part.getText());
        pageWageCompensations.addCustomCompensation(paymentWageCompensationD);

        Stage stage = (Stage) base.getScene().getWindow();
        stage.close();
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;

        if(choosenAbsenceDS.size()!=1)
            flag=false;

        if((part.getText() == null || part.getText().trim().isEmpty()))
            flag=false;
        if((base.getSelectionModel().isEmpty()))
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

        if(!part.isDisable() && !part.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
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
