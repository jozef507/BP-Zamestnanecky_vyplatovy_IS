package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.models.EmployeeOV;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class ControllerPageEmployees
{

    @FXML
    private TableView<EmployeeOV> tab = new TableView<EmployeeOV>();
    @FXML
    public TableColumn nameCol, lastNameCol, idCol, phoneNumberCol, bornNumberCol, countCol;
    @FXML
    public TextField input;
    @FXML
    public ComboBox place = new ComboBox();
    @FXML
    public ComboBox relat = new ComboBox();

    private ObservableList<EmployeeOV> employeeOVS;
    private ArrayList<String> places;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {

        //set table columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bornNumberCol.setCellValueFactory(new PropertyValueFactory<>("bornnumber"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("actrelat"));

        tab.setItems(employeeOVS);

        relat.getItems().addAll(
                "Pracovný vzťah:",
                "PP: na plný úväzok",
                "PP: na kratší pracovný čas",
                "PP: telepráca",
                "D: o vykonaní práce",
                "D: o pracovnej činnosti",
                "D: o brig. práci študentov",
                "Bez aktuálneho vzťahu"
        );
        relat.getSelectionModel().selectFirst();




        place.getItems().add("Pracovisko:");
        for (String p:places)
        {
            place.getItems().add(p);
        }
        place.getSelectionModel().selectFirst();

        FilteredList<EmployeeOV> filteredData = new FilteredList<>(employeeOVS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super EmployeeOV>) employeeOV -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    String pla = place.getValue().toString();
                    String rel = relat.getValue().toString();
                    return isFiltered(employeeOV, inp, pla, rel);
                });
            }));
            SortedList<EmployeeOV> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });


        FilteredList<EmployeeOV> filteredData1 = new FilteredList<>(employeeOVS, e->true);
        relat.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            filteredData1.setPredicate((Predicate<? super EmployeeOV>) employeeOV ->{
                if(newValue == null || newValue.toString().isEmpty()){
                    return true;
                }

                String inp = input.getText();
                String pla = place.getValue().toString();
                String rel = newValue.toString();
                return isFiltered(employeeOV, inp, pla, rel);
            });
            SortedList<EmployeeOV> sortedData = new SortedList<>(filteredData1);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });


        FilteredList<EmployeeOV> filteredData2 = new FilteredList<>(employeeOVS, e->true);
        place.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            filteredData2.setPredicate((Predicate<? super EmployeeOV>) employeeOV ->{
                if(newValue == null || newValue.toString().isEmpty()){
                    return true;
                }

                String inp = input.getText();
                String pla = newValue.toString();
                String rel = relat.getValue().toString();
                return isFiltered(employeeOV, inp, pla, rel);
            });
            SortedList<EmployeeOV> sortedData = new SortedList<>(filteredData2);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        tab.setRowFactory(tv -> {
            TableRow<EmployeeOV> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmployeeOV e = row.getItem();
                    System.out.println(e.toString());

                    FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_employee_details"+".fxml"));
                    l.setControllerFactory(c -> {
                        return new ControllerPageEmployeeDetails(Integer.toString(e.getId()));
                    });
                    MainPaneManager.getC().loadScrollPage(l);
                }
            });
            return row ;
        });

        MainPaneManager.getC().desibleBackPage();
    }

    public ControllerPageEmployees() {
        try{
            employeeOVS = employeeSelect();
            places = getPlaces();
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

    private boolean isFiltered(EmployeeOV employeeOV, String inp, String pla, String rel)
    {

        boolean flag1=false, flag2=false, flag3=false;
        boolean imp1= false, imp2= false, imp3= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(employeeOV.getName().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(employeeOV.getLastname().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(employeeOV.getBornnumber().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else{
                flag1=false;
            }
        }

        if(!pla.equals("Pracovisko:"))
        {
            imp2=true;
            String filter1 = pla;
            ArrayList<String> tmp1 = employeeOV.getPlaces();
            for (String s : tmp1) {
                if (s.equals(filter1))
                    flag2 = true;
            }
        }

        if(!rel.equals("Pracovný vzťah:"))
        {
            imp3=true;
            String filter2 = rel;
            ArrayList<String> tmp2 = employeeOV.getWorkRelations();
            for (String s : tmp2) {
                if (s.equals(filter2))
                    flag3 = true;
            }
            if(rel.equals("Bez aktuálneho vzťahu") && tmp2.isEmpty())
            {
                flag3 = true;
            }
        }

        boolean flag = true;
        if(imp1)
            flag&=flag1;
        if(imp2)
            flag&=flag2;
        if(imp3)
            flag&=flag3;

        return flag;
    }

    private ObservableList<EmployeeOV> employeeSelect() throws IOException, InterruptedException, CommunicationException {
        ObservableList<EmployeeOV> employeeOVS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("employee", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        EmployeeOV newEmployeeOV = null;
        for(int i=0; i<json.getSize(); i++)
        {
            newEmployeeOV = new EmployeeOV(
                        (Integer.parseInt(json.getElement(i, "id"))),
                        (json.getElement(i, "meno")),
                        (json.getElement(i, "priezvisko")),
                        (json.getElement(i, "telefon")),
                        (json.getElement(i, "rodne_cislo")),
                        (json.getElement(i, "datum_narodenia")),
                        (json.getElement(i, "prihlasovacie_konto")),
                        (Integer.parseInt(json.getElement(i, "c")))
            );
            employeeOVS.add(newEmployeeOV);
        }

        for (EmployeeOV e: employeeOVS)
        {
            try {
                e.setLists();
            } catch (Exception ex) {
                ex.printStackTrace();
                break;
            }
        }
        return employeeOVS;
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

    public void btn(ActionEvent actionEvent)
    {
        try {
            EmployeeOV e = tab.getSelectionModel().getSelectedItem();
            System.out.println(e.toString());
        }catch (Exception e)
        {
            System.out.println("Nevybraný žiadny element!");
        }

    }
}
