package application.gui.hours;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.gui.employee.PageEmployeeDetails;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.HoursD;
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

public class PageHours
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<HoursD> hoursDS;
    private ArrayList<String> places;
    private FilteredList<HoursD> filteredData, filteredData2;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageHours() {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            hoursDS = hoursSelect(sdf.format(new Date()));
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
            hoursDS = hoursSelect(date);
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
        tab.setItems(hoursDS);
    }

    public void updateInfo()
    {
        date.getEditor().clear();
        try {
            hoursDS = hoursSelect(null);
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
        tab.setItems(hoursDS);
    }

    private ObservableList<HoursD> hoursSelect(String date) throws IOException, InterruptedException, CommunicationException {
        ObservableList<HoursD> hoursDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("hours/hrs/"+date, LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {

            HoursD hoursD = new HoursD();

            hoursD.setId(json.getElement(i, "oh_id"));
            hoursD.setDate(json.getElement(i, "nice_date1"));
            hoursD.setFrom(json.getElement(i, "nice_time1"));
            hoursD.setTo(json.getElement(i, "nice_time2"));
            hoursD.setOverTime(json.getElement(i, "z_toho_nadcas"));
            hoursD.setUnitsDone(json.getElement(i, "pocet_vykonanych_jednotiek"));
            hoursD.setPartBase(json.getElement(i, "zaklad_podielovej_mzdy"));
            hoursD.setEmergencyType(json.getElement(i, "druh_casti_pohotovosti"));
            hoursD.setUpdated(json.getElement(i, "aktualizovane"));

            hoursD.setWageID(json.getElement(i, "zm_id"));
            hoursD.setWageFormID(json.getElement(i, "fm_id"));
            hoursD.setWageFormLabel(json.getElement(i, "fm_nazov")+" / "+json.getElement(i, "zm_popis"));

            hoursD.setMonthID(json.getElement(i, "om_id"));
            hoursD.setYearID(json.getElement(i, "orr_id"));
            hoursD.setConsID(json.getElement(i, "ppv_id"));
            hoursD.setRelID(json.getElement(i, "pv_id"));

            hoursD.setEmployeeID(json.getElement(i, "p_id"));
            hoursD.setEmployeeNameLastname(json.getElement(i, "priezvisko")+" "+json.getElement(i, "meno"));

            hoursD.setPositionID(json.getElement(i, "po_id"));
            hoursD.setPositionName(json.getElement(i, "po_nazov"));

            hoursD.setPlaceID(json.getElement(i, "pr_id"));
            hoursD.setPlaceName(json.getElement(i, "pr_nazov"));

            hoursDS.add(hoursD);
        }

        return hoursDS;
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
    private TableView<HoursD> tab = new TableView<HoursD>();
    @FXML
    public TableColumn idCol, dateCol, nameCol, positionCol,  wageCol, fromCol, toCol, overtimeCol, doneCol, partCol, emergencyCol, placeCol, updatedCol;
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
        nameCol.setCellValueFactory(new PropertyValueFactory<>("employeeNameLastname"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        placeCol.setCellValueFactory(new PropertyValueFactory<>("placeName"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("positionName"));
        wageCol.setCellValueFactory(new PropertyValueFactory<>("wageFormLabel"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        overtimeCol.setCellValueFactory(new PropertyValueFactory<>("overTime"));
        doneCol.setCellValueFactory(new PropertyValueFactory<>("unitsDone"));
        partCol.setCellValueFactory(new PropertyValueFactory<>("partBase"));
        emergencyCol.setCellValueFactory(new PropertyValueFactory<>("emergencyType"));
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("updatedT"));
        tab.setItems(hoursDS);

        place.getItems().add("Pracovisko:");
        for (String p:places)
        {
            place.getItems().add(p);
        }
        place.getSelectionModel().selectFirst();


        filteredData = new FilteredList<>(hoursDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super HoursD>) hourD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    String pla = place.getValue().toString();
                    return isFiltered(hourD, inp, pla);
                });
            }));
            SortedList<HoursD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        filteredData2 = new FilteredList<>(hoursDS, e->true);
        place.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            filteredData2.setPredicate((Predicate<? super HoursD>) hoursD ->{
                if(newValue == null || newValue.toString().isEmpty()){
                    return true;
                }

                String inp = input.getText();
                String pla = newValue.toString();
                return isFiltered(hoursD, inp, pla);
            });
            SortedList<HoursD> sortedData = new SortedList<>(filteredData2);
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
            filteredData = new FilteredList<>(hoursDS, e->true);
            filteredData2 = new FilteredList<>(hoursDS, e->true);

        });

        tab.setRowFactory(tv -> {
            TableRow<HoursD> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    HoursD h = row.getItem();
                    System.out.println(h.toString());

                    FXMLLoader l = new FXMLLoader(getClass().getResource("UpdateHours.fxml"));
                    l.setControllerFactory(c -> {
                        return new PageEmployeeDetails(h.getId());
                    });
                    MainPaneManager.getC().loadScrollPage(l);
                }
            });
            return row ;
        });

        MainPaneManager.getC().desibleBackPage();
    }

    private boolean isFiltered(HoursD hoursD, String inp, String pla)
    {

        boolean flag1=false, flag2=false;
        boolean imp1= false, imp2= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(hoursD.getEmployeeNameLastname().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(hoursD.getPositionName().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else{
                flag1=false;
            }
        }

        if(!pla.equals("Pracovisko:"))
        {
            imp2=true;
            String tmp1 = hoursD.getPlaceName();

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddHours.fxml"));
        loader.setControllerFactory(c -> {
            return new AddHours(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie odpracovaných hodín");
        primaryStage.setScene(new Scene(root1, 505, 600));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

    }

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        HoursD hd = tab.getSelectionModel().getSelectedItem();
        if(hd==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie odpracovaných hodín", "Ste si istý, že chcete odstrániť vybrané odpracované hodiny?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendDelete("hours/del_hrs/"+hd.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
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

        updateInfo(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public void onUpdateClick(MouseEvent mouseEvent)
    {
        HoursD hd = tab.getSelectionModel().getSelectedItem();
        if(hd==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateHours.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdateHours(this, hd);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Úprava odpracovaných hodín");
        primaryStage.setScene(new Scene(root1, 505, 600));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

}
