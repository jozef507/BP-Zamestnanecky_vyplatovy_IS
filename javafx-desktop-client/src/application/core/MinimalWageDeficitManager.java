package application.core;

import application.models.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MinimalWageDeficitManager
{
    private GrossWageManager grossWageManager;

    private PaymentOtherComponentD paymentOtherComponentD;


    public MinimalWageDeficitManager(GrossWageManager grossWageManager) {
        this.grossWageManager = grossWageManager;
        this.paymentOtherComponentD = new PaymentOtherComponentD();
        this.paymentOtherComponentD.setName("Dopl. do MM");
        this.paymentOtherComponentD.setWage("0");
        calculateMinimalWageDeficitTotal();
    }



    public GrossWageManager getGrossWageManager() {
        return grossWageManager;
    }

    public void setGrossWageManager(GrossWageManager grossWageManager) {
        this.grossWageManager = grossWageManager;
    }

    public PaymentOtherComponentD getPaymentOtherComponentD() {
        return paymentOtherComponentD;
    }

    private void calculateMinimalWageDeficitTotal()
    {
        EmployeeConditionsManager employeeConditionsManager = this.grossWageManager.getPaymentManager()
                .getEmployeeConditionsManager();

        if(employeeConditionsManager.getRelationD().getType().equals("PP: na plný úväzok")
            || employeeConditionsManager.getRelationD().getType().equals("PP: na kratší pracovný čas")
            || employeeConditionsManager.getRelationD().getType().equals("PP: telepráca"))
        {

            Boolean isItMonthWage = getIsItMonthWage();
            if(isItMonthWage)
            {
                BigDecimal workedTime = new BigDecimal("0");
                BigDecimal wage = new BigDecimal("0");
                for (PaymentBasicComponentD w : grossWageManager.getBasicComponentManager().getPaymentBasicComponentDS())
                {
                    if(w.getWageD() == null)
                    {
                        if(w.getWorkedPerformanceOfWage().getWageD().getTimeImportant().equals("áno"))
                        {
                            BigDecimal tmp = w.getWorkedPerformanceOfWage().getTimeTotal()
                                    .subtract(w.getWorkedPerformanceOfWage().getOvertimeFromTotal())
                                    .subtract(w.getWorkedPerformanceOfWage().getActiveEmergencyTimeFromTotal())
                                    .subtract(w.getWorkedPerformanceOfWage().getInactiveEmergencyInPlaceFromTotal());
                            workedTime = workedTime.add(tmp);

                            BigDecimal tmp1 = tmp.divide(new BigDecimal(grossWageManager.getPaymentManager()
                                    .getPaymentD().getHoursFund()), 4, RoundingMode.HALF_UP)
                                    .multiply(new BigDecimal(w.getWorkedPerformanceOfWage().getWageD().getTarif()));
                            wage = wage.add(tmp1);
                        }
                        else
                        {
                            wage = wage.add(new BigDecimal(w.getWageForUnits()));
                        }
                    }
                    else
                    {
                        wage = wage.add(new BigDecimal(w.getWageForUnits()));
                    }
                }


                for (PaymentDynamicComponentD w : grossWageManager.getDynamicComponentManager().getPaymentDynamicComponentDS())
                {
                    wage = wage.add(new BigDecimal(w.getWage()));
                }

                BigDecimal minimalWage = new BigDecimal(grossWageManager.getPaymentManager().getEmployeeConditionsManager()
                        .getMinimumWageD().getMonthValue());

                minimalWage = workedTime.divide(new BigDecimal(grossWageManager.getPaymentManager().getPaymentD().getHoursFund()), 4, RoundingMode.HALF_UP)
                        .multiply(minimalWage);

                BigDecimal deficit = minimalWage.subtract(wage);
                if(deficit.compareTo(new BigDecimal("0"))>0)
                    paymentOtherComponentD.setWage(deficit.setScale(2, RoundingMode.HALF_UP).toPlainString());

            }
            else if(getIsItHourWage())
            {
                BigDecimal workedTime = new BigDecimal("0");
                BigDecimal wage = new BigDecimal("0");
                BigDecimal timeTotal = new BigDecimal("0");

                for (PaymentBasicComponentD w : grossWageManager.getBasicComponentManager().getPaymentBasicComponentDS())
                {
                    if(w.getWageD() == null)
                    {
                        if(w.getWorkedPerformanceOfWage().getWageD().getTimeImportant().equals("áno"))
                        {
                            timeTotal = w.getWorkedPerformanceOfWage().getTimeTotal();
                            BigDecimal tmp = w.getWorkedPerformanceOfWage().getTimeTotal()
                                    .subtract(w.getWorkedPerformanceOfWage().getOvertimeFromTotal())
                                    .subtract(w.getWorkedPerformanceOfWage().getActiveEmergencyTimeFromTotal())
                                    .subtract(w.getWorkedPerformanceOfWage().getInactiveEmergencyInPlaceFromTotal());
                            workedTime = workedTime.add(tmp);

                            BigDecimal tmp1 = tmp.multiply(new BigDecimal(w.getWorkedPerformanceOfWage().getWageD().getTarif()));
                            wage = wage.add(tmp1);
                        }
                        else
                        {
                            wage = wage.add(new BigDecimal(w.getWageForUnits()));
                        }
                    }
                    else
                    {
                        wage = wage.add(new BigDecimal(w.getWageForUnits()));
                    }
                }

                for (PaymentDynamicComponentD w : grossWageManager.getDynamicComponentManager().getPaymentDynamicComponentDS())
                {
                    wage = wage.add(new BigDecimal(w.getWage()));
                }

                BigDecimal minimalWage = new BigDecimal(grossWageManager.getPaymentManager().getEmployeeConditionsManager()
                        .getMinimumWageD().getHourValue());
                minimalWage = minimalWage.multiply(new BigDecimal("40").divide(new BigDecimal(
                        grossWageManager.getPaymentManager().getEmployeeConditionsManager().getNextConditionsD().getApWeekTime())));

                wage = wage.divide(workedTime, 4, RoundingMode.HALF_UP);

                BigDecimal deficit = minimalWage.subtract(wage);
                if(deficit.compareTo(new BigDecimal("0"))>0)
                    paymentOtherComponentD.setWage(deficit.multiply(timeTotal).setScale(2, RoundingMode.HALF_UP).toPlainString());
            }
            else
            {
                BigDecimal workedTime = new BigDecimal("0");
                BigDecimal wage = new BigDecimal("0");
                BigDecimal timeTotal = new BigDecimal("0");

                for (PaymentBasicComponentD w : grossWageManager.getBasicComponentManager().getPaymentBasicComponentDS())
                {
                    if(w.getWageD() == null)
                    {
                        if(w.getWorkedPerformanceOfWage().getWageD().getTimeImportant().equals("áno"))
                        {
                            timeTotal = w.getWorkedPerformanceOfWage().getTimeTotal();
                            BigDecimal tmp = w.getWorkedPerformanceOfWage().getTimeTotal()
                                    .subtract(w.getWorkedPerformanceOfWage().getOvertimeFromTotal())
                                    .subtract(w.getWorkedPerformanceOfWage().getActiveEmergencyTimeFromTotal())
                                    .subtract(w.getWorkedPerformanceOfWage().getInactiveEmergencyInPlaceFromTotal());
                            workedTime = workedTime.add(tmp);

                            wage = wage.add(new BigDecimal(w.getWageForUnits()));
                        }
                        else
                        {
                            wage = wage.add(new BigDecimal(w.getWageForUnits()));
                        }
                    }
                    else
                    {
                        wage = wage.add(new BigDecimal(w.getWageForUnits()));
                    }
                }

                for (PaymentDynamicComponentD w : grossWageManager.getDynamicComponentManager().getPaymentDynamicComponentDS())
                {
                    wage = wage.add(new BigDecimal(w.getWage()));
                }

                BigDecimal minimalWage = new BigDecimal(grossWageManager.getPaymentManager().getEmployeeConditionsManager()
                        .getMinimumWageD().getHourValue());
                minimalWage = minimalWage.multiply(new BigDecimal("40").divide(new BigDecimal(
                        grossWageManager.getPaymentManager().getEmployeeConditionsManager().getNextConditionsD().getApWeekTime())));

                wage = wage.divide(workedTime, 4, RoundingMode.HALF_UP);

                BigDecimal deficit = minimalWage.subtract(wage);
                if(deficit.compareTo(new BigDecimal("0"))>0)
                    paymentOtherComponentD.setWage(deficit.multiply(timeTotal).setScale(2, RoundingMode.HALF_UP).toPlainString());
            }
        }

        setPaymentModel();
    }

    private boolean getIsItMonthWage()
    {
        for (WageD w : grossWageManager.getPaymentManager().getEmployeeConditionsManager().getWageDS())
        {
            if(w.getWageFormName().equals("časová") && w.getWageFormUnit().equals("mesiac"))
                return true;
        }
        return false;
    }

    private boolean getIsItHourWage()
    {
        for (WageD w : grossWageManager.getPaymentManager().getEmployeeConditionsManager().getWageDS())
        {
            if(w.getWageFormName().equals("časová") && w.getWageFormUnit().equals("hodina"))
                return true;
        }
        return false;
    }

    private void setPaymentModel()
    {
        grossWageManager.getPaymentManager().getPaymentD().setGrossWage(grossWageManager.getGrossWage().setScale(2, RoundingMode.HALF_UP).toPlainString());

        WorkedPerformanceOfWage workedPerformanceOfWage = null;
        for(WorkedPerformanceOfWage w : grossWageManager.getWorkedPerformanceManager().getWorkedPerformanceOfWages())
        {
            if(w.getWageD().getTimeImportant().equals("áno"))
            {
                workedPerformanceOfWage = w;
            }
        }

        grossWageManager.getPaymentManager().getPaymentD().setHoursWorked(workedPerformanceOfWage.getTimeTotal().setScale(0, RoundingMode.HALF_UP).toPlainString());
        grossWageManager.getPaymentManager().getPaymentD().setDaysWorked((workedPerformanceOfWage.getDaysTotal().toPlainString()));
    }


}
