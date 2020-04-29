package application.core;

import application.httpcomunication.LoggedInUser;
import javafx.fxml.FXML;

import javax.net.ssl.ManagerFactoryParameters;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class NetWageManager
{
    PaymentManager paymentManager;

    Map<String,BigDecimal[]> levie;
    boolean halfOfHealthInsurence;
    BigDecimal deductionFromAssessmentBasis;


    BigDecimal assessmentBasis;

    LeviesSocialInsuranceManager leviesSocialInsuranceManager;

    BigDecimal deductibleItem;
    LeviesHealthInsuranceManager leviesHealthInsuranceManager;

    BigDecimal employeeLeviesTotal;
    BigDecimal employerLeviesTotal;
    BigDecimal workPrice;

    BigDecimal partialTaxBase;

    BigDecimal nonTaxablePartOfTaxBase;
    BigDecimal taxableWage;

    BigDecimal taxRate;
    BigDecimal incomeTaxAdvance;

    BigDecimal taxBonusForChild;
    BigDecimal netWage;

    DeductionsManager deductionsManager;

    BigDecimal sumTotal;
    BigDecimal toBankAccountFromTotal;
    BigDecimal inCashFromTotal;


    public NetWageManager(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

    public DeductionsManager getDeductionsManager() {
        return deductionsManager;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public Map<String, BigDecimal[]> getLevie() {
        return levie;
    }

    public boolean isHalfOfHealthInsurence() {
        return halfOfHealthInsurence;
    }

    public BigDecimal getDeductionFromAssessmentBasis() {
        return deductionFromAssessmentBasis;
    }

    public BigDecimal getAssessmentBasis() {
        return assessmentBasis;
    }

    public LeviesSocialInsuranceManager getLeviesSocialInsuranceManager() {
        return leviesSocialInsuranceManager;
    }

    public BigDecimal getDeductibleItem() {
        return deductibleItem;
    }

    public LeviesHealthInsuranceManager getLeviesHealthInsuranceManager() {
        return leviesHealthInsuranceManager;
    }

    public BigDecimal getPartialTaxBase() {
        return partialTaxBase;
    }

    public BigDecimal getNonTaxablePartOfTaxBase() {
        return nonTaxablePartOfTaxBase;
    }

    public BigDecimal getTaxableWage() {
        return taxableWage;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public BigDecimal getIncomeTaxAdvance() {
        return incomeTaxAdvance;
    }

    public BigDecimal getTaxBonusForChild() {
        return taxBonusForChild;
    }

    public BigDecimal getNetWage() {
        return netWage;
    }

    public BigDecimal getSumTotal() {
        return sumTotal;
    }

    public BigDecimal getToBankAccountFromTotal() {
        return toBankAccountFromTotal;
    }

    public BigDecimal getInCashFromTotal() {
        return inCashFromTotal;
    }

    public BigDecimal getEmployeeLeviesTotal() {
        return employeeLeviesTotal;
    }

    public BigDecimal getEmployerLeviesTotal() {
        return employerLeviesTotal;
    }

    public BigDecimal getWorkPrice() {
        return workPrice;
    }

    public void process()
    {
        assessmentBasis = paymentManager.getGrossWageManager().getGrossWage();
        employeeLeviesTotal = new BigDecimal("0");
        employerLeviesTotal = new BigDecimal("0");
        workPrice = new BigDecimal("0");
        deductibleItem = new BigDecimal("0");
        partialTaxBase = new BigDecimal("0");
        nonTaxablePartOfTaxBase = new BigDecimal("0");
        taxableWage = new BigDecimal("0");
        taxRate = new BigDecimal("0");
        incomeTaxAdvance = new BigDecimal("0");
        taxBonusForChild = new BigDecimal("0");
        netWage = new BigDecimal("0");
        sumTotal = new BigDecimal("0");
        toBankAccountFromTotal = new BigDecimal("0");
        inCashFromTotal = new BigDecimal("0");

        deductionFromAssessmentBasis = new BigDecimal("200");
        halfOfHealthInsurence = false;
        determineAboutLevie();

        leviesSocialInsuranceManager = new LeviesSocialInsuranceManager(this);
        leviesHealthInsuranceManager = new LeviesHealthInsuranceManager(this);

        calculateNetWage();
    }

    private void determineAboutLevie()
    {
        Levies levies = new Levies();
        if(paymentManager.getEmployeeConditionsManager().getConditionsD().getRetirement().equals("1"))
        {
            if(paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("PP:"))
            {
                levie = levies.getSD1A();
                if(paymentManager.getEmployeeConditionsManager().getConditionsD().getDisabled().equals("1"))
                    halfOfHealthInsurence = true;
            }
            else if (paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("D:"))
            {
                if(paymentManager.getEmployeeConditionsManager().getConditionsD().getExemption().equals("0"))
                {
                    levie = levies.getSD2B();
                }
                else
                {
                    if(this.assessmentBasis.compareTo(new BigDecimal("200"))>0)
                    {
                        levie = levies.getSD2B();
                        deductionFromAssessmentBasis = new BigDecimal("200");
                    }
                    else
                    {
                        levie = levies.getSD2A();
                    }
                }
            }
        }
        else if(paymentManager.getEmployeeConditionsManager().getConditionsD().getInvalidity40().equals("1")
            || paymentManager.getEmployeeConditionsManager().getConditionsD().getInvalidity70().equals("1"))
        {
            if(paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("PP:"))
            {
                if(paymentManager.getEmployeeConditionsManager().getConditionsD().getInvalidity40().equals("1"))
                    levie = levies.getID1A();
                else
                    levie = levies.getID1B();
            }
            else if (paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("D:"))
            {
                if(paymentManager.getEmployeeConditionsManager().getConditionsD().getExemption().equals("0"))
                {
                    levie = levies.getID2A();
                }
                else
                {
                    if(this.assessmentBasis.compareTo(new BigDecimal("200"))>0)
                    {
                        levie = levies.getID2B();
                        deductionFromAssessmentBasis = new BigDecimal("200");
                    }
                    else
                    {
                        levie = levies.getID2A();
                    }
                }
            }
        }
        else if(paymentManager.getEmployeeConditionsManager().getConditionsD().getPremature().equals("1"))
        {
            if(paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("PP:"))
            {
                levie = levies.getPD1A();
                if(paymentManager.getEmployeeConditionsManager().getConditionsD().getDisabled().equals("1"))
                    halfOfHealthInsurence = true;
            }
            else if (paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("D:"))
            {
                if(paymentManager.getEmployeeConditionsManager().getConditionsD().getExemption().equals("0"))
                {
                    levie = levies.getPD2C();
                    if(paymentManager.getEmployeeConditionsManager().getConditionsD().getDisabled().equals("1"))
                        halfOfHealthInsurence = true;
                }
                else
                {
                    if(this.assessmentBasis.compareTo(new BigDecimal("200"))>0)
                    {
                        levie = levies.getPD2B();
                        deductionFromAssessmentBasis = new BigDecimal("200");
                    }
                    else
                    {
                        levie = levies.getPD2A();
                    }
                }
            }
        }
        else
        {
            if(paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("PP:"))
            {
                levie = levies.getNZ1A();
                if(paymentManager.getEmployeeConditionsManager().getConditionsD().getDisabled().equals("1"))
                    halfOfHealthInsurence = true;
            }
            else if (paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("D:"))
            {
                if(paymentManager.getEmployeeConditionsManager().getRelationD().getType().equals("D: o brig. práci študentov"))
                {
                    if(paymentManager.getEmployeeConditionsManager().getConditionsD().getExemption().equals("0"))
                    {
                        levie = levies.getNZ2C2();
                    }
                    else
                    {
                        if(this.assessmentBasis.compareTo(new BigDecimal("200"))>0)
                        {
                            levie = levies.getNZ2C2();
                            deductionFromAssessmentBasis = new BigDecimal("200");
                        }
                        else
                        {
                            levie = levies.getNZ2C1();
                        }
                    }
                }
                else if (paymentManager.getEmployeeConditionsManager().getWageDS().get(0).getPayWay().equals("pravidelne"))
                {
                    levie = levies.getNZ2A();
                    if(paymentManager.getEmployeeConditionsManager().getConditionsD().getDisabled().equals("1"))
                        halfOfHealthInsurence = true;
                }
                else if (paymentManager.getEmployeeConditionsManager().getWageDS().get(0).getPayWay().equals("nepravidelne"))
                {
                    levie = levies.getNZ2B();
                    if(paymentManager.getEmployeeConditionsManager().getConditionsD().getDisabled().equals("1"))
                        halfOfHealthInsurence = true;
                }

            }
        }
    }


    private void calculateNetWage()
    {
        leviesSocialInsuranceManager.calculateLevies(assessmentBasis.subtract(deductionFromAssessmentBasis), levie);

        calculateDeductibleItem();
        leviesHealthInsuranceManager.calculateLevies(assessmentBasis.subtract(deductionFromAssessmentBasis)
                .subtract(deductibleItem), levie, halfOfHealthInsurence);

        employeeLeviesTotal = leviesHealthInsuranceManager.getEmployeeSumTotal().add(leviesSocialInsuranceManager.getEmployeeSumTotal());
        employerLeviesTotal = leviesHealthInsuranceManager.getEmployerSumTotal().add(leviesSocialInsuranceManager.getEmployerSumTotal());
        workPrice = assessmentBasis.add(employerLeviesTotal);

        partialTaxBase = assessmentBasis
                .subtract(leviesSocialInsuranceManager.getEmployeeSumTotal())
                .subtract(leviesHealthInsuranceManager.getEmployeeSumTotal())
                .setScale(2, RoundingMode.HALF_UP);

        calculateNonTaxablePartOfTaxBase();
        taxableWage = partialTaxBase.subtract(nonTaxablePartOfTaxBase);

        calculateTaxRate();
        incomeTaxAdvance = taxableWage.multiply(taxRate);

        calculateTaxBonusForChild();
        netWage = assessmentBasis
                .subtract(leviesSocialInsuranceManager.getEmployeeSumTotal())
                .subtract(leviesHealthInsuranceManager.getEmployeeSumTotal())
                .subtract(incomeTaxAdvance)
                .add(taxBonusForChild)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void processDeductionManager()
    {
        if(this.deductionsManager == null)
            this.deductionsManager = new DeductionsManager(this);
    }

    public void calculateSumTotal()
    {
        sumTotal = new BigDecimal("0");
        sumTotal = netWage.subtract(deductionsManager.getDeductionsTotal()).setScale(2, RoundingMode.HALF_UP);

        BigDecimal bankPart = null;
        if(paymentManager.getEmployeeConditionsManager().getConditionsD().getBank().equals("1"))
        {
            bankPart = new BigDecimal(paymentManager.getEmployeeConditionsManager().getConditionsD().getBankPart());
        }
        else
        {
            bankPart = new BigDecimal("0");
        }

        BigDecimal cashPart = new BigDecimal("1").subtract(bankPart);

        this.toBankAccountFromTotal = sumTotal.multiply(bankPart).setScale(2, RoundingMode.HALF_UP);
        this.inCashFromTotal = sumTotal.multiply(cashPart).setScale(2, RoundingMode.HALF_UP);

        setPaymentModel();
    }

    private void calculateDeductibleItem()
    {
        if(paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("PP:")) {
            if (paymentManager.getEmployeeConditionsManager().getNextConditionsD().getDeductableItem().equals("1"))
            {
                BigDecimal maxAssessmentBasisForDeductibleItem = new BigDecimal(this.getPaymentManager().getWageConstantsD()
                        .getMaxAssessmentBasisOP());
                if(assessmentBasis.compareTo(maxAssessmentBasisForDeductibleItem)<0)
                {
                    BigDecimal maxDeductibleItem = new BigDecimal(this.getPaymentManager().getWageConstantsD()
                            .getMaxOP());
                    if(assessmentBasis.compareTo(maxDeductibleItem)<0)
                    {
                        this.deductibleItem = this.assessmentBasis;
                    }
                    else
                    {
                        this.deductibleItem = maxDeductibleItem.subtract(this.assessmentBasis.subtract(maxDeductibleItem).multiply(new BigDecimal("2")));
                        if(this.deductibleItem.compareTo(maxDeductibleItem)>0)
                            this.deductibleItem = maxDeductibleItem;
                    }
                }

            }
        }
    }

    private void calculateNonTaxablePartOfTaxBase()
    {
        if(paymentManager.getEmployeeConditionsManager().getConditionsD().getTaxFree().equals("1"))
        {
            this.nonTaxablePartOfTaxBase = new BigDecimal(paymentManager.getWageConstantsD().getNonTaxablePart());
        }
    }

    private void calculateTaxRate()
    {
        BigDecimal multipleOfLivingMinimum = new BigDecimal(paymentManager.getWageConstantsD()
                .getSubsistenceMinimumForAdvances());
        if(taxableWage.compareTo(multipleOfLivingMinimum)<=0)
        {
            this.taxRate = new BigDecimal("0.19");
        }
        else
        {
            this.taxRate = new BigDecimal("0.25");
        }
    }

    private void calculateTaxBonusForChild()
    {
        BigDecimal childrenUnder = new BigDecimal(paymentManager.getEmployeeConditionsManager().getImportantD().getChildUnder());
        BigDecimal childrenOber = new BigDecimal(paymentManager.getEmployeeConditionsManager().getImportantD().getChildOver());

        BigDecimal taxBonusForUnder = new BigDecimal(paymentManager.getWageConstantsD().getTaxBonusUnder6());
        BigDecimal taxBonusForOver = new BigDecimal(paymentManager.getWageConstantsD().getTaxBonusOver6());

        this.taxBonusForChild = childrenUnder.multiply(taxBonusForUnder).add(childrenOber.multiply(taxBonusForOver));
    }

    private void setPaymentModel()
    {
        paymentManager.getPaymentD().setAssessmentBasis(assessmentBasis.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setEmployeeEnsurence(employeeLeviesTotal.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setNonTaxableWage(nonTaxablePartOfTaxBase.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setTaxableWage(taxableWage.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setTaxAdvances(incomeTaxAdvance.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setTaxBonus(taxBonusForChild.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setNetWage(netWage.setScale(2, RoundingMode.HALF_UP).toPlainString());

        paymentManager.getPaymentD().setWorkPrice(workPrice.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setEmployerLevies(employerLeviesTotal.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setEmployeeLevies(employeeLeviesTotal.add(incomeTaxAdvance).setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setLeviesSum(employeeLeviesTotal.add(incomeTaxAdvance).add(employerLeviesTotal).setScale(2, RoundingMode.HALF_UP).toPlainString());

        paymentManager.getPaymentD().setTotal(sumTotal.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setOnAccount(toBankAccountFromTotal.setScale(2, RoundingMode.HALF_UP).toPlainString());
        paymentManager.getPaymentD().setCash(inCashFromTotal.setScale(2, RoundingMode.HALF_UP).toPlainString());

        paymentManager.getPaymentD().setEmployeeImportantID(paymentManager.getEmployeeConditionsManager().getImportantD().getId());
        paymentManager.getPaymentD().setWhoCreatedID(LoggedInUser.getId());
        if(paymentManager.getEmployeeConditionsManager().getRelationD().getType().contains("D:"))
            paymentManager.getPaymentD().setMinimumWageID(paymentManager.getBasicMinimumWageD().getId());
        else
            paymentManager.getPaymentD().setMinimumWageID(paymentManager.getEmployeeConditionsManager().getMinimumWageD().getId());
        paymentManager.getPaymentD().setWageConstantsID(paymentManager.getWageConstantsD().getId());

    }
}
