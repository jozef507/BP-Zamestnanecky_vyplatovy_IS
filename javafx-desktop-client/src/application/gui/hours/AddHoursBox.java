package application.gui.hours;

import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.HoursD;
import application.models.RelationD;
import application.models.WageD;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddHoursBox
{

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private HoursInterface hoursInterface;
    private HoursD hoursD;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public AddHoursBox(HoursInterface hoursInterface)
    {
        this.hoursInterface = hoursInterface;
        hoursD = new HoursD();
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    public void createHours(RelationD relationD, WageD wageD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.addParam("consid", relationD.getConditionsID());
        ht.addParam("wageid", wageD.getId());
        ht.addParam("date", this.hoursD.getDate());
        ht.addParam("from", this.hoursD.getFrom());
        ht.addParam("to", this.hoursD.getTo());
        ht.addParam("overtime", this.hoursD.getOverTime());
        ht.addParam("units", this.hoursD.getUnitsDone());
        ht.addParam("part", this.hoursD.getPartBase());
        ht.addParam("emergency", this.hoursD.getEmergencyType());
        ht.sendPost("hours/crt_hrs", LoggedInUser.getToken(), LoggedInUser.getId());
    }

    public void updateHours(HoursD prevHoursD) throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.addParam("omid", prevHoursD.getMonthID());
        ht.addParam("id", prevHoursD.getId());
        ht.addParam("from", this.hoursD.getFrom());
        ht.addParam("to", this.hoursD.getTo());
        ht.addParam("overtime", this.hoursD.getOverTime());
        ht.addParam("units", this.hoursD.getUnitsDone());
        ht.addParam("part", this.hoursD.getPartBase());
        ht.addParam("emergency", this.hoursD.getEmergencyType());
        ht.sendPost("hours/upd_hrs", LoggedInUser.getToken(), LoggedInUser.getId());
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public CheckBox overtimeCB, partCB, timeCB, unitsCB, emergencyCB;
    public TextField from, to, units, part, overtime;
    public DatePicker date;
    public ComboBox emergency;
    public Label label;
    public VBox vb;
    public Button cross;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    public void initialize()
    {
        setDatePicker();
        setCheckBoxes();
        setComboBoxes();
        setInputsDisable();
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
        date.setConverter(converter);
        date.setPromptText("D.M.RRRR");
    }

    private void setComboBoxes()
    {
        emergency.getItems().addAll(
                "aktívna",
                "neaktívna"
        );
    }

    private void setInputsDisable()
    {
        String formName = this.hoursInterface.getChoosenWage().getWageFormName();
        String timeRequired = this.hoursInterface.getChoosenWage().getEmployeeEnter();
        String emergencyRequired = this.hoursInterface.getChoosenWage().getEmergencyImportant();
        if(formName.matches("^časová-?.*$"))
        {
            timeCB.setSelected(true);
        }
        else if(formName.matches("^výkonová-?.*$"))
        {
            unitsCB.setSelected(true);
        }
        else if(formName.matches("^podielová-?.*$"))
        {
            partCB.setSelected(true);
        }

        if(timeRequired.equals("áno"))
        {
            timeCB.setSelected(true);
        }

        if(emergencyRequired.equals("áno"))
        {
            emergencyCB.setSelected(true);
        }

    }

    private void setCheckBoxes()
    {
        timeCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    to.setDisable(false);
                    from.setDisable(false);
                } else {
                    to.clear();
                    from.clear();
                    to.setDisable(true);
                    from.setDisable(true);
                }
            }
        });

        overtimeCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    overtime.setDisable(false);
                } else {
                    overtime.clear();
                    overtime.setDisable(true);
                }
            }
        });

        unitsCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    units.setDisable(false);
                } else {
                    units.clear();
                    units.setDisable(true);
                }
            }
        });

        partCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    part.setDisable(false);
                } else {
                    part.clear();
                    part.setDisable(true);
                }
            }
        });

        emergencyCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    emergency.setDisable(false);
                } else {
                    emergency.getSelectionModel().clearSelection();
                    emergency.setDisable(true);
                }
            }
        });
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void onCloseClick(MouseEvent mouseEvent)
    {
        hoursInterface.removeHoursBox(vb, this);
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    public boolean checkFormular()
    {

        boolean flag = true;
        if(date.getValue()==null)
            flag=false;
        else if(!from.isDisable() && (from.getText() == null || from.getText().trim().isEmpty()))
            flag=false;
        else if(!to.isDisable() && (to.getText() == null || to.getText().trim().isEmpty()))
            flag=false;
        else if(!overtime.isDisable() && (overtime.getText() == null || overtime.getText().trim().isEmpty()))
            flag=false;
        else if(!units.isDisable() && (units.getText() == null || units.getText().trim().isEmpty()))
            flag=false;
        else if(!part.isDisable() && (part.getText() == null || part.getText().trim().isEmpty()))
            flag=false;
        else if(!emergency.isDisable() && emergency.getSelectionModel().isEmpty())
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené údaje.");
            label.setText("Nevyplené údaje.");
            label.setVisible(true);
        }

        return flag;
    }

    public boolean checkFormularTypes()
    {
        boolean flag = true;

        if(!from.isDisable() && !from.getText().matches("^((0?[0-9])|(1[0-9])|(2[0-3])):(00|15|30|45)$"))
            flag=false;
        else if(!to.isDisable() && !to.getText().matches("^((0?[0-9])|(1[0-9])|(2[0-3])):(00|15|30|45)$"))
            flag=false;
        else if(!overtime.isDisable() && !overtime.getText().matches("^((0?[0-9])|(1[0-9])|(2[0-3])):(00|15|30|45)$"))
            flag=false;
        else if(!units.isDisable() && !units.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(!part.isDisable() && !part.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
        }

        return flag;
    }

    public void setModelsFromInputs()
    {

        hoursD.setDate(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if(from.isDisable())
            hoursD.setFrom("NULL");
        else
            hoursD.setFrom(from.getText());

        if(to.isDisable())
            hoursD.setTo("NULL");
        else
            hoursD.setTo(to.getText());

        if(emergency.getSelectionModel().isEmpty())
            hoursD.setEmergencyType("NULL");
        else
            hoursD.setEmergencyType(emergency.getValue().toString());

        if(units.isDisable())
            hoursD.setUnitsDone("NULL");
        else
            hoursD.setUnitsDone(units.getText());

        if(part.isDisable())
            hoursD.setPartBase("NULL");
        else
            hoursD.setPartBase(part.getText());

        if(!overtime.getText().trim().isEmpty())
        {
            String ot = overtime.getText();
            String[] parts = ot.split(":");
            String part1 = parts[0];
            Double part2 = Double.parseDouble(parts[1]);
            part2 = part2/60.0;
            hoursD.setOverTime(part1+"."+part2);
        }
        else
        {
            hoursD.setOverTime("NULL");
        }

    }

}
