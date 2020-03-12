package application.models;

public class ImportantD
{
    private String id, insComp, town, street, num, childUnder, childOver,
        part, retirement, invalidity, from, to;

    public ImportantD(String id, String insComp, String town, String street, String num, String childUnder, String childOver, String part, String retirement, String invalidity, String from, String to) {
        this.id = id;
        this.insComp = insComp;
        this.town = town;
        this.street = street;
        this.num = num;
        this.childUnder = childUnder;
        this.childOver = childOver;
        this.part = part;
        this.retirement = retirement;
        this.invalidity = invalidity;
        this.from = from;
        if(to==null || to.equals("00.00.0000"))
            this.to = "aktuálne";
        else
            this.to = to;    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsComp() {
        return insComp;
    }

    public void setInsComp(String insComp) {
        this.insComp = insComp;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getChildUnder() {
        return childUnder;
    }

    public void setChildUnder(String childUnder) {
        this.childUnder = childUnder;
    }

    public String getChildOver() {
        return childOver;
    }

    public void setChildOver(String childOver) {
        this.childOver = childOver;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getRetirement() {
        return retirement;
    }

    public void setRetirement(String retirement) {
        this.retirement = retirement;
    }

    public String getInvalidity() {
        return invalidity;
    }

    public void setInvalidity(String invalidity) {
        this.invalidity = invalidity;
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
}
