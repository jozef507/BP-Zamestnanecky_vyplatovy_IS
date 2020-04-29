package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PageEmployeeDetailsRelationBox
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ConditionsD conditionsD;
    private String Splace;
    private String Sposition;
    private NextConditionsD nextConditionsD;
    private ArrayList<WageD> wageDS;
    private ArrayList<WageFormD> wageFormDS;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageEmployeeDetailsRelationBox(ConditionsD conditionsD, String splace, String sposition, NextConditionsD nextConditionsD) {
        this.conditionsD = conditionsD;
        this.Splace = splace;
        this.Sposition = sposition;
        this.nextConditionsD = nextConditionsD;
        this.wageDS = new ArrayList<>();
        this.wageFormDS = new ArrayList<>();
        try {
            this.setWageDS();
            this.setWageFormDS();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
        }
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private void setWageFormDS() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("wage/forms", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        for(int i=0; i<json.getSize(); i++)
        {
            this.wageFormDS.add(
                    new WageFormD(
                            json.getElement(i, "id"),
                            json.getElement(i, "nazov"),
                            json.getElement(i, "jednotka_vykonu"),
                            json.getElement(i, "skratka_jednotky")
                    )
            );
        }
    }

    private void setWageDS() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("wage/wage_of_con/"+this.conditionsD.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for(int i = 0; i<json.getSize(); i++)
        {
            WageD wageD = new WageD();
            wageD.setId(json.getElement(i, "id"));
            wageD.setLabel(json.getElement(i, "popis"));
            wageD.setEmployeeEnter(json.getElement(i, "vykon_eviduje_zamestnanec"));
            wageD.setTarif(json.getElement(i, "tarifa_za_jednotku_mzdy"));
            wageD.setPayWay(json.getElement(i, "sposob_vyplacania"));
            wageD.setPayDate(json.getElement(i, "datum_vyplatenia"));
            wageD.setTimeImportant(json.getElement(i, "nutne_evidovanie_casu"));
            wageD.setEmergencyImportant(json.getElement(i, "mozne_evidovanie_pohotovosti"));
            wageD.setWageFormID(json.getElement(i, "forma_mzdy"));
            wageD.setConditionsID(json.getElement(i, "podmienky_pracovneho_vztahu"));
            this.wageDS.add(wageD);
        }
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public VBox vb;
    public HBox hb;
    public Text isMain;
    public Text isUniform;
    public Text testTime;
    public Text hollidayTime;
    public Text weekTIme;
    public Text sackTime;
    public Text place;
    public Text position;
    public VBox vbWage;
    public Text from;
    public Text to;
    public Text apWeekTime, bank, bankPart, dayTime,
            deductableItem, disabled, exemption, iban,
            invalidity40, invalidity70, premature,
            retirement, taxBonus, taxFree;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize()
    {
        this.setTexts();
        this.setBoxes();
    }

    private void setBoxes()
    {
        for(int i = 0; i<this.wageDS.size(); i++)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PageEmployeeDetailsRelationBoxBox.fxml"));
            int ii = i;
            loader.setControllerFactory(c -> {
                return new PageEmployeeDetailsRelationBoxBox(this.wageDS.get(ii), this.wageFormDS);
            });
            HBox newPane = null;
            try {
                newPane = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //ControllerPageEmployeeDetailsRelationBoxBox c = loader.getController();
            vbWage.getChildren().add(newPane);
        }
    }

    private void setTexts()
    {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.Y");
        LocalDate dt = LocalDate.parse(this.conditionsD.getFrom());
        from.setText(formatter.format(dt));

        String sto = null;
        if(this.conditionsD.getTo()!=null)
        {
            dt = LocalDate.parse(this.conditionsD.getTo());
            sto = formatter.format(dt);
        }
        to.setText(sto);

        deductableItem.setText("nie");
        iban.setText(conditionsD.getIban());
        try {
            bankPart.setText((Integer.parseInt(conditionsD.getBankPart()) * 100 + "%"));
        } catch (NumberFormatException e){
            ;
        }
        bank.setText(getStringBoolVal(conditionsD.getBank()));
        disabled.setText(getStringBoolVal(conditionsD.getDisabled()));
        exemption.setText(getStringBoolVal(conditionsD.getExemption()));

        invalidity40.setText(getStringBoolVal(conditionsD.getInvalidity40()));
        invalidity70.setText(getStringBoolVal(conditionsD.getInvalidity70()));
        premature.setText(getStringBoolVal(conditionsD.getPremature()));
        retirement.setText(getStringBoolVal(conditionsD.getRetirement()));
        taxBonus.setText(getStringBoolVal(conditionsD.getTaxBonus()));
        taxFree.setText(getStringBoolVal(conditionsD.getTaxFree()));


        if(this.nextConditionsD!=null)
        {
            isMain.setText(getStringBoolVal(nextConditionsD.getIsMain()));
            isUniform.setText(getStringBoolVal(nextConditionsD.getIsWeekTimeUniform()));
            testTime.setText(nextConditionsD.getTestTime());
            hollidayTime.setText(nextConditionsD.getHollidayTime());
            weekTIme.setText(nextConditionsD.getWeekTime());
            sackTime.setText(nextConditionsD.getSackTime());

            apWeekTime.setText(nextConditionsD.getApWeekTime());
            dayTime.setText(nextConditionsD.getDayTime());
            deductableItem.setText(getStringBoolVal(nextConditionsD.getDeductableItem()));
        }

        place.setText(Splace);
        position.setText(Sposition);
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/
    private String getStringBoolVal(String string)
    {
        if(string.equals("1"))
            return "áno";
        else
            return "nie";
    }
}
