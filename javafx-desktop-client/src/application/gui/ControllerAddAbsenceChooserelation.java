package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.RelationD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class ControllerAddAbsenceChooserelation
{
    public TextField input;
    public ComboBox place;
    public TableColumn idEmpCol, nameCol, idRelCol, relationCol, fromCol, toCol, placeCol, positionCol;
    public TableView<RelationD> tab;

    private ObservableList<RelationD> relationDS;
    ControllerAddAbsence controllerAddAbsence;
    private ArrayList<String> places;


    public ControllerAddAbsenceChooserelation(ControllerAddAbsence controllerAddAbsence)
    {
        this.controllerAddAbsence = controllerAddAbsence;
        try {
            this.relationDS = relationSelect();
            this.places = getPlaces();
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



    public void initialize() {

        //set table columns
        idEmpCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("employeeNameLastname"));
        idRelCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        relationCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        placeCol.setCellValueFactory(new PropertyValueFactory<>("placeName"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("positionName"));
        tab.setItems(relationDS);

        place.getItems().add("Pracovisko:");
        for (String p:places)
        {
            place.getItems().add(p);
        }
        place.getSelectionModel().selectFirst();


        FilteredList<RelationD> filteredData = new FilteredList<>(relationDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super RelationD>) relationD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    String pla = place.getValue().toString();
                    return isFiltered(relationD, inp, pla);
                });
            }));
            SortedList<RelationD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });


        FilteredList<RelationD> filteredData2 = new FilteredList<>(relationDS, e->true);
        place.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            filteredData2.setPredicate((Predicate<? super RelationD>) relationD ->{
                if(newValue == null || newValue.toString().isEmpty()){
                    return true;
                }

                String inp = input.getText();
                String pla = newValue.toString();
                return isFiltered(relationD, inp, pla);
            });
            SortedList<RelationD> sortedData = new SortedList<>(filteredData2);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        tab.setRowFactory(tv -> {
            TableRow<RelationD> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    RelationD r = row.getItem();
                    System.out.println(r.toString());
                    controllerAddAbsence.setChoosenRelation(r);
                    controllerAddAbsence.setRelationElements();
                    Stage stage = (Stage) input.getScene().getWindow();
                    stage.close();
                }
            });
            return row ;
        });
    }

    public void btn1(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) input.getScene().getWindow();
        stage.close();
    }

    public void btn2(MouseEvent mouseEvent)
    {
        try {
            RelationD r = tab.getSelectionModel().getSelectedItem();
            if(r==null)
            {
                CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
                return;
            }
            controllerAddAbsence.setChoosenRelation(r);
            controllerAddAbsence.setRelationElements();
            System.out.println(r.toString());
            Stage stage = (Stage) input.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("Nevybraný žiadny element!");
        }
    }

    private ObservableList<RelationD> relationSelect() throws IOException, InterruptedException, CommunicationException {
        ObservableList<RelationD> relationDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("relation/emp_rel_cons_po_pl", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        RelationD newRelationD = null;
        for(int i=0; i<json.getSize(); i++)
        {
            newRelationD = new RelationD();

            newRelationD.setEmployeeID(json.getElement(i, "p_id"));
            newRelationD.setEmployeeNameLastname(json.getElement(i, "p_priezvisko")+" "+json.getElement(i, "p_meno"));

            newRelationD.setId(json.getElement(i, "id"));
            newRelationD.setType(json.getElement(i, "typ"));
            newRelationD.setFrom(json.getElement(i, "nice_date1"));
            newRelationD.setTo(json.getElement(i, "nice_date2"));

            newRelationD.setConditionsID(json.getElement(i, "ppv_id"));
            newRelationD.setConditionsFrom(json.getElement(i, "ppv_platnost_od"));
            newRelationD.setConditionsTo(json.getElement(i, "ppv_platnost_do"));

            newRelationD.setPositionID(json.getElement(i, "po_id"));
            newRelationD.setPositionName(json.getElement(i, "po_nazov"));

            newRelationD.setPlaceID(json.getElement(i, "pr_id"));
            newRelationD.setPlaceName(json.getElement(i, "pr_nazov"));

            relationDS.add(newRelationD);
        }

        return relationDS;
    }

    private ArrayList<String> getPlaces() throws IOException, InterruptedException, CommunicationException {
        ArrayList<String> places = new ArrayList<>();
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("place", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        for(int i=0; i<json.getSize(); i++)
        {
            places.add(json.getElement(i, "nazov"));
        }
        return places;
    }


    private boolean isFiltered(RelationD relationD, String inp, String pla)
    {

        boolean flag1=false, flag2=false;
        boolean imp1= false, imp2= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(relationD.getEmployeeNameLastname().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else{
                flag1=false;
            }
        }

        if(!pla.equals("Pracovisko:"))
        {
            imp2=true;
            String filter1 = pla;
            String tmp1 = relationD.getPlaceName();

            if (tmp1.equals(filter1))
                flag2 = true;

        }

        boolean flag = true;
        if(imp1)
            flag&=flag1;
        if(imp2)
            flag&=flag2;

        return flag;
    }
}
