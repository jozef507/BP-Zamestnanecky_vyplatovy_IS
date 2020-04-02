package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.WageFormD;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AddRelationBox
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ObservableList<WageFormD> wageFormDs;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public AddRelationBox()
    {
        this.wageFormDs = FXCollections.observableArrayList();
        try {
            this.getWageForms();
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


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/

    public String getFormID()
    {
        String s = this.form.getValue().toString();
        for(int i = 0; i<this.wageFormDs.size(); i++)
        {
            if(this.wageFormDs.get(i).toComboboxString().equals(s))
                return this.wageFormDs.get(i).getId();
        }
        return "0";
    }

    private void getWageForms() throws InterruptedException, IOException, CommunicationException {
        ArrayList<String> places = new ArrayList<>();
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("wage/forms", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        for(int i=0; i<json.getSize(); i++)
        {
            this.wageFormDs.add(
                    new WageFormD(
                            json.getElement(i, "id"),
                            json.getElement(i, "nazov"),
                            json.getElement(i, "jednotka_vykonu"),
                            json.getElement(i, "skratka_jednotky")
                    )
            );
        }

    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public CheckBox employee;
    public CheckBox time, emergency;
    public ComboBox form;
    public ComboBox way;
    public TextField tarif;
    public TextField label;
    public DatePicker payDate;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    public void initialize()
    {
        this.setTextfieldLimit(label, 50);

        for(int i = 0; i<this.wageFormDs.size(); i++)
        {
            form.getItems().add(this.wageFormDs.get(i).toComboboxString());
        }

        way.getItems().addAll(
                "pravidelne",
                "nepravidelne"
        );
        way.getSelectionModel().selectFirst();

        setDatePicker();
        payDate.setDisable(true);
        way.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals("pravidelne") )
                payDate.setDisable(true);
            else
                payDate.setDisable(false);
        });

    }

    private void setTextfieldLimit(TextField textArea, int limit)
    {
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= limit ? change : null));
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
        payDate.setConverter(converter);
        payDate.setPromptText("D.M.RRRR");
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    public CheckBox getEmployee() {
        return employee;
    }

    public CheckBox getTime() {
        return time;
    }

    public ComboBox getForm() {
        return form;
    }

    public ComboBox getWay() {
        return way;
    }

    public TextField getTarif() {
        return tarif;
    }

    public TextField getLabel() {
        return label;
    }

    public DatePicker getPayDate() {
        return payDate;
    }



}
