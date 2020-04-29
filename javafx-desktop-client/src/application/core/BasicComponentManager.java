package application.core;

import application.models.PaymentBasicComponentD;
import application.models.WageD;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class BasicComponentManager
{
    GrossWageManager grossWageManager;
    ArrayList<PaymentBasicComponentD> paymentBasicComponentDS;
    BigDecimal basicComponentsTotal;

    ArrayList<WageD> unregularBasicComponentsMenu;

    public BasicComponentManager(GrossWageManager grossWageManager)
    {
        this.grossWageManager = grossWageManager;
        paymentBasicComponentDS = new ArrayList<>();
        basicComponentsTotal = new BigDecimal("0");
        if(this.grossWageManager.getPaymentManager().getEmployeeConditionsManager().getWageDS().get(0).getPayWay()
            .equals("nepravidelne"))
            processUnregularBasicComponentsMenu();
        else
            processRegularBasicComponents();
    }

    public GrossWageManager getGrossWageManager() {
        return grossWageManager;
    }

    public ArrayList<PaymentBasicComponentD> getPaymentBasicComponentDS() {
        return paymentBasicComponentDS;
    }

    public BigDecimal getBasicComponentsTotal() {
        return basicComponentsTotal;
    }

    public ArrayList<WageD> getUnregularBasicComponentsMenu() {
        return unregularBasicComponentsMenu;
    }



    private void processRegularBasicComponents()
    {
        ArrayList<WorkedPerformanceOfWage> workedPerformanceOfWages = this.grossWageManager.getWorkedPerformanceManager()
                .getWorkedPerformanceOfWages();

        for (WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            PaymentBasicComponentD paymentBasicComponentD = new PaymentBasicComponentD(this, w);
            this.paymentBasicComponentDS.add(paymentBasicComponentD);
        }
        calculateBasicComponentsTotal();
    }

    private void processUnregularBasicComponentsMenu()
    {
        ArrayList<WorkedPerformanceOfWage> workedPerformanceOfWages = this.grossWageManager.getWorkedPerformanceManager()
                .getWorkedPerformanceOfWages();

        unregularBasicComponentsMenu = new ArrayList<>();

        for (WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            if(!unregularBasicComponentsMenu.contains(w.getWageD()))
                unregularBasicComponentsMenu.add(w.getWageD());
        }
    }

    public void addUnregularBasicComponent(WageD wageD)
    {
        PaymentBasicComponentD paymentBasicComponentD = new PaymentBasicComponentD(this, wageD);
        this.paymentBasicComponentDS.add(paymentBasicComponentD);
        calculateBasicComponentsTotal();
    }

    public void removeUnregularBasicComponent(PaymentBasicComponentD basicComponentD)
    {
        this.paymentBasicComponentDS.remove(basicComponentD);
        calculateBasicComponentsTotal();
    }

    public void calculateBasicComponentsTotal()
    {
        basicComponentsTotal = new BigDecimal("0");
        for (PaymentBasicComponentD p : this.paymentBasicComponentDS)
        {
            basicComponentsTotal = basicComponentsTotal.add(new BigDecimal(p.getWageForUnits()));
        }

        basicComponentsTotal = basicComponentsTotal.setScale(2, RoundingMode.UP);
    }
}
