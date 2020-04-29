package application.core;

import application.models.HoursD;
import application.models.MonthD;
import application.models.WageD;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WorkedPerformanceOfWage
{
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS------------------------------------------------------*/
    private WorkedPerformanceManager workedPerformanceManager;

    private WageD wageD;
    private MonthD monthD;

    private ArrayList<HoursD> hoursDS;

    private BigDecimal daysTotal;
    private BigDecimal timeTotal;
    private BigDecimal overtimeFromTotal;
    private BigDecimal activeEmergencyTimeFromTotal;
    private BigDecimal inactiveEmergencyInPlaceFromTotal;
    private BigDecimal inactiveEmergencyOutOfPlaceTime;

    private BigDecimal performanceTotal;
    private BigDecimal partTotal;

    private BigDecimal nightTime;
    private BigDecimal saturdayTime;
    private BigDecimal sundayTime;
    private BigDecimal feastTime;

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS---------------------------------------------------*/

    public WorkedPerformanceOfWage(WorkedPerformanceManager workedPerformanceManager, WageD wageD, MonthD monthD)
    {
        this.workedPerformanceManager = workedPerformanceManager;
        this.wageD = wageD;
        this.monthD = monthD;
        this.hoursDS = new ArrayList<>();
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------SETTERS/GETTERS--------------------------------------------------*/

    public WorkedPerformanceManager getWorkedPerformanceManager() {
        return workedPerformanceManager;
    }

    public WageD getWageD() {
        return wageD;
    }

    public MonthD getMonthD() {
        return monthD;
    }

    public ArrayList<HoursD> getHoursDS() {
        return hoursDS;
    }

    public BigDecimal getTimeTotal() {
        return timeTotal;
    }

    public BigDecimal getOvertimeFromTotal() {
        return overtimeFromTotal;
    }

    public BigDecimal getActiveEmergencyTimeFromTotal() {
        return activeEmergencyTimeFromTotal;
    }

    public BigDecimal getInactiveEmergencyInPlaceFromTotal() {
        return inactiveEmergencyInPlaceFromTotal;
    }

    public BigDecimal getInactiveEmergencyOutOfPlaceTime() {
        return inactiveEmergencyOutOfPlaceTime;
    }

    public BigDecimal getPerformanceTotal() {
        return performanceTotal;
    }

    public BigDecimal getPartTotal() {
        return partTotal;
    }

    public BigDecimal getNightTime() {
        return nightTime;
    }

    public BigDecimal getSaturdayTime() {
        return saturdayTime;
    }

    public BigDecimal getSundayTime() {
        return sundayTime;
    }

    public BigDecimal getFeastTime() {
        return feastTime;
    }

    public BigDecimal getDaysTotal() {
        return daysTotal;
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS------------------------------------------------------*/
    public void calculateTotalWorkedPerformance()
    {
        if(!(timeTotal==null && performanceTotal==null && partTotal==null))
        {
            return;
        }

        if(this.wageD.getTimeImportant().equals("áno")) // contains time-wage and goal-wage
        {
            calculateTotalTime();
        }
        if(this.wageD.getWageFormName().contains("výkonová"))
        {
            calculateTotalPerformance();
        }
        if(this.wageD.getWageFormName().contains("podielová"))
        {
            calculateTotalPartBase();
        }
    }

    private void calculateTotalTime()
    {
        daysTotal = new BigDecimal("0");
        timeTotal = new BigDecimal("0");
        overtimeFromTotal = new BigDecimal("0");

        if(wageD.getEmergencyImportant().equals("áno"))
        {
            activeEmergencyTimeFromTotal = new BigDecimal("0");
            inactiveEmergencyInPlaceFromTotal = new BigDecimal("0");
            inactiveEmergencyOutOfPlaceTime = new BigDecimal("0");
        }

        String prevDay = "-1";
        for(HoursD h:hoursDS)
        {
            if(!prevDay.equals(h.getDate()))
                daysTotal = daysTotal.add(new BigDecimal("1"));

            BigDecimal difference = getTimeDifference(h.getFrom(), h.getTo());

            if(wageD.getEmergencyImportant().equals("nie"))
            {
                timeTotal = timeTotal.add(difference);
                overtimeFromTotal = overtimeFromTotal.add(new BigDecimal(eliminateNull(h.getOverTime())));
            }
            else
            {
                if(h.getEmergencyType()==null)
                {
                    timeTotal = timeTotal.add(difference);
                    overtimeFromTotal = overtimeFromTotal.add(new BigDecimal(eliminateNull(h.getOverTime())));
                }
                else if(h.getEmergencyType().contains("aktívna"))
                {
                    timeTotal = timeTotal.add(difference);
                    activeEmergencyTimeFromTotal = activeEmergencyTimeFromTotal.add(difference);
                    overtimeFromTotal = overtimeFromTotal.add(new BigDecimal(eliminateNull(h.getOverTime())));
                }
                else if(h.getEmergencyType().contains("neaktívna - na pracovisku"))
                {
                    timeTotal = timeTotal.add(difference);
                    inactiveEmergencyInPlaceFromTotal = inactiveEmergencyInPlaceFromTotal.add(difference);
                    overtimeFromTotal = overtimeFromTotal.add(new BigDecimal(eliminateNull(h.getOverTime())));
                }
                else if(h.getEmergencyType().contains("neaktívna - mimo pracoviska"))
                {
                    inactiveEmergencyOutOfPlaceTime = inactiveEmergencyOutOfPlaceTime.add(difference);
                    overtimeFromTotal = overtimeFromTotal.add(new BigDecimal(eliminateNull(h.getOverTime())));
                }
            }
        }
    }

    private void calculateTotalPerformance()
    {
        performanceTotal = new BigDecimal("0");
        for(HoursD h:hoursDS)
        {
            performanceTotal = performanceTotal.add(new BigDecimal(eliminateNull(h.getPartBase())));
        }
    }

    private void calculateTotalPartBase()
    {
        partTotal = new BigDecimal("0");
        for(HoursD h:hoursDS)
        {
            partTotal = partTotal.add(new BigDecimal(eliminateNull(h.getPartBase())));
        }
    }

    private String eliminateNull(String str)
    {
        if(str==null)
            return "0";
        else
            return str;
    }




    public void calculateNightTime()
    {
        if(this.wageD.getTimeImportant().equals("áno"))
        {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String n1=workedPerformanceManager.getGrossWageManager().getPaymentManager().getWageConstantsD().getNightFrom();
            String n2=workedPerformanceManager.getGrossWageManager().getPaymentManager().getWageConstantsD().getNightTo();
            Date night1 = null, night2 = null;
            try {
                night1 = format.parse(n1);
                night2 = format.parse(n2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            boolean overNight = false;
            if(night1.compareTo(night2)>0)
            {
                night2 = Date.from(((night2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(1)).atZone(ZoneId.systemDefault()).toInstant());
                overNight = true;
            }

            nightTime = new BigDecimal("0");
            for(HoursD h : hoursDS)
            {
                String t1 = h.getFrom();
                String t2 = h.getTo();
                Date time1 = null, time2 = null;
                try {
                    time1 = format.parse(t1);
                    time2 = format.parse(t2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(overNight==true && t1.compareTo(n2)<=0)
                {
                    time1 = Date.from(((time1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(1)).atZone(ZoneId.systemDefault()).toInstant());
                    time2 = Date.from(((time2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(1)).atZone(ZoneId.systemDefault()).toInstant());
                }
                else if(time1.compareTo(time2)>0)
                {
                    time2 = Date.from(((time2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(1)).atZone(ZoneId.systemDefault()).toInstant());
                }

                if(!(night2.compareTo(time1)>0 && time2.compareTo(night1)>0))
                    continue;

                Date from=null, to=null;
                if(time1.compareTo(night1)>0)
                    from=time1;
                else
                    from=night1;
                if(time2.compareTo(night2)>0)
                    to=night2;
                else
                    to=time2;

                BigDecimal difference = new BigDecimal(to.getTime() - from.getTime());
                difference = difference.divide(new BigDecimal("3600000"));
                nightTime = nightTime.add(difference);
            }
        }
    }

    public void calculateSaturdayTime()
    {
        if(this.wageD.getTimeImportant().equals("áno"))
        {
            saturdayTime = new BigDecimal("0");
            for(HoursD h : hoursDS)
            {
                if (isItThisDay(h.getDate(), Calendar.SATURDAY))
                {
                    saturdayTime = saturdayTime.add(getTimeDifference(h.getFrom(), h.getTo()));
                }
            }
        }
    }

    public void calculateSundayTime()
    {
        if(this.wageD.getTimeImportant().equals("áno"))
        {
            sundayTime = new BigDecimal("0");
            for(HoursD h : hoursDS)
            {
                if (isItThisDay(h.getDate(), Calendar.SUNDAY))
                {
                    sundayTime = sundayTime.add(getTimeDifference(h.getFrom(), h.getTo()));
                }
            }
        }
    }

    public void calculateFeastTime()
    {
        if(this.wageD.getTimeImportant().equals("áno"))
        {
            feastTime = new BigDecimal("0");

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
            Calendar c = findHolyDay(2020);
            c.add(Calendar.DAY_OF_MONTH, 1);
            String easterMonday = formatter.format(c.getTime());
            c.add(Calendar.DAY_OF_MONTH, -3);
            String easterFriday = formatter.format(c.getTime());

            for(HoursD h : hoursDS)
            {
                if (isItFeast(h.getDate(), easterFriday, easterMonday))
                {
                    feastTime = feastTime.add(getTimeDifference(h.getFrom(), h.getTo()));
                }
            }
        }
    }


    private boolean isItThisDay(String day, int weekday)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);

        if ((c1.get(Calendar.DAY_OF_WEEK)) == weekday)
            return true;
        else
            return false;
    }

    private boolean isItFeast(String date, String easterFriday, String easterMonday)
    {
        String[] arrOfStr = date.split("-", 2);
        if(arrOfStr[1].equals("01-01")
            || arrOfStr[1].equals("01-06")
            || arrOfStr[1].equals(easterFriday)
            || arrOfStr[1].equals(easterMonday)
            || arrOfStr[1].equals("05-01")
            || arrOfStr[1].equals("05-08")
            || arrOfStr[1].equals("07-05")
            || arrOfStr[1].equals("08-29")
            || arrOfStr[1].equals("09-01")
            || arrOfStr[1].equals("09-15")
            || arrOfStr[1].equals("11-01")
            || arrOfStr[1].equals("11-17")
            || arrOfStr[1].equals("12-24")
            || arrOfStr[1].equals("12-25")
            || arrOfStr[1].equals("12-26")
        )
            return true;
        else
            return false;
    }

    /**
     * Easter - compute the day on which Easter falls.
     *
     * In the Christian religion, Easter is possibly the most important holiday of
     * the year, so getting its date <I>just so </I> is worthwhile.
     *
     * @author: Ian F. Darwin, http://www.darwinsys.com/, based on a detailed
     *          algorithm in Knuth, vol 1, pg 155.
     *
     * @Version: $Id: Easter.java,v 1.5 2004/02/09 03:33:46 ian Exp $ Written in C,
     *           Toronto, 1988. Java version 1996.
     *
     * @Note: It's not proven correct, although it gets the right answer for years
     *        around the present.
     *
     * Compute the day of the year that Easter falls on. Step names E1 E2 etc.,
     * are direct references to Knuth, Vol 1, p 155. @exception
     * IllegalArgumentexception If the year is before 1582 (since the algorithm
     * only works on the Gregorian calendar).
     */
    public static final Calendar findHolyDay(int year) {
        if (year <= 1582) {
            throw new IllegalArgumentException("Algorithm invalid before April 1583");
        }
        int golden, century, x, z, d, epact, n;

        golden = (year % 19) + 1; /* E1: metonic cycle */
        century = (year / 100) + 1; /* E2: e.g. 1984 was in 20th C */
        x = (3 * century / 4) - 12; /* E3: leap year correction */
        z = ((8 * century + 5) / 25) - 5; /* E3: sync with moon's orbit */
        d = (5 * year / 4) - x - 10;
        epact = (11 * golden + 20 + z - x) % 30; /* E5: epact */
        if ((epact == 25 && golden > 11) || epact == 24)
            epact++;
        n = 44 - epact;
        n += 30 * (n < 21 ? 1 : 0); /* E6: */
        n += 7 - ((d + n) % 7);
        if (n > 31) /* E7: */
            return new GregorianCalendar(year, 4 - 1, n - 31); /* April */
        else
            return new GregorianCalendar(year, 3 - 1, n); /* March */
    }


    private BigDecimal getTimeDifference(String from, String to)
    {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = null, date2 = null;
        try {
            date1 = format.parse(from);
            date2 = format.parse(to);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BigDecimal difference = new BigDecimal(date2.getTime() - date1.getTime());
        difference = difference.divide(new BigDecimal("3600000"));
        if(difference.compareTo(new BigDecimal("0"))<0)
        {
            difference = difference.add(new BigDecimal("24"));
        }
        return difference;
    }

}
