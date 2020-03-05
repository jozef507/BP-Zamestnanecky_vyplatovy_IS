package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControllerUpdateEmployee
{
    private String employeeID;
    private String employeeName;
    private String employeeLastname;
    private String employeePhone;
    private String employeeBornnumber;
    private String employeeBorndate;

    private ControllerPageEmployeeDetails c;

    public TextField name;
    public TextField lastname;
    public TextField phone;
    public TextField bornnumber;
    public DatePicker borndate;
    public Button cancel;
    public Button ok;
    public Label label;



    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        this.setDatePicker();
        this.setInputs();

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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

    public ControllerUpdateEmployee(String employeeID, String employeeName, String employeeLastname,
                                    String employeePhone, String employeeBornnumber, String employeeBorndate, ControllerPageEmployeeDetails c) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeLastname = employeeLastname;
        this.employeePhone = employeePhone;
        this.employeeBornnumber = employeeBornnumber;
        this.employeeBorndate = employeeBorndate;
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
        borndate.setConverter(converter);
        borndate.setPromptText("D.M.RRRR");
    }

    private void updateEmployee()  {

        String sName = name.getText();
        String sLastname = lastname.getText();
        String sPhone = phone.getText();
        String sBornnumber = bornnumber.getText();
        String sDate = borndate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(sName.equals("") || sLastname.equals("") || sPhone.equals("") || sBornnumber.equals("") || sDate.equals(""))
        {
            label.setTextFill(Color.RED);
            label.setText("Vstupné textové polia nesmú ostať prázdne!");
            return;
        }

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
}
