package application.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AbsenceD
{
    String id, from, to, reason, characteristic, half, updated;
    String halfT;
    String  employeeID, employeeNameLastname,
            conID,
            relID,
            yearID, yearNumber,
            monthID, monthNumber,

            positionID, positionName,
            placeID, placeName;
    int Iid;
    LocalDateTime updatedT;

    public AbsenceD() {
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
        this.Iid = Integer.parseInt(id);
    }

    public String getHalfT() {
        return halfT;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getHalf() {
        return half;
    }

    public void setHalf(String half) {
        this.half = half;
        if(half.equals("1"))
            this.halfT = "áno";
        else
            this.halfT = "nie";
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

    public String getConID() {
        return conID;
    }

    public void setConID(String conID) {
        this.conID = conID;
    }

    public String getRelID() {
        return relID;
    }

    public void setRelID(String relID) {
        this.relID = relID;
    }

    public String getYearID() {
        return yearID;
    }

    public void setYearID(String yearID) {
        this.yearID = yearID;
    }

    public String getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(String yearNumber) {
        this.yearNumber = yearNumber;
    }

    public String getMonthID() {
        return monthID;
    }

    public void setMonthID(String monthID) {
        this.monthID = monthID;
    }

    public String getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(String monthNumber) {
        this.monthNumber = monthNumber;
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

    public int getIid() {
        return Iid;
    }

    public void setIid(int iid) {
        Iid = iid;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
        String str = updated;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.updatedT = LocalDateTime.parse(str, formatter);
    }

    public String getUpdatedT()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return this.updatedT.format(formatter);
    }
}
