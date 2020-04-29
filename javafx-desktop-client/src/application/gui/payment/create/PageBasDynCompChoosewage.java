package application.gui.payment.create;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.hours.HoursInterface;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.RelationD;
import application.models.WageD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PageBasDynCompChoosewage
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<WageD> wageDS;
    private PageBasDynComp pageBasDynComp;
    private RelationD relationD;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageBasDynCompChoosewage(PageBasDynComp pageBasDynComp)
    {
        this.pageBasDynComp = pageBasDynComp;
        this.wageDS = FXCollections.observableArrayList(pageBasDynComp.getPaneCreate().getPaymentManager()
                .getGrossWageManager().getBasicComponentManager().getUnregularBasicComponentsMenu());
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/



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
                    pageBasDynComp.getPaneCreate().getPaymentManager().getGrossWageManager().getBasicComponentManager()
                            .getUnregularBasicComponentsMenu().remove(p);
                    pageBasDynComp.addUnregularBasicComponent(p);
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
            pageBasDynComp.addUnregularBasicComponent(p);
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
