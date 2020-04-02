package application.gui.user;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
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

public class PageUsers
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<EmployeeD> employeeDS;
    private ArrayList<String> places;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageUsers() {
        try{
            employeeDS = employeeSelect();
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
            employeeDS = employeeSelect();
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
        tab.setItems(employeeDS);
    }

    private ObservableList<EmployeeD> employeeSelect() throws IOException, InterruptedException, CommunicationException {
        ObservableList<EmployeeD> employeeDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("employee/emps_usrs", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        for(int i=0; i<json.getSize(); i++)
        {
            EmployeeD employeeD = new EmployeeD();
            employeeD.setId(json.getElement(i, "p_id"));
            employeeD.setName(json.getElement(i, "meno"));
            employeeD.setLastname(json.getElement(i, "priezvisko"));
            employeeD.setPhone(json.getElement(i, "telefon"));
            employeeD.setBornNum(json.getElement(i, "rodne_cislo"));
            employeeD.setBornDate(json.getElement(i, "datum_narodenia"));
            employeeD.setPkID(json.getElement(i, "pk_id"));
            employeeD.setEmail(json.getElement(i, "email"));
            employeeD.setUserType(json.getElement(i, "typ_prav"));
            employeeD.setIsCurrent(json.getElement(i, "aktualne"));
            employeeD.setLastLogIn(json.getElement(i, "posledne_prihlasenie"));
            employeeD.setCreated(json.getElement(i, "vytvorene_v"));
            employeeDS.add(employeeD);
        }

        return employeeDS;
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    @FXML
    private TableView<EmployeeD> tab = new TableView<EmployeeD>();
    @FXML
    public TableColumn nameCol, lastNameCol, idCol,  bornNumberCol, emailCol, roleCol;
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
        bornNumberCol.setCellValueFactory(new PropertyValueFactory<>("bornNum"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("userType"));

        tab.setItems(employeeDS);

        FilteredList<EmployeeD> filteredData = new FilteredList<>(employeeDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super EmployeeD>) employeeD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    return isFiltered(employeeD, inp);
                });
            }));
            SortedList<EmployeeD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });


        tab.setRowFactory(tv -> {
            TableRow<EmployeeD> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmployeeD e = row.getItem();
                    System.out.println(e.toString());

                    FXMLLoader l = new FXMLLoader(getClass().getResource("PageUsersDetails.fxml"));
                    l.setControllerFactory(c -> {
                        return new PageUsersDetails(e.getId());
                    });
                    MainPaneManager.getC().loadScrollPage(l);
                }
            });
            return row ;
        });

        MainPaneManager.getC().desibleBackPage();
    }

    private boolean isFiltered(EmployeeD employeeD, String inp)
    {

        boolean flag1=false;
        boolean imp1= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(employeeD.getId().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(employeeD.getName().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(employeeD.getLastname().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(employeeD.getBornNum().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else{
                flag1=false;
            }
        }

        boolean flag = true;
        if(imp1)
            flag&=flag1;

        return flag;
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void btn(ActionEvent actionEvent)
    {
        EmployeeD employeeD = tab.getSelectionModel().getSelectedItem();
        if(employeeD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_users_details"+".fxml"));
        l.setControllerFactory(c -> {
            return new PageUsersDetails(employeeD.getId());
        });
        MainPaneManager.getC().loadScrollPage(l);
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

}
