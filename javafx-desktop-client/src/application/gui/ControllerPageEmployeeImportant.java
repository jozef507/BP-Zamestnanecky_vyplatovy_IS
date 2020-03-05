package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.RelationOV;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class ControllerPageEmployeeImportant
{
    public VBox vb;
    public Text name;

    private String sname;
    private String id;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        name.setText(sname);
        try {
            setPage();
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

    public ControllerPageEmployeeImportant(String name, String id){
        this.id = id;
        this.sname = name;
    }

    private void setPage() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("employee/all_imp/"+this.id, LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for (int i = 0; i < json.getSize(); i++) {
            String finalS = json.getElement(i, "zdravotna_poistovna");
            String finalS1 = json.getElement(i, "mesto");
            String finalS2 = json.getElement(i, "ulica");
            String finalS3 = json.getElement(i, "cislo");
            String finalS4 = json.getElement(i, "pocet_deti_do_6_rokov");
            String finalS5 = json.getElement(i, "pocet_deti_nad_6_rokov");
            String finalS6 = json.getElement(i, "uplatnenie_nedzanitelnej_casti");
            String finalS7 = json.getElement(i, "poberatel_starobneho_dochodku");
            String finalS8 = json.getElement(i, "poberatel_invalidneho_dochodku");
            String finalS11 = json.getElement(i, "nice_date1");
            String finalS12 = json.getElement(i, "nice_date2");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/" + "page_employee_important_box" + ".fxml"));
            loader.setControllerFactory(c -> {
                return new ControllerPageEmployeeImportantBox(finalS, finalS1, finalS2, finalS3, finalS4, finalS5, finalS6, finalS7, finalS8, finalS11, finalS12);
            });
            HBox newPane = loader.load();
            vb.getChildren().addAll(newPane);
        }
    }
}
