package application.models;

public class WageD
{
    String id, label, employeeEnter, timeImportant, emergencyImportant, tarif, payWay, payDate;
    String wageFormID, wageFormName, wageFormUnit, wageFormUnitShort;
    String conditionsID;

    public WageD() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEmployeeEnter() {
        if(employeeEnter.equals("0"))
            return "nie";
        else
            return "áno";    }

    public void setEmployeeEnter(String employeeEnter) {
        this.employeeEnter = employeeEnter;
    }

    public String getTimeImportant() {
        if(timeImportant.equals("0"))
            return "nie";
        else
            return "áno";
    }

    public void setTimeImportant(String timeImportant) {
        this.timeImportant = timeImportant;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayDate() {
        if(payDate==null || payDate.equals("0000-00-00"))
            return "";
        else
            return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getWageFormID() {
        return wageFormID;
    }

    public void setWageFormID(String wageFormID) {
        this.wageFormID = wageFormID;
    }

    public String getConditionsID() {
        return conditionsID;
    }

    public void setConditionsID(String conditionsID) {
        this.conditionsID = conditionsID;
    }

    public String getWageFormName() {
        return wageFormName;
    }

    public void setWageFormName(String wageFormName) {
        this.wageFormName = wageFormName;
    }

    public String getWageFormUnit() {
        return wageFormUnit;
    }

    public void setWageFormUnit(String getWageFormUnit) {
        this.wageFormUnit = getWageFormUnit;
    }

    public String getWageFormUnitShort() {
        return wageFormUnitShort;
    }

    public void setWageFormUnitShort(String getGetWageFormUnitShort) {
        this.wageFormUnitShort = getGetWageFormUnitShort;
    }

    public String getEmergencyImportant() {
        if(emergencyImportant.equals("0"))
            return "nie";
        else
            return "áno";
    }

    public void setEmergencyImportant(String emergencyImportant) {
        this.emergencyImportant = emergencyImportant;
    }


}
