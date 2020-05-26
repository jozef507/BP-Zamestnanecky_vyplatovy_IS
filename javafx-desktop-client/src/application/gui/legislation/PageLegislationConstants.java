package application.gui.legislation;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.SurchargeTypeD;
import application.models.WageConstantsD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Predicate;

public class PageLegislationConstants
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<WageConstantsD> wageConstantsDS;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageLegislationConstants() {
        try{
            wageConstantsDS = wageConstantsSelect();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return;
        }
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    public void updateInfo()
    {
        try {
            wageConstantsDS = wageConstantsSelect();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return;
        }
        tab.setItems(wageConstantsDS);
    }

    private ObservableList<WageConstantsD> wageConstantsSelect() throws InterruptedException, IOException, CommunicationException {
        ObservableList<WageConstantsD> wageConstantsDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("wageconstants", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {
            WageConstantsD wageConstantsD = new WageConstantsD();
            wageConstantsD.setId(json.getElement(i, "id"));
            wageConstantsD.setFrom(json.getElement(i, "nice_date1"));
            wageConstantsD.setTo(json.getElement(i, "nice_date2"));
            wageConstantsD.setBasicWeekTime(json.getElement(i, "zakladny_tyzdenny_pracovny_cas"));
            wageConstantsD.setMaxAssessmentBasis(json.getElement(i, "max_vymeriavaci_zaklad"));
            wageConstantsD.setMinAssessmentBasis(json.getElement(i, "min_vymeriavaci_zaklad"));
            wageConstantsD.setMaxDayAssessmentBasis(json.getElement(i, "max_denny_vymeriavaci_zaklad"));
            wageConstantsD.setTaxBonusOver6(json.getElement(i, "danovy_bonus_na_dieta_nad_6"));
            wageConstantsD.setTaxBonusUnder6(json.getElement(i, "danovy_bonus_na_dieta_pod_6"));
            wageConstantsD.setNonTaxablePart(json.getElement(i, "NCZD_na_danovnika"));
            wageConstantsD.setSubsistenceMinimumForAdvances(json.getElement(i, "nasobok_zivotneho_minima_pre_preddavok"));
            wageConstantsD.setNightFrom(json.getElement(i, "zaciatok_nocnej_prace"));
            wageConstantsD.setNightTo(json.getElement(i, "koniec_nocnej_prace"));
            wageConstantsD.setMaxAssessmentBasisOP(json.getElement(i, "max_vymeriavaci_zaklad_pre_OP"));
            wageConstantsD.setMaxOP(json.getElement(i, "max_vyska_OP"));
            wageConstantsD.setLimitOV(json.getElement(i, "hranica_prekrocenia_OV"));
            wageConstantsDS.add(wageConstantsD);
        }
        return wageConstantsDS;
    }

    private void removeSurcharge(WageConstantsD wageConstantsD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendDelete("wageconstants/del_cons/"+wageConstantsD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        this.updateInfo();
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    @FXML
    private TableView<WageConstantsD> tab = new TableView<WageConstantsD>();

    @FXML
    public TextField input;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn fromCol;
    @FXML
    private TableColumn toCol;
    @FXML
    private TableColumn basicWeekTime;
    @FXML
    private TableColumn maxAssessmentBasis;
    @FXML
    private TableColumn minAssessmentBasis;
    @FXML
    private TableColumn maxDayAssessmentBasis;
    @FXML
    private TableColumn taxBonusOver6;
    @FXML
    private TableColumn taxBonusUnder6;
    @FXML
    private TableColumn nonTaxablePart;
    @FXML
    private TableColumn subsistenceMinimumForAdvances;
    @FXML
    private TableColumn nightFrom;
    @FXML
    private TableColumn nightTo;
    @FXML
    private TableColumn maxAssessmentBasisOP;
    @FXML
    private TableColumn maxOP;
    @FXML
    private TableColumn limitOV;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        basicWeekTime.setCellValueFactory(new PropertyValueFactory<>("basicWeekTime"));
        maxAssessmentBasis.setCellValueFactory(new PropertyValueFactory<>("maxAssessmentBasis"));
        minAssessmentBasis.setCellValueFactory(new PropertyValueFactory<>("minAssessmentBasis"));
        maxDayAssessmentBasis.setCellValueFactory(new PropertyValueFactory<>("maxDayAssessmentBasis"));
        taxBonusOver6.setCellValueFactory(new PropertyValueFactory<>("taxBonusOver6"));
        taxBonusUnder6.setCellValueFactory(new PropertyValueFactory<>("taxBonusUnder6"));
        nonTaxablePart.setCellValueFactory(new PropertyValueFactory<>("nonTaxablePart"));
        subsistenceMinimumForAdvances.setCellValueFactory(new PropertyValueFactory<>("subsistenceMinimumForAdvances"));
        nightFrom.setCellValueFactory(new PropertyValueFactory<>("nightFrom"));
        nightTo.setCellValueFactory(new PropertyValueFactory<>("nightTo"));
        maxAssessmentBasisOP.setCellValueFactory(new PropertyValueFactory<>("maxAssessmentBasisOP"));
        maxOP.setCellValueFactory(new PropertyValueFactory<>("maxOP"));
        limitOV.setCellValueFactory(new PropertyValueFactory<>("limitOV"));


        tab.setItems(wageConstantsDS);

        FilteredList<WageConstantsD> filteredData = new FilteredList<>(wageConstantsDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super WageConstantsD>) wageConstantsD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    return isFiltered(wageConstantsD, inp);
                });
            }));
            SortedList<WageConstantsD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        MainPaneManager.getC().setBackPage("PageLegislation");
    }

    private boolean isFiltered(WageConstantsD surchargeTypeD, String inp)
    {

        boolean flag1=false;
        boolean imp1= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(surchargeTypeD.getId().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }
        }

        boolean flag = true;
        if(imp1)
            flag&=flag1;

        return flag;
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void onUpdateClick(MouseEvent mouseEvent)
    {
        WageConstantsD wageConstantsD = tab.getSelectionModel().getSelectedItem();
        if(wageConstantsD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateConstants.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdateConstants(this, wageConstantsD);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Upravenie príplatku");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        WageConstantsD wageConstantsD = tab.getSelectionModel().getSelectedItem();
        if(wageConstantsD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie stupňa náročnosti", "Ste si istý, že chcete odstrániť vybraný stupeň náročnosti?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        try {
            this.removeSurcharge(wageConstantsD);
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return;
        }

        updateInfo();
    }

    public void onAddClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddConstants.fxml"));
        loader.setControllerFactory(c -> {
            return new AddConstants(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie typu príplatku");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

}
