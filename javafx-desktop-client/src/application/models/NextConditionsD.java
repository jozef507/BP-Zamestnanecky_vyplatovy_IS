package application.models;

public class NextConditionsD
{
    private String id, isMain, hollidayTime, weekTime, isWeekTimeUniform, testTime, sackTime,
        apWeekTime, dayTime, deductableItem;

    public NextConditionsD() {
    }

    public NextConditionsD(String id, String isMain, String hollidayTime, String weekTime, String isWeekTimeUniform, String testTime, String sackTime) {
        this.id = id;
        this.isMain = isMain;
        this.hollidayTime = hollidayTime;
        this.weekTime = weekTime;
        this.isWeekTimeUniform = isWeekTimeUniform;
        this.testTime = testTime;
        this.sackTime = sackTime;
    }

    public NextConditionsD(String isMain, String hollidayTime, String weekTime, String isWeekTimeUniform, String testTime, String sackTime) {
        this.isMain = isMain;
        this.hollidayTime = hollidayTime;
        this.weekTime = weekTime;
        this.isWeekTimeUniform = isWeekTimeUniform;
        this.testTime = testTime;
        this.sackTime = sackTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getHollidayTime() {
        return hollidayTime;
    }

    public void setHollidayTime(String hollidayTime) {
        this.hollidayTime = hollidayTime;
    }

    public String getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(String weekTime) {
        this.weekTime = weekTime;
    }

    public String getIsWeekTimeUniform() {
        return isWeekTimeUniform;
    }

    public void setIsWeekTimeUniform(String isWeekTimeUniform) {

        this.isWeekTimeUniform = isWeekTimeUniform;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getSackTime() {
        return sackTime;
    }

    public void setSackTime(String sackTime) {
        this.sackTime = sackTime;
    }

    public String getApWeekTime() {
        return apWeekTime;
    }

    public void setApWeekTime(String apWeekTime) {
        this.apWeekTime = apWeekTime;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getDeductableItem() {
        return deductableItem;
    }

    public void setDeductableItem(String deductableItem) {
        this.deductableItem = deductableItem;
    }
}
