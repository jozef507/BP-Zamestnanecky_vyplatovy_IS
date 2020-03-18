package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
import application.models.EmployeeOV;
import application.models.PositionD;
import application.models.WageFormD;
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

public class ControllerPageFirmForm
{

    public TableColumn unitCol;
    public TableColumn unitShorCol;
    @FXML
    private TableView<WageFormD> tab = new TableView<WageFormD>();
    @FXML
    public TableColumn nameCol,  idCol;
    @FXML
    public TextField input;
    @FXML

    private ObservableList<WageFormD> wageFormDS;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitShorCol.setCellValueFactory(new PropertyValueFactory<>("unitShort"));

        tab.setItems(wageFormDS);

        FilteredList<WageFormD> filteredData = new FilteredList<>(wageFormDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super WageFormD>) wageFormD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    return isFiltered(wageFormD, inp);
                });
            }));
            SortedList<WageFormD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        MainPaneManager.getC().setBackPage("page_firm");
    }

    public ControllerPageFirmForm() {
        try{
            wageFormDS = wageFormSelect();
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

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        WageFormD wageFormD = tab.getSelectionModel().getSelectedItem();
        if(wageFormD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie formy mzdy", "Ste si istý, že chcete odstrániť vybranú formu mzdy?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        try {
            this.removeWageForm(wageFormD);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/add_wageform.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddWageform(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pridanie formy mzdy");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void updateInfo()
    {
        try {
            wageFormDS = wageFormSelect();
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
        tab.setItems(wageFormDS);
    }

    private boolean isFiltered(WageFormD wageFormD, String inp)
    {

        boolean flag1=false;
        boolean imp1= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(wageFormD.getId().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(wageFormD.getName().toLowerCase().contains(lowerCaseFilter)) {
                flag1 = true;
            }
        }

        boolean flag = true;
        if(imp1)
            flag&=flag1;

        return flag;
    }


    private ObservableList<WageFormD> wageFormSelect() throws InterruptedException, IOException, CommunicationException {
        ObservableList<WageFormD> wageFormDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("wage/forms", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        for(int i=0; i<json.getSize(); i++)
        {
            wageFormDS.add(
                    new WageFormD(
                            json.getElement(i, "id"),
                            json.getElement(i, "nazov"),
                            json.getElement(i, "jednotka_vykonu"),
                            json.getElement(i, "skratka_jednotky")
                    )
            );
        }
        return wageFormDS;
    }

    private void removeWageForm(WageFormD wageFormD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendDelete("wage/del_form/"+wageFormD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        this.updateInfo();
    }

}
