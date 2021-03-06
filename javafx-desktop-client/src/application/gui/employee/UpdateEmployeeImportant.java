package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import javafx.beans.property.SimpleBooleanProperty;
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

public class UpdateEmployeeImportant
{

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private String id;
    private String insComp;
    private String town;
    private String street;
    private String num;
    private String childUnder;
    private String childOver;
    private String employeeID;
    private PageEmployeeImportant c;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public UpdateEmployeeImportant(String id, String insComp, String town, String street, String num,
                                   String childUnder, String childOver, PageEmployeeImportant c)
    {
        this.id = id;
        this.insComp = insComp;
        this.town = town;
        this.street = street;
        this.num = num;
        this.childUnder = childUnder;
        this.childOver = childOver;
        this.c = c;
        this.employeeID = c.getEmployeeID();
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private void updateEmployeeImp()  {

        String sInsComp = insCompF.getText();
        String sTown = townF.getText();
        String sStreet = streetF.getText();
        String sNum = numF.getText();
        String sChildUnder = childUnderF.getText();
        String sChildOver = childOverF.getText();

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
        ht.addParam("end", sEnd);
        ht.addParam("begin", sBegin);
        ht.addParam("employee", this.employeeID);

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



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public TextField insCompF, townF, streetF, numF, childUnderF,
            childOverF;
    public Button cancel;
    public Button ok;
    public Label label;
    public DatePicker begin;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.changeFocus();
        this.setDatePicker();
        this.setInputs();

        this.setTextfieldLimit(insCompF, 30);
        this.setTextfieldLimit(townF, 255);
        this.setTextfieldLimit(streetF, 255);
        this.setTextfieldLimit(numF, 255);
        this.setTextfieldLimit(childOverF, 2);
        this.setTextfieldLimit(childUnderF, 2);

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!checkFormular()) return;
                if(!checkFormularTypes()) return;


                CustomAlert al = new CustomAlert("Uloženie zmien dôležitých údajov", "Ste si istý že chcete uložiť zmeny dôležitých údajov pracujúceho?", "", "Áno", "Nie");
                if(!al.showWait())
                    return;

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

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        insCompF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                insCompF.getParent().requestFocus();
                firstTime.setValue(false);
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
        begin.setConverter(converter);
        begin.setPromptText("D.M.RRRR");
    }

    private void setTextfieldLimit(TextField textArea, int limit)
    {
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= limit ? change : null));
    }

    private void setInputs()
    {
        insCompF.setText(this.insComp);
        townF.setText(this.town);
        streetF.setText(this.street);
        numF.setText(this.num);
        childUnderF.setText(this.childUnder);
        childOverF.setText(this.childOver);
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(insCompF.getText() == null || insCompF.getText().trim().isEmpty())
            flag=false;
        else if(townF.getText() == null || townF.getText().trim().isEmpty())
            flag=false;
        else if(streetF.getText() == null || streetF.getText().trim().isEmpty())
            flag=false;
        else if(numF.getText() == null || numF.getText().trim().isEmpty())
            flag=false;
        else if(childUnderF.getText() == null || childUnderF.getText().trim().isEmpty())
            flag=false;
        else if(childOverF.getText() == null || childOverF.getText().trim().isEmpty())
            flag=false;
        else if(begin.getValue()==null)
            flag=false;


        if(!flag)
        {
            System.out.println("Nevyplené alebo nesprávne vyplnené údaje.");
            label.setText("Nevyplené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        if(!childOverF.getText().matches("(^\\d\\d?$)")) flag=false;
        else if(!childUnderF.getText().matches("(^\\d\\d?$)")) flag=false;
        else if(!numF.getText().matches("(^\\d*$)")) flag=false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return flag;
        }
        return flag;
        /*try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }*/
    }
}
