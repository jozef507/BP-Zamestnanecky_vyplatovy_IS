package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeOV;
import application.models.PositionD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class ControllerAddRelationChoosePosition
{
    public TextField input;
    public ComboBox place;
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn placeCol;
    public TableColumn characteristicCol;
    public TableView<PositionD> tab;

    private ObservableList<PositionD> positionDs;
    ControllerRelation controllerAddRelation;
    private ArrayList<String> places;


    public ControllerAddRelationChoosePosition(ControllerRelation controllerRelation)
    {
        this.controllerAddRelation = controllerRelation;
        try {
            this.positionDs = positionSelect();
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
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        placeCol.setCellValueFactory(new PropertyValueFactory<>("place"));
        characteristicCol.setCellValueFactory(new PropertyValueFactory<>("characteristic"));
        tab.setItems(positionDs);

        place.getItems().add("Pracovisko:");
        for (String p:places)
        {
            place.getItems().add(p);
        }
        place.getSelectionModel().selectFirst();


        FilteredList<PositionD> filteredData = new FilteredList<>(positionDs, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super PositionD>) positionD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    String pla = place.getValue().toString();
                    return isFiltered(positionD, inp, pla);
                });
            }));
            SortedList<PositionD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });


        FilteredList<PositionD> filteredData2 = new FilteredList<>(positionDs, e->true);
        place.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            filteredData2.setPredicate((Predicate<? super PositionD>) positionD ->{
                if(newValue == null || newValue.toString().isEmpty()){
                    return true;
                }

                String inp = input.getText();
                String pla = newValue.toString();
                return isFiltered(positionD, inp, pla);
            });
            SortedList<PositionD> sortedData = new SortedList<>(filteredData2);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        tab.setRowFactory(tv -> {
            TableRow<PositionD> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    PositionD p = row.getItem();
                    System.out.println(p.toString());
                    controllerAddRelation.setChoosenPosition(p);
                    controllerAddRelation.setPositionElements();
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
            PositionD p = tab.getSelectionModel().getSelectedItem();
            controllerAddRelation.setChoosenPosition(p);
            controllerAddRelation.setPositionElements();
            System.out.println(p.toString());
            Stage stage = (Stage) input.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("Nevybraný žiadny element!");
        }
    }

    private ObservableList<PositionD> positionSelect() throws IOException, InterruptedException, CommunicationException {
        ObservableList<PositionD> positionDs = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("position", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        PositionD newPositionD = null;
        for(int i=0; i<json.getSize(); i++)
        {
            newPositionD = new PositionD(
                    (json.getElement(i, "id")),
                    (json.getElement(i, "nazov")),
                    (json.getElement(i, "nazov2")),
                    (json.getElement(i, "charakteristika"))
            );
            positionDs.add(newPositionD);
        }

        return positionDs;
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


    private boolean isFiltered(PositionD positionD, String inp, String pla)
    {

        boolean flag1=false, flag2=false;
        boolean imp1= false, imp2= false;
        /*if(!inp.equals(""))
        {*/
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(positionD.getName().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else{
                flag1=false;
            }
        /*}*/

        if(!pla.equals("Pracovisko:"))
        {
            imp2=true;
            String filter1 = pla;
            String tmp1 = positionD.getPlace();

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
