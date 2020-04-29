package application.gui.legislation;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.SurchargeTypeD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Predicate;

public class PageLegislationSurcharge
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<SurchargeTypeD> surchargeTypeDS;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageLegislationSurcharge() {
        try{
            surchargeTypeDS = surchargeTypeSelect();
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
            surchargeTypeDS = surchargeTypeSelect();
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
        tab.setItems(surchargeTypeDS);
    }

    private ObservableList<SurchargeTypeD> surchargeTypeSelect() throws InterruptedException, IOException, CommunicationException {
        ObservableList<SurchargeTypeD> surchargeTypeDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("surcharge", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {
            SurchargeTypeD surchargeTypeD = new SurchargeTypeD();
            surchargeTypeD.setId(json.getElement(i, "id"));
            surchargeTypeD.setName(json.getElement(i, "nazov"));
            surchargeTypeD.setPart(json.getElement(i, "percentualna_cast"));
            surchargeTypeD.setBase(json.getElement(i, "pocitany_zo"));
            surchargeTypeD.setFrom(json.getElement(i, "nice_date1"));
            surchargeTypeD.setTo(json.getElement(i, "nice_date2"));
            surchargeTypeDS.add(surchargeTypeD);
        }
        return surchargeTypeDS;
    }

    private void removeSurcharge(SurchargeTypeD surchargeTypeD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendDelete("surcharge/del_sur/"+surchargeTypeD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        this.updateInfo();
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    @FXML
    private TableView<SurchargeTypeD> tab = new TableView<SurchargeTypeD>();
    @FXML
    public TableColumn idCol, nameCol, descriptionCol, partCol, baseCol, fromCol, toCol;
    @FXML
    public TextField input;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        partCol.setCellValueFactory(new PropertyValueFactory<>("part"));
        baseCol.setCellValueFactory(new PropertyValueFactory<>("base"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        tab.setItems(surchargeTypeDS);

        FilteredList<SurchargeTypeD> filteredData = new FilteredList<>(surchargeTypeDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super SurchargeTypeD>) surchargeTypeD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    return isFiltered(surchargeTypeD, inp);
                });
            }));
            SortedList<SurchargeTypeD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        MainPaneManager.getC().setBackPage("PageLegislation");
    }

    private boolean isFiltered(SurchargeTypeD surchargeTypeD, String inp)
    {

        boolean flag1=false;
        boolean imp1= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(surchargeTypeD.getId().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(surchargeTypeD.getName().toLowerCase().contains(lowerCaseFilter)) {
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
        SurchargeTypeD surchargeTypeD = tab.getSelectionModel().getSelectedItem();
        if(surchargeTypeD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSurcharge.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdateSurcharge(this, surchargeTypeD);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Upravenie príplatku");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        SurchargeTypeD surchargeTypeD = tab.getSelectionModel().getSelectedItem();
        if(surchargeTypeD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie stupňa náročnosti", "Ste si istý, že chcete odstrániť vybraný stupeň náročnosti?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        try {
            this.removeSurcharge(surchargeTypeD);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSurcharge.fxml"));
        loader.setControllerFactory(c -> {
            return new AddSurcharge(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie typu príplatku");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

}
