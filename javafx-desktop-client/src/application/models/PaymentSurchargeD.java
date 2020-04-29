package application.models;

import application.core.GrossWageManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentSurchargeD {
    /* model */
    String id, hours, wage;
    String surcharegeTypeID;
    String paymentID;

    /* app */
    String appID;

    public PaymentSurchargeD(String appID, GrossWageManager grossWageManager) {
        this.appID = appID;
        processHours(grossWageManager);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSurcharegeTypeID() {
        return surcharegeTypeID;
    }

    public void setSurcharegeTypeID(String surcharegeTypeID) {
        this.surcharegeTypeID = surcharegeTypeID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getAppID() {
        return appID;
    }

    private void processHours(GrossWageManager grossWageManager)
    {
        if(this.appID.equals("night"))
        {
            this.hours = grossWageManager.getWorkedPerformanceManager().getNightTime();
        }
        else if(this.appID.equals("saturday"))
        {
            this.hours = grossWageManager.getWorkedPerformanceManager().getSaturdayTime();
        }
        else if(this.appID.equals("sunday"))
        {
            this.hours = grossWageManager.getWorkedPerformanceManager().getSundayTime();
        }
        else if(this.appID.equals("feast"))
        {
            this.hours = grossWageManager.getWorkedPerformanceManager().getFeastTime();
        }
        else if(this.appID.equals("overtime"))
        {
            this.hours = grossWageManager.getWorkedPerformanceManager().getOvertimeTime();
        }
    }

    public void calculateWage(GrossWageManager grossWageManager, SurchargeTypeD surchargeTypeD)
    {
        this.surcharegeTypeID = surchargeTypeD.getId();

        BigDecimal base=null;
        if(surchargeTypeD.getBase().equals("priemerná mzda"))
        {
            if(grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o vykonaní práce")
                    || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o pracovnej činnosti")
                    || grossWageManager.getPaymentManager().getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
            {
                base = new BigDecimal("0");
            }
            else
            {
                base = grossWageManager.getAverageWage();
            }
        }
        else
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

        BigDecimal part = new BigDecimal(surchargeTypeD.getPart());


        this.wage = part.multiply(base).multiply(new BigDecimal(this.hours)).setScale(4, RoundingMode.HALF_UP).toString();

    }
}
