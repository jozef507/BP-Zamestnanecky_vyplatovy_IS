package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.LevelD;
import application.models.PlaceD;
import application.models.PositionD;
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

public class ControllerAddLevel
{
    public Button cancel;
    public Button create;
    public DatePicker from;
    public TextField number;
    public TextArea car;
    public Label label;


    private ControllerPageLegislationLevel controllerPageLegislationLevel;
    private LevelD levelD;

    private ArrayList<PlaceD> placeDS;
    private ArrayList<LevelD> levelDS;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setDatePicker();
        this.changeFocus();
    }

    public ControllerAddLevel(ControllerPageLegislationLevel controllerPageLegislationLevel) {
        this.controllerPageLegislationLevel = controllerPageLegislationLevel;
        this.levelD = new LevelD();
    }

    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Vytvorenie stupňa náročnosti", "Ste si istý že chcete vytvoriť nový stupeň náročnosti?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        this.setModelsFromInputs();
        try {
            this.createLevel();
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

        this.controllerPageLegislationLevel.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(number.getText() == null || number.getText().trim().isEmpty())
            flag=false;
        else if(car.getText() == null || car.getText().trim().isEmpty())
            flag=false;

        if(from.getValue()==null)
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené alebo nesprávne vyplnené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        if(!(number.getText().length()>0 && number.getText().length()<3))
            flag = false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return false;
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
        this.levelD.setNumber(number.getText());
        this.levelD.setCaracteristic(car.getText());
        this.levelD.setFrom(from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    private void createLevel() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("num", this.levelD.getNumber());
        ht.addParam("caracteristic", this.levelD.getCaracteristic());
        ht.addParam("from", this.levelD.getFrom());

        ht.sendPost("level/crt_lev", LoggedInUser.getToken(), LoggedInUser.getId());
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
        number.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                number.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }

}
