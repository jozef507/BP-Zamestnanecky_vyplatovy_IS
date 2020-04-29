package application.core;

import application.models.PaymentDeductionD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class DeductionsManager
{
    NetWageManager netWageManager;

    ArrayList<PaymentDeductionD> paymentDeductionDS;
    BigDecimal deductionsTotal;

    public DeductionsManager(NetWageManager netWageManager) {
        this.netWageManager = netWageManager;
        paymentDeductionDS = new ArrayList<>();
        deductionsTotal = new BigDecimal("0");
    }

    public BigDecimal getDeductionsTotal() {
        return deductionsTotal;
    }

    public ArrayList<PaymentDeductionD> getPaymentDeductionDS() {
        return paymentDeductionDS;
    }

    public void addDeduction(PaymentDeductionD paymentDeductionD)
    {
        paymentDeductionDS.add(paymentDeductionD);
        calculateDedutionsTotal();
    }

    public void removeDeduction(PaymentDeductionD paymentDeductionD)
    {
        paymentDeductionDS.remove(paymentDeductionD);
        calculateDedutionsTotal();
    }

    private void calculateDedutionsTotal()
    {
        deductionsTotal = new BigDecimal("0");
        for(PaymentDeductionD p : paymentDeductionDS)
        {
            deductionsTotal = deductionsTotal.add(new BigDecimal(p.getSum()));
        }
        deductionsTotal = deductionsTotal.setScale(2, RoundingMode.HALF_UP);
    }
}
