package application.models;

public class MinimumWageD
{
    private String id, from, to, hourValue, monthValaue,
            levelID, levelNum;

    public MinimumWageD() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getHourValue() {
        return hourValue;
    }

    public void setHourValue(String hourValue) {
        this.hourValue = hourValue;
    }

    public String getMonthValue() {
        return monthValaue;
    }

    public void setMonthValue(String monthVlaue) {
        this.monthValaue = monthVlaue;
    }

    public String getLevelID() {
        return levelID;
    }

    public void setLevelID(String levelID) {
        this.levelID = levelID;
    }

    public String getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(String levelNum) {
        this.levelNum = levelNum;
    }


}
