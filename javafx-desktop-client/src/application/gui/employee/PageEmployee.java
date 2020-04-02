package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class PageEmployee
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<EmployeeOV> employeeOVS;
    private ArrayList<String> places;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageEmployee() {
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


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    public void updateInfo()
    {
        try {
            employeeOVS = employeeSelect();
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
        tab.setItems(employeeOVS);
    }


    private ObservableList<EmployeeOV> employeeSelect() throws IOException, InterruptedException, CommunicationException {
        ObservableList<EmployeeOV> employeeOVS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("employee", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        String prevEmployeeID="-1";
        for(int i=0; i<json.getSize(); i++)
        {
            if(!json.getElement(i,"id").equals(prevEmployeeID))
            {
                EmployeeOV newEmployeeOV = new EmployeeOV();

                newEmployeeOV.setId(Integer.parseInt(json.getElement(i, "id")));
                newEmployeeOV.setName(json.getElement(i, "meno"));
                newEmployeeOV.setLastname(json.getElement(i, "priezvisko"));
                newEmployeeOV.setPhonenumber(json.getElement(i, "telefon"));
                newEmployeeOV.setBornnumber(json.getElement(i, "rodne_cislo"));
                newEmployeeOV.setBorndate(json.getElement(i, "datum_narodenia"));
                newEmployeeOV.setLogin(json.getElement(i, "prihlasovacie_konto"));
                if(json.getElement(i, "p2_nazov")==null)
                {
                    newEmployeeOV.setActrelat(0);
                }
                else
                {
                    newEmployeeOV.setActrelat(1);
                    newEmployeeOV.addWorkRelation(json.getElement(i, "p2_nazov"));
                    newEmployeeOV.addPlace(json.getElement(i, "p3_nazov"));
                }
                employeeOVS.add(newEmployeeOV);
            }
            else
            {
                employeeOVS.get(employeeOVS.size()-1).addWorkRelation(json.getElement(i, "p2_nazov"));
                employeeOVS.get(employeeOVS.size()-1).addPlace(json.getElement(i, "p3_nazov"));
                employeeOVS.get(employeeOVS.size()-1).setActrelat(employeeOVS.get(employeeOVS.size()-1).getActrelat()+1);
            }
            prevEmployeeID = json.getElement(i,"id");
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




    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
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


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
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

                    FXMLLoader l = new FXMLLoader(getClass().getResource("PageEmployeeDetails.fxml"));
                    l.setControllerFactory(c -> {
                        return new PageEmployeeDetails(Integer.toString(e.getId()));
                    });
                    MainPaneManager.getC().loadScrollPage(l);
                }
            });
            return row ;
        });

        MainPaneManager.getC().desibleBackPage();
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



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void btn(ActionEvent actionEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEmployee.fxml"));
        loader.setControllerFactory(c -> {
            return new AddEmployee(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Úprava informácií pracujúceho");
        primaryStage.setScene(new Scene(root1, 505, 600));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        EmployeeOV em = tab.getSelectionModel().getSelectedItem();
        if(em==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie pracujúceho", "Ste si istý, že chcete odstrániť vybraného pracujúceho?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendDelete("employee/del_emp/"+em.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
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

    public void OnEnterClick(MouseEvent mouseEvent)
    {
        EmployeeOV e = tab.getSelectionModel().getSelectedItem();
        if(e==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        FXMLLoader l = new FXMLLoader(getClass().getResource("PageEmployeeDetails.fxml"));
        l.setControllerFactory(c -> {
            return new PageEmployeeDetails(Integer.toString(e.getId()));
        });
        MainPaneManager.getC().loadScrollPage(l);
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
}
