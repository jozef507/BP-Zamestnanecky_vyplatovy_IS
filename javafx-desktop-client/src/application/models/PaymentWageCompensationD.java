package application.models;

import application.core.GrossWageManager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class PaymentWageCompensationD
{
    String id, reason, days, hours, wage;
    String paymentID;

    String calculatedFrom;
    String partOfBase;
    AbsenceD absenceD;

    String days1, days2;
    public PaymentWageCompensationD() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getCalculatedFrom() {
        return calculatedFrom;
    }

    public void setCalculatedFrom(String calculatedFrom) {
        this.calculatedFrom = calculatedFrom;
    }

    public String getDays1() {
        return days1;
    }

    public void setDays1(String days1) {
        this.days1 = days1;
    }

    public String getDays2() {
        return days2;
    }

    public void setDays2(String days2) {
        this.days2 = days2;
    }

    public AbsenceD getAbsenceD() {
        return absenceD;
    }

    public void setAbsenceD(AbsenceD absenceD) {
        this.absenceD = absenceD;
    }

    public String getPartOfBase() {
        return partOfBase;
    }

    public void setPartOfBase(String partOfBase) {
        this.partOfBase = partOfBase;
    }

    public void calculateAsCustom(GrossWageManager grossWageManager)
    {
        if(absenceD.getFrom().equals(absenceD.getTo()))
        {
            if(absenceD.getHalf().equals("áno"))
            {
                days = new BigDecimal("0.5").toPlainString();
            }
            else
            {
                days = new BigDecimal("1").toPlainString();
            }
        }
        else
        {
            String t1 = absenceD.getFrom();
            String t2 = absenceD.getTo();
            Date time1 = null, time2 = null;

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

            try {
                time1 = format.parse(t1);
                time2 = format.parse(t2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(!(month2.compareTo(time1)>=0 && time2.compareTo(month1)>=0))
                return;

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
            days = difference.toPlainString();
        }


        BigDecimal days = new BigDecimal(this.days);
        BigDecimal hours = days.multiply(new BigDecimal(grossWageManager.getPaymentManager()
                .getEmployeeConditionsManager().getNextConditionsD().getDayTime()));
        this.hours = hours.toPlainString();

        BigDecimal base = null;
        if(this.calculatedFrom.equals("priemerná mzda")
            && !(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov")))
        {
            base = grossWageManager.getAverageWage();
        }
        else // it is minimal wage
        {
            if(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
                || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
            {
                base = new BigDecimal(grossWageManager.getPaymentManager().getBasicMinimumWageD().getHourValue());
            }
            else
            {
                base = new BigDecimal(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMinimumWageD().getHourValue());
            }
        }

        BigDecimal wage = base.multiply(hours);
        this.reason = absenceD.getReason();
        this.setHours(hours.toPlainString());
        this.setWage(wage.toPlainString());
    }
}
