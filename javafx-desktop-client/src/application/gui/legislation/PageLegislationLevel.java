package application.gui.legislation;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.LevelD;
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

public class PageLegislationLevel
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<LevelD> levelDS;
    private ArrayList<String> places;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageLegislationLevel() {
        try{
            levelDS = levelSelect();
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
            levelDS = levelSelect();
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
        tab.setItems(levelDS);
    }

    private ObservableList<LevelD> levelSelect() throws InterruptedException, IOException, CommunicationException {
        ObservableList<LevelD> levelDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("level", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {
            LevelD levelD = new LevelD();
            levelD.setId(json.getElement(i, "id"));
            levelD.setNumber(json.getElement(i, "cislo_stupna"));
            levelD.setCaracteristic(json.getElement(i, "charakteristika"));
            levelD.setFrom(json.getElement(i, "nice_date1"));
            levelD.setTo(json.getElement(i, "nice_date2"));
            levelDS.add(levelD);
        }
        return levelDS;
    }

    private void removeLevel(LevelD levelD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendDelete("level/del_lev/"+levelD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        this.updateInfo();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public ComboBox place;
    @FXML
    private TableView<LevelD> tab = new TableView<LevelD>();
    @FXML
    public TableColumn idCol, numCol, fromCol, toCol, carCol;
    @FXML
    public TextField input;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        carCol.setCellValueFactory(new PropertyValueFactory<>("caracteristic"));
        tab.setItems(levelDS);

        FilteredList<LevelD> filteredData = new FilteredList<>(levelDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super LevelD>) levelD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    return isFiltered(levelD, inp);
                });
            }));
            SortedList<LevelD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        MainPaneManager.getC().setBackPage("PageLegislation");
    }

    private boolean isFiltered(LevelD levelD, String inp)
    {

        boolean flag1=false;
        boolean imp1= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(levelD.getId().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(levelD.getNumber().toLowerCase().contains(lowerCaseFilter)) {
                flag1 = true;
            }
        }

        boolean flag = true;
        if(imp1)
            flag&=flag1;

        return flag;
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void onUpdateClick(MouseEvent mouseEvent)
    {
        LevelD levelD = tab.getSelectionModel().getSelectedItem();
        if(levelD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateLevel.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdateLevel(this, levelD);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Upravenie platnosti");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        LevelD levelD = tab.getSelectionModel().getSelectedItem();
        if(levelD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie stupňa náročnosti", "Ste si istý, že chcete odstrániť vybraný stupeň náročnosti?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        try {
            this.removeLevel(levelD);
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

    public void onAddClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddLevel.fxml"));
        loader.setControllerFactory(c -> {
            return new AddLevel(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie stupňa náročnosti");
        primaryStage.setScene(new Scene(root1, 505, 290));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
}
