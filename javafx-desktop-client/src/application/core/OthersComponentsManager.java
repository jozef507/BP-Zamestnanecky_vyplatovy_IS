package application.core;

import application.models.PaymentOtherComponentD;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OthersComponentsManager {
    private GrossWageManager grossWageManager;

    private ArrayList<PaymentOtherComponentD> otherComponentDS;
    private BigDecimal otherComponentsTotal;

    public OthersComponentsManager(GrossWageManager grossWageManager) {
        this.grossWageManager = grossWageManager;
        otherComponentDS = new ArrayList<>();
        otherComponentsTotal = new BigDecimal("0");
    }

    public ArrayList<PaymentOtherComponentD> getOtherComponentDS() {
        return otherComponentDS;
    }

    public BigDecimal getOtherComponentsTotal() {
        return otherComponentsTotal;
    }

    public void addOtherComponent(PaymentOtherComponentD paymentOtherComponentD)
    {
        otherComponentDS.add(paymentOtherComponentD);
        calculateOtherComponentsTotal();
    }

    public void removeOtherComponent(PaymentOtherComponentD paymentOtherComponentD)
    {
        otherComponentDS.remove(paymentOtherComponentD);
        calculateOtherComponentsTotal();
    }

    private void calculateOtherComponentsTotal()
    {
        otherComponentsTotal = new BigDecimal("0");
        for (PaymentOtherComponentD paymentOtherComponentD : otherComponentDS)
        {
            otherComponentsTotal = otherComponentsTotal.add(new BigDecimal(paymentOtherComponentD.getWage()));
        }
    }

}
