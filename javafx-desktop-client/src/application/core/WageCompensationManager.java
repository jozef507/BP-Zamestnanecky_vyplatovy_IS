package application.core;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.AbsenceD;
import application.models.PaymentWageCompensationD;
import application.models.SurchargeTypeD;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class WageCompensationManager
{
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS------------------------------------------------------*/
    GrossWageManager grossWageManager;
    ArrayList<AbsenceD> absenceDS;

    PaymentWageCompensationD inActiveEmergency;
    PaymentWageCompensationD holiday;
    PaymentWageCompensationD incapacityForWork;
    PaymentWageCompensationD nursing;
    BigDecimal specificCompensationsTotal;

    ArrayList<PaymentWageCompensationD> customCompensations;
    BigDecimal customCompensationsTotal;

    BigDecimal compensationsTotal;


    /*----------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS---------------------------------------------------*/
    public WageCompensationManager(GrossWageManager grossWageManager) {
        this.grossWageManager = grossWageManager;

        this.absenceDS = new ArrayList<>();
        downloadAbsences();

        inActiveEmergency = new PaymentWageCompensationD();
        holiday = new PaymentWageCompensationD();
        incapacityForWork = new PaymentWageCompensationD();
        nursing = new PaymentWageCompensationD();
        specificCompensationsTotal = new BigDecimal("0");

        customCompensations = new ArrayList<>();
        customCompensationsTotal = new BigDecimal("0");

        compensationsTotal = new BigDecimal("0");

        processSpecificCompensations();
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------SETTERS/GETTERS--------------------------------------------------*/
    public GrossWageManager getGrossWageManager() {
        return grossWageManager;
    }

    public void setGrossWageManager(GrossWageManager grossWageManager) {
        this.grossWageManager = grossWageManager;
    }

    public ArrayList<AbsenceD> getAbsenceDS() {
        return absenceDS;
    }

    public void setAbsenceDS(ArrayList<AbsenceD> absenceDS) {
        this.absenceDS = absenceDS;
    }

    public PaymentWageCompensationD getNonActiveEmergency() {
        return inActiveEmergency;
    }

    public void setNonActiveEmergency(PaymentWageCompensationD nonActiveEmergency) {
        this.inActiveEmergency = nonActiveEmergency;
    }

    public PaymentWageCompensationD getHoliday() {
        return holiday;
    }

    public void setHoliday(PaymentWageCompensationD holiday) {
        this.holiday = holiday;
    }

    public PaymentWageCompensationD getIncapacityForWork() {
        return incapacityForWork;
    }

    public void setIncapacityForWork(PaymentWageCompensationD incapacityForWork) {
        this.incapacityForWork = incapacityForWork;
    }

    public PaymentWageCompensationD getNursing() {
        return nursing;
    }

    public void setNursing(PaymentWageCompensationD nursing) {
        this.nursing = nursing;
    }

    public BigDecimal getSpecificCompensationsTotal() {
        return specificCompensationsTotal;
    }

    public void setSpecificCompensationsTotal(BigDecimal specificCompensationsTotal) {
        this.specificCompensationsTotal = specificCompensationsTotal;
    }

    public ArrayList<PaymentWageCompensationD> getCustomCompensations() {
        return customCompensations;
    }

    public void setCustomCompensations(ArrayList<PaymentWageCompensationD> customCompensations) {
        this.customCompensations = customCompensations;
    }

    public BigDecimal getCustomCompensationsTotal() {
        return customCompensationsTotal;
    }

    public void setCustomCompensationsTotal(BigDecimal customCompensationsTotal) {
        this.customCompensationsTotal = customCompensationsTotal;
    }

    public BigDecimal getCompensationsTotal() {
        return compensationsTotal;
    }

    public void setCompensationsTotal(BigDecimal compensationsTotal) {
        this.compensationsTotal = compensationsTotal;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS------------------------------------------------------*/
    private void downloadAbsences()
    {
        String monthID = grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMonthDS().get(0).getId();
        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendGet("absence/bsnc-by-mnthid/"+monthID, LoggedInUser.getToken(), LoggedInUser.getId());
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
            AbsenceD absenceD = new AbsenceD();
            absenceD.setId(json.getElement(i, "id"));
            absenceD.setFrom(json.getElement(i, "od"));
            absenceD.setTo(json.getElement(i, "do"));
            absenceD.setHalf(json.getElement(i, "je_polovica_dna"));
            absenceD.setReason(json.getElement(i, "typ_dovodu"));
            absenceD.setCharacteristic(json.getElement(i, "popis_dovodu"));
            absenceD.setUpdated(json.getElement(i, "aktualizovane"));
            absenceDS.add(absenceD);
        }
    }

    private void processSpecificCompensations()
    {
        processInactiveEmergency();
        processHoliday();
        processIncapacityForWork();
        processNursing();
        calculateSpecificCompensationsTotal();
    }

    private void processInactiveEmergency()
    {
        if(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            inActiveEmergency.setReason("Neakt. pohot. mimo prac.");
            inActiveEmergency.setDays("0");
            inActiveEmergency.setHours("0");
            inActiveEmergency.setWage("0");
            return;
        }

        BigDecimal innactiveEmergencyHours = new BigDecimal("0");
        ArrayList<WorkedPerformanceOfWage> workedPerformanceOfWages = this.grossWageManager.getWorkedPerformanceManager().getWorkedPerformanceOfWages();
        for(WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            if(w.getInactiveEmergencyOutOfPlaceTime()!=null)
                innactiveEmergencyHours = innactiveEmergencyHours.add(w.getInactiveEmergencyOutOfPlaceTime());
        }

        BigDecimal minimalWage = null;
        minimalWage = new BigDecimal(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMinimumWageD().getHourValue());

        BigDecimal innactiveEmergencyWage = innactiveEmergencyHours.multiply(new BigDecimal("0.2")).multiply(minimalWage).setScale(4, RoundingMode.HALF_UP);
        inActiveEmergency.setReason("Neakt. pohot. mimo prac.");
        inActiveEmergency.setDays("0");
        inActiveEmergency.setHours(innactiveEmergencyHours.toPlainString());
        inActiveEmergency.setWage(innactiveEmergencyWage.toPlainString());
    }


    private void processHoliday()
    {
        if(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            holiday.setReason("Dovolenka");
            holiday.setDays("0");
            holiday.setHours("0");
            holiday.setWage("0");
            return;
        }

        ArrayList<AbsenceD> filteredList= absenceDS.stream().filter(article -> article.getReason().contains("dovolenka"))
                .collect(Collectors.toCollection(ArrayList::new));
        if(filteredList.isEmpty())
        {
            holiday.setReason("Dovolenka");
            holiday.setDays("0");
            holiday.setHours("0");
            holiday.setWage("0");
            return;
        }

        absenceDS.removeAll(filteredList);

        BigDecimal holidayDays = getCountOfHolidayDays(filteredList);
        BigDecimal holidayHours = holidayDays.multiply(new BigDecimal(grossWageManager.getPaymentManager()
                .getEmployeeConditionsManager().getNextConditionsD().getDayTime()));
        BigDecimal averageHourWage = grossWageManager.getAverageWage();

        BigDecimal holidayWage = holidayHours.multiply(averageHourWage).setScale(4, RoundingMode.HALF_UP);
        holiday.setReason("Dovolenka");
        holiday.setDays(holidayDays.toPlainString());
        holiday.setHours(holidayHours.toPlainString());
        holiday.setWage(holidayWage.toPlainString());
    }

    private BigDecimal getCountOfHolidayDays(ArrayList<AbsenceD> filteredList)
    {
        String date = grossWageManager.getPaymentManager().getEmployeeConditionsManager().getYearDS().get(0).getYearNumber()
                +"-"+ grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMonthDS().get(0).getMonthNumber()+"-01";
        LocalDate convertedDate = null;
        try{
            convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-dd"));
        }catch (DateTimeParseException e){
            e.printStackTrace();
        }
        Date month1 = java.util.Date.from(convertedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
        Date month2 = java.util.Date.from(convertedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        BigDecimal holidayDays = new BigDecimal("0");
        for (AbsenceD a : filteredList)
        {
            if(a.getFrom().equals(a.getTo()))
            {
                if(a.getHalf().equals("áno"))
                {
                    holidayDays = holidayDays.add(new BigDecimal("0.5"));
                }
                else
                {
                    holidayDays = holidayDays.add(new BigDecimal("1"));
                }
            }
            else
            {
                String t1 = a.getFrom();
                String t2 = a.getTo();
                Date time1 = null, time2 = null;
                try {
                    time1 = format.parse(t1);
                    time2 = format.parse(t2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(!(month2.compareTo(time1)>=0 && time2.compareTo(month1)>=0))
                    continue;

                Date from=null, to=null;
                if(time1.compareTo(month1)>0)
                    from=time1;
                else
                    from=month1;
                if(time2.compareTo(month2)>0)
                    to=month2;
                else
                    to=time2;

                BigDecimal difference = new BigDecimal(to.getTime() - from.getTime());
                difference = difference.divide(new BigDecimal("86400000")).add(new BigDecimal("1"));
                holidayDays = holidayDays.add(difference);
            }
        }
        return holidayDays;
    }



    private void processIncapacityForWork()
    {
        if(!isRightFroIncapacityForWork())
        {
            incapacityForWork.setReason("PN");
            incapacityForWork.setDays("0");
            incapacityForWork.setHours("0");
            incapacityForWork.setWage("0");
            incapacityForWork.setDays1("0");
            incapacityForWork.setDays2("0");
            return;
        }

        ArrayList<AbsenceD> filteredList= absenceDS.stream().filter(article -> article.getReason().contains("PN"))
                .collect(Collectors.toCollection(ArrayList::new));
        if(filteredList.isEmpty())
        {
            incapacityForWork.setReason("PN");
            incapacityForWork.setDays("0");
            incapacityForWork.setHours("0");
            incapacityForWork.setWage("0");
            incapacityForWork.setDays1("0");
            incapacityForWork.setDays2("0");
            return;
        }

        absenceDS.removeAll(filteredList);

        ArrayList<BigDecimal> incapacityForWorkDays = getCountOfIncapacityForWorkDays(filteredList);
        BigDecimal incapacityForWorkDays0 = incapacityForWorkDays.get(0);
        BigDecimal incapacityForWorkDays1 = incapacityForWorkDays.get(1);
        BigDecimal incapacityForWorkDays2 = incapacityForWorkDays.get(2);

        BigDecimal incapacityForWorkHours = incapacityForWorkDays0.multiply(new BigDecimal(grossWageManager.getPaymentManager()
                .getEmployeeConditionsManager().getNextConditionsD().getDayTime()));

        incapacityForWork.setReason("PN");
        incapacityForWork.setDays(incapacityForWorkDays0.toPlainString());
        incapacityForWork.setDays1(incapacityForWorkDays1.toPlainString());
        incapacityForWork.setDays2(incapacityForWorkDays2.toPlainString());
        incapacityForWork.setHours(incapacityForWorkHours.toPlainString());
        incapacityForWork.setWage("0");
    }

    private boolean isRightFroIncapacityForWork()
    {
        if(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti"))
        {
            if(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getConditionsD().getPremature().equals("0")
                && grossWageManager.getPaymentManager().getEmployeeConditionsManager().getConditionsD().getInvalidity40().equals("0")
                && grossWageManager.getPaymentManager().getEmployeeConditionsManager().getConditionsD().getInvalidity70().equals("0")
                && grossWageManager.getPaymentManager().getEmployeeConditionsManager().getConditionsD().getRetirement().equals("0")
                && grossWageManager.getPaymentManager().getEmployeeConditionsManager().getWageDS().get(0).getPayWay().equals("pravidelne"))
                return true;
            else
                return false;
        }
        else if(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private ArrayList<BigDecimal> getCountOfIncapacityForWorkDays(ArrayList<AbsenceD> filteredList)
    {
        String date = grossWageManager.getPaymentManager().getEmployeeConditionsManager().getYearDS().get(0).getYearNumber()
                +"-"+ grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMonthDS().get(0).getMonthNumber()+"-01";
        LocalDate convertedDate = null;
        try{
            convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-dd"));
        }catch (DateTimeParseException e){
            e.printStackTrace();
        }
        Date month1 = java.util.Date.from(convertedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
        Date month2 = java.util.Date.from(convertedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<BigDecimal> incapacityForWorkDays = new ArrayList<>();
        incapacityForWorkDays.add((new BigDecimal("0")));
        incapacityForWorkDays.add((new BigDecimal("0")));
        incapacityForWorkDays.add((new BigDecimal("0")));
        for (AbsenceD a : filteredList)
        {
            if(a.getFrom().equals(a.getTo()))
            {
                if(a.getHalf().equals("áno"))
                {
                    BigDecimal bd = incapacityForWorkDays.get(0);
                    incapacityForWorkDays.remove(0);
                    bd = bd.add(new BigDecimal("0.5"));
                    incapacityForWorkDays.add(0, bd);

                    bd = incapacityForWorkDays.get(1);
                    incapacityForWorkDays.remove(1);
                    bd = bd.add(new BigDecimal("0.5"));
                    incapacityForWorkDays.add(1, bd);
                }
                else
                {
                    BigDecimal bd = incapacityForWorkDays.get(0);
                    incapacityForWorkDays.remove(0);
                    bd = bd.add(new BigDecimal("1"));
                    incapacityForWorkDays.add(0, bd);

                    bd = incapacityForWorkDays.get(1);
                    incapacityForWorkDays.remove(1);
                    bd = bd.add(new BigDecimal("1"));
                    incapacityForWorkDays.add(1, bd);
                }
            }
            else
            {
                String t1 = a.getFrom();
                String t2 = a.getTo();
                Date time1 = null, time2 = null;
                try {
                    time1 = format.parse(t1);
                    time2 = format.parse(t2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(!(month2.compareTo(time1)>=0 && time2.compareTo(month1)>=0))
                    continue;

                Date from=null, to=null;
                if(time1.compareTo(month1)>0)
                    from=time1;
                else
                    from=month1;
                if(time2.compareTo(month2)>0)
                    to=month2;
                else
                    to=time2;

                BigDecimal difference = new BigDecimal(to.getTime() - from.getTime());
                difference = difference.divide(new BigDecimal("86400000")).add(new BigDecimal("1")); // pocet dni na dovolenke v mesiaci
                BigDecimal bd = incapacityForWorkDays.get(0);
                incapacityForWorkDays.remove(0);
                bd = bd.add(difference);
                incapacityForWorkDays.add(0, bd);

                BigDecimal tmp = new BigDecimal(to.getTime() - time1.getTime());
                tmp = tmp.divide(new BigDecimal("86400000")).add(new BigDecimal("1")); // pocet dni na dovolenke v mesiaci

                Date time3 = time1, time4 = null, time5 = null, time6 = null;
                BigDecimal difference1 = null;
                if(tmp.compareTo(new BigDecimal("0"))==0)
                {
                    difference1 = new BigDecimal("0");
                }
                else
                {
                    tmp = tmp.subtract(new BigDecimal("1"));
                    if(tmp.subtract(new BigDecimal("2")).compareTo(new BigDecimal("0"))>=0)
                    {
                        time4 = Date.from(((time3.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(2)).atZone(ZoneId.systemDefault()).toInstant());
                        tmp = tmp.subtract(new BigDecimal("2"));
                    }
                    else
                    {
                        time4 = Date.from(((time3.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(tmp.longValue()).atZone(ZoneId.systemDefault()).toInstant()));
                        tmp = tmp.subtract(tmp);
                    }

                    if(!(month2.compareTo(time3)>=0 && time4.compareTo(month1)>=0))
                    {
                        difference1 = new BigDecimal("0");
                    }
                    else
                    {
                        from=null; to=null;
                        if(time3.compareTo(month1)>0)
                            from=time3;
                        else
                            from=month1;
                        if(time4.compareTo(month2)>0)
                            to=month2;
                        else
                            to=time4;

                        difference1 = new BigDecimal(to.getTime() - from.getTime());
                        difference1 = difference1.divide(new BigDecimal("86400000")).add(new BigDecimal("1")); // pocet dni na dovolenke v mesiaci
                    }
                }
                bd = incapacityForWorkDays.get(1);
                incapacityForWorkDays.remove(1);
                bd = bd.add(difference1);
                incapacityForWorkDays.add(1, bd);


                BigDecimal difference2 = null;
                if(tmp.compareTo(new BigDecimal("0"))==0)
                {
                    difference2 = new BigDecimal("0");
                }
                else {
                    if (tmp.subtract(new BigDecimal("1")).compareTo(new BigDecimal("0")) >= 0) {
                        time5 = Date.from(((time4.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(1)).atZone(ZoneId.systemDefault()).toInstant());
                        tmp = tmp.subtract(new BigDecimal("1"));
                    } else {
                        time5 = Date.from(((time4.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(tmp.longValue()).atZone(ZoneId.systemDefault()).toInstant()));
                        tmp = tmp.subtract(tmp);
                    }
                    if (tmp.subtract(new BigDecimal("6")).compareTo(new BigDecimal("0")) >= 0) {
                        time6 = Date.from(((time5.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(6)).atZone(ZoneId.systemDefault()).toInstant());
                        tmp = tmp.subtract(new BigDecimal("6"));
                    } else {
                        time6 = Date.from(((time5.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(tmp.longValue()).atZone(ZoneId.systemDefault()).toInstant()));
                        tmp = tmp.subtract(tmp);
                    }
                    if (!(month2.compareTo(time5) >= 0 && time6.compareTo(month1) >= 0)) {
                        difference2 = new BigDecimal("0");
                    } else {
                        from = null;
                        to = null;
                        if (time5.compareTo(month1) > 0)
                            from = time5;
                        else
                            from = month1;
                        if (time6.compareTo(month2) > 0)
                            to = month2;
                        else
                            to = time6;

                        difference2 = new BigDecimal(to.getTime() - from.getTime());
                        difference2 = difference2.divide(new BigDecimal("86400000")).add(new BigDecimal("1")); // pocet dni na dovolenke v mesiaci
                    }
                }
                bd = incapacityForWorkDays.get(2);
                incapacityForWorkDays.remove(2);
                bd = bd.add(difference2);
                incapacityForWorkDays.add(2, bd);
            }
        }
        return incapacityForWorkDays;
    }

    public void calculateIncapacityForWork(String base)
    {
        BigDecimal firstPaidDays = new BigDecimal(this.incapacityForWork.getDays1());
        BigDecimal secondPaidDays = new BigDecimal(this.incapacityForWork.getDays2());

        BigDecimal firstPaidDaysWage = firstPaidDays.multiply(new BigDecimal(0.25)).multiply(new BigDecimal(base)).setScale(5, RoundingMode.HALF_UP);
        BigDecimal secondPaidDaysWage = secondPaidDays.multiply(new BigDecimal(0.55)).multiply(new BigDecimal(base)).setScale(5, RoundingMode.HALF_UP);

        BigDecimal wage = firstPaidDaysWage.add(secondPaidDaysWage).setScale(4, RoundingMode.HALF_UP);
        this.incapacityForWork.setWage(wage.toPlainString());
        this.incapacityForWork.setCalculatedFrom(base);

        calculateSpecificCompensationsTotal();
    }



    private void processNursing()
    {
        if(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
        {
            nursing.setReason("OČR");
            nursing.setDays("0");
            nursing.setHours("0");
            nursing.setWage("0");
            return;
        }

        ArrayList<AbsenceD> filteredList= absenceDS.stream().filter(article -> article.getReason().contains("OČR"))
                .collect(Collectors.toCollection(ArrayList::new));
        if(filteredList.isEmpty())
        {
            nursing.setReason("OČR");
            nursing.setDays("0");
            nursing.setHours("0");
            nursing.setWage("0");
            return;
        }

        absenceDS.removeAll(filteredList);

        BigDecimal nursingDays = getCountOfNursingDays(filteredList);
        BigDecimal nursinHours = nursingDays.multiply(new BigDecimal(grossWageManager.getPaymentManager()
                .getEmployeeConditionsManager().getNextConditionsD().getDayTime()));

        nursing.setReason("OČR");
        nursing.setDays(nursinHours.toPlainString());
        nursing.setHours(nursinHours.toPlainString());
        nursing.setWage("0");
    }

    private BigDecimal getCountOfNursingDays(ArrayList<AbsenceD> filteredList)
    {
        String date = grossWageManager.getPaymentManager().getEmployeeConditionsManager().getYearDS().get(0).getYearNumber()
                +"-"+ grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMonthDS().get(0).getMonthNumber()+"-01";
        LocalDate convertedDate = null;
        try{
            convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-dd"));
        }catch (DateTimeParseException e){
            e.printStackTrace();
        }
        Date month1 = java.util.Date.from(convertedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
        Date month2 = java.util.Date.from(convertedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        BigDecimal holidayDays = new BigDecimal("0");
        for (AbsenceD a : filteredList)
        {
            if(a.getFrom().equals(a.getTo()))
            {
                if(a.getHalf().equals("áno"))
                {
                    holidayDays = holidayDays.add(new BigDecimal("0.5"));
                }
                else
                {
                    holidayDays = holidayDays.add(new BigDecimal("1"));
                }
            }
            else
            {
                String t1 = a.getFrom();
                String t2 = a.getTo();
                Date time1 = null, time2 = null;
                try {
                    time1 = format.parse(t1);
                    time2 = format.parse(t2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(!(month2.compareTo(time1)>=0 && time2.compareTo(month1)>=0))
                    continue;

                Date from=null, to=null;
                if(time1.compareTo(month1)>0)
                    from=time1;
                else
                    from=month1;
                if(time2.compareTo(month2)>0)
                    to=month2;
                else
                    to=time2;

                BigDecimal difference = new BigDecimal(to.getTime() - from.getTime());
                difference = difference.divide(new BigDecimal("86400000")).add(new BigDecimal("1"));
                holidayDays = holidayDays.add(difference);
            }
        }
        return holidayDays;
    }

    private void calculateSpecificCompensationsTotal()
    {
        specificCompensationsTotal = new BigDecimal("0");
        specificCompensationsTotal = specificCompensationsTotal.add(new BigDecimal(inActiveEmergency.getWage()))
                .add(new BigDecimal(holiday.getWage())).add(new BigDecimal(incapacityForWork.getWage()))
                .add(new BigDecimal(nursing.getWage()));
        specificCompensationsTotal = specificCompensationsTotal.setScale(4, RoundingMode.HALF_UP);
        calculateCompensationsTotal();
    }


    public void addCustomConpensation(PaymentWageCompensationD paymentWageCompensationD)
    {
        AbsenceD absenceD = paymentWageCompensationD.getAbsenceD();
        if(absenceD!=null)
            absenceDS.remove(absenceD);

        paymentWageCompensationD.calculateAsCustom(grossWageManager);
        customCompensations.add(paymentWageCompensationD);
        calculateCustomCompensationsTotal();
    }

    public void removeCustomConpensation(PaymentWageCompensationD paymentWageCompensationD)
    {
        AbsenceD absenceD = paymentWageCompensationD.getAbsenceD();
        if(absenceD!=null)
            absenceDS.add(absenceD);

        customCompensations.remove(paymentWageCompensationD);
        calculateCustomCompensationsTotal();
    }

    private void calculateCustomCompensationsTotal()
    {
        customCompensationsTotal = new BigDecimal("0");
        for(PaymentWageCompensationD p : customCompensations)
        {
            customCompensationsTotal = customCompensationsTotal.add(new BigDecimal(p.getWage()));
        }
        customCompensationsTotal = customCompensationsTotal.setScale(4, RoundingMode.HALF_UP);

        calculateCompensationsTotal();
    }

    private void calculateCompensationsTotal()
    {
        compensationsTotal = new BigDecimal("0");
        compensationsTotal = this.specificCompensationsTotal.add(this.customCompensationsTotal);
        compensationsTotal = compensationsTotal.setScale(2, RoundingMode.HALF_UP);
    }
}
