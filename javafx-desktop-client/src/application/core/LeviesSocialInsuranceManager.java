package application.core;

import application.models.LevyD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;

public class LeviesSocialInsuranceManager
{
    NetWageManager netWageManager;

    BigDecimal maxAssessmentBasis;

    LevyD sickness;
    LevyD retirement;
    LevyD disability;
    LevyD unemployment;
    LevyD guarantee;
    LevyD accident;
    LevyD solidarity;

    ArrayList<LevyD> allLevyDS;

    BigDecimal employeeSumTotal;
    BigDecimal employerSumTotal;

    public LeviesSocialInsuranceManager(NetWageManager netWageManager) {
        this.netWageManager = netWageManager;

        this.maxAssessmentBasis = new BigDecimal(netWageManager.getPaymentManager().getWageConstantsD()
                  .getMaxAssessmentBasis());

        this.sickness = new LevyD("Nemoc. pois.", "0", "0", "0");
        this.retirement = new LevyD("Starob. pois.", "0", "0", "0");
        this.disability = new LevyD("Inval. pois.", "0", "0", "0");
        this.unemployment = new LevyD("Pois. v nezam.", "0", "0", "0");
        this.guarantee = new LevyD("Úraz. pois.", "0", "0", "0");
        this.accident = new LevyD("Garan. pois.", "0", "0", "0");
        this.solidarity = new LevyD("Rezer. fond.", "0", "0", "0");

        this.allLevyDS = new ArrayList<>();
        this.allLevyDS.add(sickness);
        this.allLevyDS.add(retirement);
        this.allLevyDS.add(disability);
        this.allLevyDS.add(unemployment);
        this.allLevyDS.add(guarantee);
        this.allLevyDS.add(accident);
        this.allLevyDS.add(solidarity);

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

    public LevyD getSickness() {
        return sickness;
    }

    public LevyD getRetirement() {
        return retirement;
    }

    public LevyD getDisability() {
        return disability;
    }

    public LevyD getUnemployment() {
        return unemployment;
    }

    public LevyD getGuarantee() {
        return guarantee;
    }

    public LevyD getAccident() {
        return accident;
    }

    public LevyD getSolidarity() {
        return solidarity;
    }

    public void calculateLevies(BigDecimal assessmentBasis, Map<String,BigDecimal[]> levie)
    {
        BigDecimal updatedAssessmentBasis = assessmentBasis;
        if(updatedAssessmentBasis.compareTo(maxAssessmentBasis)>0)
            updatedAssessmentBasis = maxAssessmentBasis;

        sickness.calculate(updatedAssessmentBasis, levie.get("nemocenske"));
        retirement.calculate(updatedAssessmentBasis, levie.get("starobne"));
        disability.calculate(updatedAssessmentBasis, levie.get("invalidne"));
        unemployment.calculate(updatedAssessmentBasis, levie.get("nezamestnanost"));
        guarantee.calculate(updatedAssessmentBasis, levie.get("garancne"));
        accident.calculate(assessmentBasis, levie.get("urazove"));
        solidarity.calculate(updatedAssessmentBasis, levie.get("solidarita"));

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
