package application.gui.firm;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.PositionD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class PageFirmPosition
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<PositionD> positionDS;
    private ArrayList<String> places;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageFirmPosition() {
        try{
            positionDS = positionSelect();
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
            positionDS = positionSelect();
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
        tab.setItems(positionDS);
    }

    private ObservableList<PositionD> positionSelect() throws InterruptedException, IOException, CommunicationException {
        ObservableList<PositionD> positionDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("position/pos_lvl", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {
            PositionD positionD = new PositionD();
            positionD.setId(json.getElement(i, "id"));
            positionD.setName(json.getElement(i, "nazov"));
            positionD.setPlace(json.getElement(i, "pracovisko"));
            positionD.setPlaceName(json.getElement(i, "pr_nazov"));
            positionD.setCharacteristic(json.getElement(i, "charakteristika"));
            positionD.setLevelID(json.getElement(i, "sn_id"));
            positionD.setLevelLevel(json.getElement(i, "cislo_stupna"));
            positionDS.add(positionD);
        }

        return positionDS;
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

    private void removePosition(PositionD positionD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendDelete("position/del_pos/"+positionD.getId()+"/"+positionD.getLevelID(), LoggedInUser.getToken(), LoggedInUser.getId());
        this.updateInfo();
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public TableColumn placeCol;
    public TableColumn levelCol;
    public TableColumn carCol;
    public ComboBox place;
    @FXML
    private TableView<PositionD> tab = new TableView<PositionD>();
    @FXML
    public TableColumn nameCol,  idCol;
    @FXML
    public TextField input;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        placeCol.setCellValueFactory(new PropertyValueFactory<>("placeName"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("levelLevel"));
        carCol.setCellValueFactory(new PropertyValueFactory<>("characteristic"));
        tab.setItems(positionDS);

        place.getItems().add("Pracovisko:");
        for (String p:places)
        {
            place.getItems().add(p);
        }
        place.getSelectionModel().selectFirst();

        FilteredList<PositionD> filteredData = new FilteredList<>(positionDS, e->true);
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

        FilteredList<PositionD> filteredData2 = new FilteredList<>(positionDS, e->true);
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

        MainPaneManager.getC().setBackPage("PageFirm");
    }

    private boolean isFiltered(PositionD positionD, String inp, String pla)
    {

        boolean flag1=false, flag2=false;
        boolean imp1= false , imp2=false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(positionD.getId().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(positionD.getName().toLowerCase().contains(lowerCaseFilter)) {
                flag1 = true;
            }
        }

        if(!pla.equals("Pracovisko:"))
        {
            imp2=true;
            String filter1 = pla;
            String tmp1 = positionD.getPlaceName();
            if (tmp1.equals(filter1))
                flag2 = true;
        }


        boolean flag = true;
        if(imp1)
            flag&=flag1;
        else if(imp2)
            flag&=flag2;

        return flag;
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void onUpdateClick(MouseEvent mouseEvent)
    {
        PositionD positionD = tab.getSelectionModel().getSelectedItem();
        if(positionD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdatePosition.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdatePosition(this, positionD);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie pracovnej pozície");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        PositionD positionD = tab.getSelectionModel().getSelectedItem();
        if(positionD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie pozície", "Ste si istý, že chcete odstrániť vybranú pracovnú pozíciu?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        try {
            this.removePosition(positionD);
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

    public void onAddClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPosition.fxml"));
        loader.setControllerFactory(c -> {
            return new AddPosition(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie pracovnej pozície");
        primaryStage.setScene(new Scene(root1, 510, 300));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
}
