package application.core;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.gui.payment.create.PaneCreate;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class PaymentManager
{

    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS------------------------------------------------------*/
    private PaneCreate paneCreate;

    private WageConstantsD wageConstantsD;
    private MinimumWageD basicMinimumWageD;
    private EmployeeConditionsManager employeeConditionsManager;

    private PaymentD paymentD;
    private GrossWageManager grossWageManager;
    private NetWageManager netWageManager;

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS---------------------------------------------------*/
    public PaymentManager(PaneCreate paneCreate, String monthID) {
        if(!setInitialData(monthID)) return;
        this.paneCreate = paneCreate;
        this.paymentD = new PaymentD();
        this.grossWageManager = new GrossWageManager(this);
        this.netWageManager = new NetWageManager(this);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------SETTERS/GETTERS--------------------------------------------------*/

    public WageConstantsD getWageConstantsD() {
        return wageConstantsD;
    }

    public EmployeeConditionsManager getEmployeeConditionsManager() {
        return employeeConditionsManager;
    }

    public PaymentD getPaymentD() {
        return paymentD;
    }

    public GrossWageManager getGrossWageManager() {
        return grossWageManager;
    }

    public NetWageManager getNetWageManager() {
        return netWageManager;
    }

    public MinimumWageD getBasicMinimumWageD() {
        return basicMinimumWageD;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS------------------------------------------------------*/
    private boolean setInitialData(String monthID)
    {
        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendGet("payment/nffr_mplpmnt/" + monthID, LoggedInUser.getToken(), LoggedInUser.getId());
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return false;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return false;
        }

        try{
            JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

            int basicWagesNum = Integer.parseInt(json.getElement(0, "pocet_miezd"));
            int unclosedMonthsNum = Integer.parseInt(json.getElement(0, "pocet_neuzatvorenach_mesiacov"));

            setConstantsData(json, 1);
            setBasicMinimumWageData(json, 2);
            setEmployeeConditionsManagerData(json, 3);
            for(int i = 4; i<4+basicWagesNum; i++){
                setEmployeeConditionsManagerWagesData(json, i);
            }
            for(int i = 4+basicWagesNum; i<4+basicWagesNum+unclosedMonthsNum; i++){
                setEmployeeConditionsManagerYearMonthData(json, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Chyba",
                    "", e.getMessage());
            return false;
        }

        return true;
    }

    private void setConstantsData(JsonArrayClass json, int row)
    {
        this.wageConstantsD = new WageConstantsD();
        this.wageConstantsD.setId(json.getElement(row, "id"));
        this.wageConstantsD.setBasicWeekTime(json.getElement(row, "zakladny_tyzdenny_pracovny_cas"));
        this.wageConstantsD.setMaxAssessmentBasis(json.getElement(row, "max_vymeriavaci_zaklad"));
        this.wageConstantsD.setMinAssessmentBasis(json.getElement(row, "min_vymeriavaci_zaklad"));
        this.wageConstantsD.setMaxDayAssessmentBasis(json.getElement(row, "max_denny_vymeriavaci_zaklad"));
        this.wageConstantsD.setTaxBonusOver6(json.getElement(row, "danovy_bonus_na_dieta_nad_6"));
        this.wageConstantsD.setTaxBonusUnder6(json.getElement(row, "danovy_bonus_na_dieta_pod_6"));
        this.wageConstantsD.setNonTaxablePart(json.getElement(row, "NCZD_na_danovnika"));
        this.wageConstantsD.setSubsistenceMinimumForAdvances(json.getElement(row, "nasobok_zivotneho_minima_pre_preddavok"));
        this.wageConstantsD.setNightFrom(json.getElement(row, "zaciatok_nocnej_prace"));
        this.wageConstantsD.setNightTo(json.getElement(row, "koniec_nocnej_prace"));
        this.wageConstantsD.setMaxAssessmentBasisOP(json.getElement(row, "max_vymeriavaci_zaklad_pre_OP"));
        this.wageConstantsD.setMaxOP(json.getElement(row, "max_vyska_OP"));
        this.wageConstantsD.setLimitOV(json.getElement(row, "hranica_prekrocenia_OV"));
        this.wageConstantsD.setFrom(json.getElement(row, "platnost_od"));
        this.wageConstantsD.setTo(json.getElement(row, "platnost_do"));
    }

    private void setBasicMinimumWageData(JsonArrayClass json, int row)
    {
        this.basicMinimumWageD = new MinimumWageD();
        this.basicMinimumWageD.setId(json.getElement(row, "id"));
        this.basicMinimumWageD.setFrom(json.getElement(row, "platnost_od"));
        this.basicMinimumWageD.setTo(json.getElement(row, "platnost_do"));
        this.basicMinimumWageD.setHourValue(json.getElement(row, "hodinova_hodnota"));
        this.basicMinimumWageD.setMonthValue(json.getElement(row, "mesacna_hodnota"));
        this.basicMinimumWageD.setLevelID(json.getElement(row, "stupen_narocnosti"));
    }

    private void setEmployeeConditionsManagerData(JsonArrayClass json, int row)
    {
        this.employeeConditionsManager = new EmployeeConditionsManager();

        ImportantD importantD = new ImportantD();
        importantD.setId(json.getElement(row, "dup_id"));
        importantD.setInsComp(json.getElement(row, "zdravotna_poistovna"));
        importantD.setTown(json.getElement(row, "dup_mesto"));
        importantD.setStreet(json.getElement(row, "dup_ulica"));
        importantD.setNum(json.getElement(row, "dup_cislo"));
        importantD.setChildUnder(json.getElement(row, "pocet_deti_do_6_rokov"));
        importantD.setChildOver(json.getElement(row, "pocet_deti_nad_6_rokov"));
        importantD.setFrom(json.getElement(row, "platnost_od"));
        importantD.setTo(json.getElement(row, "platnost_do"));
        importantD.setEmployeeID(json.getElement(row, "pracujuci"));
        this.employeeConditionsManager.setImportantD(importantD);

        EmployeeD employeeD = new EmployeeD();
        employeeD.setId(json.getElement(row, "p_id"));
        employeeD.setName(json.getElement(row, "meno"));
        employeeD.setLastname(json.getElement(row, "priezvisko"));
        employeeD.setBornNum(json.getElement(row, "rodne_cislo"));
        employeeD.setBornDate(json.getElement(row, "p_datum_narodenia"));
        employeeD.setPkID(json.getElement(row, "prihlasovacie_konto"));
        this.employeeConditionsManager.setEmployeeD(employeeD);

        RelationD relationD = new RelationD();
        relationD.setId(json.getElement(row, "pv_id"));
        relationD.setFrom(json.getElement(row, "pv_datum_vzniku"));
        relationD.setTo(json.getElement(row, "pv_datum_vyprsania"));
        relationD.setType(json.getElement(row, "typ"));
        this.employeeConditionsManager.setRelationD(relationD);

        ConditionsD conditionsD = new ConditionsD();
        conditionsD.setId(json.getElement(row, "ppv_id"));
        conditionsD.setFrom(json.getElement(row, "ppv_platnost_od"));
        conditionsD.setTo(json.getElement(row, "ppv_platnost_do"));
        conditionsD.setRelationID(json.getElement(row, "pracovny_vztah"));
        conditionsD.setPositionID(json.getElement(row, "pozicia"));
        conditionsD.setNextConditionsID(json.getElement(row, "dalsie_podmienky"));
        conditionsD.setTaxFree(json.getElement(row, "uplatnenie_nezdanitelnej_casti"));
        conditionsD.setTaxBonus(json.getElement(row, "uplatnenie_danoveho_bonusu"));
        conditionsD.setDisabled(json.getElement(row, "drzitel_tzp_preukazu"));
        conditionsD.setRetirement(json.getElement(row, "poberatel_starobneho_vysluhoveho_dochodku"));
        conditionsD.setInvalidity40(json.getElement(row, "poberatel_invalidneho_vysluhoveho_dochodku_nad_40"));
        conditionsD.setInvalidity70(json.getElement(row, "poberatel_invalidneho_vysluhoveho_dochodku_nad_70"));
        conditionsD.setPremature(json.getElement(row, "poberatel_predcasneho_dochodku"));
        conditionsD.setExemption(json.getElement(row, "uplatnenie_odvodovej_vynimky"));
        conditionsD.setBank(json.getElement(row, "posielanie_vyplaty_na_ucet"));
        conditionsD.setBankPart(json.getElement(row, "cast_z_vyplaty_na_ucet"));
        conditionsD.setIban(json.getElement(row, "iban_uctu_pre_vyplatu"));
        this.employeeConditionsManager.setConditionsD(conditionsD);

        NextConditionsD nextConditionsD = new NextConditionsD();
        nextConditionsD.setId(json.getElement(row, "dp_id"));
        nextConditionsD.setIsMain(json.getElement(row, "je_hlavny_pp"));
        nextConditionsD.setHollidayTime(json.getElement(row, "vymera_dovolenky"));
        nextConditionsD.setWeekTime(json.getElement(row, "dohodnuty_tyzdenny_pracovny_cas"));
        nextConditionsD.setIsWeekTimeUniform(json.getElement(row, "je_pracovny_cas_rovnomerny"));
        nextConditionsD.setTestTime(json.getElement(row, "skusobvna_doba"));
        nextConditionsD.setSackTime(json.getElement(row, "vypovedna_doba"));
        nextConditionsD.setApWeekTime(json.getElement(row, "ustanoveny_tyzdenny_pracovny_cas"));
        nextConditionsD.setDayTime(json.getElement(row, "dohodnuty_denny_pracovny_cas"));
        nextConditionsD.setDeductableItem(json.getElement(row, "uplatnenie_odpocitatelnej_polozky"));
        this.employeeConditionsManager.setNextConditionsD(nextConditionsD);

        PositionD positionD = new PositionD();
        positionD.setId(json.getElement(row, "p2_id"));
        positionD.setName(json.getElement(row, "p2_nazov"));
        positionD.setCharacteristic(json.getElement(row, "p2_charakteristika"));
        this.employeeConditionsManager.setPositionD(positionD);

        PlaceD placeD = new PlaceD();
        placeD.setId(json.getElement(row, "p3_id"));
        placeD.setName(json.getElement(row, "p3_nazov"));
        this.employeeConditionsManager.setPlaceD(placeD);

        LevelD levelD = new LevelD();
        levelD.setId(json.getElement(row, "sn_id"));
        levelD.setCaracteristic(json.getElement(row, "sn_charakteristika"));
        levelD.setNumber(json.getElement(row, "cislo_stupna"));
        this.employeeConditionsManager.setLevelD(levelD);

        MinimumWageD minimumWageD = new MinimumWageD();
        minimumWageD.setId(json.getElement(row, "mm_id"));
        minimumWageD.setFrom(json.getElement(row, "mm_platnost_od"));
        minimumWageD.setTo(json.getElement(row, "mm_platnost_do"));
        minimumWageD.setHourValue(json.getElement(row, "hodinova_hodnota"));
        minimumWageD.setLevelNum(json.getElement(row, "stupen_narocnosti"));
        minimumWageD.setMonthValue(json.getElement(row, "mesacna_hodnota"));
        this.employeeConditionsManager.setMinimumWageD(minimumWageD);

        YearD yearD = new YearD() ;
        yearD.setId(json.getElement(row, "o_id"));
        yearD.setYearNumber(json.getElement(row, "rok"));
        yearD.setHolidayEntitlement(json.getElement(row, "narok_na_dovolenku"));
        yearD.setHolidayEntitlementFromLastYear(json.getElement(row, "narok_na_dovolenku_z_minuleho_roka"));
        yearD.setHolidaysTaken(json.getElement(row, "vycerpana_dovolenka"));
        yearD.setAvarageWage1(json.getElement(row, "priemerna_mzda_1"));
        yearD.setAvarageWage2(json.getElement(row, "priemerna_mzda_2"));
        yearD.setAvarageWage3(json.getElement(row, "priemerna_mzda_3"));
        yearD.setAvarageWage4(json.getElement(row, "priemerna_mzda_4"));
        yearD.setConditionID(json.getElement(row, "podmienky_pracovneho_vztahu"));
        this.employeeConditionsManager.getYearDS().add(yearD);

        MonthD monthD = new MonthD() ;
        monthD.setId(json.getElement(row, "om_id"));
        monthD.setMonthNumber(json.getElement(row, "poradie_mesiaca"));
        monthD.setYearID(json.getElement(row, "odpracovany_rok"));
        monthD.setPaymentID(json.getElement(row, "vyplatna_paska"));
        monthD.setIsClosed(json.getElement(row, "je_mesiac_uzatvoreny"));
        this.employeeConditionsManager.getMonthDS().add(monthD);

    }

    private void setEmployeeConditionsManagerWagesData(JsonArrayClass json, int row)
    {
        WageD wageD = new WageD() ;
        wageD.setId(json.getElement(row, "zm_id"));
        wageD.setLabel(json.getElement(row, "popis"));
        wageD.setEmployeeEnter(json.getElement(row, "vykon_eviduje_zamestnanec"));
        wageD.setTimeImportant(json.getElement(row, "nutne_evidovanie_casu"));
        wageD.setEmergencyImportant(json.getElement(row, "mozne_evidovanie_pohotovosti"));
        wageD.setTarif(json.getElement(row, "tarifa_za_jednotku_mzdy"));
        wageD.setPayWay(json.getElement(row, "sposob_vyplacania"));
        wageD.setPayDate(json.getElement(row, "datum_vyplatenia"));
        wageD.setConditionsID(json.getElement(row, "podmienky_pracovneho_vztahu"));
        wageD.setWageFormID(json.getElement(row, "fm_id"));
        wageD.setWageFormName(json.getElement(row, "nazov"));
        wageD.setWageFormUnit(json.getElement(row, "jednotka_vykonu"));
        wageD.setWageFormUnitShort(json.getElement(row, "skratka_jednotky"));
        this.employeeConditionsManager.getWageDS().add(wageD);
    }

    private void setEmployeeConditionsManagerYearMonthData(JsonArrayClass json, int row)
    {
        YearD yearD = new YearD() ;
        yearD.setId(json.getElement(row, "o_id"));
        yearD.setYearNumber(json.getElement(row, "rok"));
        yearD.setHolidayEntitlement(json.getElement(row, "narok_na_dovolenku"));
        yearD.setHolidayEntitlementFromLastYear(json.getElement(row, "narok_na_dovolenku_z_minuleho_roka"));
        yearD.setHolidaysTaken(json.getElement(row, "vycerpana_dovolenka"));
        yearD.setAvarageWage1(json.getElement(row, "priemerna_mzda_1"));
        yearD.setAvarageWage2(json.getElement(row, "priemerna_mzda_2"));
        yearD.setAvarageWage3(json.getElement(row, "priemerna_mzda_3"));
        yearD.setAvarageWage4(json.getElement(row, "priemerna_mzda_4"));
        yearD.setConditionID(json.getElement(row, "podmienky_pracovneho_vztahu"));
        this.employeeConditionsManager.getYearDS().add(yearD);

        MonthD monthD = new MonthD() ;
        monthD.setId(json.getElement(row, "om_id"));
        monthD.setMonthNumber(json.getElement(row, "poradie_mesiaca"));
        monthD.setYearID(json.getElement(row, "odpracovany_rok"));
        monthD.setPaymentID(json.getElement(row, "vyplatna_paska"));
        monthD.setIsClosed(json.getElement(row, "je_mesiac_uzatvoreny"));
        this.employeeConditionsManager.getMonthDS().add(monthD);
    }

    public boolean exportPayment()
    {
        try{
            exportModels();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return false;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                "\nKontaktujte administrátora systému!", e.toString());
            return false;
        }

        return true;
    }


    private void exportModels() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();
        ht.addParam("hoursFund", paymentD.getHoursFund());
        ht.addParam("daysFund", paymentD.getDaysFund());
        ht.addParam("hoursWorked", this.paymentD.getHoursWorked());
        ht.addParam("daysWorked", this.paymentD.getDaysWorked());
        ht.addParam("grossWage", this.paymentD.getGrossWage());
        ht.addParam("assessmentBasis", this.paymentD.getAssessmentBasis());
        ht.addParam("employeeEnsurence", this.paymentD.getEmployeeEnsurence());
        ht.addParam("nonTaxableWage", this.paymentD.getNonTaxableWage());
        ht.addParam("taxableWage", this.paymentD.getTaxableWage());
        ht.addParam("taxAdvances", this.paymentD.getTaxAdvances());
        ht.addParam("taxBonus", this.paymentD.getTaxBonus());
        ht.addParam("netWage", this.paymentD.getNetWage());
        ht.addParam("workPrice", this.paymentD.getWorkPrice());
        ht.addParam("employerLevies", this.paymentD.getEmployerLevies());
        ht.addParam("employeeLevies", this.paymentD.getEmployeeLevies());
        ht.addParam("leviesSum", this.paymentD.getLeviesSum());
        ht.addParam("total", this.paymentD.getTotal());
        ht.addParam("onAccount", this.paymentD.getOnAccount());
        ht.addParam("cash", this.paymentD.getCash());
        ht.addParam("whoCreatedID", this.paymentD.getWhoCreatedID());
        ht.addParam("employeeImportantID", this.paymentD.getEmployeeImportantID());
        ht.addParam("minimumWageID", this.paymentD.getMinimumWageID());
        ht.addParam("wageConstantsID", this.paymentD.getWageConstantsID());
        ht.addParam("yearAverageWage", this.paymentD.getYearAverageWage());

        int i = 0;
        for(MonthD m : employeeConditionsManager.getMonthDS())
        {
            ht.addParam("month"+i, m.getId());
            i++;
        }
        ht.addParam("monthsCount", i+"");

        i = 0;
        for(PaymentBasicComponentD m : grossWageManager.getBasicComponentManager().getPaymentBasicComponentDS())
        {
            ht.addParam("bcWorkedUnits"+i, m.getWorkedUnits());
            ht.addParam("bcWageForUnits"+i, m.getWageForUnits());
            if(m.getWageD()==null)
                ht.addParam("bcWageID"+i, m.getWorkedPerformanceOfWage().getWageD().getId());
            else
                ht.addParam("bcWageID"+i, m.getWageD().getId());
            i++;
        }
        ht.addParam("bcCount", i+"");

        i = 0;
        for(PaymentDynamicComponentD m : grossWageManager.getDynamicComponentManager().getPaymentDynamicComponentDS())
        {
            ht.addParam("dcType"+i, m.getType());
            ht.addParam("dcCharacteristic"+i, m.getCharacteristic());
            ht.addParam("dcWage"+i, m.getWage());
            i++;
        }
        ht.addParam("dcCount", i+"");

        i = 0;
        for(PaymentWageCompensationD m : grossWageManager.getWageCompensationManager().getWageCompensationDS())
        {
            ht.addParam("wcReason"+i, m.getReason());
            ht.addParam("wcDays"+i, m.getDays());
            ht.addParam("wcHours"+i, m.getHours());
            ht.addParam("wcWage"+i, m.getWage());
            i++;
        }
        ht.addParam("wcCount", i+"");

        i = 0;
        for(PaymentSurchargeD m : grossWageManager.getSurchargesManager().getPaymentSurchargeDS())
        {
            ht.addParam("sHours"+i, m.getHours());
            ht.addParam("sWage"+i, m.getWage());
            ht.addParam("sSurchargeTypeID"+i, m.getSurcharegeTypeID());
            i++;
        }
        ht.addParam("sCount", i+"");

        i = 0;
        for(PaymentOtherComponentD m : grossWageManager.getOthersComponentsManager().getOtherComponentDS())
        {
            ht.addParam("ocName"+i, m.getName());
            ht.addParam("ocWage"+i, m.getWage());
            i++;
        }
        ht.addParam("ocCount", i+"");

        i = 0;
        ArrayList<LevyD> levyDS = new ArrayList<>();
        levyDS.addAll(netWageManager.getLeviesSocialInsuranceManager().getAllLevyDS());
        levyDS.addAll(netWageManager.getLeviesHealthInsuranceManager().getAllLevyDS());
        for(LevyD m : levyDS)
        {
            ht.addParam("lName"+i, m.getName());
            ht.addParam("lAssessmentBasis"+i, m.getAssessmentBasis());
            ht.addParam("lEmployeeSum"+i, m.getEmployeeSum());
            ht.addParam("lEmployerSum"+i, m.getEmployerSum());
            i++;
        }
        ht.addParam("lCount", i+"");

        i=0;
        for(PaymentDeductionD m : netWageManager.getDeductionsManager().getPaymentDeductionDS())
        {
            ht.addParam("dName"+i, m.getName());
            ht.addParam("dSum"+i, m.getSum());
            i++;
        }
        ht.addParam("dCount", i+"");


        ht.sendPost("payment/crt_pmnt", LoggedInUser.getToken(), LoggedInUser.getId());
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        paymentD.setId(json.getElement(0, "id"));
    }

    public void calculateAverageWage()
    {
        if(!(employeeConditionsManager.getMonthDS().get(0).getMonthNumber().equals("3")
            || employeeConditionsManager.getMonthDS().get(0).getMonthNumber().equals("6")
            || employeeConditionsManager.getMonthDS().get(0).getMonthNumber().equals("9")
            || employeeConditionsManager.getMonthDS().get(0).getMonthNumber().equals("12")))
            return;

        HttpClientClass ht = new HttpClientClass();
        try{
            ht.sendGet("payment/lst_thr_mnths/"+employeeConditionsManager.getRelationD().getId()+"/"+employeeConditionsManager.getYearDS().get(0).getYearNumber()+"/"
                    +employeeConditionsManager.getMonthDS().get(0).getMonthNumber(), LoggedInUser.getToken(), LoggedInUser.getId());
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
        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());

        if(json.getSize()!=2)
            return;

        BigDecimal grossWage = new BigDecimal("0");
        grossWage = grossWage
                .add(grossWageManager.getGrossWage())
                .add(new BigDecimal(json.getElement(0, "hruba_mzda")))
                .add(new BigDecimal(json.getElement(1, "hruba_mzda")));

        BigDecimal hoursWorked = new BigDecimal("0");
        hoursWorked = hoursWorked
                .add(new BigDecimal(paymentD.getHoursWorked()))
                .add(new BigDecimal(json.getElement(0, "odpracovane_hodiny")))
                .add(new BigDecimal(json.getElement(1, "odpracovane_hodiny")));

        BigDecimal averageWage = grossWage.divide(hoursWorked, 2, RoundingMode.HALF_UP);
        int averagetype ;
        if(employeeConditionsManager.getMonthDS().get(0).getMonthNumber().equals("3"))
            averagetype = 1;
        else if(employeeConditionsManager.getMonthDS().get(0).getMonthNumber().equals("6"))
            averagetype = 2;
        else if(employeeConditionsManager.getMonthDS().get(0).getMonthNumber().equals("9"))
            averagetype = 3;
        else
            averagetype = 4;


        ht = new HttpClientClass();
        ht.addParam("yearID", employeeConditionsManager.getYearDS().get(0).getId());
        ht.addParam("averagetype", averagetype+"");
        ht.addParam("averagewage", averageWage.toPlainString());

        try{
            ht.sendPost("payment/st_vrg_wg", LoggedInUser.getToken(), LoggedInUser.getId());
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

}
