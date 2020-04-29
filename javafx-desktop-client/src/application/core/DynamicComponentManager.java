package application.core;

import application.models.PaymentBasicComponentD;
import application.models.PaymentDynamicComponentD;
import application.models.WageD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class DynamicComponentManager {
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS------------------------------------------------------*/
    GrossWageManager grossWageManager;
    ArrayList<PaymentDynamicComponentD> paymentDynamicComponentDS;
    BigDecimal dynamicComponentsTotal;

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS---------------------------------------------------*/
    public DynamicComponentManager(GrossWageManager grossWageManager) {
        this.grossWageManager = grossWageManager;
        paymentDynamicComponentDS = new ArrayList<>();
        dynamicComponentsTotal = new BigDecimal("0");
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------SETTERS/GETTERS--------------------------------------------------*/
    public GrossWageManager getGrossWageManager() {
        return grossWageManager;
    }

    public ArrayList<PaymentDynamicComponentD> getPaymentDynamicComponentDS() {
        return paymentDynamicComponentDS;
    }

    public BigDecimal getDynamicComponentsTotal() {
        return dynamicComponentsTotal;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS------------------------------------------------------*/
    public void addDynamicComponent(PaymentDynamicComponentD paymentDynamicComponentD)
    {
        this.paymentDynamicComponentDS.add(paymentDynamicComponentD);
        calculateBasicComponentsTotal();
    }

    public void removeDynamicComponent(PaymentDynamicComponentD paymentDynamicComponentD)
    {
        this.paymentDynamicComponentDS.remove(paymentDynamicComponentD);
        calculateBasicComponentsTotal();
    }

    public void calculateBasicComponentsTotal()
    {
        dynamicComponentsTotal = new BigDecimal("0");
        for (PaymentDynamicComponentD p : this.paymentDynamicComponentDS)
        {
            dynamicComponentsTotal = dynamicComponentsTotal.add(new BigDecimal(p.getWage()));
        }

        dynamicComponentsTotal = dynamicComponentsTotal.setScale(2, RoundingMode.UP);
    }

}
