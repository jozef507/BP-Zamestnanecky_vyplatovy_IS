package application.models;

import application.core.BasicComponentManager;
import application.core.WorkedPerformanceOfWage;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentBasicComponentD
{
    /* model */
    String id, workedUnits, wageForUnits;
    String paymentID;
    String wageID;

    /* table */
    String wageForm, wageCharacteristic, wageTarif;

    /* app*/
    String workedHours;
    BasicComponentManager basicComponentManager;

    WorkedPerformanceOfWage workedPerformanceOfWage;
    WageD wageD;

    public PaymentBasicComponentD() {
    }

    public PaymentBasicComponentD(BasicComponentManager basicComponentManager, WorkedPerformanceOfWage workedPerformanceOfWage) {
        this.basicComponentManager = basicComponentManager;
        this.workedPerformanceOfWage = workedPerformanceOfWage;
        this.wageID = workedPerformanceOfWage.getWageD().getId();

        this.wageForm = workedPerformanceOfWage.getWageD().getWageFormName();
        this.wageCharacteristic = workedPerformanceOfWage.getWageD().getLabel();
        this.wageTarif = workedPerformanceOfWage.getWageD().getTarif();

        processWorkedUnits();
        processWageForUnits();
    }

    public PaymentBasicComponentD(BasicComponentManager basicComponentManager, WageD wageD) {
        this.basicComponentManager = basicComponentManager;
        this.wageD = wageD;
        this.wageID = wageD.getId();

        this.wageForm = wageD.getWageFormName();
        this.wageCharacteristic = wageD.getLabel();
        this.wageTarif = wageD.getTarif();

        this.workedUnits = "1";
        processWageForUnits();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkedUnits() {
        return workedUnits;
    }

    public void setWorkedUnits(String workedUnits) {
        this.workedUnits = workedUnits;
    }

    public String getWageForUnits() {
        return wageForUnits;
    }

    public void setWageForUnits(String wageForUnits) {
        this.wageForUnits = wageForUnits;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getWageID() {
        return wageID;
    }

    public void setWageID(String wageID) {
        this.wageID = wageID;
    }

    public String getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(String workedHours) {
        this.workedHours = workedHours;
    }

    public WorkedPerformanceOfWage getWorkedPerformanceOfWage() {
        return workedPerformanceOfWage;
    }

    public void setWorkedPerformanceOfWage(WorkedPerformanceOfWage workedPerformanceOfWage) {
        this.workedPerformanceOfWage = workedPerformanceOfWage;
    }

    public String getWageForm() {
        return wageForm;
    }

    public String getWageCharacteristic() {
        return wageCharacteristic;
    }

    public String getWageTarif() {
        return wageTarif;
    }

    public BasicComponentManager getBasicComponentManager() {
        return basicComponentManager;
    }

    public WageD getWageD() {
        return wageD;
    }

    private void processWorkedUnits()
    {
        if(this.workedPerformanceOfWage.getPerformanceTotal()!=null)
        {
            this.workedUnits = this.workedPerformanceOfWage.getPerformanceTotal().toString();
            if(this.workedPerformanceOfWage.getTimeTotal()!=null)
                this.workedHours = this.workedPerformanceOfWage.getTimeTotal().toString();
        }
        else if(this.workedPerformanceOfWage.getPartTotal()!=null)
        {
            this.workedUnits = this.workedPerformanceOfWage.getPartTotal().toString();
            if(this.workedPerformanceOfWage.getTimeTotal()!=null)
                this.workedHours = this.workedPerformanceOfWage.getTimeTotal().toString();
        }
        else if(this.workedPerformanceOfWage.getTimeTotal()!=null)
        {
            this.workedUnits = this.workedPerformanceOfWage.getTimeTotal().toString();
        }
    }

    private void processWageForUnits()
    {
        BigDecimal workedUnits = new BigDecimal(this.workedUnits);

        BigDecimal tarif;
        if(this.workedPerformanceOfWage==null)
        {
            tarif = new BigDecimal(this.wageD.getTarif());
        }
        else
        {
            tarif = new BigDecimal(this.workedPerformanceOfWage.getWageD().getTarif());
        }

        BigDecimal wageForUnits;
        if(this.workedPerformanceOfWage!=null)
        {
            if(workedPerformanceOfWage.getWageD().getWageFormName().equals("časová")
                    && workedPerformanceOfWage.getWageD().getWageFormUnit().equals("mesiac"))
            {
                wageForUnits = workedUnits.divide(new BigDecimal(basicComponentManager.getGrossWageManager()
                        .getPaymentManager().getPaymentD().getHoursFund()), 4, RoundingMode.HALF_UP)
                        .multiply(tarif);
            }
            else
            {
                wageForUnits = workedUnits.multiply(tarif);
            }
        }
        else
        {
            wageForUnits = workedUnits.multiply(tarif);
        }


        this.wageForUnits = wageForUnits.toString();
    }
}
