package application.pdf;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.IOException;
import java.util.ArrayList;

public class PaymentPDF
{
    ArrayList<PaymentD> paymentDS;
    ArrayList<JsonArrayClass> jsons;

    ArrayList<PaymnetTable> paymnetTables;

    GeneratePdf generatePdf;

    public static String path;


    public PaymentPDF(String name, ArrayList<PaymentD> inputPaymentDS)
    {

        try {
            downloadPaymentsDetails(inputPaymentDS);
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

        try {
            processPaymentDS();
            processPaymentTables();

            setDocument(path+name);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void downloadPaymentsDetails(ArrayList<PaymentD> inputPaymentDS) throws InterruptedException, IOException, CommunicationException
    {
        jsons = new ArrayList<>();
        for (PaymentD p: inputPaymentDS)
        {
            HttpClientClass ht = new HttpClientClass();
            ht.sendGet("payment/pmnt/"+p.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
            JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
            jsons.add(json);
        }
    }

    private void processPaymentDS() throws Exception
    {
        paymentDS = new ArrayList<>();
        for (JsonArrayClass j: jsons)
        {
            processPayment(j);
        }
    }

    private void processPayment(JsonArrayClass json)
    {
        int months = Integer.parseInt(json.getElement(0, "mesiace"));
        int basics = Integer.parseInt(json.getElement(0, "zakladne"));
        int dynamics = Integer.parseInt(json.getElement(0, "dynamicke"));
        int compensations = Integer.parseInt(json.getElement(0, "nahrady"));
        int surcharges = Integer.parseInt(json.getElement(0, "priplatky"));
        int others = Integer.parseInt(json.getElement(0, "ine"));
        int levies = Integer.parseInt(json.getElement(0, "odvody"));
        int deductions = Integer.parseInt(json.getElement(0, "zrazky"));

        PaymentD paymentD = new PaymentD();

        paymentD.setId(json.getElement(1, "id"));
        paymentD.setHoursFund(json.getElement(1, "fond_hodin"));
        paymentD.setDaysFund(json.getElement(1, "fond_dni"));
        paymentD.setHoursWorked(json.getElement(1, "odpracovane_hodiny"));
        paymentD.setDaysWorked(json.getElement(1, "odpracovane_dni"));
        paymentD.setGrossWage(json.getElement(1, "hruba_mzda"));
        paymentD.setAssessmentBasis(json.getElement(1, "vymeriavaci_zaklad"));
        paymentD.setEmployeeEnsurence(json.getElement(1, "poistne_zamestnanca"));
        paymentD.setNonTaxableWage(json.getElement(1, "nezdanitelna_mzda"));
        paymentD.setTaxableWage(json.getElement(1, "zdanitelna_mzda"));
        paymentD.setTaxAdvances(json.getElement(1, "preddavky_na_dan"));
        paymentD.setTaxBonus(json.getElement(1, "danovy_bonus"));
        paymentD.setNetWage(json.getElement(1, "cista_mzda"));
        paymentD.setWorkPrice(json.getElement(1, "cena_prace"));
        paymentD.setEmployerLevies(json.getElement(1, "odvody_zamestnavatela"));
        paymentD.setEmployeeLevies(json.getElement(1, "odvody_dan_zamestnanca"));
        paymentD.setLeviesSum(json.getElement(1, "odvody_dan_spolu"));
        paymentD.setTotal(json.getElement(1, "k_vyplate"));
        paymentD.setOnAccount(json.getElement(1, "na_ucet"));
        paymentD.setCash(json.getElement(1, "v_hotovosti"));
        paymentD.setEmployeeImportantID(json.getElement(1, "dolezite_udaje_pracujuceho"));
        paymentD.setWhoCreatedID(json.getElement(1, "vypracoval_pracujuci"));
        paymentD.setMinimumWageID(json.getElement(1, "minimalna_mzda"));
        paymentD.setWageConstantsID(json.getElement(1, "mzdove_konstanty"));
        paymentD.setYearAverageWage(json.getElement(1, "priemerny_zarobok"));

        paymentD.setPositionName(json.getElement(2, "pozicia"));
        paymentD.setPlaceName(json.getElement(2, "pracovisko"));
        paymentD.setRelationType(json.getElement(2, "typ"));
        paymentD.setWeekTime(json.getElement(2, "dohodnuty_tyzdenny_pracovny_cas"));
        paymentD.setEmployeeLastnameName(json.getElement(2, "priezvisko")+" "+json.getElement(2, "meno"));

        int index = 3;
        String monthss = "";
        for (int i = index; i < index+months; i++)
        {
            if(i==index)
                monthss += json.getElement(i, "poradie_mesiaca")+"/"
                        +json.getElement(i, "rok");
            else
                monthss += ","+json.getElement(i, "poradie_mesiaca")+"/"
                        +json.getElement(i, "rok");
            paymentD.setYearHoliday(json.getElement(i, "narok_na_dovolenku"));
            paymentD.setYearHolidayLastYear(json.getElement(i, "narok_na_dovolenku_z_minuleho_roka"));
            paymentD.setYearHolideyTaken(json.getElement(i, "vycerpana_dovolenka"));
        }
        paymentD.setMonthNumber(monthss);
        index+=months;

        ArrayList<PaymentBasicComponentD> basicComponentDS = new ArrayList<>();
        for (int i = index; i < index+basics; i++)
        {
            PaymentBasicComponentD paymentBasicComponentD = new PaymentBasicComponentD();
            paymentBasicComponentD.setWorkedUnits(json.getElement(i, "mnozstvo_jednotiek"));
            paymentBasicComponentD.setWageForUnits(json.getElement(i, "suma_za_mnozstvo"));
            paymentBasicComponentD.setWageCharacteristic(json.getElement(i, "popis"));
            paymentBasicComponentD.setWageForm(json.getElement(i, "nazov"));
            paymentBasicComponentD.setWageFormUnitShort(json.getElement(i, "skratka_jednotky"));
            paymentBasicComponentD.setWageTarif(json.getElement(i, "tarifa_za_jednotku_mzdy"));
            basicComponentDS.add(paymentBasicComponentD);

        }
        paymentD.setBasicComponentDS(basicComponentDS);
        index+=basics;

        ArrayList<PaymentDynamicComponentD> dynamicComponentDS = new ArrayList<>();
        for (int i = index;i < index+dynamics; i++)
        {
            PaymentDynamicComponentD paymentDynamicComponentD = new PaymentDynamicComponentD();
            paymentDynamicComponentD.setType(json.getElement(i, "typ"));
            paymentDynamicComponentD.setCharacteristic(json.getElement(i, "popis"));
            paymentDynamicComponentD.setWage(json.getElement(i, "suma"));
            dynamicComponentDS.add(paymentDynamicComponentD);
        }
        index+=dynamics;
        paymentD.setDynamicComponentDS(dynamicComponentDS);

        ArrayList<PaymentWageCompensationD> compensationDS = new ArrayList<>();
        for (int i = index;i < index+compensations; i++)
        {
            PaymentWageCompensationD paymentWageCompensationD = new PaymentWageCompensationD();
            paymentWageCompensationD.setReason(json.getElement(i, "typ"));
            paymentWageCompensationD.setDays(json.getElement(i, "pocet_dni"));
            paymentWageCompensationD.setHours(json.getElement(i, "mnozstvo_jednotiek"));
            paymentWageCompensationD.setWage(json.getElement(i, "suma"));
            compensationDS.add(paymentWageCompensationD);
        }
        index+=compensations;
        paymentD.setWageCompensationDS(compensationDS);

        ArrayList<PaymentSurchargeD> surchargeDS = new ArrayList<>();
        for (int i = index;i < index+surcharges; i++)
        {
            PaymentSurchargeD surchargeD = new PaymentSurchargeD();
            surchargeD.setSurchargeName(json.getElement(i, "nazov"));
            surchargeD.setHours(json.getElement(i, "mnozstvo_jednotiek"));
            surchargeD.setWage(json.getElement(i, "suma"));
            surchargeDS.add(surchargeD);
        }
        index+=surcharges;
        paymentD.setSurchargeDS(surchargeDS);

        ArrayList<PaymentOtherComponentD> otherComponentDS = new ArrayList<>();
        for (int i = index;i < index+others; i++)
        {
            PaymentOtherComponentD otherComponentD = new PaymentOtherComponentD();
            otherComponentD.setName(json.getElement(i, "nazov"));
            otherComponentD.setWage(json.getElement(i, "suma"));
            otherComponentDS.add(otherComponentD);
        }
        index+=others;
        paymentD.setOtherComponentDS(otherComponentDS);

        ArrayList<LevyD> levyDS = new ArrayList<>();
        for (int i = index;i < index+levies; i++)
        {
            LevyD levyD = new LevyD();
            levyD.setName(json.getElement(i, "nazov"));
            levyD.setAssessmentBasis(json.getElement(i, "vymeriavaci_zaklad"));
            levyD.setEmployeeSum(json.getElement(i, "suma_zamestnanec"));
            levyD.setEmployerSum(json.getElement(i, "suma_zamestnavatel"));
            levyDS.add(levyD);
        }
        index+=levies;
        paymentD.setLevyDS(levyDS);

        ArrayList<PaymentDeductionD> paymentDeductionDS = new ArrayList<>();
        for (int i = index;i < index+deductions; i++)
        {
            PaymentDeductionD deductionD = new PaymentDeductionD();
            deductionD.setName(json.getElement(i, "nazov"));
            deductionD.setSum(json.getElement(i, "suma"));
            paymentDeductionDS.add(deductionD);
        }
        paymentD.setDeductionDS(paymentDeductionDS);

        paymentDS.add(paymentD);
    }

    private void processPaymentTables() throws Exception
    {
        paymnetTables = new ArrayList<>();
        for(PaymentD p : paymentDS)
        {
            PaymnetTable paymnetTable = new PaymnetTable(p);
            paymnetTables.add(paymnetTable);
        }
    }

    private void setDocument(String name) throws Exception
    {
        generatePdf = new GeneratePdf(name);
        generatePdf.setPageAndOpen(PageSize.A4, 10, 10, 10, 10);
        generatePdf.setAttributes("JOZEF", "Vyplata", "Návod na to ako generovať výplatu");

        Document doc = generatePdf.getDocument();

        LineSeparator s = new LineSeparator();
        s.setLineWidth(0.1f);
        for(PaymnetTable paymnetTable : paymnetTables)
        {
            doc.add(paymnetTable.getMainTable());
            doc.add(s);
        }

        generatePdf.close();
    }
}
