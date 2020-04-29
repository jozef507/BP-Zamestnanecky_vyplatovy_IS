package application.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LevyD
{
    private String id, name, assessmentBasis, employeeSum, employerSum, paymentID;

    public LevyD() {
    }

    public LevyD(String name, String assessmentBasis, String employeeSum, String employerSum) {
        this.name = name;
        this.assessmentBasis = assessmentBasis;
        this.employeeSum = employeeSum;
        this.employerSum = employerSum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssessmentBasis() {
        return assessmentBasis;
    }

    public void setAssessmentBasis(String assessmentBasis) {
        this.assessmentBasis = assessmentBasis;
    }

    public String getEmployeeSum() {
        return employeeSum;
    }

    public void setEmployeeSum(String employeeSum) {
        this.employeeSum = employeeSum;
    }

    public String getEmployerSum() {
        return employerSum;
    }

    public void setEmployerSum(String employerSum) {
        this.employerSum = employerSum;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public void calculate(BigDecimal assesmentBasis, BigDecimal[] partsOfLevies)
    {
        if (partsOfLevies.length != 2)
            return;

        BigDecimal employeePart = partsOfLevies[0];
        BigDecimal employerPart = partsOfLevies[1];

        BigDecimal employeeLevy = employeePart.multiply(assesmentBasis);
        BigDecimal employerLevy = employerPart.multiply(assesmentBasis);

        this.assessmentBasis = assesmentBasis.setScale(2, RoundingMode.HALF_UP).toPlainString();
        this.employeeSum = employeeLevy.setScale(2, RoundingMode.HALF_UP).toPlainString();
        this.employerSum = employerLevy.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
