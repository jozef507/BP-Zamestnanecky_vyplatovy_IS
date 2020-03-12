package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ControllerUpdateRelation implements ControllerRelation
{

    public VBox vb;
    public HBox hb;
    public Text name;
    public DatePicker from;
    public CheckBox isMain;
    public CheckBox isUniform;
    public TextField testTime;
    public TextField hollidayTime;
    public TextField weekTime;
    public TextField sackTime;
    public Button addPosition;
    public Text place;
    public Text position;
    public VBox vb_wage;
    public Label infoLabel;

    private ArrayList<ControllerAddRelationBox> wagesControllers;
    private ControllerPageEmployeeDetailsRelation controllerPageEmployeeDetailsRelation;
    private PositionD choosenPosition;
    private RelationD relationD;
    private ConditionsD conditionsD;
    private NextConditionsD nextConditionsD;
    private ArrayList<WageD> wageDs;


    public ControllerUpdateRelation(ControllerPageEmployeeDetailsRelation controllerPageEmployeeDetailsRelation)
    {
        this.wagesControllers = new ArrayList<>();
        this.wageDs = new ArrayList<>();
        this.controllerPageEmployeeDetailsRelation = controllerPageEmployeeDetailsRelation;
        this.relationD = this.controllerPageEmployeeDetailsRelation.getRelationD();
    }

    public void initialize()
    {

        setDatePicker();

        if(controllerPageEmployeeDetailsRelation.getRelationD().getType().equals("D: o vykonaní práce")
            || controllerPageEmployeeDetailsRelation.getRelationD().getType().equals("D: o pracovnej činnosti")
            || controllerPageEmployeeDetailsRelation.getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            hb.setDisable(true);
        }

    }


    private void setDatePicker()
    {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("d.M.yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        from.setConverter(converter);
        from.setPromptText("D.M.RRRR");
    }

    public void addWage(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/"+"add_relation_box"+".fxml"));
        HBox newPane = null;
        try {
            newPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ControllerAddRelationBox c = loader.getController();
        wagesControllers.add(c);
        vb_wage.getChildren().addAll(newPane);
    }

    public void removeWage(MouseEvent mouseEvent)
    {
        vb_wage.getChildren().remove(vb_wage.getChildren().get(vb_wage.getChildren().size()-1));
        wagesControllers.remove(wagesControllers.size()-1);
    }

    public void addPosition(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/add_relation_choose_position.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddRelationChoosePosition(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Výber pracovnej pozície");
        primaryStage.setScene(new Scene(root1, 810, 570));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void setChoosenPosition(PositionD choosenPosition) {
        this.choosenPosition = choosenPosition;
    }

    public void setPositionElements()
    {
        place.setText(this.choosenPosition.getPlace());
        position.setText(this.choosenPosition.getName());
    }

    public void cancel(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }

    public void create(MouseEvent mouseEvent)
    {
        if(!checkFormular())
            return;


        ConditionsD conditionsD = new ConditionsD();
        conditionsD.setFrom(from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        conditionsD.setTo("NULL");
        conditionsD.setPositionID(this.choosenPosition.getId());
        this.conditionsD = conditionsD;

        NextConditionsD nextConditionsD = new NextConditionsD();
        if(hb.isDisable()) {
            nextConditionsD = null;
        } else {
            if(isMain.isSelected())
                nextConditionsD.setIsMain("1");
            else
                nextConditionsD.setIsMain("0");

            if(isUniform.isSelected())
                nextConditionsD.setIsWeekTimeUniform("1");
            else
                nextConditionsD.setIsWeekTimeUniform("0");
            nextConditionsD.setHollidayTime(hollidayTime.getText());
            nextConditionsD.setWeekTime(weekTime.getText());
            nextConditionsD.setTestTime(testTime.getText());
            nextConditionsD.setSackTime(sackTime.getText());
        }
        this.nextConditionsD = nextConditionsD;

        for (int i = 0; i<wagesControllers.size(); i++)
        {
            WageD wageD = new WageD();
            wageD.setLabel(this.wagesControllers.get(i).label.getText());
            if(this.wagesControllers.get(i).employee.isSelected())
                wageD.setEmployeeEnter("1");
            else
                wageD.setEmployeeEnter("0");
            if(this.wagesControllers.get(i).time.isSelected())
                wageD.setTimeImportant("1");
            else
                wageD.setTimeImportant("0");
            wageD.setTarif(this.wagesControllers.get(i).tarif.getText());
            wageD.setPayWay(this.wagesControllers.get(i).way.getValue().toString());
            if(this.wagesControllers.get(i).payDate.isDisable())
                wageD.setPayDate("NULL");
            else
                wageD.setPayDate(this.wagesControllers.get(i).payDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            wageD.setWageFormID(this.wagesControllers.get(i).getFormID());

            wageDs.add(wageD);
        }

        try {
            updateRelation();
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

        controllerPageEmployeeDetailsRelation.updateInfo();
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        infoLabel.setVisible(false);

        if(!hb.isDisable())
        {
            if(hollidayTime.getText() == null || hollidayTime.getText().trim().isEmpty())
                flag=false;
            else if(weekTime.getText() == null || weekTime.getText().trim().isEmpty())
                flag=false;
            else if(testTime.getText() == null || testTime.getText().trim().isEmpty())
                flag=false;
            else if(sackTime.getText() == null || sackTime.getText().trim().isEmpty())
                flag=false;
        }


        else if(choosenPosition == null)
            flag=false;
        else if(wagesControllers.size() == 0)
            flag=false;

        for(int i = 0;i<wagesControllers.size();i++)
        {
            if(wagesControllers.get(i).getForm().getSelectionModel().isEmpty())
                flag=false;
            else if(wagesControllers.get(i).getTarif().getText() == null || wagesControllers.get(i).getTarif().getText().trim().isEmpty())
                flag=false;
            else if(wagesControllers.get(i).getLabel().getText() == null || wagesControllers.get(i).getLabel().getText().trim().isEmpty())
                flag=false;
            else if( wagesControllers.get(i).getPayDate().isDisable()==false && wagesControllers.get(i).getPayDate().getValue()==null)
                flag=false;

            if(!flag)
                break;
        }

        if(!flag)
        {
            System.out.println("Nevyplené alebo nesprávne vyplnené údaje.");
            infoLabel.setVisible(true);
            return false;
        }
        return flag;
    }

    private void updateRelation() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.addParam("relid", this.relationD.getId());
        ht.addParam("prevconds", this.controllerPageEmployeeDetailsRelation.getConditionsDs().get(this.controllerPageEmployeeDetailsRelation.getConditionsDs().size()-1).getId());

        if(this.nextConditionsD!=null)
        {
            ht.addParam("nextconditions", "true");
            ht.addParam("main", this.nextConditionsD.getIsMain());
            ht.addParam("holliday", this.nextConditionsD.getHollidayTime());
            ht.addParam("weektime", this.nextConditionsD.getWeekTime());
            ht.addParam("uniform", this.nextConditionsD.getIsWeekTimeUniform());
            ht.addParam("testtime", this.nextConditionsD.getTestTime());
            ht.addParam("sacktime", this.nextConditionsD.getSackTime());
        }
        else
        {
            ht.addParam("nextconditions", "false");
        }

        ht.addParam("from", this.conditionsD.getFrom());
        ht.addParam("to", this.conditionsD.getTo());
        ht.addParam("positionid", this.conditionsD.getPositionID());

        ht.sendPost("relation/upd_rel", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        String nextConID = json.getElement(0, "nextcon_id");
        String conID = json.getElement(0, "con_id");

        for(int i = 0; i<this.wageDs.size();i++)
        {
            ht.addParam("label", this.wageDs.get(i).getLabel());
            ht.addParam("employee",  this.wageDs.get(i).getEmployeeEnter());
            ht.addParam("time",  this.wageDs.get(i).getTimeImportant());
            ht.addParam("tarif",  this.wageDs.get(i).getTarif());
            ht.addParam("way",  this.wageDs.get(i).getPayWay());
            ht.addParam("paydate", this.wageDs.get(i).getPayDate());
            ht.addParam("formid",  this.wageDs.get(i).getWageFormID());
            ht.addParam("conid",  conID);

            ht.addParam("relid", "NULL");
            ht.addParam("conid", conID);
            ht.addParam("nextconid", nextConID);

            ht.sendPost("wage/crt_wage", LoggedInUser.getToken(), LoggedInUser.getId());
        }

    }
}
