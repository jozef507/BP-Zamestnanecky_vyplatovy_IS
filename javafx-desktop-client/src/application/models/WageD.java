package application.models;

public class WageD
{
    String id, label, employeeEnter, timeImportant, tarif, payWay, payDate, wageFormID, conditionsID;

    public WageD() {
    }

    public WageD(String id, String label, String employeeEnter, String timeImportant, String tarif, String payWay, String payDate, String wageFormID, String conditionsID) {
        this.id = id;
        this.label = label;
        this.employeeEnter = employeeEnter;
        this.timeImportant = timeImportant;
        this.tarif = tarif;
        this.payWay = payWay;
        if(payDate==null || payDate.equals("00.00.0000"))
            this.payDate = "žiadne";
        else
            this.payDate = payDate;
        this.wageFormID = wageFormID;
        this.conditionsID = conditionsID;
    }

    public WageD(String label, String employeeEnter, String timeImportant, String tarif, String payWay, String payDate, String wageFormID, String conditionsID) {
        this.label = label;
        this.employeeEnter = employeeEnter;
        this.timeImportant = timeImportant;
        this.tarif = tarif;
        this.payWay = payWay;
        if(payDate==null || payDate.equals("00.00.0000"))
            this.payDate = "žiadne";
        else
            this.payDate = payDate;
        this.wageFormID = wageFormID;
        this.conditionsID = conditionsID;
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
}
