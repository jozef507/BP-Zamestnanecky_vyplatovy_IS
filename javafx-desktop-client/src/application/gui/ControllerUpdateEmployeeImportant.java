package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControllerUpdateEmployeeImportant
{
    private String id;
    private String insComp;
    private String town;
    private String street;
    private String num;
    private String childUnder;
    private String childOver;
    private String part;
    private String retirement;
    private String invalidity;

    private ControllerPageEmployeeDetails c;

    public TextField insCompF, townF, streetF, numF, childUnderF,
            childOverF;
    public ComboBox partF, retirementF, invalidityF;
    public Button cancel;
    public Button ok;
    public Label label;
    public DatePicker begin;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.setDatePicker();
        this.setComboBoxes();
        this.setInputs();

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateEmployeeImp();
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) cancel.getScene().getWindow();
                stage.close();
            }
        });
    }

    public ControllerUpdateEmployeeImportant(String id, String insComp, String town, String street, String num,
                                             String childUnder, String childOver, String part, String retirement,
                                             String invalidity, ControllerPageEmployeeDetails c) {
        this.id = id;
        this.insComp = insComp;
        this.town = town;
        this.street = street;
        this.num = num;
        this.childUnder = childUnder;
        this.childOver = childOver;
        this.part = part;
        this.retirement = retirement;
        this.invalidity = invalidity;
        this.c = c;
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
        begin.setConverter(converter);
        begin.setPromptText("D.M.RRRR");
    }

    private void updateEmployeeImp()  {
        CustomAlert al = new CustomAlert("Uloženie zmien dôležitých údajov", "Ste si istý že chcete uložiť zmeny dôležitých údajov pracujúceho?", "", "Áno", "Nie");
        if(!al.showWait())
            return;

        String sInsComp = insCompF.getText();
        String sTown = townF.getText();
        String sStreet = streetF.getText();
        String sNum = numF.getText();
        String sChildUnder = childUnderF.getText();
        String sChildOver = childOverF.getText();
        String sPart = partF.getValue().toString();
        if(sPart.equals("áno"))
            sPart="1";
        else
            sPart="0";
        String sRetirement = retirementF.getValue().toString();
        if(sRetirement.equals("áno"))
            sRetirement="1";
        else
            sRetirement="0";
        String sInvalidity = invalidityF.getValue().toString();
        if(sInvalidity.equals("áno"))
            sInvalidity="1";
        else
            sInvalidity="0";

        String sEnd=null;
        String sBegin=null;
        try{
            LocalDate db = begin.getValue();
            LocalDate de = db.minusDays(1);
            sEnd = de.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            sBegin = db.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (NullPointerException e){
            sBegin="";
        }


        if(sInsComp.equals("") || sTown.equals("") || sStreet.equals("") || sNum.equals("")
                || sChildUnder.equals("")|| sChildOver.equals("")|| sBegin.equals(""))
        {
            label.setTextFill(Color.RED);
            label.setText("Vstupné textové polia nesmú ostať prázdne!");
            return;
        }

        HttpClientClass ht = new HttpClientClass();
        ht.addParam("id", this.id);
        ht.addParam("inscomp", sInsComp);
        ht.addParam("town", sTown);
        ht.addParam("street", sStreet);
        ht.addParam("num", sNum);
        ht.addParam("childunder", sChildUnder);
        ht.addParam("childover", sChildOver);
        ht.addParam("part", sPart);
        ht.addParam("retirement", sRetirement);
        ht.addParam("invalidity", sInvalidity);
        ht.addParam("end", sEnd);
        ht.addParam("begin", sBegin);

        try {
            ht.sendPost("employee/update_imp", LoggedInUser.getToken(), LoggedInUser.getId());
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

        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
        c.updateInfo();
    }

    private void setInputs()
    {
        insCompF.setText(this.insComp);
        townF.setText(this.town);
        streetF.setText(this.street);
        numF.setText(this.num);
        childUnderF.setText(this.childUnder);
        childOverF.setText(this.childOver);

        if(this.part.equals("nie"))
            partF.getSelectionModel().select(1);
        else
            partF.getSelectionModel().select(0);

        if(this.retirement.equals("nie"))
            retirementF.getSelectionModel().select(1);
        else
            retirementF.getSelectionModel().select(0);

        if(this.invalidity.equals("nie"))
            invalidityF.getSelectionModel().select(1);
        else
            invalidityF.getSelectionModel().select(0);
    }

    private void setComboBoxes()
    {
        partF.getItems().addAll(
                "áno",
                "nie"
        );

        retirementF.getItems().addAll(
                "áno",
                "nie"
        );

        invalidityF.getItems().addAll(
                "áno",
                "nie"
        );
    }
}
