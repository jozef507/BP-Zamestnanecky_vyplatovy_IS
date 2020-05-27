package application.gui.legislation;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.MinimumWageD;
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

public class PageLegislationMinimum
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<MinimumWageD> minimumWageDS;
    private ArrayList<String> places;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageLegislationMinimum() {
        try{
            minimumWageDS = minimumWageSelect();
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

    private ObservableList<MinimumWageD> minimumWageSelect() throws InterruptedException, IOException, CommunicationException {
        ObservableList<MinimumWageD> minimumWageDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("minwage", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {
            MinimumWageD minimumWageD = new MinimumWageD();
            minimumWageD.setId(json.getElement(i, "id"));
            minimumWageD.setFrom(json.getElement(i, "nice_date1"));
            minimumWageD.setTo(json.getElement(i, "nice_date2"));
            minimumWageD.setHourValue(json.getElement(i, "hodinova_hodnota"));
            minimumWageD.setMonthValue(json.getElement(i, "mesacna_hodnota"));
            minimumWageD.setLevelID(json.getElement(i, "stupen_narocnosti"));
            minimumWageD.setLevelNum(json.getElement(i, "cislo_stupna"));
            minimumWageDS.add(minimumWageD);
        }
        return minimumWageDS;
    }

    private void removeMinimumWage(MinimumWageD minimumWageD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendDelete("minwage/del_minwg/"+minimumWageD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        this.updateInfo();
    }

    public void updateInfo()
    {
        try {
            minimumWageDS = minimumWageSelect();
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
        tab.setItems(minimumWageDS);
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public ComboBox place;
    @FXML
    private TableView<MinimumWageD> tab = new TableView<MinimumWageD>();
    @FXML
    public TableColumn idCol, fromCol, toCol, hourCol, monthCol, numCol;
    @FXML
    public TextField input;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        hourCol.setCellValueFactory(new PropertyValueFactory<>("hourValue"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("monthValue"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("levelNum"));
        tab.setItems(minimumWageDS);

        FilteredList<MinimumWageD> filteredData = new FilteredList<>(minimumWageDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super MinimumWageD>) minimumWageD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    return isFiltered(minimumWageD, inp);
                });
            }));
            SortedList<MinimumWageD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        MainPaneManager.getC().setBackPage("PageLegislation");
    }

    private boolean isFiltered(MinimumWageD minimumWageD, String inp)
    {

        boolean flag1=false;
        boolean imp1= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(minimumWageD.getId().toLowerCase().contains(lowerCaseFilter))
                flag1 = true;
        }

        boolean flag = true;
        if(imp1)
            flag&=flag1;

        return flag;
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void onRemoveClick(MouseEvent mouseEvent)
    {
        MinimumWageD minimumWageD = tab.getSelectionModel().getSelectedItem();
        if(minimumWageD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie stupňa náročnosti", "Ste si istý, že chcete odstrániť vybraný stupeň náročnosti?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        try {
            this.removeMinimumWage(minimumWageD);
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

    public void onUpdateClick(MouseEvent mouseEvent)
    {
        MinimumWageD minimumWageD = tab.getSelectionModel().getSelectedItem();
        if(minimumWageD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateMinimumwage.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdateMinimumwage(this, minimumWageD);
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

    public void onAddClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddMinimumwage.fxml"));
        loader.setControllerFactory(c -> {
            return new AddMinimumwage(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie minimálnej mzdy");
        primaryStage.setScene(new Scene(root1, 505, 290));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
}
