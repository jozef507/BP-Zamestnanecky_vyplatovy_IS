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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class PageBasDynComp {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;

    private ObservableList<PaymentBasicComponentD> paymentBasicComponentDS;
    private ObservableList<PaymentDynamicComponentD> paymentDynamicComponentDS;


    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/

    public PageBasDynComp(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
        paneCreate.getPaymentManager().getGrossWageManager()
                .getWorkedPerformanceManager().calculateTotalWorkedPerformance();
        paneCreate.getPaymentManager().getGrossWageManager().processImportantManagers();



    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------SETTERS/GETTERS------------------------------------------------*/

    public PaneCreate getPaneCreate() {
        return paneCreate;
    }
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------METHODS----------------------------------------------------*/
    public void addUnregularBasicComponent(WageD wageD)
    {
        paneCreate.getPaymentManager().getGrossWageManager().getBasicComponentManager().addUnregularBasicComponent(wageD);
        refreshTab1Items();
    }

    public void addDynamicComponent(PaymentDynamicComponentD paymentDynamicComponentD)
    {
        paneCreate.getPaymentManager().getGrossWageManager().getDynamicComponentManager().addDynamicComponent(paymentDynamicComponentD);
        refreshTab2Items();
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
    private VBox regularVb;
    @FXML
    private TableView<PaymentBasicComponentD> tab0 = new TableView<PaymentBasicComponentD>();
    @FXML
    private TableColumn formCol0;
    @FXML
    private TableColumn characteristicCol0;
    @FXML
    private TableColumn doneCol0;
    @FXML
    private TableColumn tarifCol0;
    @FXML
    private TableColumn sumCol0;
    @FXML
    private Text regularWageSum;

    @FXML
    private VBox unregularVb;
    @FXML
    private TableView<PaymentBasicComponentD> tab1 = new TableView<PaymentBasicComponentD>();
    @FXML
    private TableColumn formCol1;
    @FXML
    private TableColumn characteristicCol1;
    @FXML
    private TableColumn sumCol1;
    @FXML
    private Text unregularWageSum;

    @FXML
    private VBox dynamicVb;
    @FXML
    private TableView<PaymentDynamicComponentD> tab2 = new TableView<PaymentDynamicComponentD>();
    @FXML
    private TableColumn typeCol2;
    @FXML
    private TableColumn characteristicCol2;
    @FXML
    private TableColumn sumCol2;
    @FXML
    private Text dynamicWageSum;

    
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        formCol0.setCellValueFactory(new PropertyValueFactory<>("wageForm"));
        characteristicCol0.setCellValueFactory(new PropertyValueFactory<>("wageCharacteristic"));
        doneCol0.setCellValueFactory(new PropertyValueFactory<>("workedUnits"));
        tarifCol0.setCellValueFactory(new PropertyValueFactory<>("wageTarif"));
        sumCol0.setCellValueFactory(new PropertyValueFactory<>("wageForUnits"));

        formCol1.setCellValueFactory(new PropertyValueFactory<>("wageForm"));
        characteristicCol1.setCellValueFactory(new PropertyValueFactory<>("wageCharacteristic"));
        sumCol1.setCellValueFactory(new PropertyValueFactory<>("wageForUnits"));

        typeCol2.setCellValueFactory(new PropertyValueFactory<>("type"));
        characteristicCol2.setCellValueFactory(new PropertyValueFactory<>("characteristic"));
        sumCol2.setCellValueFactory(new PropertyValueFactory<>("wage"));


        if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getWageDS().get(0).getPayWay().equals("pravidelne")) {
           unregularVb.setDisable(true);
           setTab0Items();
        } else {
            regularVb.setDisable(true);
            setTab1Items();
        }
        setTab2Items();

    }

    private void setTab0Items()
    {
        paymentBasicComponentDS = FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getPaymentBasicComponentDS());
        tab0.setItems(this.paymentBasicComponentDS);
        regularWageSum.setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getBasicComponentsTotal().toPlainString() + " €");

        tab0.setFixedCellSize(25);
        tab0.prefHeightProperty().bind(tab0.fixedCellSizeProperty().multiply(Bindings.size(tab0.getItems()).add(1.2)));
        tab0.maxHeightProperty().bind(tab0.prefHeightProperty());

        if(paymentBasicComponentDS.size()==0)
            tab0.minHeightProperty().setValue(50);
        else
            tab0.minHeightProperty().bind(tab0.prefHeightProperty());

    }

    private void setTab1Items()
    {
        paymentBasicComponentDS = FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getPaymentBasicComponentDS());
        tab1.setItems(this.paymentBasicComponentDS);
        unregularWageSum.setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getBasicComponentsTotal().toPlainString() + " €");

        tab1.setFixedCellSize(25);
        tab1.prefHeightProperty().bind(tab1.fixedCellSizeProperty().multiply(Bindings.size(tab1.getItems()).add(1.2)));
        tab1.maxHeightProperty().bind(tab1.prefHeightProperty());

        if(paymentBasicComponentDS.size()==0)
            tab1.minHeightProperty().setValue(50);
        else
            tab1.minHeightProperty().bind(tab1.prefHeightProperty());
    }

    private void setTab2Items()
    {
        paymentDynamicComponentDS = FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getDynamicComponentManager().getPaymentDynamicComponentDS());
        tab2.setItems(this.paymentDynamicComponentDS);
        dynamicWageSum.setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getDynamicComponentManager().getDynamicComponentsTotal().toPlainString() + " €");

        tab2.setFixedCellSize(25);
        tab2.prefHeightProperty().bind(tab2.fixedCellSizeProperty().multiply(Bindings.size(tab2.getItems()).add(1.2)));
        tab2.maxHeightProperty().bind(tab2.prefHeightProperty());

        if(paymentDynamicComponentDS.size()==0)
            tab2.minHeightProperty().setValue(50);
        else
            tab2.minHeightProperty().bind(tab2.prefHeightProperty());
    }

    private void refreshTab0Items()
    {
        paymentBasicComponentDS.removeAll(paymentBasicComponentDS);
        paymentBasicComponentDS.addAll(FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getPaymentBasicComponentDS()));
        regularWageSum.setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getBasicComponentsTotal().toPlainString() + " €");

        if(paymentBasicComponentDS.size()==0)
            tab0.minHeightProperty().setValue(50);
        else
            tab0.minHeightProperty().bind(tab0.prefHeightProperty());
    }

    private void refreshTab1Items()
    {
        paymentBasicComponentDS.removeAll(paymentBasicComponentDS);
        paymentBasicComponentDS.addAll(FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getPaymentBasicComponentDS()));
        unregularWageSum.setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getBasicComponentsTotal().toPlainString() + " €");

        if(paymentBasicComponentDS.size()==0)
            tab1.minHeightProperty().setValue(50);
        else
            tab1.minHeightProperty().bind(tab1.prefHeightProperty());
    }

    private void refreshTab2Items()
    {
        paymentDynamicComponentDS.removeAll(paymentDynamicComponentDS);
        paymentDynamicComponentDS.addAll(FXCollections.observableArrayList(paneCreate.getPaymentManager().getGrossWageManager()
                .getDynamicComponentManager().getPaymentDynamicComponentDS()));
        dynamicWageSum.setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getDynamicComponentManager().getDynamicComponentsTotal().toPlainString() + " €");

        if(paymentDynamicComponentDS.size()==0)
            tab2.minHeightProperty().setValue(50);
        else
            tab2.minHeightProperty().bind(tab2.prefHeightProperty());
    }

    /*private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        days.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                days.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }*/


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/

    @FXML
    private void onAddClick1(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PageBasDynCompChoosewage.fxml"));
        loader.setControllerFactory(c -> {
            return new PageBasDynCompChoosewage(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Výber nepravidelne vyplácanej mzdy zamestrnanca");
        primaryStage.setScene(new Scene(root1, 810, 570));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    @FXML
    private void onRemoveClick1(MouseEvent mouseEvent)
    {
        PaymentBasicComponentD p = tab1.getSelectionModel().getSelectedItem();
        if(p==null) {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        paneCreate.getPaymentManager().getGrossWageManager().getBasicComponentManager().getUnregularBasicComponentsMenu()
                .add(p.getWageD());
        paneCreate.getPaymentManager().getGrossWageManager().getBasicComponentManager().removeUnregularBasicComponent(p);
        refreshTab1Items();
    }

    @FXML
    private void onAddClick2(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PageBasDynCompAddDynComp.fxml"));
        loader.setControllerFactory(c -> {
            return new PageBasDynCompAddDynComp(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie pohyblivej zložky mzdy");
        primaryStage.setScene(new Scene(root1, 400, 300));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    @FXML
    private void onRemoveClick2(MouseEvent mouseEvent)
    {
        PaymentDynamicComponentD p = tab2.getSelectionModel().getSelectedItem();
        if(p==null) {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }
         paneCreate.getPaymentManager().getGrossWageManager().getDynamicComponentManager().removeDynamicComponent(p);
        refreshTab2Items();
    }

    @FXML
    private void onBackClick(MouseEvent mouseEvent)
    {
        setBackPage();
    }

    @FXML
    private void onNextClick(MouseEvent mouseEvent)
    {
        setPaneElements();

        String relationType = this.paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType();
        if(relationType.equals("PP: na plný úväzok") || relationType.equals("PP: na kratší pracovný čas")
            || relationType.equals("PP: telepráca"))
        {
            if (findAverageWage())
                setNextPageSurcharges();
            else
                setNextPageAverageWage();
        }
        else
        {
            setNextPageSurcharges();
        }
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/


    private void setPaneElements()
    {
        paneCreate.getBasecomponent().setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getBasicComponentManager().getBasicComponentsTotal().toString() + " €");
        paneCreate.getDynamiccomponent().setText(paneCreate.getPaymentManager().getGrossWageManager()
                .getDynamicComponentManager().getDynamicComponentsTotal().toString() + " €");

    }

    private void setBackPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageRecapitulation.fxml"));
        l.setControllerFactory(c -> {
            return new PageFund(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }

    private void setNextPageSurcharges()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageSurcharges.fxml"));
        l.setControllerFactory(c -> {
            return new PageSurcharges(paneCreate);
        });
        paneCreate.setWasAveragePage(false);
        paneCreate.loadAnchorPage(l);
    }

    private void setNextPageAverageWage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageAverage.fxml"));
        l.setControllerFactory(c -> {
            return new PageAverage(paneCreate);
        });
        paneCreate.setWasAveragePage(true);
        paneCreate.loadAnchorPage(l);
    }

    private boolean findAverageWage()
    {
        YearD yearD = this.paneCreate.getPaymentManager().getEmployeeConditionsManager().getYearDS().get(0);
        MonthD monthD = this.paneCreate.getPaymentManager().getEmployeeConditionsManager().getMonthDS().get(0);

        String year = yearD.getYearNumber();
        String month = monthD.getMonthNumber();

        int flag = Integer.parseInt(month) - 3;
        BigDecimal averageWage = null;
        ArrayList<YearD> yearDS;

        if(flag <= 0)
        {
            year = Integer.toString(Integer.parseInt(year)-1);
            yearDS = yearDArrayList(year);
            for(int i =0; i<yearDS.size(); i++)
            {
                if(yearDS.get(i).getAvarageWage4()!=null)
                {
                    averageWage = new BigDecimal(yearDS.get(i).getAvarageWage4());
                    break;
                }
            }
        }
        else if(flag <= 3)
        {
            yearDS = yearDArrayList(year);
            for(int i =0; i<yearDS.size(); i++)
            {
                if(yearDS.get(i).getAvarageWage4()!=null)
                {
                    averageWage = new BigDecimal(yearDS.get(i).getAvarageWage1());
                    break;
                }
            }
        }
        else if(flag <= 6)
        {
            yearDS = yearDArrayList(year);
            for(int i =0; i<yearDS.size(); i++)
            {
                if(yearDS.get(i).getAvarageWage4()!=null)
                {
                    averageWage = new BigDecimal(yearDS.get(i).getAvarageWage2());
                    break;
                }
            }
        }
        else if(flag <= 9)
        {
            yearDS = yearDArrayList(year);
            for(int i =0; i<yearDS.size(); i++)
            {
                if(yearDS.get(i).getAvarageWage4()!=null)
                {
                    averageWage = new BigDecimal(yearDS.get(i).getAvarageWage3());
                    break;
                }
            }
        }

        if(averageWage==null)
        {
            return false;
        }
        else
        {
            paneCreate.getPaymentManager().getGrossWageManager().setAverageWage(averageWage);
            paneCreate.getAverageWage().setText(paneCreate.getPaymentManager().getGrossWageManager().getAverageWage().toPlainString());
            return true;
        }

    }

    private ArrayList<YearD> yearDArrayList(String year)
    {
        String relationID = this.paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getId();
        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendGet("year/yrs/"+year+"/"+relationID, LoggedInUser.getToken(), LoggedInUser.getId());
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return null;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return null;
        }
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        ArrayList<YearD> yearDS = new ArrayList<>();
        for(int i = 0; i<json.getSize(); i++)
        {
            YearD yearD = new YearD();
            yearD.setId(json.getElement(i, "id"));
            yearD.setYearNumber(json.getElement(i, "rok"));
            yearD.setAvarageWage1(json.getElement(i, "priemerna_mzda_1"));
            yearD.setAvarageWage2(json.getElement(i, "priemerna_mzda_2"));
            yearD.setAvarageWage3(json.getElement(i, "priemerna_mzda_3"));
            yearD.setAvarageWage4(json.getElement(i, "priemerna_mzda_4"));
            yearDS.add(yearD);
        }

        return yearDS;
    }
}
