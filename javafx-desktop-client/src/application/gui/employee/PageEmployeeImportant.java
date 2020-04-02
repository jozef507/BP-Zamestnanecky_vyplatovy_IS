package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.ImportantD;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class PageEmployeeImportant
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private String employeeID;
    private String employeeName;
    private ArrayList<ImportantD> importants;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageEmployeeImportant(String name, String id){
        this.employeeID = id;
        this.employeeName = name;
        this.importants = new ArrayList<>();
        MainPaneManager.getC().setBackPage("PageEmployeeDetails", this.employeeID);
        try {
            setImportants();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
        }
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private void setImportants() throws InterruptedException, IOException, CommunicationException
    {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("employee/all_imp/"+this.employeeID, LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for (int i = 0; i < json.getSize(); i++) {
            this.importants.add(new ImportantD(
                    json.getElement(i, "id"),
                    json.getElement(i, "zdravotna_poistovna"),
                    json.getElement(i, "mesto"),
                    json.getElement(i, "ulica"),
                    json.getElement(i, "cislo"),
                    json.getElement(i, "pocet_deti_do_6_rokov"),
                    json.getElement(i, "pocet_deti_nad_6_rokov"),
                    json.getElement(i, "uplatnenie_nedzanitelnej_casti"),
                    json.getElement(i, "poberatel_starobneho_dochodku"),
                    json.getElement(i, "poberatel_invalidneho_dochodku"),
                    json.getElement(i, "nice_date1"),
                    json.getElement(i, "nice_date2")
            ));
        }
    }

    public void updateInfo()
    {
        this.clear();
        try {
            setImportants();
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

        try {
            setPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clear()
    {
        this.importants.clear();
        this.vb.getChildren().clear();
    }

    public String getEmployeeID() {
        return employeeID;
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public VBox vb;
    public Text name;
    public Button add, delete;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openUpdateEmployeeImportantScene();
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                returnEmployeeImportant();
            }
        });

        name.setText(employeeName);
        try {
            setPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPage() throws IOException {
        for (int finalI = 0; finalI < this.importants.size();finalI++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource( "PageEmployeeImportantBox.fxml"));
            int a = finalI;
            loader.setControllerFactory(c -> {
                return new PageEmployeeImportantBox(
                        this.importants.get(a).getInsComp(),
                        this.importants.get(a).getTown(),
                        this.importants.get(a).getStreet(),
                        this.importants.get(a).getNum(),
                        this.importants.get(a).getChildUnder(),
                        this.importants.get(a).getChildOver(),
                        this.importants.get(a).getPart(),
                        this.importants.get(a).getRetirement(),
                        this.importants.get(a).getInvalidity(),
                        this.importants.get(a).getFrom(),
                        this.importants.get(a).getTo()
                );
            });
            HBox newPane = loader.load();
            vb.getChildren().addAll(newPane);
        }
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/



    private void openUpdateEmployeeImportantScene(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateEmployeeImportant.fxml"));
        loader.setControllerFactory(c -> {

            try {
                return new UpdateEmployeeImportant(
                        this.importants.get((this.importants.size()) - 1).getId(),
                        this.importants.get((this.importants.size()) - 1).getInsComp(),
                        this.importants.get((this.importants.size()) - 1).getTown(),
                        this.importants.get((this.importants.size()) - 1).getStreet(),
                        this.importants.get((this.importants.size()) - 1).getNum(),
                        this.importants.get((this.importants.size()) - 1).getChildUnder(),
                        this.importants.get((this.importants.size()) - 1).getChildOver(),
                        this.importants.get((this.importants.size()) - 1).getPart(),
                        this.importants.get((this.importants.size()) - 1).getRetirement(),
                        this.importants.get((this.importants.size()) - 1).getInvalidity(),
                        this
                );
            }catch (IndexOutOfBoundsException e){
                return new UpdateEmployeeImportant(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        this
                );
            }

        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Úprava dôležitých informácií pracujúceho");
        primaryStage.setScene(new Scene(root1, 505, 495));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    private void returnEmployeeImportant()
    {
        CustomAlert al = new CustomAlert("Vrátenie zmien dôležitých údajov", "Ste si istý že chcete navrátiť zmeny dôležitých údajov pracujúceho?", "", "Áno", "Nie");
        if(!al.showWait())
            return;
        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendDelete("employee/delete_imp/"+this.importants.get(this.importants.size()-1).getId(), LoggedInUser.getToken(), LoggedInUser.getId());
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



}
