package application.gui.payment;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.gui.payment.create.PaneCreate;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.PaymentD;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class PagePayment
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<PaymentD> paymentDS;
    private FilteredList<PaymentD> filteredData;
    private SortedList<PaymentD> sortedData;
    private ArrayList<String> places;
    private ArrayList<String> years;
    private String year, month;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PagePayment() {
        try{
            paymentDS = paymentFirstSelect();
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
            paymentDS = paymentSelect(yearC.getSelectionModel().getSelectedItem().toString(), monthC.getSelectionModel().getSelectedItem().toString());
            filterAfterSelect();
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


    private ObservableList<PaymentD> paymentFirstSelect() throws IOException, InterruptedException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("payment/lstmnth", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        int yearCount = Integer.parseInt(json.getElement(0, "pocet"));
        this.month = json.getElement(0, "mesiac");
        this.year = json.getElement(0, "rok");

        this.years = new ArrayList<>();
        for(int i=1; i<1+yearCount; i++)
        {
           this.years.add(json.getElement(i,"rok"));
        }

        ObservableList<PaymentD> paymentDS = FXCollections.observableArrayList();
        for(int i=1+yearCount; i<json.getSize(); i++)
        {
            PaymentD paymentD = new PaymentD();
            paymentD.setId(json.getElement(i, "vp_id"));
            /*paymentD.setHoursFund(json.getElement(i, "fond_hodin"));
            paymentD.setDaysFund(json.getElement(i, "fond_dni"));
            paymentD.setHoursWorked(json.getElement(i, "odpracovane_hodiny"));
            paymentD.setDaysWorked(json.getElement(i, "odpracovane_dni"));*/

            paymentD.setGrossWage(json.getElement(i, "hruba_mzda"));
            /*paymentD.setEmployeeEnsurence(json.getElement(i, "poistne_zamestnanca"));
            paymentD.setNonTaxableWage(json.getElement(i, "nezdanitelna_mzda"));
            paymentD.setTaxableWage(json.getElement(i, "zdanitelna_mzda"));*/

            /*paymentD.setTaxAdvances(json.getElement(i, "preddavky_na_dan"));
            paymentD.setTaxBonus(json.getElement(i, "danovy_bonus"));*/
            paymentD.setNetWage(json.getElement(i, "cista_mzda"));
            /*paymentD.setOnAccount(json.getElement(i, "na_ucet"));
            paymentD.setCash(json.getElement(i, "k_vyplate"));*/

            paymentD.setWorkPrice(json.getElement(i, "cena_prace"));
            /*paymentD.setEmployerLevies(json.getElement(i, "odvody_zamestnavatela"));
            paymentD.setEmployeeLevies(json.getElement(i, "odvody_dan_zamestnanca"));
            paymentD.setLeviesSum(json.getElement(i, "odvody_dan_spolu"));

            paymentD.setWhoCreatedID(json.getElement(i, "vypracoval_pracujuci"));
            paymentD.setEmployeeImportantID(json.getElement(i, "dolezite_udaje_pracujuceho"));
            paymentD.setMinimumWageID(json.getElement(i, "minimalna_mzda"));*/

            paymentD.setEmployeeLastnameName(json.getElement(i, "priezvisko") + " " + json.getElement(i, "meno"));
            paymentD.setEmployeeID(json.getElement(i, "p_id"));

            paymentD.setRelationID(json.getElement(i, "p_id"));
            paymentD.setRelationType(json.getElement(i, "typ"));

            paymentD.setConditionsID(json.getElement(i, "ppv_id"));

            paymentD.setPositionID(json.getElement(i, "p2_id"));
            paymentD.setPositionName(json.getElement(i, "p2_nazov"));

            paymentD.setPlaceID(json.getElement(i, "p3_id"));
            paymentD.setPlaceName(json.getElement(i, "p3_nazov"));

            paymentD.setYearID(json.getElement(i, "o_id"));
            paymentD.setYearNumber(json.getElement(i, "rok"));

            paymentD.setMonthID(json.getElement(i, "om_id"));
            paymentD.setMonthNumber(json.getElement(i, "poradie_mesiaca"));
            paymentD.setMonthIsClosed(json.getElement(i, "je_mesiac_uzatvoreny"));
            paymentDS.add(paymentD);
        }

        return paymentDS;
    }

    private ObservableList<PaymentD> paymentSelect(String year, String month) throws IOException, InterruptedException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("payment/mnth/"+year+"/"+month, LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());


        ObservableList<PaymentD> paymentDS = FXCollections.observableArrayList();
        for(int i=0; i<json.getSize(); i++)
        {
            PaymentD paymentD = new PaymentD();
            paymentD.setId(json.getElement(i, "vp_id"));
            /*paymentD.setHoursFund(json.getElement(i, "fond_hodin"));
            paymentD.setDaysFund(json.getElement(i, "fond_dni"));
            paymentD.setHoursWorked(json.getElement(i, "odpracovane_hodiny"));
            paymentD.setDaysWorked(json.getElement(i, "odpracovane_dni"));*/

            paymentD.setGrossWage(json.getElement(i, "hruba_mzda"));
            /*paymentD.setAssessmentBasis(json.getElement(i, "vymeriavaci_zaklad"));
            paymentD.setEmployeeEnsurence(json.getElement(i, "poistne_zamestnanca"));
            paymentD.setNonTaxableWage(json.getElement(i, "nezdanitelna_mzda"));
            paymentD.setTaxableWage(json.getElement(i, "zdanitelna_mzda"));*/

            /*paymentD.setTaxAdvances(json.getElement(i, "preddavky_na_dan"));
            paymentD.setTaxBonus(json.getElement(i, "danovy_bonus"));*/
            paymentD.setNetWage(json.getElement(i, "cista_mzda"));
            /*paymentD.setOnAccount(json.getElement(i, "na_ucet"));
            paymentD.setCash(json.getElement(i, "k_vyplate"));*/

            paymentD.setWorkPrice(json.getElement(i, "cena_prace"));
            /*paymentD.setEmployerLevies(json.getElement(i, "odvody_zamestnavatela"));
            paymentD.setEmployeeLevies(json.getElement(i, "odvody_dan_zamestnanca"));
            paymentD.setLeviesSum(json.getElement(i, "odvody_dan_spolu"));

            paymentD.setWhoCreatedID(json.getElement(i, "vypracoval_pracujuci"));
            paymentD.setEmployeeImportantID(json.getElement(i, "dolezite_udaje_pracujuceho"));
            paymentD.setMinimumWageID(json.getElement(i, "minimalna_mzda"));*/

            paymentD.setEmployeeLastnameName(json.getElement(i, "priezvisko") + " " + json.getElement(i, "meno"));
            paymentD.setEmployeeID(json.getElement(i, "p_id"));

            paymentD.setRelationID(json.getElement(i, "p_id"));
            paymentD.setRelationType(json.getElement(i, "typ"));

            paymentD.setConditionsID(json.getElement(i, "ppv_id"));

            paymentD.setPositionID(json.getElement(i, "p2_id"));
            paymentD.setPositionName(json.getElement(i, "p2_nazov"));

            paymentD.setPlaceID(json.getElement(i, "p3_id"));
            paymentD.setPlaceName(json.getElement(i, "p3_nazov"));

            paymentD.setYearID(json.getElement(i, "o_id"));
            paymentD.setYearNumber(json.getElement(i, "rok"));

            paymentD.setMonthID(json.getElement(i, "om_id"));
            paymentD.setMonthNumber(json.getElement(i, "poradie_mesiaca"));
            paymentD.setMonthIsClosed(json.getElement(i, "je_mesiac_uzatvoreny"));
            paymentDS.add(paymentD);
        }

        return paymentDS;
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
    private TableView<PaymentD> tab = new TableView<PaymentD>();
    @FXML
    public TableColumn idEmpCol, lastnameNameCol, typeRelCol, placeCol, positionCol, idPaymentCol,
            idRelationCol, priceCol, grossWageCol, netWageCol;
    @FXML
    public TextField input;
    @FXML
    public ComboBox place = new ComboBox();
    @FXML
    public ComboBox relat = new ComboBox();
    @FXML
    public ComboBox yearC = new ComboBox();
    @FXML
    public ComboBox monthC = new ComboBox();
    public ToggleButton closed, unclosed;
    public ToggleGroup toggle1;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.setComboBoxes();
        idEmpCol.setCellValueFactory(new PropertyValueFactory<>("IEmployeeID"));
        lastnameNameCol.setCellValueFactory(new PropertyValueFactory<>("employeeLastnameName"));
        typeRelCol.setCellValueFactory(new PropertyValueFactory<>("relationType"));
        placeCol.setCellValueFactory(new PropertyValueFactory<>("placeName"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("positionName"));
        idPaymentCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idRelationCol.setCellValueFactory(new PropertyValueFactory<>("relationID"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("workPrice"));
        grossWageCol.setCellValueFactory(new PropertyValueFactory<>("grossWage"));
        netWageCol.setCellValueFactory(new PropertyValueFactory<>("netWage"));

        setListeners();

        filterAfterSelect();

        MainPaneManager.getC().desibleBackPage();
    }

    private void setComboBoxes()
    {
        relat.getItems().addAll(
                "Pracovný vzťah:",
                "PP: na plný úväzok",
                "PP: na kratší pracovný čas",
                "PP: telepráca",
                "D: o vykonaní práce",
                "D: o pracovnej činnosti",
                "D: o brig. práci študentov"
        );
        relat.getSelectionModel().selectFirst();

        place.getItems().add("Pracovisko:");
        for (String p:places)
        {
            place.getItems().add(p);
        }
        place.getSelectionModel().selectFirst();

        int i = 0, select=0;
        for (String p:years)
        {
            yearC.getItems().add(p);
            if(p.equals(this.year)) select=i;
            i++;
        }
        yearC.getSelectionModel().select(select);

        monthC.getItems().add("Mesiac:");
        monthC.getItems().addAll(
                "1","2","3","4","5","6","7","8","9","10","11","12"
        );

        i=0;
        if(this.month.equals("1")) i=1;
        else if(this.month.equals("2")) i=2;
        else if(this.month.equals("3")) i=3;
        else if(this.month.equals("4")) i=4;
        else if(this.month.equals("5")) i=5;
        else if(this.month.equals("6")) i=6;
        else if(this.month.equals("7")) i=7;
        else if(this.month.equals("8")) i=8;
        else if(this.month.equals("9")) i=9;
        else if(this.month.equals("10")) i=10;
        else if(this.month.equals("11")) i=11;
        else if(this.month.equals("12")) i=12;
        monthC.getSelectionModel().select(i);

    }

    private void filterAfterSelect()
    {
        filteredData = new FilteredList<>(paymentDS, e->true);
        filteredData.setPredicate((Predicate<? super PaymentD>) paymentD -> {
            boolean cls = closed.isSelected();
            String inp = input.getText();
            String pla = place.getValue().toString();
            String rel = relat.getValue().toString();
            return isFiltered(paymentD, cls, inp, pla, rel);
        });
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        tab.setItems(sortedData);
    }

    private void setListeners()
    {
        yearC.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            monthC.getSelectionModel().selectFirst();
            input.clear();
            place.getSelectionModel().selectFirst();
            relat.getSelectionModel().selectFirst();
            paymentDS.clear();
            //tab.getItems().removeAll(paymentDS);
        });

        monthC.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            input.clear();
            place.getSelectionModel().selectFirst();
            relat.getSelectionModel().selectFirst();
            paymentDS.clear();

            if(!monthC.getSelectionModel().isSelected(0))
            {
                try {
                    paymentDS = paymentSelect(yearC.getSelectionModel().getSelectedItem().toString(), newValue.toString());
                    filterAfterSelect();
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
        });

        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super PaymentD>) paymentD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    boolean cls = closed.isSelected();
                    String inp = newValue.toLowerCase();
                    String pla = place.getValue().toString();
                    String rel = relat.getValue().toString();
                    return isFiltered(paymentD, cls, inp, pla, rel);
                });
            }));
            sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        relat.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super PaymentD>) paymentD ->{
                if(newValue == null || newValue.toString().isEmpty()){
                    return true;
                }
                boolean cls = closed.isSelected();
                String inp = input.getText();
                String pla = place.getValue().toString();
                String rel = newValue.toString();
                return isFiltered(paymentD, cls, inp, pla, rel);
            });
            sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        place.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super PaymentD>) paymentD ->{
                if(newValue == null || newValue.toString().isEmpty()){
                    return true;
                }
                boolean cls = closed.isSelected();
                String inp = input.getText();
                String pla = newValue.toString();
                String rel = relat.getValue().toString();
                return isFiltered(paymentD, cls, inp, pla, rel);
            });
            sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });


        toggle1.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if(newValue==null)
                {
                    if(oldValue.equals(closed))
                        unclosed.setSelected(true);
                    else if(oldValue.equals(unclosed))
                        closed.setSelected(true);
                }
                else if(newValue.equals(closed))
                {
                    filteredData.setPredicate((Predicate<? super PaymentD>) paymentD ->{
                        boolean cls = true;
                        String inp = input.getText();
                        String pla = place.getValue().toString();
                        String rel = relat.getValue().toString();
                        return isFiltered(paymentD, cls, inp, pla, rel);
                    });
                    sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(tab.comparatorProperty());
                    tab.setItems(sortedData);
                }
                else if (newValue.equals(unclosed))
                {
                    filteredData.setPredicate((Predicate<? super PaymentD>) paymentD ->{
                        boolean cls = false;
                        String inp = input.getText();
                        String pla = place.getValue().toString();
                        String rel = relat.getValue().toString();
                        return isFiltered(paymentD, cls, inp, pla, rel);
                    });
                    sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(tab.comparatorProperty());
                    tab.setItems(sortedData);
                }
            }
        });

        /*ab.setRowFactory(tv -> {
            TableRow<PaymentD> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    PaymentD e = row.getItem();
                    System.out.println(e.toString());

                    FXMLLoader l = new FXMLLoader(getClass().getResource("PageEmployeeDetails.fxml"));
                    l.setControllerFactory(c -> {
                        return new PageEmployeeDetails(e.getId());
                    });
                    MainPaneManager.getC().loadScrollPage(l);
                }
            });
            return row ;
        });*/
    }

    private boolean isFiltered(PaymentD paymentD, boolean cls, String inp, String pla, String rel)
    {

        boolean flag0=false, flag1=false, flag2=false, flag3=false;
        boolean imp0=false, imp1= false, imp2= false, imp3= false;


        if(cls)
        {
            imp0=true;
            if(paymentD.getMonthIsClosed().equals("1"))
                flag0=true;
        }else{
            imp0=true;
            if(paymentD.getMonthIsClosed().equals("0"))
                flag0=true;
        }

        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(paymentD.getEmployeeLastnameName().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(String.valueOf(paymentD.getIEmployeeID()).toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(paymentD.getPositionName().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else{
                flag1=false;
            }
        }

        if(!pla.equals("Pracovisko:"))
        {
            imp2=true;
            if(paymentD.getPlaceName().equals(pla))
                flag2=true;
        }

        if(!rel.equals("Pracovný vzťah:"))
        {
            imp3=true;
            if(paymentD.getRelationType().equals(rel))
                flag3=true;
        }

        boolean flag = true;
        if(imp0)
            flag&=flag0;
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


    @FXML
    private void onCalculateClick(MouseEvent mouseEvent)
    {
        PaymentD p = tab.getSelectionModel().getSelectedItem();
        if(p==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }
        if(p.getMonthIsClosed().equals("1"))
        {
            CustomAlert al = new CustomAlert("Opätovný prepočet výplaty", "Výplata pre tento pracovný vzťah je už vypočítana.\nSte si istý, že chcete túto výplatu prepočítať opäť?", "", "Áno", "Nie");
            if(!al.showWait()) return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("create/PaneCreate.fxml"));
        loader.setControllerFactory(c -> {
            return new PaneCreate(this, p);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Výpočet výplaty");
        primaryStage.setScene(new Scene(root1, 891, 497));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
       /* primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            CustomAlert al = new CustomAlert("Ukončenie výpočtu výplaty",
                    "Ste si istý, že chcete ukončiť výpočet vypláty?\n Doterajšie zmeny nebudú uložené.",
                    "", "Áno", "Nie");
            if(!al.showWait()) return;
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                LoggedInUser.logout();
            }
        });*/
        primaryStage.show();
    }


    /*public void btn(ActionEvent actionEvent)
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
        primaryStage.setTitle("Vytvorenie nového pracujúceho");
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
    }*/

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
}
