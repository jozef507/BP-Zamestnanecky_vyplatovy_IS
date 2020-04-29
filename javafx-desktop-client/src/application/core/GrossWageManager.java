package application.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class GrossWageManager {

    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS------------------------------------------------------*/
    private PaymentManager paymentManager;

    private WorkedPerformanceManager workedPerformanceManager;
    private BasicComponentManager basicComponentManager;
    private DynamicComponentManager dynamicComponentManager;
    private SurchargesManager surchargesManager;
    private WageCompensationManager wageCompensationManager;
    private OthersComponentsManager othersComponentsManager;
    private MinimalWageDeficitManager minimalWageDeficitManager;

    private BigDecimal averageWage;
    private BigDecimal dayAssessmentBasis;

    private BigDecimal grossWage;
    private BigDecimal assessmentBasis;

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS---------------------------------------------------*/
    public GrossWageManager (PaymentManager paymentManager){
        this.paymentManager = paymentManager;
        this.workedPerformanceManager = new WorkedPerformanceManager(this);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------SETTERS/GETTERS--------------------------------------------------*/

    public BasicComponentManager getBasicComponentManager() {
        return basicComponentManager;
    }

    public void setBasicComponentManager(BasicComponentManager basicComponentManager) {
        this.basicComponentManager = basicComponentManager;
    }

    public DynamicComponentManager getDynamicComponentManager() {
        return dynamicComponentManager;
    }

    public void setDynamicComponentManager(DynamicComponentManager dynamicComponentManager) {
        this.dynamicComponentManager = dynamicComponentManager;
    }

    public SurchargesManager getSurchargesManager() {
        return surchargesManager;
    }

    public void setSurchargesManager(SurchargesManager surchargesManager) {
        this.surchargesManager = surchargesManager;
    }

    public WageCompensationManager getWageCompensationManager() {
        return wageCompensationManager;
    }

    public void setWageCompensationManager(WageCompensationManager wageCompensationManager) {
        this.wageCompensationManager = wageCompensationManager;
    }

    public OthersComponentsManager getOthersComponentsManager() {
        return othersComponentsManager;
    }

    public void setOthersComponentsManager(OthersComponentsManager othersComponentsManager) {
        this.othersComponentsManager = othersComponentsManager;
    }

    public BigDecimal getGrossWage() {
        return grossWage;
    }

    public void setGrossWage(BigDecimal grossWage) {
        this.grossWage = grossWage;
    }

    public BigDecimal getAssessmentBasis() {
        return assessmentBasis;
    }

    public void setAssessmentBasis(BigDecimal assessmentBasis) {
        this.assessmentBasis = assessmentBasis;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public WorkedPerformanceManager getWorkedPerformanceManager() {
        return workedPerformanceManager;
    }

    public void setPaymentManager(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

    public void setWorkedPerformanceManager(WorkedPerformanceManager workedPerformanceManager) {
        this.workedPerformanceManager = workedPerformanceManager;
    }

    public BigDecimal getAverageWage() {
        return averageWage;
    }

    public void setAverageWage(BigDecimal averageWage) {
        this.averageWage = averageWage;
    }

    public BigDecimal getDayAssessmentBasis() {
        return dayAssessmentBasis;
    }

    public void setDayAssessmentBasis(BigDecimal dayAssessmentBasis) {
        this.dayAssessmentBasis = dayAssessmentBasis;
    }

    public MinimalWageDeficitManager getMinimalWageDeficitManager() {
        return minimalWageDeficitManager;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS------------------------------------------------------*/
    public void processImportantManagers()
    {
        if(this.basicComponentManager==null)
            this.basicComponentManager = new BasicComponentManager(this);
        if(this.dynamicComponentManager==null)
            this.dynamicComponentManager = new DynamicComponentManager(this);
    }

    public  void processSurchargesManager()
    {
        if(this.surchargesManager==null)
            this.surchargesManager = new SurchargesManager(this);
    }

    public  void processWageCompensationsManager()
    {
        if(this.wageCompensationManager==null)
            this.wageCompensationManager = new WageCompensationManager(this);
    }

    public  void processOtherComponentsManager()
    {
        if(this.othersComponentsManager==null)
            this.othersComponentsManager = new OthersComponentsManager(this);
    }

    public  void processMinimalWageDeficitManager()
    {
        if(this.minimalWageDeficitManager==null)
            this.minimalWageDeficitManager = new MinimalWageDeficitManager(this);
    }

    public void calculateGrossWageTotal()
    {
        BigDecimal grossWage = new BigDecimal("0");

        grossWage = grossWage
                .add(basicComponentManager.getBasicComponentsTotal())
                .add(dynamicComponentManager.getDynamicComponentsTotal())
                .add(surchargesManager.getSurchargesTotal())
                .add(wageCompensationManager.getCompensationsTotal())
                .add(othersComponentsManager.getOtherComponentsTotal())
                .add(new BigDecimal(minimalWageDeficitManager.getPaymentOtherComponentD().getWage()))
                .setScale(2, RoundingMode.HALF_UP);

        this.grossWage = grossWage;
        this.assessmentBasis = grossWage;
    }


}



