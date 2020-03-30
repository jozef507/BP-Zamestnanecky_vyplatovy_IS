package application.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HoursD
{
    String id, date, from, to, overTime, unitsDone, partBase, emergencyType, updated,
        employeeID, employeeNameLastname,
        consID,
        relID,
        yearID, yearNumber,
        monthID, monthNumber,

        wageID, wageLabel,
        wageFormID, wageFormName,

        positionID, positionName,
        placeID, placeName;

    int Iid;
    String wageFormLabel;
    LocalDateTime updatedT;

    public HoursD() {
    }

    public String getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(String monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.Iid = Integer.parseInt(id);
    }

    public int getIid() {
        return Iid;
    }

    public void setIid(int iid) {
        Iid = iid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getUnitsDone() {
        return unitsDone;
    }

    public void setUnitsDone(String unitsDone) {
        this.unitsDone = unitsDone;
    }

    public String getPartBase() {
        return partBase;
    }

    public void setPartBase(String partBase) {
        this.partBase = partBase;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getMonthID() {
        return monthID;
    }

    public void setMonthID(String monthID) {
        this.monthID = monthID;
    }

    public String getWageID() {
        return wageID;
    }

    public void setWageID(String wageID) {
        this.wageID = wageID;
    }

    public String getWageLabel() {
        return wageLabel;
    }

    public void setWageLabel(String wageLabel) {
        this.wageLabel = wageLabel;
    }

    public String getWageFormID() {
        return wageFormID;
    }

    public void setWageFormID(String wageFormID) {
        this.wageFormID = wageFormID;
    }

    public String getWageFormName() {
        return wageFormName;
    }

    public void setWageFormName(String wageFormName) {
        this.wageFormName = wageFormName;
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

    public String getWageFormLabel() {
        return wageFormLabel;
    }

    public void setWageFormLabel(String wageFormLabel) {
        this.wageFormLabel = wageFormLabel;
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

    public String getConsID() {
        return consID;
    }

    public void setConsID(String consID) {
        this.consID = consID;
    }

    public String getRelID() {
        return relID;
    }

    public void setRelID(String relID) {
        this.relID = relID;
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
