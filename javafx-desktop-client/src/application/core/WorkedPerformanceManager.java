package application.core;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.HoursD;
import application.models.MonthD;
import application.models.WageD;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class WorkedPerformanceManager
{
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS------------------------------------------------------*/
    private GrossWageManager grossWageManager;
    private ArrayList<WorkedPerformanceOfWage> workedPerformanceOfWages;


    /*----------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS---------------------------------------------------*/
    public WorkedPerformanceManager(GrossWageManager grossWageManager) {
        this.grossWageManager = grossWageManager;
        this.workedPerformanceOfWages = new ArrayList<>();
        if (!setWorkedPerformances()) return;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------SETTERS/GETTERS--------------------------------------------------*/

    public GrossWageManager getGrossWageManager() {
        return grossWageManager;
    }

    public ArrayList<WorkedPerformanceOfWage> getWorkedPerformanceOfWages() {
        return workedPerformanceOfWages;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS------------------------------------------------------*/
    private boolean setWorkedPerformances()
    {
        ArrayList<String> httpResponses;
        try {
            httpResponses = downloadPerformance();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return false;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return false;
        }

        for(int i = 0; i<httpResponses.size();i++)
        {
            try{
                setNewWorkedPerformanceOfWage(httpResponses.get(i));
            } catch (Exception e) {
                e.printStackTrace();
                CustomAlert a = new CustomAlert("error", "Chyba",
                        "", e.getMessage());
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> downloadPerformance() throws InterruptedException, IOException, CommunicationException
    {
        ArrayList<String> httpResponses = new ArrayList<>();
        ArrayList<MonthD> monthsDS = grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMonthDS();
        for (int i = 0; i < monthsDS.size(); i++) {
            HttpClientClass ht = new HttpClientClass();
            ht.sendGet("hours/emp-hrs-mnth/" + monthsDS.get(i).getId(), LoggedInUser.getToken(), LoggedInUser.getId());
            httpResponses.add(ht.getRespnseBody());
        }
        return httpResponses;
    }

    private void setNewWorkedPerformanceOfWage(String response) throws Exception
    {
        JsonArrayClass json = new JsonArrayClass(response);

        String prevWageID = "-1";
        WorkedPerformanceOfWage currentWPFW = null;
        for(int i = 0; i<json.getSize(); i++)
        {
            if(!json.getElement(i, "zm_id").equals(prevWageID))
            {
                String wageID = json.getElement(i, "zm_id");
                String monthID = json.getElement(i, "om_id");
                WageD wageD = this.grossWageManager.getPaymentManager().getEmployeeConditionsManager().getWageDS()
                        .stream().filter(wage -> wageID.equals(wage.getId())).findFirst().orElse(null);
                MonthD monthD = this.grossWageManager.getPaymentManager().getEmployeeConditionsManager().getMonthDS()
                        .stream().filter(month -> monthID.equals(month.getId())).findFirst().orElse(null);
                WorkedPerformanceOfWage workedPerformanceOfWage = new WorkedPerformanceOfWage(this, wageD, monthD);
                this.workedPerformanceOfWages.add(workedPerformanceOfWage);
                currentWPFW = workedPerformanceOfWage;
                prevWageID = wageID;
            }

            HoursD hoursD = new HoursD();
            hoursD.setId(json.getElement(i, "oh_id"));
            hoursD.setDate(json.getElement(i, "datum"));
            hoursD.setFrom(json.getElement(i, "od"));
            hoursD.setTo(json.getElement(i, "do"));
            hoursD.setOverTime(json.getElement(i, "z_toho_nadcas"));
            hoursD.setUnitsDone(json.getElement(i, "pocet_vykonanych_jednotiek"));
            hoursD.setPartBase(json.getElement(i, "zaklad_podielovej_mzdy"));
            hoursD.setEmergencyType(json.getElement(i, "druh_casti_pohotovosti"));
            hoursD.setUpdated(json.getElement(i, "aktualizovane"));
            currentWPFW.getHoursDS().add(hoursD);
        }
    }

    public void calculateTotalWorkedPerformance()
    {
        for (WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            w.calculateTotalWorkedPerformance();
        }
    }

    public String getNightTime()
    {
        BigDecimal nightTime = new BigDecimal("0");

        for(WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            if(w.getWageD().getTimeImportant().equals("áno"))
            {
                w.calculateNightTime();
                nightTime = nightTime.add(w.getNightTime());
            }
        }

        return nightTime.toString();
    }

    public String getSaturdayTime()
    {
        BigDecimal saturdayTime = new BigDecimal("0");

        for(WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            if(w.getWageD().getTimeImportant().equals("áno"))
            {
                w.calculateSaturdayTime();
                saturdayTime = saturdayTime.add(w.getSaturdayTime());
            }
        }

        return saturdayTime.toString();
    }

    public String getSundayTime()
    {
        BigDecimal sundayTime = new BigDecimal("0");

        for(WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            if(w.getWageD().getTimeImportant().equals("áno"))
            {
                w.calculateSundayTime();
                sundayTime = sundayTime.add(w.getSundayTime());
            }
        }

        return sundayTime.toString();
    }

    public String getFeastTime()
    {
        BigDecimal feastTime = new BigDecimal("0");

        for(WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            if(w.getWageD().getTimeImportant().equals("áno"))
            {
                w.calculateFeastTime();
                feastTime = feastTime.add(w.getFeastTime());
            }
        }

        return feastTime.toString();
    }

    public String getOvertimeTime()
    {
        BigDecimal overtime = new BigDecimal("0");

        for(WorkedPerformanceOfWage w : workedPerformanceOfWages)
        {
            if(w.getWageD().getTimeImportant().equals("áno"))
            {
                overtime = overtime.add(w.getOvertimeFromTotal());
                if(w.getActiveEmergencyTimeFromTotal() != null)
                    overtime = overtime.add(w.getActiveEmergencyTimeFromTotal());
            }
        }

        return overtime.toString();
    }

}
