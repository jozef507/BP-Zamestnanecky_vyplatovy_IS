package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
import application.models.RelationOV;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ControllerPageEmployeeDetails
{
    @FXML
    private Text name, phone, email, born_num, born_date, username, role, town, street,
            num, from, children_under, children_over, retirement, invalidity, insComp, part;
    @FXML
    private HBox hb;
    @FXML
    private VBox vb;
    @FXML
    private Button update1, update2, ret1;

    private String employeeID;
    private EmployeeD employeeD;
    private ArrayList<ControllerPageEmployeeDetailsBox> cBox;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        try {
            this.setEmployee();
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

        setTextFields();

        for(int i = 0; i<this.employeeD.getRelations().size(); i++)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/"+"page_employee_details_box"+".fxml"));
            RelationOV rel = this.employeeD.getRelations().get(i);
            loader.setControllerFactory(c -> {
                return new ControllerPageEmployeeDetailsBox(rel);
            });
            HBox newPane = loader.load();
            vb.getChildren().addAll(newPane);
        }

        update1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openUpdateEmployeeInfoScene();
            }
        });

        update2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_employee_important"+".fxml"));
                l.setControllerFactory(c -> {
                    return new ControllerPageEmployeeImportant(employeeD.getName()+" "+employeeD.getLastname(), employeeID);
                });
                MainPaneManager.getC().loadScrollPage(l);
            }
        });

    }

    public ControllerPageEmployeeDetails(String employeID){
        this.employeeID = employeID;
    }


    private void setEmployee() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("employee/emp_usr_imp/"+this.employeeID, LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        this.employeeD = new EmployeeD(
                json.getElement(0, "p_id"),
                json.getElement(0, "meno"),
                json.getElement(0, "priezvisko"),
                json.getElement(0, "telefon"),
                json.getElement(0, "rodne_cislo"),
                json.getElement(0, "nice_date1"),
                json.getElement(0, "pk_id"),
                json.getElement(0, "email"),
                json.getElement(0, "typ_prav"),
                json.getElement(0, "du_id"),
                json.getElement(0, "zdravotna_poistovna"),
                json.getElement(0, "mesto"),
                json.getElement(0, "ulica"),
                json.getElement(0, "cislo"),
                json.getElement(0, "nice_date2"),
                json.getElement(0, "pocet_deti_do_6_rokov"),
                json.getElement(0, "pocet_deti_nad_6_rokov"),
                json.getElement(0, "uplatnenie_nedzanitelnej_casti"),
                json.getElement(0, "poberatel_starobneho_dochodku"),
                json.getElement(0, "poberatel_invalidneho_dochodku")
        );

        ht.sendGet("employee/count_imp_pay/"+this.employeeD.getDuID(), LoggedInUser.getToken(), LoggedInUser.getId());
        json = new JsonArrayClass(ht.getRespnseBody());
        String c = json.getElement(0, "c");

        ht.sendGet("employee/emp_rel_ov/"+this.employeeID, LoggedInUser.getToken(), LoggedInUser.getId());
        json = new JsonArrayClass(ht.getRespnseBody());
        ArrayList<RelationOV> relations = new ArrayList<>();
        for (int i = 0 ; i<json.getSize(); i++) {
            RelationOV rel = new RelationOV(
                    json.getElement(i, "id"),
                    json.getElement(i, "typ"),
                    json.getElement(i, "nice_date1"),
                    json.getElement(i, "nice_date2"),
                    json.getElement(i, "dalsie_podmienky"),
                    json.getElement(i, "poz_nazov"),
                    json.getElement(i, "pr_nazov")
            );
            rel.setMainByNextDemands();
            relations.add(rel);
        }
        this.employeeD.setRelations(relations);
    }

    private void openUpdateEmployeeInfoScene(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/update_employee_info.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerUpdateEmployee(employeeID, employeeD.getName(), employeeD.getLastname(),
                    employeeD.getPhone(), employeeD.getBornNum(), employeeD.getBornDate(), this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Úprava informácií pracujúceho");
        primaryStage.setScene(new Scene(root1, 457, 375));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    /*private void openUpdateEmployeeImportantScene(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/update_employee_important.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerUpdateEmployeeImportant(employeeD.getDuID(), employeeD.getInsComp(), employeeD.getTown(),
                    employeeD.getStreet(), employeeD.getNumber(), employeeD.getChildrenUnder(),
                    employeeD.getChildrenOver(), employeeD.getPart(), employeeD.getRetirement(),employeeD.getInvalidity(), this);
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
    }*/

    /*private void returnEmployeeImportant()
    {
        CustomAlert al = new CustomAlert("Vrátenie zmien dôležitých údajov", "Ste si istý že chcete navrátiť zmeny dôležitých údajov pracujúceho?", "", "Áno", "Nie");
        if(!al.showWait())
            return;
        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendDelete("employee/delete_imp/"+this.employeeD.getDuID(), LoggedInUser.getToken(), LoggedInUser.getId());
            setEmployee();
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

        setTextFields();
    }*/



    private void setTextFields()
    {
        name.setText(this.employeeD.getName()+" "+ this.employeeD.getLastname());
        phone.setText(this.employeeD.getPhone());
        email.setText(this.employeeD.getEmail());
        born_num.setText(this.employeeD.getBornNum());
        born_date.setText(this.employeeD.getBornDate());
        username.setText(this.employeeD.getEmail());
        role.setText(this.employeeD.getUserType());
        insComp.setText(this.employeeD.getInsComp());
        town.setText(this.employeeD.getTown());
        street.setText(this.employeeD.getStreet());
        num.setText(this.employeeD.getNumber());
        from.setText(this.employeeD.getFrom());
        children_under.setText(this.employeeD.getChildrenUnder());
        children_over.setText(this.employeeD.getChildrenOver());
        part.setText(this.employeeD.getPart());
        retirement.setText(this.employeeD.getRetirement());
        invalidity.setText(this.employeeD.getInvalidity());
    }

    public void updateInfo()
    {
        try {
            this.setEmployee();
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
        setTextFields();
    }
}
