package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.HoursD;
import application.models.RelationD;
import application.models.WageD;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ControllerUpdateHours implements ControllerIntHours
{

    public ScrollPane sp;
    public VBox vb;
    public HBox hb;
    public Text name, relEmp, relFrom, relTo, relId, relPlace, relPos, wageForm, wageId, wageLabel, wageUnit ;
    public FlowPane fp_hours;
    public Label label;

    private ControllerPageHours controllerPageHours;

    private RelationD choosenRelationD;
    private WageD choosenWage;

    private ControllerAddHoursBox controllerAddHoursBox;
    private HoursD hoursD;


    public ControllerUpdateHours(ControllerPageHours controllerPageHours, HoursD hoursD)
    {
        this.controllerPageHours = controllerPageHours;
        this.hoursD=hoursD;
        try {
            setRelation();
            setWage();
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

    public void initialize()
    {
        setRelationElements();
        setWageElements();
        addAddHoursBox();
    }

    private void setRelation() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("relation/rel_by_cons/"+hoursD.getConsID(), LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        RelationD newRelationD = null;

        newRelationD = new RelationD();

        newRelationD.setEmployeeID(json.getElement(0, "p_id"));
        newRelationD.setEmployeeNameLastname(json.getElement(0, "p_priezvisko")+" "+json.getElement(0, "p_meno"));

        newRelationD.setId(json.getElement(0, "id"));
        newRelationD.setType(json.getElement(0, "typ"));
        newRelationD.setFrom(json.getElement(0, "nice_date1"));
        newRelationD.setTo(json.getElement(0, "nice_date2"));

        newRelationD.setConditionsID(json.getElement(0, "ppv_id"));
        newRelationD.setConditionsFrom(json.getElement(0, "ppv_platnost_od"));
        newRelationD.setConditionsTo(json.getElement(0, "ppv_platnost_do"));

        newRelationD.setPositionID(json.getElement(0, "po_id"));
        newRelationD.setPositionName(json.getElement(0, "po_nazov"));

        newRelationD.setPlaceID(json.getElement(0, "pr_id"));
        newRelationD.setPlaceName(json.getElement(0, "pr_nazov"));

        choosenRelationD = newRelationD;
    }

    private void setWage() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("wage/wage_form/"+hoursD.getWageID(), LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        WageD wageD = new WageD();
        wageD.setId(json.getElement(0, "zm_id"));
        wageD.setLabel(json.getElement(0, "popis"));
        wageD.setEmployeeEnter(json.getElement(0, "vykon_eviduje_zamestnanec"));
        wageD.setTimeImportant(json.getElement(0, "nutne_evidovanie_casu"));
        wageD.setEmergencyImportant(json.getElement(0, "mozne_evidovanie_pohotovosti"));
        wageD.setTarif(json.getElement(0, "tarifa_za_jednotku_mzdy"));
        wageD.setPayWay(json.getElement(0, "sposob_vyplacania"));
        wageD.setPayDate(json.getElement(0, "nice_date1"));
        wageD.setWageFormID(json.getElement(0, "fm_id"));
        wageD.setWageFormName(json.getElement(0, "nazov"));
        wageD.setWageFormUnit(json.getElement(0, "jednotka_vykonu"));
        wageD.setWageFormUnitShort(json.getElement(0, "skratka_jednotky"));

        choosenWage = wageD;
    }

    private void addAddHoursBox()
    {
        loadBox();
        setBoxItems();

    }

    private void loadBox()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/"+"add_hours_box"+".fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddHoursBox(this);
        });
        VBox newPane = null;
        try {
            newPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ControllerAddHoursBox c = loader.getController();
        this.controllerAddHoursBox = c;
        this.fp_hours.getChildren().add(newPane);
    }

    private void setBoxItems()
    {
        controllerAddHoursBox.cross.setVisible(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        String date = hoursD.getDate();
        LocalDate localDate = LocalDate.parse(date, formatter);

        controllerAddHoursBox.date.setValue(localDate);
        controllerAddHoursBox.date.setDisable(true);

        if(hoursD.getFrom()!=null)
        {
            controllerAddHoursBox.timeCB.setSelected(true);
            controllerAddHoursBox.from.setText(hoursD.getFrom());
            controllerAddHoursBox.to.setText(hoursD.getTo());
        }
        if(hoursD.getEmergencyType()!=null)
        {
            controllerAddHoursBox.emergencyCB.setSelected(true);
            if(hoursD.getEmergencyType().equals("aktívna"))
                controllerAddHoursBox.emergency.getSelectionModel().selectFirst();
            else
                controllerAddHoursBox.emergency.getSelectionModel().select(1);
        }
        if(hoursD.getOverTime()!=null)
        {
            controllerAddHoursBox.overtimeCB.setSelected(true);
            controllerAddHoursBox.overtime.setText(hoursD.getOverTime());
        }
        if(hoursD.getUnitsDone()!=null)
        {
            controllerAddHoursBox.unitsCB.setSelected(true);
            controllerAddHoursBox.units.setText(hoursD.getUnitsDone());
        }
        if(hoursD.getPartBase()!=null)
        {
            controllerAddHoursBox.partCB.setSelected(true);
            controllerAddHoursBox.part.setText(hoursD.getPartBase());
        }
    }

    public void cancel(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }

    public void update(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Pridanie odpracovaných hodín", "Ste si istý že chcete pridať odpracované hodiny?", "", "Áno", "Nie");
        if(!al.showWait())
            return;

        this.setModelsFromInputs();

        try {
            updateHours();
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

        controllerPageHours.updateInfo();
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }


    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(choosenRelationD == null)
            flag=false;
        else if(choosenWage == null)
            flag=false;

        flag=controllerAddHoursBox.checkFormular();

        if(!flag)
        {
            System.out.println("Nevyplené údaje.");
            label.setText("Nevyplené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        flag=controllerAddHoursBox.checkFormularTypes();

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return flag;
        }
        return flag;

    }

    private void setModelsFromInputs()
    {
        controllerAddHoursBox.setModelsFromInputs();
    }

    private void updateHours() throws InterruptedException, IOException, CommunicationException
    {
        controllerAddHoursBox.updateHours(this.hoursD);
    }


    public void removeHoursBox(VBox vbox, ControllerAddHoursBox controllerAddHoursBox)
    {
    }

    @Override
    public void setChoosenRelation(RelationD relationD) {
        this.choosenRelationD = relationD;
        this.choosenWage = null;
    }

    @Override
    public RelationD getChoosenRelation() {
        return this.choosenRelationD;
    }

    @Override
    public void setChoosenWage(WageD wageD) {
        this.choosenWage = wageD;

    }

    @Override
    public WageD getChoosenWage() {
        return this.choosenWage;
    }

    @Override
    public void setRelationElements() {
        relId.setText(this.choosenRelationD.getId());
        relPlace.setText(this.choosenRelationD.getPlaceName());
        relFrom.setText(this.choosenRelationD.getFrom());
        relEmp.setText(this.choosenRelationD.getEmployeeNameLastname());
        relPos.setText(this.choosenRelationD.getPositionName());
        relTo.setText(this.choosenRelationD.getTo());
    }

    @Override
    public void setWageElements() {
        wageId.setText(this.choosenWage.getId());
        wageForm.setText(this.choosenWage.getWageFormName());
        wageLabel.setText(this.choosenWage.getLabel());
        wageUnit.setText(this.choosenWage.getWageFormUnit());
    }




}
