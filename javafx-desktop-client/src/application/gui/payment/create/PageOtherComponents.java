package application.gui.payment.create;

import application.alerts.CustomAlert;
import application.models.PaymentOtherComponentD;
import application.models.PaymentWageCompensationD;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PageOtherComponents {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;

    private ObservableList<PaymentOtherComponentD> otherComponentDS;
    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/
    public PageOtherComponents(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
        paneCreate.getPaymentManager().getGrossWageManager().processOtherComponentsManager();
        otherComponentDS = FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getOthersComponentsManager().getOtherComponentDS());
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------SETTERS/GETTERS------------------------------------------------*/
    public PaneCreate getPaneCreate() {
        return paneCreate;
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
    private TableView<PaymentOtherComponentD> tab0 = new TableView<>();
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn sumCol;

    @FXML
    private Text sum;


    
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        sumCol.setCellValueFactory(new PropertyValueFactory<>("wage"));
        setTab0();

        setInitialData();
    }

    private void setTab0()
    {
        tab0.setPlaceholder(new Label("Nevytvorili ste žiadné iné zložky mzdy"));

        tab0.setFixedCellSize(25);
        tab0.prefHeightProperty().bind(tab0.fixedCellSizeProperty().multiply(Bindings.size(tab0.getItems()).add(1.2)));
        tab0.minHeightProperty().setValue(50);
        tab0.maxHeightProperty().bind(tab0.prefHeightProperty());
    }

    private void setInitialData()
    {
       tab0.setItems(otherComponentDS);
       sum.setText(paneCreate.getPaymentManager().getGrossWageManager().getOthersComponentsManager()
               .getOtherComponentsTotal().toPlainString() + " €");
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/
    @FXML
    private void onAddClick1(MouseEvent mouseEvent)
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PageOtherComponentsAdd.fxml"));
        loader.setControllerFactory(c -> {
            return new PageOtherComponentsAdd(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie inej zložky mzdy");
        primaryStage.setScene(new Scene(root1, 400, 300));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    @FXML
    private void onRemoveClick1(MouseEvent mouseEvent)
    {
        PaymentOtherComponentD p = tab0.getSelectionModel().getSelectedItem();
        if(p==null) {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        paneCreate.getPaymentManager().getGrossWageManager().getOthersComponentsManager().removeOtherComponent(p);
        refreshTab1Items();
        sum.setText(paneCreate.getPaymentManager().getGrossWageManager().getOthersComponentsManager()
                .getOtherComponentsTotal().toPlainString() + " €");

    }

    @FXML
    private void onBackClick(MouseEvent mouseEvent) {
        setBackPage();
    }

    @FXML
    private void onNextClick(MouseEvent mouseEvent) {
        paneCreate.getOthers().setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getOthersComponentsManager().getOtherComponentsTotal().toPlainString() + " €");
        setNextPage();
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/
    private void refreshTab1Items()
    {
        otherComponentDS.removeAll(otherComponentDS);
        otherComponentDS.addAll(FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getOthersComponentsManager().getOtherComponentDS()));
    }

    public void addOtherComponent(PaymentOtherComponentD paymentOtherComponentD)
    {
        paneCreate.getPaymentManager().getGrossWageManager().getOthersComponentsManager()
                .addOtherComponent(paymentOtherComponentD);
        refreshTab1Items();
        sum.setText(paneCreate.getPaymentManager().getGrossWageManager().getOthersComponentsManager()
                .getOtherComponentsTotal().toPlainString() + " €");
    }

    private void setBackPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageWageCompensations.fxml"));
        l.setControllerFactory(c -> {
            return new PageWageCompensations(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

    private void setNextPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageMinimalWageDeficit.fxml"));
        l.setControllerFactory(c -> {
            return new PageMinimalWageDeficit(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

}
