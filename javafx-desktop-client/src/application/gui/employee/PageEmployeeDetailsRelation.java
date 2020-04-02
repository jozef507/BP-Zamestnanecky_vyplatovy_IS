package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.MainPaneManager;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class PageEmployeeDetailsRelation
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private EmployeeD employeeD;
    private RelationOV relationOV;
    private RelationD relationD;
    private ArrayList<ConditionsD> conditionsDs;
    private ArrayList<String> places;
    private ArrayList<String> positions;
    private ArrayList<NextConditionsD> nextConditionsDs;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageEmployeeDetailsRelation(EmployeeD employeeD, RelationOV relationOV) {
        this.employeeD = employeeD;
        this.relationOV = relationOV;
        this.conditionsDs = new ArrayList<>();
        this.places = new ArrayList<>();
        this.positions = new ArrayList<>();
        this.nextConditionsDs = new ArrayList<>();
        this.vBoxes = new ArrayList<>();

        try {
            this.setRelationD();
            this.setConditionsDs();
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
    public void updateInfo()
    {
        try {
            //this.setRelationD();
            this.setConditionsDs();
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

        this.setTexts();
        this.setBoxes();
    }

    public RelationD getRelationD()
    {
        return relationD;
    }

    public ArrayList<ConditionsD> getConditionsDs()
    {
        return  this.conditionsDs;
    }

    private void setRelationD() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("relation/detail/"+this.relationOV.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        RelationD relationD = new RelationD();
        relationD.setId(json.getElement(0, "id"));
        relationD.setType(json.getElement(0, "typ"));
        relationD.setFrom(json.getElement(0, "nice_date1"));
        relationD.setTo(json.getElement(0, "nice_date2"));
        relationD.setEmployeeID(json.getElement(0, "pracujuci"));

        this.relationD = relationD;
    }

    private void setConditionsDs() throws InterruptedException, IOException, CommunicationException {
        this.conditionsDs.clear();
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("relation/con_ncon_po_pl/"+this.relationD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i = 0; i<json.getSize();i++)
        {
            ConditionsD conditionsD = new ConditionsD();
            conditionsD.setId(json.getElement(i, "ppv_id"));
            conditionsD.setRelationID(json.getElement(i, "pracovny_vztah"));
            conditionsD.setFrom(json.getElement(i, "platnost_od"));
            conditionsD.setTo(json.getElement(i, "platnost_do"));
            conditionsD.setNextConditionsID(json.getElement(i, "dalsie_podmienky"));
            conditionsD.setPositionID(json.getElement(i, "pozicia"));

            String place = json.getElement(i, "pr_nazov");
            String position = json.getElement(i, "po_nazov");

            NextConditionsD nextConditionsD = null;
            if(conditionsD.getNextConditionsID()!=null){
                nextConditionsD = new NextConditionsD();
                nextConditionsD.setId(json.getElement(i, "dp_id"));
                nextConditionsD.setIsMain(json.getElement(i, "je_hlavny_pp"));
                nextConditionsD.setHollidayTime(json.getElement(i, "vymera_dovolenky"));
                nextConditionsD.setWeekTime(json.getElement(i, "tyzdenny_pracovny_cas"));
                nextConditionsD.setIsWeekTimeUniform(json.getElement(i, "je_pracovny_cas_rovnomerny"));
                nextConditionsD.setTestTime(json.getElement(i, "skusobvna_doba"));
                nextConditionsD.setSackTime(json.getElement(i, "vypovedna_doba"));
            }

            this.conditionsDs.add(conditionsD);
            this.places.add(place);
            this.positions.add(position);
            this.nextConditionsDs.add(nextConditionsD);
        }
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public VBox vb;
    public Text name;
    public Text id;
    public Text type;
    public Text relFrom;
    public Text relTo;
    public VBox vbConditions;
    public Label infoLabel;
    private ArrayList<VBox> vBoxes;

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize()
    {
        this.setTexts();
        this.setBoxes();
    }

    private void setTexts()
    {
        name.setText(this.employeeD.getName()+" "+this.employeeD.getLastname());
        id.setText(this.relationD.getId());
        type.setText(this.relationD.getType());
        relFrom.setText(this.relationD.getFrom());
        relTo.setText(this.relationD.getTo());

    }

    private void setBoxes()
    {
        for(int i = 0; i<this.vBoxes.size(); i++)
        {
            vbConditions.getChildren().remove(this.vBoxes.get(i));
        }
        vBoxes.clear();

        for(int i = 0; i<this.conditionsDs.size(); i++)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PageEmployeeDetailsRelationBox.fxml"));
            int ii = i;
            loader.setControllerFactory(c -> {
                return new PageEmployeeDetailsRelationBox(this.conditionsDs.get(ii), this.places.get(ii),this.positions.get(ii),this.nextConditionsDs.get(ii));
            });
            VBox newPane = null;
            try {
                newPane = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //ControllerPageEmployeeDetailsRelationBox c = loader.getController();
            vbConditions.getChildren().addAll(newPane);
            vBoxes.add(newPane);
        }

    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void changeCon(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateRelation.fxml"));
        loader.setControllerFactory(c -> {
            return new UpdateRelation(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Úprava informácií pracujúceho");
        primaryStage.setScene(new Scene(root1, 826, 600));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void removeCon(MouseEvent mouseEvent)
    {
        CustomAlert al = new CustomAlert("Odstránenie podmienok pracovného vzťahu", "Ste si istý že chcete odstrániť tieto podmienky pracovného vzťahu.", "", "Áno", "Nie");
        if(!al.showWait())
            return;

        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendDelete("relation/del_lst_cons/"+this.conditionsDs.get(this.conditionsDs.size()-1).getId()+"/"+this.conditionsDs.get(this.conditionsDs.size()-2).getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Chyba",
                    "Nie je možné odstrániť posledné podmienky pracovného vzťahu.", e.getMessage());
            return;
        }catch (IOException e) {
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

    public void remove(MouseEvent mouseEvent)
    {
        CustomAlert al = new CustomAlert("Odstránenie pracovného vzťahu", "Ste si istý že chcete odstrániť tento pracovný vzťah?", "", "Áno", "Nie");
        if(!al.showWait())
            return;

        try {
            HttpClientClass ht = new HttpClientClass();
            ht.sendDelete("relation/del_rel/"+this.relationD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
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

        FXMLLoader l = new FXMLLoader(getClass().getResource("PageEmployeeDetails.fxml"));
        l.setControllerFactory(c -> {
            return new PageEmployeeDetails(this.employeeD.getId());
        });
        MainPaneManager.getC().loadScrollPage(l);
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

}
