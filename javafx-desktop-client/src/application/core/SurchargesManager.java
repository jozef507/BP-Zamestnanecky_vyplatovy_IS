package application.core;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.PaymentDynamicComponentD;
import application.models.PaymentSurchargeD;
import application.models.SurchargeTypeD;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class SurchargesManager {
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS------------------------------------------------------*/
    GrossWageManager grossWageManager;
    ArrayList<PaymentSurchargeD> paymentSurchargeDS;
    BigDecimal surchargesTotal;

    ArrayList<SurchargeTypeD> currentSurchargeTypeDS;

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS---------------------------------------------------*/
    public SurchargesManager(GrossWageManager grossWageManager) {
        this.grossWageManager = grossWageManager;
        paymentSurchargeDS = new ArrayList<>();
        paymentSurchargeDS.add(new PaymentSurchargeD("night", grossWageManager));
        paymentSurchargeDS.add(new PaymentSurchargeD("saturday", grossWageManager));
        paymentSurchargeDS.add(new PaymentSurchargeD("sunday", grossWageManager));
        paymentSurchargeDS.add(new PaymentSurchargeD("feast", grossWageManager));
        paymentSurchargeDS.add(new PaymentSurchargeD("overtime", grossWageManager));
        currentSurchargeTypeDS = new ArrayList<>();
        surchargesTotal = new BigDecimal("0");
        downloadSurchargeTypes();
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------SETTERS/GETTERS--------------------------------------------------*/

    public GrossWageManager getGrossWageManager() {
        return grossWageManager;
    }

    public ArrayList<PaymentSurchargeD> getPaymentSurchargeDS() {
        return paymentSurchargeDS;
    }

    public BigDecimal getSurchargesTotal() {
        return surchargesTotal;
    }

    public ArrayList<SurchargeTypeD> getCurrentSurchargeTypeDS() {
        return currentSurchargeTypeDS;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS------------------------------------------------------*/
    private void downloadSurchargeTypes()
    {
        String monthID = grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMonthDS().get(0).getId();
        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendGet("surcharge/stbm/"+monthID, LoggedInUser.getToken(), LoggedInUser.getId());
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return ;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return ;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return ;
        }
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        for (int i = 0; i<json.getSize(); i++)
        {
            SurchargeTypeD surchargeTypeD = new SurchargeTypeD();
            surchargeTypeD.setId(json.getElement(i, "id"));
            surchargeTypeD.setName(json.getElement(i, "nazov"));
            surchargeTypeD.setDescription(json.getElement(i, "popis"));
            surchargeTypeD.setPart(json.getElement(i, "percentualna_cast"));
            surchargeTypeD.setBase(json.getElement(i, "pocitany_zo"));
            surchargeTypeD.setFrom(json.getElement(i, "platnost_od"));
            surchargeTypeD.setTo(json.getElement(i, "platnost_do"));
            currentSurchargeTypeDS.add(surchargeTypeD);
        }
    }

    public void calculateSurcharge(String appID, SurchargeTypeD surchargeTypeD)
    {
        PaymentSurchargeD paymentSurchargeD = this.paymentSurchargeDS.stream().filter(carnet -> appID.equals(carnet.getAppID())).findFirst().orElse(null);
        paymentSurchargeD.calculateWage(grossWageManager, surchargeTypeD);
        calculateSurchargesTotalTotal();
    }

    public PaymentSurchargeD getSurchargeTypeD(String appID)
    {
        PaymentSurchargeD paymentSurchargeD = this.paymentSurchargeDS.stream().filter(carnet -> appID.equals(carnet.getAppID())).findFirst().orElse(null);
        return paymentSurchargeD;
    }

    public void calculateSurchargesTotalTotal()
    {
        surchargesTotal = new BigDecimal("0");
        for ( PaymentSurchargeD p : this.paymentSurchargeDS)
        {
            if(p.getWage()!=null)
                surchargesTotal = surchargesTotal.add(new BigDecimal(p.getWage()));
        }
        surchargesTotal = surchargesTotal.setScale(2, RoundingMode.UP);
    }
}
