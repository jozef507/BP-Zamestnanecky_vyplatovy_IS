package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;
import javafx.beans.property.SimpleBooleanProperty;
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

public class UpdateRelation implements RelationInterface
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ArrayList<AddRelationBox> wagesControllers;
    private PageEmployeeDetailsRelation pageEmployeeDetailsRelation;
    private PositionD choosenPosition;
    private RelationD relationD;
    private ConditionsD conditionsD;
    private NextConditionsD nextConditionsD;
    private ArrayList<WageD> wageDs;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public UpdateRelation(PageEmployeeDetailsRelation pageEmployeeDetailsRelation)
    {
        this.wagesControllers = new ArrayList<>();
        this.wageDs = new ArrayList<>();
        this.pageEmployeeDetailsRelation = pageEmployeeDetailsRelation;
        this.relationD = this.pageEmployeeDetailsRelation.getRelationD();
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    public void setChoosenPosition(PositionD choosenPosition) {
        this.choosenPosition = choosenPosition;
    }

    public void setPositionElements()
    {
        place.setText(this.choosenPosition.getPlace());
        position.setText(this.choosenPosition.getName());
    }

    private void updateRelation() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.addParam("relid", this.relationD.getId());
        ht.addParam("prevconds", this.pageEmployeeDetailsRelation.getConditionsDs().get(this.pageEmployeeDetailsRelation.getConditionsDs().size()-1).getId());

        if(this.nextConditionsD!=null)
        {
            ht.addParam("nextconditions", "true");
            ht.addParam("main", this.nextConditionsD.getIsMain());
            ht.addParam("holliday", this.nextConditionsD.getHollidayTime());
            ht.addParam("weektime", this.nextConditionsD.getWeekTime());
            ht.addParam("uniform", this.nextConditionsD.getIsWeekTimeUniform());
            ht.addParam("testtime", this.nextConditionsD.getTestTime());
            ht.addParam("sacktime", this.nextConditionsD.getSackTime());

            ht.addParam("daytime", this.nextConditionsD.getDayTime());
            ht.addParam("apweektime", this.nextConditionsD.getApWeekTime());
            ht.addParam("deductableitem", this.nextConditionsD.getDeductableItem());
        }
        else
        {
            ht.addParam("nextconditions", "false");
        }

        ht.addParam("from", this.conditionsD.getFrom());
        ht.addParam("to", this.conditionsD.getTo());
        ht.addParam("positionid", this.conditionsD.getPositionID());

        ht.addParam("taxfree", this.conditionsD.getTaxFree());
        ht.addParam("taxbonus", this.conditionsD.getTaxBonus());
        ht.addParam("disabled", this.conditionsD.getDisabled());
        ht.addParam("retirement", this.conditionsD.getRetirement());
        ht.addParam("invalidity40", this.conditionsD.getInvalidity40());
        ht.addParam("invalidity70", this.conditionsD.getInvalidity70());
        ht.addParam("premature", this.conditionsD.getPremature());
        ht.addParam("exemption", this.conditionsD.getExemption());
        ht.addParam("bank", this.conditionsD.getBank());
        ht.addParam("bankpart", this.conditionsD.getBankPart());
        ht.addParam("iban", this.conditionsD.getIban());

        ht.sendPost("relation/upd_rel", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
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

            ht.addParam("relid", "NULL");
            ht.addParam("conid", conID);
            ht.addParam("nextconid", nextConID);
            ht.addParam("prevconds", this.pageEmployeeDetailsRelation.getConditionsDs().get(this.pageEmployeeDetailsRelation.getConditionsDs().size()-1).getId());

            ht.sendPost("wage/crt_wage", LoggedInUser.getToken(), LoggedInUser.getId());
        }

    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
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

    public CheckBox bank, deductableItem, desibled, exemption,
            invalidity40, invalidity70, premature, retirement,
            taxBonus, taxFree;
    public TextField apWeekTime, dayTime, bankPart, iban;

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    public void initialize()
    {
        setDatePicker();
        changeFocus();
        setCheckBoxes();

        isMain.setSelected(true);
        isUniform.setSelected(true);
        bank.setSelected(false);
        bankPart.setDisable(true);
        iban.setDisable(true);

        if(pageEmployeeDetailsRelation.getRelationD().getType().equals("D: o vykonaní práce")
                || pageEmployeeDetailsRelation.getRelationD().getType().equals("D: o pracovnej činnosti")
                || pageEmployeeDetailsRelation.getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            hb.setDisable(true);
        }

    }

    private void setCheckBoxes()
    {
        isUniform.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    dayTime.setDisable(true);
                    dayTime.clear();
                } else {
                    dayTime.setDisable(false);
                }
            }
        });

        retirement.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    premature.setSelected(false);
                    invalidity40.setSelected(false);
                    invalidity70.setSelected(false);
                }
            }
        });

        premature.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    retirement.setSelected(false);
                    invalidity40.setSelected(false);
                    invalidity70.setSelected(false);
                }
            }
        });

        invalidity40.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    premature.setSelected(false);
                    retirement.setSelected(false);
                    invalidity70.setSelected(false);
                }
            }
        });

        invalidity70.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    premature.setSelected(false);
                    invalidity40.setSelected(false);
                    retirement.setSelected(false);
                }
            }
        });

        bank.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    bankPart.setDisable(false);
                    iban.setDisable(false);
                } else {
                    bankPart.setDisable(true);
                    bankPart.clear();
                    iban.setDisable(true);
                    iban.clear();
                }
            }
        });
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

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        from.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                from.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }
    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void addWage(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRelationBox.fxml"));
        HBox newPane = null;
        try {
            newPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddRelationBox c = loader.getController();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRelationChooseposition.fxml"));
        loader.setControllerFactory(c -> {
            return new AddRelationChooseposition(this);
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

    public void cancel(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }

    public void create(MouseEvent mouseEvent)
    {
        if(!checkFormular()) return;
        if(!checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Zmena podmienok pracovného vzťahu", "Ste si istý že chcete zmeňiť podmienky pracovného vzťahu?", "", "Áno", "Nie");
        if(!al.showWait())
            return;

        this.setModelsFromInputs();

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

        pageEmployeeDetailsRelation.updateInfo();
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
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
            else if(!dayTime.isDisable() && (dayTime.getText() == null || dayTime.getText().trim().isEmpty()))
                flag=false;
            else if(apWeekTime.getText() == null || apWeekTime.getText().trim().isEmpty())
                flag=false;
        }
        else if(!bankPart.isDisable() && (bankPart.getText() == null || bankPart.getText().trim().isEmpty()))
            flag=false;
        else if(!iban.isDisable() && (iban.getText() == null || iban.getText().trim().isEmpty()))
            flag=false;
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

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        if(!hb.isDisable()) {
            if(!testTime.getText().matches("^\\+?-?\\d+$")) flag=false;
            else if(!sackTime.getText().matches("^\\+?-?\\d+$")) flag=false;
            else if(!hollidayTime.getText().matches("^\\+?-?\\d+(\\.\\d+)?$")) flag=false;
            else if (!dayTime.isDisable() &&!dayTime.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
                flag = false;
            try {
                double d = Double.parseDouble(weekTime.getText());
                double e = Double.parseDouble(apWeekTime.getText());
                if (d > 40) flag = false;
                if (e > 40) flag = false;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }

        try {
            if(!bankPart.isDisable()){
                double d = Double.parseDouble(bankPart.getText());
                if (d<=0 || d>=1) flag = false;
            }
        } catch (NumberFormatException nfe) {
            flag = false;
        }

        int timeEvidence = 0, timeForm = 0, unregularly = 0, wages = 0;
        for(int i = 0;i<wagesControllers.size();i++)
        {
            if(!wagesControllers.get(i).getTarif().getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
                flag=false;
            if(!flag) break;
            if(wagesControllers.get(i).getForm().getValue().toString().contains("časová")) timeForm++;
            if(wagesControllers.get(i).getTime().isSelected()) timeEvidence++;
            if(wagesControllers.get(i).getWay().getValue().toString().contains("nepravidelne")) unregularly++;
            wages++;
        }
        if(flag)
        {
            if(!(unregularly==0 || wages==unregularly))
            {
                System.out.println("Pravidelne vyplácane mzdy môžu byť buď všetky alebo žiadna!");
                infoLabel.setText("Pravidelne vyplácane mzdy môžu byť buď všetky alebo žiadna!");
                infoLabel.setVisible(true);
                return false;
            }

            if(unregularly==0)
            {
                if(timeForm!=0)
                {
                    if(timeForm!=1)
                    {
                        System.out.println("Možné zadať len jednú časovú mzdu!");
                        infoLabel.setText("Možné zadať len jednú časovú mzdu!");
                        infoLabel.setVisible(true);
                        return false;
                    }

                    if(timeForm!=timeEvidence)
                    {
                        System.out.println("Ak existuje časová forma mzdy, len pri nej je možné evidovanie času!");
                        infoLabel.setText("Ak existuje časová forma mzdy, len pri nej je možné evidovanie času!");
                        infoLabel.setVisible(true);
                        return false;
                    }
                }
                else
                {
                    if(timeEvidence!=1)
                    {
                        System.out.println("Len pri jednej pravidelnej, nečasovej mzde je možné evidovať čas!");
                        infoLabel.setText("Len pri jednej pravidelnej, nečasovej mzde je možné evidovať čas!");
                        infoLabel.setVisible(true);
                        return false;
                    }
                }
            }
            else
            {
                if(wages!=timeEvidence)
                {
                    System.out.println("Pri každej nepravidelnej mzde je nutné evidovať čas.");
                    infoLabel.setText("Pri každej nepravidelnej mzde je nutné evidovať čas.");
                    infoLabel.setVisible(true);
                    return false;
                }
            }

        }


        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            infoLabel.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            infoLabel.setVisible(true);
            return flag;
        }
        return flag;
        /*try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }*/
    }

    private void setModelsFromInputs()
    {
        ConditionsD conditionsD = new ConditionsD();
        conditionsD.setFrom(from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        conditionsD.setTo("");
        conditionsD.setPositionID(this.choosenPosition.getId());
        conditionsD.setTaxFree(taxFree.isSelected()? "1":"0");
        conditionsD.setTaxBonus(taxBonus.isSelected()? "1":"0");
        conditionsD.setDisabled(desibled.isSelected()? "1":"0");
        conditionsD.setRetirement(retirement.isSelected()? "1":"0");
        conditionsD.setInvalidity40(invalidity40.isSelected()? "1":"0");
        conditionsD.setInvalidity70(invalidity70.isSelected()? "1":"0");
        conditionsD.setPremature(premature.isSelected()? "1":"0");
        conditionsD.setExemption(exemption.isSelected()? "1":"0");
        conditionsD.setBank(bank.isSelected()? "1":"0");
        conditionsD.setBankPart(bankPart.getText());
        conditionsD.setIban(iban.getText());
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
            nextConditionsD.setDayTime(dayTime.getText());
            nextConditionsD.setApWeekTime(apWeekTime.getText());
            nextConditionsD.setDeductableItem(deductableItem.isSelected()? "1":"0");
        }
        this.nextConditionsD = nextConditionsD;

        this.wageDs.clear();
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

            wageDs.add(wageD);
        }
    }

}
