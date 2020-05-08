package application.gui.payment.create;

import application.alerts.CustomAlert;
import application.models.PaymentDeductionD;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PageDeductions {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;

    private ObservableList<PaymentDeductionD> paymentDeductionDS;
    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/
    public PageDeductions(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
        paneCreate.getPaymentManager().getNetWageManager().processDeductionManager();
        paymentDeductionDS = FXCollections.observableArrayList(paneCreate.getPaymentManager().getNetWageManager()
                .getDeductionsManager().getPaymentDeductionDS());
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
    private TableView<PaymentDeductionD> tab0 = new TableView<>();
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
        sumCol.setCellValueFactory(new PropertyValueFactory<>("sum"));

        setInitialData();
        setTab0();
    }

    private void setTab0()
    {
        tab0.setPlaceholder(new Label("Nevytvorili ste žiadné zrážky zo mzdy"));

        tab0.setFixedCellSize(25);
        tab0.prefHeightProperty().bind(tab0.fixedCellSizeProperty().multiply(Bindings.size(tab0.getItems()).add(1.2)));
        tab0.maxHeightProperty().bind(tab0.prefHeightProperty());

        if(paymentDeductionDS.size()==0)
            tab0.minHeightProperty().setValue(50);
        else
            tab0.minHeightProperty().bind(tab0.prefHeightProperty());
    }

    private void setInitialData()
    {
       tab0.setItems(paymentDeductionDS);
       sum.setText(paneCreate.getPaymentManager().getNetWageManager().getDeductionsManager()
               .getDeductionsTotal().toPlainString() + " €");
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/
    @FXML
    private void onAddClick1(MouseEvent mouseEvent)
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PageDeductionsAdd.fxml"));
        loader.setControllerFactory(c -> {
            return new PageDeductionsAdd(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie zrážky zo mzdy");
        primaryStage.setScene(new Scene(root1, 400, 300));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    @FXML
    private void onRemoveClick1(MouseEvent mouseEvent)
    {
        PaymentDeductionD p = tab0.getSelectionModel().getSelectedItem();
        if(p==null) {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        paneCreate.getPaymentManager().getNetWageManager().getDeductionsManager().removeDeduction(p);
        refreshTab1Items();
        sum.setText(paneCreate.getPaymentManager().getNetWageManager().getDeductionsManager()
                .getDeductionsTotal().toPlainString() + " €");

    }

    @FXML
    private void onBackClick(MouseEvent mouseEvent) {
        setBackPage();
    }

    @FXML
    private void onNextClick(MouseEvent mouseEvent) {
        paneCreate.getWagededuction().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getDeductionsManager().getDeductionsTotal().toPlainString() + " €");
        setNextPage();
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/
    private void refreshTab1Items()
    {
        paymentDeductionDS.removeAll(paymentDeductionDS);
        paymentDeductionDS.addAll(FXCollections.observableArrayList(paneCreate.getPaymentManager().getNetWageManager()
                .getDeductionsManager().getPaymentDeductionDS()));

        if(paymentDeductionDS.size()==0)
            tab0.minHeightProperty().setValue(50);
        else
            tab0.minHeightProperty().bind(tab0.prefHeightProperty());
    }

    public void addDeduction(PaymentDeductionD paymentDeductionD)
    {
        paneCreate.getPaymentManager().getNetWageManager().getDeductionsManager()
                .addDeduction(paymentDeductionD);
        refreshTab1Items();
        sum.setText(paneCreate.getPaymentManager().getNetWageManager().getDeductionsManager()
                .getDeductionsTotal().toPlainString() + " €");
    }

    private void setBackPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageLevies.fxml"));
        l.setControllerFactory(c -> {
            return new PageLevies(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

    private void setNextPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageRecapitulation.fxml"));
        l.setControllerFactory(c -> {
            return new PageRecapitulation(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

}
