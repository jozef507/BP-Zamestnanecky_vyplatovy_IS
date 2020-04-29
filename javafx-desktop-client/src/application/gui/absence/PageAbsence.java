package application.gui.absence;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.AbsenceD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

public class PageAbsence
{

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<AbsenceD> absenceDS;
    private ArrayList<String> places;
    private FilteredList<AbsenceD> filteredData, filteredData2;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageAbsence() {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            absenceDS = absenceSelect(sdf.format(new Date()));
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
    public void updateInfo(String date)
    {
        try {
            absenceDS = absenceSelect(date);
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
        tab.setItems(absenceDS);
    }

    public void updateInfo()
    {
        date.getEditor().clear();
        try {
            absenceDS = absenceSelect(null);
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
        tab.setItems(absenceDS);
    }

    private ObservableList<AbsenceD> absenceSelect(String date) throws IOException, InterruptedException, CommunicationException {
        ObservableList<AbsenceD> absenceDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("absence/bsnc/"+date, LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {

            AbsenceD absenceD = new AbsenceD();

            absenceD.setId(json.getElement(i, "n_id"));
            absenceD.setFrom(json.getElement(i, "nice_date1"));
            absenceD.setTo(json.getElement(i, "nice_date2"));
            absenceD.setReason(json.getElement(i, "typ_dovodu"));
            absenceD.setCharacteristic(json.getElement(i, "popis_dovodu"));
            absenceD.setHalf(json.getElement(i, "je_polovica_dna"));
            absenceD.setUpdated(json.getElement(i, "aktualizovane"));


            //absenceD.setMonthID(json.getElement(i, "om_id"));
            //absenceD.setYearID(json.getElement(i, "orr_id"));
            absenceD.setConID(json.getElement(i, "ppv_id"));
            absenceD.setRelID(json.getElement(i, "pv_id"));

            absenceD.setEmployeeID(json.getElement(i, "p_id"));
            absenceD.setEmployeeNameLastname(json.getElement(i, "priezvisko")+" "+json.getElement(i, "meno"));

            absenceD.setPositionID(json.getElement(i, "po_id"));
            absenceD.setPositionName(json.getElement(i, "po_nazov"));

            absenceD.setPlaceID(json.getElement(i, "pr_id"));
            absenceD.setPlaceName(json.getElement(i, "pr_nazov"));

            absenceDS.add(absenceD);
        }

        return absenceDS;
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
    private TableView<AbsenceD> tab = new TableView<AbsenceD>();
    @FXML
    public TableColumn idCol, fromCol, toCol, nameCol, positionCol,  placeCol, reasonCol, charCol, halfCol, updatedCol;
    @FXML
    public TextField input;
    @FXML
    public ComboBox place = new ComboBox();
    @FXML
    public DatePicker date;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setDatePicker();
        changeFocus();
        idCol.setCellValueFactory(new PropertyValueFactory<>("Iid"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("employeeNameLastname"));
        placeCol.setCellValueFactory(new PropertyValueFactory<>("placeName"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("positionName"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        charCol.setCellValueFactory(new PropertyValueFactory<>("characteristic"));
        halfCol.setCellValueFactory(new PropertyValueFactory<>("halfT"));
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("updatedT"));
        tab.setItems(absenceDS);

        place.getItems().add("Pracovisko:");
        for (String p:places)
        {
            place.getItems().add(p);
        }
        place.getSelectionModel().selectFirst();


        filteredData = new FilteredList<>(absenceDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super AbsenceD>) absenceD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    String pla = place.getValue().toString();
                    return isFiltered(absenceD, inp, pla);
                });
            }));
            SortedList<AbsenceD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        filteredData2 = new FilteredList<>(absenceDS, e->true);
        place.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            filteredData2.setPredicate((Predicate<? super AbsenceD>) absenceD ->{
                if(newValue == null || newValue.toString().isEmpty()){
                    return true;
                }

                String inp = input.getText();
                String pla = newValue.toString();
                return isFiltered(absenceD, inp, pla);
            });
            SortedList<AbsenceD> sortedData = new SortedList<>(filteredData2);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        date.valueProperty().addListener((observable, oldValue, newValue) -> {
            input.clear();
            place.getSelectionModel().selectFirst();
            if(newValue!=null)
            {
                updateInfo(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            else
            {
                updateInfo(null);
            }
            filteredData = new FilteredList<>(absenceDS, e->true);
            filteredData2 = new FilteredList<>(absenceDS, e->true);
        });

        MainPaneManager.getC().desibleBackPage();
    }

    private boolean isFiltered(AbsenceD absenceD, String inp, String pla)
    {

        boolean flag1=false, flag2=false;
        boolean imp1= false, imp2= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(absenceD.getEmployeeNameLastname().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(absenceD.getPositionName().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else{
                flag1=false;
            }
        }

        if(!pla.equals("Pracovisko:"))
        {
            imp2=true;
            String tmp1 = absenceD.getPlaceName();

            if (tmp1.equals(pla))
                flag2 = true;
        }


        boolean flag = true;
        if(imp1)
            flag&=flag1;
        if(imp2)
            flag&=flag2;

        return flag;
    }

    private void setDatePicker()
    {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("d.M.yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        date.setConverter(converter);
        date.setPromptText("D.M.RRRR");
        date.setValue(LocalDate.now());
    }

    private void changeFocus() {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        date.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                date.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void onAddClick(MouseEvent mouseEvent)
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAbsence.fxml"));
        loader.setControllerFactory(c -> {
            return new AddAbsence(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie neprítomnosti");
        primaryStage.setScene(new Scene(root1, 727, 600));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

    }

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        AbsenceD a = tab.getSelectionModel().getSelectedItem();
        if(a==null)
        {
            CustomAlert ac = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie neprítomnosti", "Ste si istý, že chcete odstrániť vybrané odpracované hodiny?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendDelete("absence/del_bsnc/"+a.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert ac = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert ac = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert ac = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return;
        }

        updateInfo(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public void onFeastClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddFeast.fxml"));
        loader.setControllerFactory(c -> {
            return new AddFeast(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie sviatku");
        primaryStage.setScene(new Scene(root1, 730, 600));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/


















}
