package application.core;

import application.models.LevyD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class LeviesHealthInsuranceManager
{
    NetWageManager netWageManager;

    BigDecimal maxAssessmentBasis;

    LevyD health;

    ArrayList<LevyD> allLevyDS;

    BigDecimal employeeSumTotal;
    BigDecimal employerSumTotal;

    public LeviesHealthInsuranceManager(NetWageManager netWageManager) {
        this.netWageManager = netWageManager;

        this.maxAssessmentBasis = new BigDecimal(netWageManager.getPaymentManager().getWageConstantsD()
                .getMaxAssessmentBasis());

        this.health = new LevyD("Zdrav. pois.", "0", "0", "0");

        this.allLevyDS = new ArrayList<>();
        this.allLevyDS.add(health);

        this.employeeSumTotal = new BigDecimal("0");
        this.employerSumTotal = new BigDecimal("0");
    }

    public BigDecimal getEmployeeSumTotal() {
        return employeeSumTotal;
    }

    public BigDecimal getEmployerSumTotal() {
        return employerSumTotal;
    }

    public ArrayList<LevyD> getAllLevyDS() {
        return allLevyDS;
    }

    public NetWageManager getNetWageManager() {
        return netWageManager;
    }

    public BigDecimal getMaxAssessmentBasis() {
        return maxAssessmentBasis;
    }

    public LevyD getHealth() {
        return health;
    }

    public void calculateLevies(BigDecimal assessmentBasis, Map<String,BigDecimal[]> levie, boolean isHalfOfHealthInsurence)
    {
        /*BigDecimal updatedAssessmentBasis = assessmentBasis;
        if(updatedAssessmentBasis.compareTo(maxAssessmentBasis)>0)
            updatedAssessmentBasis = maxAssessmentBasis;*/

        BigDecimal healthLevies[] = Arrays.copyOf(levie.get("zdravotne"), levie.get("zdravotne").length);
        if(isHalfOfHealthInsurence)
            healthLevies[0] = healthLevies[0].multiply(new BigDecimal("0.5"));

        health.calculate(assessmentBasis, healthLevies);

        calculateEmployeeTotal();
        calculateEmployerTotal();

    }

    private void calculateEmployeeTotal()
    {
        this.employeeSumTotal = new BigDecimal("0");

        for (LevyD levyD : allLevyDS)
        {
            this.employeeSumTotal = this.employeeSumTotal.add(new BigDecimal(levyD.getEmployeeSum()));
        }

        this.employeeSumTotal = this.employeeSumTotal.setScale(2, RoundingMode.HALF_UP);
    }

    private void calculateEmployerTotal()
    {
        this.employerSumTotal = new BigDecimal("0");

        for (LevyD levyD : allLevyDS)
        {
            this.employerSumTotal = this.employerSumTotal.add(new BigDecimal(levyD.getEmployerSum()));
        }

        this.employerSumTotal = this.employerSumTotal.setScale(2, RoundingMode.HALF_UP);
    }

}
