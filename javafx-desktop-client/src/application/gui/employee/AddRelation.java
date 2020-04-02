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

public class ControllerAddRelation implements ControllerIntRelation
{

    public VBox vb;
    public HBox hb;
    public Text name;
    public ComboBox relType;
    public CheckBox isEnd;
    public DatePicker relBegin;
    public DatePicker relEnd;
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
    private ControllerPageEmployeeDetails controllerPageEmployeeDetails;
    private PositionD choosenPosition;
    private RelationD relationD;
    private ConditionsD conditionsD;
    private NextConditionsD nextConditionsD;
    private ArrayList<WageD> wageDs;


    public ControllerAddRelation(ControllerPageEmployeeDetails controllerPageEmployeeDetails)
    {
        this.wagesControllers = new ArrayList<>();
        this.wageDs = new ArrayList<>();
        this.controllerPageEmployeeDetails = controllerPageEmployeeDetails;
    }

    public void initialize()
    {
        setElements();

        relEnd.setDisable(true);
        hb.setDisable(true);
        isEnd.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    relEnd.setDisable(false);
                } else {
                    relEnd.setDisable(true);
                }
            }
        });

        relType.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
           if(newValue.equals("PP: na plný úväzok") || newValue.equals("PP: na kratší pracovný čas") || newValue.equals("PP: telepráca"))
               hb.setDisable(false);
           else
               hb.setDisable(true);
        });

    }

    private void setElements()
    {
        setDatePicker();
        relType.getItems().addAll(
                "PP: na plný úväzok",
                "PP: na kratší pracovný čas",
                "PP: telepráca",
                "D: o vykonaní práce",
                "D: o pracovnej činnosti",
                "D: o brig. práci študentov"
        );

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
        relBegin.setConverter(converter);
        relBegin.setPromptText("D.M.RRRR");
        relEnd.setConverter(converter);
        relEnd.setPromptText("D.M.RRRR");
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/add_relation_chooseposition.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddRelationChooseposition(this);
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
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Pridanie pracovného vzťahu", "Ste si istý že chcete pridať nový pracovný vzťah vybranému pracujúcemu?", "", "Áno", "Nie");
        if(!al.showWait())
            return;

        this.setModelsFromInputs();

        try {
            createRelation();
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

        controllerPageEmployeeDetails.updateInfo();
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        infoLabel.setVisible(false);
        if(relType.getSelectionModel().isEmpty())
            flag=false;
        else if(relBegin.getValue()==null)
            flag=false;
        else if(relEnd.isDisable()==false && relEnd.getValue()==null)
            flag=false;

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
            System.out.println("Nevyplené údaje.");
            infoLabel.setText("Nevyplené údaje.");
            infoLabel.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        if(!hb.isDisable()) {
            if (!testTime.getText().matches("^\\+?-?\\d+$"))
                flag = false;
            else if (!sackTime.getText().matches("^\\+?-?\\d+$"))
                flag = false;
            else if (!hollidayTime.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
                flag = false;
            try {
                double d = Double.parseDouble(weekTime.getText());
                if (d > 40) flag = false;
            } catch (NumberFormatException nfe) {
                flag = false;
            }
        }

        for(int i = 0;i<wagesControllers.size();i++)
        {
            if(!wagesControllers.get(i).getTarif().getText().matches("^\\+?-?\\d+(\\.\\d+)?$")) flag=false;
            if(!flag) break;
        }

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            infoLabel.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            infoLabel.setVisible(true);
            return flag;
        }
        return flag;

    }

    private void setModelsFromInputs()
    {
        RelationD relationD = new RelationD();
        relationD.setType(relType.getValue().toString());
        relationD.setFrom(relBegin.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if(isEnd.isSelected())
            relationD.setTo(relEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        else
            relationD.setTo("NULL");
        relationD.setEmployeeID(this.controllerPageEmployeeDetails.getEmployeeD().getId());
        this.relationD = relationD;

        ConditionsD conditionsD = new ConditionsD();
        conditionsD.setFrom(relBegin.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
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
            if(this.wagesControllers.get(i).emergency.isSelected())
                wageD.setEmergencyImportant("1");
            else
                wageD.setEmergencyImportant("0");
            wageD.setTarif(this.wagesControllers.get(i).tarif.getText());
            wageD.setPayWay(this.wagesControllers.get(i).way.getValue().toString());
            if(this.wagesControllers.get(i).payDate.isDisable())
                wageD.setPayDate("NULL");
            else
                wageD.setPayDate(this.wagesControllers.get(i).payDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            wageD.setWageFormID(this.wagesControllers.get(i).getFormID());

            this.wageDs.add(wageD);
        }
    }

    private void createRelation() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.addParam("type", this.relationD.getType());
        ht.addParam("begin", this.relationD.getFrom());
        ht.addParam("end", this.relationD.getTo());
        ht.addParam("employeeid", this.relationD.getEmployeeID());

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

        ht.sendPost("relation/crt_rel", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        String relID = json.getElement(0, "rel_id");
        String nextConID = json.getElement(0, "nextcon_id");
        String conID = json.getElement(0, "con_id");

        for(int i = 0; i<this.wageDs.size();i++)
        {
            ht.addParam("label", this.wageDs.get(i).getLabel());
            ht.addParam("employee",  this.wageDs.get(i).getEmployeeEnter());
            ht.addParam("time",  this.wageDs.get(i).getTimeImportant());
            ht.addParam("emergency",  this.wageDs.get(i).getEmergencyImportant());
            ht.addParam("tarif",  this.wageDs.get(i).getTarif());
            ht.addParam("way",  this.wageDs.get(i).getPayWay());
            ht.addParam("paydate", this.wageDs.get(i).getPayDate());
            ht.addParam("formid",  this.wageDs.get(i).getWageFormID());
            ht.addParam("conid",  conID);

            ht.addParam("relid",relID);
            ht.addParam("conid", conID);
            ht.addParam("nextconid", nextConID);

            ht.sendPost("wage/crt_wage", LoggedInUser.getToken(), LoggedInUser.getId());
        }

    }
}
