package application.gui.payment.create;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class PageWageCompensations {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;

    private ObservableList<PaymentWageCompensationD> customConpensations;


    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/
    public PageWageCompensations(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
        paneCreate.getPaymentManager().getGrossWageManager().processWageCompensationsManager();
        customConpensations = FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getWageCompensationManager().getCustomCompensations());
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------SETTERS/GETTERS------------------------------------------------*/
    public PaneCreate getPaneCreate() {
        return paneCreate;
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------METHODS----------------------------------------------------*/
    private boolean isRightFroIncapacityForWork()
    {
        if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti"))
        {
            if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getConditionsD().getPremature().equals("0")
                    && paneCreate.getPaymentManager().getEmployeeConditionsManager().getConditionsD().getInvalidity40().equals("0")
                    && paneCreate.getPaymentManager().getEmployeeConditionsManager().getConditionsD().getInvalidity70().equals("0")
                    && paneCreate.getPaymentManager().getEmployeeConditionsManager().getConditionsD().getRetirement().equals("0")
                    && paneCreate.getPaymentManager().getEmployeeConditionsManager().getWageDS().get(0).getPayWay().equals("pravidelne"))
                return true;
            else
                return false;
        }
        else if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }



    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI FIELDS---------------------------------------------------*/
    @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private Label label;

    @FXML
    private Text name0;
    @FXML
    private Text name1;
    @FXML
    private Text name2;
    @FXML
    private Text name3;
    @FXML
    private Text days0;
    @FXML
    private Text days1;
    @FXML
    private Text days2;
    @FXML
    private Text days3;
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
    @FXML
    private Text hours0;
    @FXML
    private Text hours1;
    @FXML
    private Text hours2;
    @FXML
    private Text hours3;
    @FXML
    private Text calcFrom0;
    @FXML
    private Text calcFrom1;
    @FXML
    private Text calcFrom3;
    @FXML
    private TextField calcFrom2;
    @FXML
    private TableColumn reasonCol;
    @FXML
    private TableColumn daysCol;
    @FXML
    private TableColumn hoursCol;
    @FXML
    private TableColumn calcFromCol;
    @FXML
    private TableColumn sumCol;
    @FXML
    private TableView<PaymentWageCompensationD> tab1 = new TableView<>();
    @FXML
    private Text sum5;


    
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        hoursCol.setCellValueFactory(new PropertyValueFactory<>("hours"));
        calcFromCol.setCellValueFactory(new PropertyValueFactory<>("calculatedFrom"));
        sumCol.setCellValueFactory(new PropertyValueFactory<>("wage"));
        setTab1();

        setInitialData();
        setElementsDisable();
        setListeners();


    }

    private void setTab1()
    {
        tab1.setFixedCellSize(25);
        tab1.prefHeightProperty().bind(tab1.fixedCellSizeProperty().multiply(Bindings.size(tab1.getItems()).add(1.2)));
        tab1.minHeightProperty().setValue(50);
        tab1.maxHeightProperty().bind(tab1.prefHeightProperty());
    }


    private void setElementsDisable()
    {
        if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
                || paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            name0.setDisable(true); days0.setDisable(true); hours0.setDisable(true); calcFrom0.setDisable(true); sum0.setDisable(true);
            name1.setDisable(true); days1.setDisable(true); hours1.setDisable(true); calcFrom1.setDisable(true); sum1.setDisable(true);
            name3.setDisable(true); days3.setDisable(true); hours3.setDisable(true); calcFrom3.setDisable(true); sum3.setDisable(true);
        }

        if(!isRightFroIncapacityForWork())
        {
            name2.setDisable(true); days2.setDisable(true); hours2.setDisable(true); calcFrom2.setDisable(true); sum2.setDisable(true);
        }
    }

    private void setInitialData()
    {
        days0.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getNonActiveEmergency().getDays());
        hours0.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getNonActiveEmergency().getHours());
        sum0.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getNonActiveEmergency().getWage());

        days1.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getHoliday().getDays());
        hours1.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getHoliday().getHours());
        sum1.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getHoliday().getWage());

        days2.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getIncapacityForWork().getDays());
        hours2.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getIncapacityForWork().getHours());
        if(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getIncapacityForWork().getCalculatedFrom()!=null)
            calcFrom2.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getIncapacityForWork().getCalculatedFrom());
        sum2.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getIncapacityForWork().getWage());

        days3.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getNursing().getDays());
        hours3.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getNursing().getHours());
        sum3.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().getNursing().getWage());
        sum4.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                .getSpecificCompensationsTotal().toPlainString() + " €");

        tab1.setItems(customConpensations);
        sum5.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                .getCustomCompensationsTotal().toPlainString() + " €");
    }

    private void setListeners()
    {
        calcFrom2.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                if(!calcFrom2.getText().trim().equals(""))
                {
                    if(calcFrom2.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
                    {
                        paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                                .calculateIncapacityForWork(calcFrom2.getText());
                        sum2.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                                .getIncapacityForWork().getWage() + " €");
                        sum4.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                                .getSpecificCompensationsTotal().toPlainString() + " €");
                    }
                    else
                    {
                        CustomAlert a = new CustomAlert("warning", "Chyba", "Vstupom musí byť celé alebo desatiné číslo!" );
                        return;
                    }
                }
                else
                {
                    paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                            .calculateIncapacityForWork("0");
                    sum2.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                            .getIncapacityForWork().getWage() + " €");
                    sum4.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                            .getSpecificCompensationsTotal().toPlainString() + " €");
                }
            }
        });
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/
    @FXML
    private void onAddClick1(MouseEvent mouseEvent)
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PageWageCompensationsAddCustom.fxml"));
        loader.setControllerFactory(c -> {
            return new PageWageCompensationsAddCustom(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie vlastnej náhrady mzdy");
        primaryStage.setScene(new Scene(root1, 903, 420));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    @FXML
    private void onRemoveClick1(MouseEvent mouseEvent) {
        PaymentWageCompensationD p = tab1.getSelectionModel().getSelectedItem();
        if(p==null) {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager().removeCustomConpensation(p);
        refreshTab1Items();
        sum5.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                .getCustomCompensationsTotal().toPlainString() + " €");

    }

    @FXML
    private void onBackClick(MouseEvent mouseEvent) {
        setBackPage();
    }

    @FXML
    private void onNextClick(MouseEvent mouseEvent) {
        paneCreate.getCompensations().setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getWageCompensationManager().getCompensationsTotal().toPlainString() + " €");
        setNextPage();
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/
    private void refreshTab1Items()
    {
        customConpensations.removeAll(customConpensations);
        customConpensations.addAll(FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getWageCompensationManager().getCustomCompensations()));
    }

    public void addCustomCompensation(PaymentWageCompensationD paymentWageCompensationD)
    {
        paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                .addCustomConpensation(paymentWageCompensationD);
        refreshTab1Items();
        sum5.setText(paneCreate.getPaymentManager().getGrossWageManager().getWageCompensationManager()
                .getCustomCompensationsTotal().toPlainString() + " €");
    }

    private void setBackPage()
    {
        if(paneCreate.isWasWorkDayTimePage())
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("PageDayWorkTime.fxml"));
            l.setControllerFactory(c -> {
                return new PageDayWorkTime(paneCreate);
            });
            paneCreate.loadAnchorPage(l);
        }
        else
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("PageSurcharges.fxml"));
            l.setControllerFactory(c -> {
                return new PageSurcharges(paneCreate);
            });
            paneCreate.loadAnchorPage(l);
        }

    }

    private void setNextPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageOtherComponents.fxml"));
        l.setControllerFactory(c -> {
            return new PageOtherComponents(paneCreate);
        });
        paneCreate.setWasWorkDayTimePage(true);
        paneCreate.loadAnchorPage(l);
    }

}
