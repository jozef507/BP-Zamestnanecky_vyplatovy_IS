package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.LevyD;
import application.models.SurchargeTypeD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControllerUpdateLevy
{
    public Button cancel;
    public Button create;
    public DatePicker to;
    public Label label;

    private ControllerPageLegislationLevy controllerPageLegislationLevy;
    private LevyD levyD;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setDatePicker();
    }

    public ControllerUpdateLevy(ControllerPageLegislationLevy controllerPageLegislationLevy, LevyD levyD) {
        this.controllerPageLegislationLevy = controllerPageLegislationLevy;
        this.levyD = new LevyD();
        this.levyD.setId(levyD.getId());
    }

    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Upravenie platnosti odvodu", "Ste si istý že chcete upraviť platnosť vybraného odvodu?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        this.setModelsFromInputs();
        try {
            this.updateLevy();
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

        this.controllerPageLegislationLevy.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(to.getValue()==null)
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
        this.levyD.setTo(to.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    private void updateLevy() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("id", this.levyD.getId());
        ht.addParam("to", this.levyD.getTo());

        ht.sendPost("levy/upd_levy", LoggedInUser.getToken(), LoggedInUser.getId());
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
        to.setConverter(converter);
        to.setPromptText("D.M.RRRR");
    }




}
