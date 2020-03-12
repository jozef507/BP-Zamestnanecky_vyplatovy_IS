package application.models;

public class RelationD
{
    String id, type, from, to, employeeID;

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
        if(to==null || to.equals("0000-00-00"))
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
}
