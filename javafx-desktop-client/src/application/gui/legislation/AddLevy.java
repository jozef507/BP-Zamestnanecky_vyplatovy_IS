package application.gui.legislation;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.LevyD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddLevy
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private PageLegislationLevy pageLegislationLevy;
    private LevyD levyD;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public AddLevy(PageLegislationLevy pageLegislationLevy) {
        this.pageLegislationLevy = pageLegislationLevy;
        this.levyD = new LevyD();
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
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
            this.createLevy();
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

        this.pageLegislationLevy.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private void createLevy() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("name", this.levyD.getName());
        ht.addParam("part_employee", this.levyD.getPartEe());
        ht.addParam("part_employer", this.levyD.getPartEr());
        ht.addParam("from", this.levyD.getFrom());

        ht.sendPost("levy/crt_levy", LoggedInUser.getToken(), LoggedInUser.getId());
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public Button cancel;
    public Button create;
    public DatePicker from;
    public TextField name, partEe, partEr;
    public Label label;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setDatePicker();
        this.changeFocus();
        this.setTextfieldLimit(name, 50);

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
        from.setConverter(converter);
        from.setPromptText("D.M.RRRR");
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
        else if(partEe.getText() == null || partEe.getText().trim().isEmpty())
            flag=false;
        else if(partEr.getText() == null || partEr.getText().trim().isEmpty())
            flag=false;

        if(from.getValue()==null)
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené údaje.");
            label.setText("Nevyplené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

       try {
            double d = Double.parseDouble(partEe.getText());
            if(!(d>0 && d<1)) flag = false;
            double e = Double.parseDouble(partEr.getText());
            if(!(e>0 && e<1)) flag = false;
        } catch (NumberFormatException nfe) {
           flag=false;
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
        this.levyD.setName(name.getText());
        this.levyD.setPartEe(partEe.getText());
        this.levyD.setPartEr(partEr.getText());
        this.levyD.setFrom(from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

}
