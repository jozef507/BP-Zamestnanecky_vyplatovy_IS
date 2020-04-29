package application.gui.payment.create;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.function.Predicate;

public class PageSurcharges {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;

    private ArrayList<SurchargeTypeD> surchargeTypeDS;
    private ArrayList<SurchargeTypeD> nightSurchargeTypeDS;
    private ArrayList<SurchargeTypeD> saturdaySurchargeTypeDS;
    private ArrayList<SurchargeTypeD> sundaySurchargeTypeDS;
    private ArrayList<SurchargeTypeD> feastSurchargeTypeDS;
    private ArrayList<SurchargeTypeD> overtimeSurchargeTypeDS;


    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/

    public PageSurcharges(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
        paneCreate.getPaymentManager().getGrossWageManager().processSurchargesManager();
        surchargeTypeDS = paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getCurrentSurchargeTypeDS();

        nightSurchargeTypeDS = new ArrayList<>();
        saturdaySurchargeTypeDS = new ArrayList<>();
        sundaySurchargeTypeDS = new ArrayList<>();
        feastSurchargeTypeDS = new ArrayList<>();
        overtimeSurchargeTypeDS = new ArrayList<>();
        sortSurchargeTypeDS();
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS-----------------------------------------------------*/
    private void sortSurchargeTypeDS(){
        for(SurchargeTypeD s : surchargeTypeDS){
            if(s.getName().equals("noc"))
            {
                nightSurchargeTypeDS.add(s);
            }
            else if(s.getName().equals("sobota"))
            {
                saturdaySurchargeTypeDS.add(s);
            }
            else if(s.getName().equals("nedeľa"))
            {
                sundaySurchargeTypeDS.add(s);
            }
            else if(s.getName().equals("sviatok"))
            {
                feastSurchargeTypeDS.add(s);
            }
            else if(s.getName().equals("nadčas"))
            {
                overtimeSurchargeTypeDS.add(s);
            }
        }
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI FIELDS---------------------------------------------------*/
    @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private Label label;
    @FXML
    private Text saturdayHours1;
    @FXML
    private Text nightHours0;
    @FXML
    private Text sundayHours2;
    @FXML
    private Text feastHours3;
    @FXML
    private Text overtimeHours4;
    @FXML
    private Text nightWage0;
    @FXML
    private Text saturdayWage1;
    @FXML
    private Text sundayWage2;
    @FXML
    private Text feastWage3;
    @FXML
    private Text overtimeWage4;
     @FXML
    private Text overtime;
    @FXML
    private ComboBox nightType0;
    @FXML
    private ComboBox saturdayType1;
    @FXML
    private ComboBox sundayType2;
    @FXML
    private ComboBox feastType3;
    @FXML
    private ComboBox overtimeType4;
    @FXML
    private Text tatalWage;
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        setCheckBoxes();
        setElementsWithInitialData();
        setDisable();
        setListeners();
    }

    private void setElementsWithInitialData()
    {
        nightHours0.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("night").getHours());
        saturdayHours1.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("saturday").getHours());
        sundayHours2.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("sunday").getHours());
        feastHours3.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("feast").getHours());
        overtimeHours4.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("overtime").getHours());

        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("night").getWage()!=null)
            nightWage0.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("night").getWage());
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("saturday").getWage()!=null)
            saturdayWage1.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("saturday").getWage());
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("sunday").getWage()!=null)
            sundayWage2.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("sunday").getWage());
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("feast").getWage()!=null)
            feastWage3.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("feast").getWage());
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("overtime").getWage()!=null)
            overtimeWage4.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("overtime").getWage());
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargesTotal()!=null)
            tatalWage.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargesTotal().toPlainString() + " €");

        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("night").getSurcharegeTypeID()!=null)
        {
            String surchargeTypeID = paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("night").getSurcharegeTypeID();
            SurchargeTypeD surchargeTypeD = nightSurchargeTypeDS.stream().filter(carnet -> surchargeTypeID.equals(carnet.getId())).findFirst().orElse(null);
            int index = nightSurchargeTypeDS.indexOf(surchargeTypeD);
            nightType0.getSelectionModel().select(index);
        }
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("saturday").getSurcharegeTypeID()!=null)
        {
            String surchargeTypeID = paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("saturday").getSurcharegeTypeID();
            SurchargeTypeD surchargeTypeD = saturdaySurchargeTypeDS.stream().filter(carnet -> surchargeTypeID.equals(carnet.getId())).findFirst().orElse(null);
            int index = saturdaySurchargeTypeDS.indexOf(surchargeTypeD);
            saturdayType1.getSelectionModel().select(index);
        }
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("sunday").getSurcharegeTypeID()!=null)
        {
            String surchargeTypeID = paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("sunday").getSurcharegeTypeID();
            SurchargeTypeD surchargeTypeD = sundaySurchargeTypeDS.stream().filter(carnet -> surchargeTypeID.equals(carnet.getId())).findFirst().orElse(null);
            int index = sundaySurchargeTypeDS.indexOf(surchargeTypeD);
            sundayType2.getSelectionModel().select(index);
        }
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("feast").getSurcharegeTypeID()!=null)
        {
            String surchargeTypeID = paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("feast").getSurcharegeTypeID();
            SurchargeTypeD surchargeTypeD = feastSurchargeTypeDS.stream().filter(carnet -> surchargeTypeID.equals(carnet.getId())).findFirst().orElse(null);
            int index = feastSurchargeTypeDS.indexOf(surchargeTypeD);
            feastType3.getSelectionModel().select(index);
        }
        if(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("overtime").getSurcharegeTypeID()!=null)
        {
            String surchargeTypeID = paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("overtime").getSurcharegeTypeID();
            SurchargeTypeD surchargeTypeD = overtimeSurchargeTypeDS.stream().filter(carnet -> surchargeTypeID.equals(carnet.getId())).findFirst().orElse(null);
            int index = overtimeSurchargeTypeDS.indexOf(surchargeTypeD);
            overtimeType4.getSelectionModel().select(index);
        }
    }

    private void setCheckBoxes()
    {
        for(SurchargeTypeD s : nightSurchargeTypeDS)
        {
            nightType0.getItems().addAll(s.getCheckboxString());
        }
        for(SurchargeTypeD s : saturdaySurchargeTypeDS)
        {
            saturdayType1.getItems().addAll(s.getCheckboxString());
        }
        for(SurchargeTypeD s : sundaySurchargeTypeDS)
        {
            sundayType2.getItems().addAll(s.getCheckboxString());
        }
        for(SurchargeTypeD s : feastSurchargeTypeDS)
        {
            feastType3.getItems().addAll(s.getCheckboxString());
        }
        for(SurchargeTypeD s : overtimeSurchargeTypeDS)
        {
            overtimeType4.getItems().addAll(s.getCheckboxString());
        }
    }

    private void setDisable()
    {
        if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
        || paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
        || paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            overtime.setDisable(true);
            overtimeHours4.setDisable(true);
            overtimeType4.setDisable(true);
            overtimeWage4.setDisable(true);
        }
    }

    private void setListeners()
    {
        nightType0.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(!newValue.equals(oldValue))
            {
                SurchargeTypeD surchargeTypeD = null;
                for(SurchargeTypeD s : nightSurchargeTypeDS)
                {
                    if(s.getCheckboxString().equals(newValue))
                    {
                        surchargeTypeD = s;
                        break;
                    }
                }
                paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().calculateSurcharge("night", surchargeTypeD);
                nightWage0.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("night").getWage() + " €");
                tatalWage.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargesTotal().toPlainString() + " €");
            }
        });

        saturdayType1.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(!newValue.equals(oldValue))
            {
                SurchargeTypeD surchargeTypeD = null;
                for(SurchargeTypeD s : saturdaySurchargeTypeDS)
                {
                    if(s.getCheckboxString().equals(newValue))
                    {
                        surchargeTypeD = s;
                        break;
                    }
                }
                paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().calculateSurcharge("saturday", surchargeTypeD);
                saturdayWage1.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("saturday").getWage() + " €");
                tatalWage.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargesTotal().toPlainString() + " €");
            }
        });
        sundayType2.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(!newValue.equals(oldValue))
            {
                SurchargeTypeD surchargeTypeD = null;
                for(SurchargeTypeD s : sundaySurchargeTypeDS)
                {
                    if(s.getCheckboxString().equals(newValue))
                    {
                        surchargeTypeD = s;
                        break;
                    }
                }
                paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().calculateSurcharge("sunday", surchargeTypeD);
                sundayWage2.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("sunday").getWage() + " €");
                tatalWage.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargesTotal().toPlainString() + " €");
            }
        });
        feastType3.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(!newValue.equals(oldValue))
            {
                SurchargeTypeD surchargeTypeD = null;
                for(SurchargeTypeD s : feastSurchargeTypeDS)
                {
                    if(s.getCheckboxString().equals(newValue))
                    {
                        surchargeTypeD = s;
                        break;
                    }
                }
                paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().calculateSurcharge("feast", surchargeTypeD);
                feastWage3.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("feast").getWage() + " €");
                tatalWage.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargesTotal().toPlainString() + " €");
            }
        });
        overtimeType4.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(!newValue.equals(oldValue))
            {
                SurchargeTypeD surchargeTypeD = null;
                for(SurchargeTypeD s : overtimeSurchargeTypeDS)
                {
                    if(s.getCheckboxString().equals(newValue))
                    {
                        surchargeTypeD = s;
                        break;
                    }
                }
                paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().calculateSurcharge("overtime", surchargeTypeD);
                overtimeWage4.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargeTypeD("overtime").getWage() + " €");
                tatalWage.setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargesTotal().toPlainString() + " €");
            }
        });
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/

    @FXML
    private void onBackClick(MouseEvent mouseEvent)
    {
        setBackPage();
    }

    @FXML
    private void onNextClick(MouseEvent mouseEvent)
    {
        if(!checkFormular()) return;
        setPaneElements();
        setNextPage();
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/
    private boolean checkFormular()
    {
        label.setVisible(false);
        boolean flag = true;
        if(!nightType0.isDisable() && nightType0.getSelectionModel().isEmpty())
            flag=false;
        else if(!saturdayType1.isDisable() && saturdayType1.getSelectionModel().isEmpty())
            flag=false;
         else if(!sundayType2.isDisable() && sundayType2.getSelectionModel().isEmpty())
            flag=false;
         else if(!feastType3.isDisable() && feastType3.getSelectionModel().isEmpty())
            flag=false;
         else if(!overtimeType4.isDisable() && overtimeType4.getSelectionModel().isEmpty())
            flag=false;

         if(!flag) {
             label.setVisible(true);
             label.setText("Nevyplnené údaje!");
             System.out.println("Nevyplnené údaje!");
         }

         return flag;
    }

    private void setPaneElements()
    {
        paneCreate.getSurcharges().setText(paneCreate.getPaymentManager().getGrossWageManager().getSurchargesManager().getSurchargesTotal().toPlainString());
    }

    private void setBackPage()
    {
        if(paneCreate.isWasAveragePage())
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("PageAverage.fxml"));
            l.setControllerFactory(c -> {
                return new PageAverage(paneCreate);
            });
            paneCreate.loadAnchorPage(l);
        }
        else
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("PageBasDynComp.fxml"));
            l.setControllerFactory(c -> {
                return new PageBasDynComp(paneCreate);
            });
            paneCreate.loadAnchorPage(l);
        }

    }

    private void setNextPage()
    {
        if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
                || paneCreate.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("PageWageCompensations.fxml"));
            l.setControllerFactory(c -> {
                return new PageWageCompensations(paneCreate);
            });
            paneCreate.setWasWorkDayTimePage(true);
            paneCreate.loadAnchorPage(l);

        }
        else
        {
            if(paneCreate.getPaymentManager().getEmployeeConditionsManager().getNextConditionsD().getDayTime()==null)
            {
                BigDecimal weekTime = new BigDecimal(paneCreate.getPaymentManager().getEmployeeConditionsManager()
                    .getNextConditionsD().getWeekTime());
                BigDecimal dayTime = weekTime.divide(new BigDecimal("5"), 4, RoundingMode.HALF_UP)
                    .setScale(4, RoundingMode.HALF_UP);
                paneCreate.getPaymentManager().getEmployeeConditionsManager().getNextConditionsD()
                    .setDayTime(dayTime.toPlainString());
            }

            FXMLLoader l = new FXMLLoader(getClass().getResource("PageWageCompensations.fxml"));
            l.setControllerFactory(c -> {
                return new PageWageCompensations(paneCreate);
            });
            paneCreate.setWasWorkDayTimePage(false);
            paneCreate.loadAnchorPage(l);
        }


    }


}
