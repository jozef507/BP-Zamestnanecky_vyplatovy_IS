package application.models;

public class RelationD
{
    String id, type, from, to;

    String employeeID, employeeNameLastname,
        conditionsID, conditionsFrom, conditionsTo,
        positionID, positionName,
        placeID, placeName;

    public RelationD() {
    }

    public RelationD(String id, String type, String from, String to, String employeeID) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.to = to;
        this.employeeID = employeeID;
    }

    public RelationD(String type, String from, String to, String employeeID) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.employeeID = employeeID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        if(to==null)
            return "";
        else
            return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }


    public String getEmployeeNameLastname() {
        return employeeNameLastname;
    }

    public void setEmployeeNameLastname(String employeeNameLastname) {
        this.employeeNameLastname = employeeNameLastname;
    }

    public String getConditionsID() {
        return conditionsID;
    }

    public void setConditionsID(String conditionsID) {
        this.conditionsID = conditionsID;
    }

    public String getConditionsFrom() {
        return conditionsFrom;
    }

    public void setConditionsFrom(String conditionsFrom) {
        this.conditionsFrom = conditionsFrom;
    }

    public String getConditionsTo() {
        return conditionsTo;
    }

    public void setConditionsTo(String conditionsTo) {
        this.conditionsTo = conditionsTo;
    }

    public String getPositionID() {
        return positionID;
    }

    public void setPositionID(String positionID) {
        this.positionID = positionID;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
