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
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateEmployee
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private String employeeID;
    private String employeeName;
    private String employeeLastname;
    private String employeePhone;
    private String employeeBornnumber;
    private String employeeBorndate;
    private PageEmployeeDetails c;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    public UpdateEmployee(String employeeID, String employeeName, String employeeLastname,
                          String employeePhone, String employeeBornnumber, String employeeBorndate, PageEmployeeDetails c) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeLastname = employeeLastname;
        this.employeePhone = employeePhone;
        this.employeeBornnumber = employeeBornnumber;
        this.employeeBorndate = employeeBorndate;
        this.c = c;
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public TextField name;
    public TextField lastname;
    public TextField phone;
    public TextField bornnumber;
    public DatePicker borndate;
    public Button cancel;
    public Button ok;
    public Label label;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.changeFocus();
        this.setDatePicker();
        this.setInputs();
        this.setTextfieldLimit(name, 255);
        this.setTextfieldLimit(lastname, 255);
        this.setTextfieldLimit(phone, 10);
        this.setTextfieldLimit(bornnumber, 10);

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!checkFormular()) return;
                if(!checkFormularTypes()) return;

                CustomAlert al = new CustomAlert("Aktualizácia údajov pracujúceho", "Ste si istý že chcete uložiť zmeny dôležitých údajov pracujúceho?", "", "Áno", "Nie");
                if(!al.showWait())
                    return;

                updateEmployee();
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
        borndate.setConverter(converter);
        borndate.setPromptText("D.M.RRRR");
    }

    private void updateEmployee()  {

        String sName = name.getText();
        String sLastname = lastname.getText();
        String sPhone = phone.getText();
        String sBornnumber = bornnumber.getText();
        String sDate = borndate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        HttpClientClass ht = new HttpClientClass();
        ht.addParam("id", this.employeeID);
        ht.addParam("name", sName);
        ht.addParam("lastname", sLastname);
        ht.addParam("phone", sPhone);
        ht.addParam("bornnumber", sBornnumber);
        ht.addParam("borndate", sDate);

        try {
            ht.sendPost("employee/update", LoggedInUser.getToken(), LoggedInUser.getId());
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
        name.setText(this.employeeName);
        lastname.setText(this.employeeLastname);
        phone.setText(this.employeePhone);
        bornnumber.setText(this.employeeBornnumber);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        String date = this.employeeBorndate;
        LocalDate localDate = LocalDate.parse(date, formatter);
        borndate.setValue(localDate);
    }

    private void setTextfieldLimit(TextField textArea, int limit)
    {
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= limit ? change : null));
    }

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                name.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(name.getText() == null || name.getText().trim().isEmpty())
            flag=false;
        else if(lastname.getText() == null || lastname.getText().trim().isEmpty())
            flag=false;
        else if(phone.getText() == null || phone.getText().trim().isEmpty())
            flag=false;
        else if(bornnumber.getText() == null || bornnumber.getText().trim().isEmpty())
            flag=false;
        else if(borndate.getValue()==null)
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

        if(!phone.getText().matches("(^0\\d{9}$)|(^[+]421\\d{9}$)|(^[+]420\\d{9}$)|(^\\d{9}$)")) flag=false;
        else if(!bornnumber.getText().matches("(^\\d{10}$)")) flag=false;


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
