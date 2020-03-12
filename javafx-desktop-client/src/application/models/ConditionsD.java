package application.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConditionsD
{
    String id, from, to, relationID, positionID, nextConditionsID;

    public ConditionsD() {
    }

    public ConditionsD(String id, String from, String to, String relationID, String positionID, String nextConditionsID) {
        this.id = id;
        this.from = from;
        if(to==null || to.equals("00.00.0000"))
            this.to = "aktuálne";
        else
            this.to = to;
        this.relationID = relationID;
        this.positionID = positionID;
        this.nextConditionsID = nextConditionsID;
    }

    public ConditionsD(String from, String to, String relationID, String positionID, String nextConditionsID) {
        this.from = from;
        if(to==null || to.equals("00.00.0000"))
            this.to = "aktuálne";
        else
            this.to = to;        this.relationID = relationID;
        this.positionID = positionID;
        this.nextConditionsID = nextConditionsID;
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

    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }

    public String getPositionID() {
        return positionID;
    }

    public void setPositionID(String positionID) {
        this.positionID = positionID;
    }

    public String getNextConditionsID() {
        return nextConditionsID;
    }

    public void setNextConditionsID(String nextConditionsID) {
        this.nextConditionsID = nextConditionsID;
    }
}
