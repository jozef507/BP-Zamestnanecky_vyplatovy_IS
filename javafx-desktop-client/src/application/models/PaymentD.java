package application.models;

import java.util.ArrayList;

public class PaymentD
{
    private String id, hoursFund, daysFund, hoursWorked, daysWorked, hwEmergency,
        grossWage, gwEmergency, assessmentBasis, employeeEnsurence, nonTaxableWage, taxableWage,
        taxAdvances, taxBonus, netWage,
        workPrice, employerLevies, employeeLevies, leviesSum,
        total, onAccount, cash;

    private String whoCreatedID;
    private String employeeImportantID;
    private String minimumWageID;
    private String wageConstantsID;

    private String employeeID, employeeLastnameName;
    private int IEmployeeID;
    private String relationID, relationType;
    private String conditionsID, weekTime;
    private String positionID, positionName;
    private String placeID, placeName;
    private String yearID, yearNumber, yearAverageWage,  yearHoliday, yearHolidayLastYear,yearHolideyTaken;
    private String monthID, monthNumber, monthIsClosed;

    private ArrayList<PaymentBasicComponentD> basicComponentDS;
    private ArrayList<PaymentDynamicComponentD> dynamicComponentDS;
    private ArrayList<PaymentWageCompensationD> wageCompensationDS;
    private ArrayList<PaymentSurchargeD> surchargeDS;
    private ArrayList<PaymentOtherComponentD> otherComponentDS;
    private ArrayList<LevyD> levyDS;
    private ArrayList<PaymentDeductionD> deductionDS;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoursFund() {
        return hoursFund;
    }

    public void setHoursFund(String hoursFund) {
        this.hoursFund = hoursFund;
    }

    public String getDaysFund() {
        return daysFund;
    }

    public void setDaysFund(String daysFund) {
        this.daysFund = daysFund;
    }

    public String getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(String hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(String daysWorked) {
        this.daysWorked = daysWorked;
    }

    public String getGrossWage() {
        return grossWage;
    }

    public void setGrossWage(String grossWage) {
        this.grossWage = grossWage;
    }

    public String getEmployeeEnsurence() {
        return employeeEnsurence;
    }

    public void setEmployeeEnsurence(String employeeEnsurence) {
        this.employeeEnsurence = employeeEnsurence;
    }

    public String getNonTaxableWage() {
        return nonTaxableWage;
    }

    public void setNonTaxableWage(String nonTaxableWage) {
        this.nonTaxableWage = nonTaxableWage;
    }

    public String getTaxableWage() {
        return taxableWage;
    }

    public void setTaxableWage(String taxableWage) {
        this.taxableWage = taxableWage;
    }

    public String getTaxAdvances() {
        return taxAdvances;
    }

    public void setTaxAdvances(String taxAdvances) {
        this.taxAdvances = taxAdvances;
    }

    public String getTaxBonus() {
        return taxBonus;
    }

    public void setTaxBonus(String taxBonus) {
        this.taxBonus = taxBonus;
    }

    public String getNetWage() {
        return netWage;
    }

    public void setNetWage(String netWage) {
        this.netWage = netWage;
    }

    public String getOnAccount() {
        return onAccount;
    }

    public void setOnAccount(String onAccount) {
        this.onAccount = onAccount;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getWorkPrice() {
        return workPrice;
    }

    public void setWorkPrice(String workPrice) {
        this.workPrice = workPrice;
    }

    public String getEmployerLevies() {
        return employerLevies;
    }

    public void setEmployerLevies(String employerLevies) {
        this.employerLevies = employerLevies;
    }

    public String getEmployeeLevies() {
        return employeeLevies;
    }

    public void setEmployeeLevies(String employeeLevies) {
        this.employeeLevies = employeeLevies;
    }

    public String getLeviesSum() {
        return leviesSum;
    }

    public void setLeviesSum(String leviesSum) {
        this.leviesSum = leviesSum;
    }

    public String getWhoCreatedID() {
        return whoCreatedID;
    }

    public void setWhoCreatedID(String whoCreatedID) {
        this.whoCreatedID = whoCreatedID;
    }

    public String getEmployeeImportantID() {
        return employeeImportantID;
    }

    public void setEmployeeImportantID(String employeeImportantID) {
        this.employeeImportantID = employeeImportantID;
    }

    public String getMinimumWageID() {
        return minimumWageID;
    }

    public void setMinimumWageID(String minimumWageID) {
        this.minimumWageID = minimumWageID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
        this.IEmployeeID = Integer.parseInt(employeeID);
    }

    public int getIEmployeeID() {
        return IEmployeeID;
    }

    public void setIEmployeeID(int IEmployeeID) {
        this.IEmployeeID = IEmployeeID;
    }

    public String getEmployeeLastnameName() {
        return employeeLastnameName;
    }

    public void setEmployeeLastnameName(String employeeLastnameName) {
        this.employeeLastnameName = employeeLastnameName;
    }

    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getConditionsID() {
        return conditionsID;
    }

    public void setConditionsID(String conditionsID) {
        this.conditionsID = conditionsID;
    }

    public String getPositionID() {
        return positionID;
    }

    public void setPositionID(String positionID) {
        this.positionID = positionID;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getYearID() {
        return yearID;
    }

    public void setYearID(String yearID) {
        this.yearID = yearID;
    }

    public String getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(String yearNumber) {
        this.yearNumber = yearNumber;
    }

    public String getMonthID() {
        return monthID;
    }

    public void setMonthID(String monthID) {
        this.monthID = monthID;
    }

    public String getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(String monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getMonthIsClosed() {
        return monthIsClosed;
    }

    public void setMonthIsClosed(String monthIsClosed) {
        this.monthIsClosed = monthIsClosed;
    }

    public String getAssessmentBasis() {
        return assessmentBasis;
    }

    public void setAssessmentBasis(String assessmentBasis) {
        this.assessmentBasis = assessmentBasis;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWageConstantsID() {
        return wageConstantsID;
    }

    public void setWageConstantsID(String wageConstantsID) {
        this.wageConstantsID = wageConstantsID;
    }

    public String getHwEmergency() {
        return hwEmergency;
    }

    public void setHwEmergency(String hwEmergency) {
        this.hwEmergency = hwEmergency;
    }

    public String getGwEmergency() {
        return gwEmergency;
    }

    public void setGwEmergency(String gwEmergency) {
        this.gwEmergency = gwEmergency;
    }

    public ArrayList<PaymentBasicComponentD> getBasicComponentDS() {
        return basicComponentDS;
    }

    public void setBasicComponentDS(ArrayList<PaymentBasicComponentD> basicComponentDS) {
        this.basicComponentDS = basicComponentDS;
    }

    public ArrayList<PaymentDynamicComponentD> getDynamicComponentDS() {
        return dynamicComponentDS;
    }

    public void setDynamicComponentDS(ArrayList<PaymentDynamicComponentD> dynamicComponentDS) {
        this.dynamicComponentDS = dynamicComponentDS;
    }

    public ArrayList<PaymentWageCompensationD> getWageCompensationDS() {
        return wageCompensationDS;
    }

    public void setWageCompensationDS(ArrayList<PaymentWageCompensationD> wageCompensationDS) {
        this.wageCompensationDS = wageCompensationDS;
    }

    public ArrayList<PaymentSurchargeD> getSurchargeDS() {
        return surchargeDS;
    }

    public void setSurchargeDS(ArrayList<PaymentSurchargeD> surchargeDS) {
        this.surchargeDS = surchargeDS;
    }

    public ArrayList<PaymentOtherComponentD> getOtherComponentDS() {
        return otherComponentDS;
    }

    public void setOtherComponentDS(ArrayList<PaymentOtherComponentD> otherComponentDS) {
        this.otherComponentDS = otherComponentDS;
    }

    public ArrayList<LevyD> getLevyDS() {
        return levyDS;
    }

    public void setLevyDS(ArrayList<LevyD> levyDS) {
        this.levyDS = levyDS;
    }

    public ArrayList<PaymentDeductionD> getDeductionDS() {
        return deductionDS;
    }

    public void setDeductionDS(ArrayList<PaymentDeductionD> deductionDS) {
        this.deductionDS = deductionDS;
    }

    public String getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(String weekTime) {
        this.weekTime = weekTime;
    }

    public String getYearHoliday() {
        return yearHoliday;
    }

    public void setYearHoliday(String yearHoliday) {
        this.yearHoliday = yearHoliday;
    }

    public String getYearHolidayLastYear() {
        return yearHolidayLastYear;
    }

    public void setYearHolidayLastYear(String yearHolidayLastYear) {
        this.yearHolidayLastYear = yearHolidayLastYear;
    }

    public String getYearHolideyTaken() {
        return yearHolideyTaken;
    }

    public void setYearHolideyTaken(String yearHolideyTaken) {
        this.yearHolideyTaken = yearHolideyTaken;
    }

    public String getYearAverageWage() {
        return yearAverageWage;
    }

    public void setYearAverageWage(String yearAverageWage) {
        this.yearAverageWage = yearAverageWage;
    }
}


