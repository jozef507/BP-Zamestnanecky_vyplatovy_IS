package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.LevelD;
import application.models.MinimumWageD;
import application.models.PlaceD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ControllerAddMinimumwage
{
    public Button cancel;
    public Button create;
    public DatePicker from;
    public TextField hour, month;
    public ComboBox<String> level;
    public Label label;

    private ArrayList<LevelD> levelDS;

    private ControllerPageLegislationMinimum controllerPageLegislationMinimum;
    private MinimumWageD minimumWageD;


    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setDatePicker();
        setComboboxes();
        this.changeFocus();
    }

    public ControllerAddMinimumwage(ControllerPageLegislationMinimum controllerPageLegislationMinimum) {
        this.controllerPageLegislationMinimum = controllerPageLegislationMinimum;
        this.minimumWageD = new MinimumWageD();
        try {
            this.levelDS = getLevels();
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

    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Vytvorenie minimalnej mzdy", "Ste si istý že chcete vytvoriť novú minimálnu mzdu?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        this.setModelsFromInputs();
        try {
            this.createMinimumwage();
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

        this.controllerPageLegislationMinimum.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(hour.getText() == null || hour.getText().trim().isEmpty())
            flag=false;
        else if(month.getText() == null || month.getText().trim().isEmpty())
            flag=false;

        if(level.getSelectionModel().isEmpty())
            flag=false;

        if(from.getValue()==null)
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené alebo nesprávne vyplnené údaje.");
            label.setText("Nevyplené alebo nesprávne vyplnené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        try {
            double d = Double.parseDouble(hour.getText());
            d = Double.parseDouble(month.getText());
        } catch (NumberFormatException nfe) {
            flag = false;
        }

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private void setModelsFromInputs()
    {
        this.minimumWageD.setHourValue(hour.getText());
        this.minimumWageD.setMonthValue(month.getText());
        this.minimumWageD.setFrom(from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        String choosenLevel = level.getValue().toString();
        for(LevelD l:this.levelDS)
        {
            if(choosenLevel.equals(l.getComboboxString()))
                this.minimumWageD.setLevelID(l.getId());
        }
    }

    private void createMinimumwage() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("hourvalue", this.minimumWageD.getHourValue());
        ht.addParam("monthvalue", this.minimumWageD.getMonthValue());
        ht.addParam("from", this.minimumWageD.getFrom());
        ht.addParam("levelid", this.minimumWageD.getLevelID());

        ht.sendPost("minwage/crt_minwg", LoggedInUser.getToken(), LoggedInUser.getId());
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


    private void setComboboxes()
    {
        for (LevelD l:this.levelDS)
        {
            level.getItems().add(l.getComboboxString());
        }
    }


    private ArrayList<LevelD> getLevels() throws IOException, InterruptedException, CommunicationException {
        ArrayList<LevelD> levels = new ArrayList<>();
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("position/lvls", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        for(int i=0; i<json.getSize(); i++)
        {
            LevelD levelD = new LevelD();
            levelD.setId(json.getElement(i, "id"));
            levelD.setNumber(json.getElement(i, "cislo_stupna"));
            levelD.setCaracteristic(json.getElement(i, "charakteristika"));
            levelD.setFrom(json.getElement(i, "platnost_od"));
            levelD.setTo(json.getElement(i, "platnost_do"));
            levels.add(levelD);
        }
        return levels;
    }

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        hour.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                hour.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }
}
