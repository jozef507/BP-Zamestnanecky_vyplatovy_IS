package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.LevyD;
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

public class ControllerPageLegislationLevy
{


    @FXML
    private TableView<LevyD> tab = new TableView<LevyD>();
    @FXML
    public TableColumn idCol, nameCol, partEeCol, partErCol, fromCol, toCol;
    @FXML
    public TextField input;
    @FXML

    private ObservableList<LevyD> levyDS;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partEeCol.setCellValueFactory(new PropertyValueFactory<>("partEe"));
        partErCol.setCellValueFactory(new PropertyValueFactory<>("partEr"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        tab.setItems(levyDS);

        FilteredList<LevyD> filteredData = new FilteredList<>(levyDS, e->true);
        input.setOnKeyPressed(e ->{
            input.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super LevyD>) levyD -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String inp = newValue.toLowerCase();
                    return isFiltered(levyD, inp);
                });
            }));
            SortedList<LevyD> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tab.comparatorProperty());
            tab.setItems(sortedData);
        });

        MainPaneManager.getC().setBackPage("page_legislation");
    }

    public ControllerPageLegislationLevy() {
        try{
            levyDS = levySelect();
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

    public void onUpdateClick(MouseEvent mouseEvent)
    {
        LevyD levyD = tab.getSelectionModel().getSelectedItem();
        if(levyD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/update_levy.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerUpdateLevy(this, levyD);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Upravenie odvodu");
        primaryStage.setScene(new Scene(root1, 505, 279));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void onRemoveClick(MouseEvent mouseEvent)
    {
        LevyD levyD = tab.getSelectionModel().getSelectedItem();
        if(levyD==null)
        {
            CustomAlert a = new CustomAlert("warning", "Chyba", "Nevybrali ste žiadny riadok z tabuľky." );
            return;
        }

        CustomAlert al = new CustomAlert("Odstránenie stupňa náročnosti", "Ste si istý, že chcete odstrániť vybraný stupeň náročnosti?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        try {
            this.removeLevy(levyD);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/add_levy.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddLevy(this);
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



    public void updateInfo()
    {
        try {
            levyDS = levySelect();
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
        tab.setItems(levyDS);
    }

    private boolean isFiltered(LevyD levyD, String inp)
    {

        boolean flag1=false;
        boolean imp1= false;
        if(!inp.equals(""))
        {
            imp1=true;
            String lowerCaseFilter = inp.toLowerCase();
            if(levyD.getId().toLowerCase().contains(lowerCaseFilter)){
                flag1 = true;
            }else if(levyD.getName().toLowerCase().contains(lowerCaseFilter)) {
                flag1 = true;
            }
        }

        boolean flag = true;
        if(imp1)
            flag&=flag1;

        return flag;
    }


    private ObservableList<LevyD> levySelect() throws InterruptedException, IOException, CommunicationException {
        ObservableList<LevyD> levyDS = FXCollections.observableArrayList();

        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("levy", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i=0; i<json.getSize(); i++)
        {
            LevyD levyD = new LevyD();
            levyD.setId(json.getElement(i, "id"));
            levyD.setName(json.getElement(i, "nazov"));
            levyD.setPartEe(json.getElement(i, "percentualna_cast_zamestnanec"));
            levyD.setPartEr(json.getElement(i, "percentualna_cast_zamestnavatel"));
            levyD.setFrom(json.getElement(i, "nice_date1"));
            levyD.setTo(json.getElement(i, "nice_date2"));
            levyDS.add(levyD);
        }
        return levyDS;
    }


    private void removeLevy(LevyD levyD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendDelete("levy/del_levy/"+levyD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        this.updateInfo();
    }


}
