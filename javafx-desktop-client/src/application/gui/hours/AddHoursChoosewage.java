package application.gui.hours;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.RelationD;
import application.models.WageD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AddHoursChoosewage
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<WageD> wageDS;
    private HoursInterface hoursInterface;
    private RelationD relationD;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public AddHoursChoosewage(HoursInterface hoursInterface)
    {
        this.hoursInterface = hoursInterface;
        this.relationD = hoursInterface.getChoosenRelation();
        try {
            this.wageDS = wageSelect();
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
    private ObservableList<WageD> wageSelect() throws IOException, InterruptedException, CommunicationException {
        ObservableList<WageD> wageDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("wage/wage_form_of_con/"+relationD.getConditionsID(), LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {
            WageD wageD = new WageD();
            wageD.setId(json.getElement(i, "zm_id"));
            wageD.setLabel(json.getElement(i, "popis"));
            wageD.setEmployeeEnter(json.getElement(i, "vykon_eviduje_zamestnanec"));
            wageD.setTimeImportant(json.getElement(i, "nutne_evidovanie_casu"));
            wageD.setEmergencyImportant(json.getElement(i, "mozne_evidovanie_pohotovosti"));
            wageD.setTarif(json.getElement(i, "tarifa_za_jednotku_mzdy"));
            wageD.setPayWay(json.getElement(i, "sposob_vyplacania"));
            wageD.setPayDate(json.getElement(i, "nice_date1"));
            wageD.setWageFormID(json.getElement(i, "fm_id"));
            wageD.setWageFormName(json.getElement(i, "nazov"));
            wageD.setWageFormUnit(json.getElement(i, "jednotka_vykonu"));
            wageD.setWageFormUnitShort(json.getElement(i, "skratka_jednotky"));
            wageDS.add(wageD);
        }
        return wageDS;
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public TableColumn idCol, formCol, labelCol, unitCol, tarifCol, empCol, timeCol, dateCol;
    public TableView<WageD> tab;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    public void initialize() {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        formCol.setCellValueFactory(new PropertyValueFactory<>("wageFormName"));
        labelCol.setCellValueFactory(new PropertyValueFactory<>("label"));
        unitCol.setCellValueFactory(new PropertyValueFactory<>("wageFormUnit"));
        tarifCol.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        empCol.setCellValueFactory(new PropertyValueFactory<>("employeeEnter"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeImportant"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("payDate"));
        tab.setItems(wageDS);

        tab.setRowFactory(tv -> {
            TableRow<WageD> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    WageD p = row.getItem();
                    System.out.println(p.toString());
                    hoursInterface.setChoosenWage(p);
                    hoursInterface.setWageElements();
                    Stage stage = (Stage) tab.getScene().getWindow();
                    stage.close();
                }
            });
            return row ;
        });
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void btn1(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) tab.getScene().getWindow();
        stage.close();
    }

    public void btn2(MouseEvent mouseEvent)
    {
        try {
            WageD p = tab.getSelectionModel().getSelectedItem();
            if(p==null)
            {
                CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
                return;
            }
            hoursInterface.setChoosenWage(p);
            hoursInterface.setWageElements();
            System.out.println(p.toString());
            Stage stage = (Stage) tab.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("Nevybraný žiadny element!");
        }
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
}
