package application.models;

import java.util.ArrayList;

public class EmployeeD
{
    private String id, name, lastname, phone, bornNum, bornDate;
    private String pkID, email, password, userType, lastLogIn, created, isCurrent;
    private String duID, insComp, town, street, number, from, childrenUnder, childrenOver;

    private ArrayList<RelationOV> relations;

    public EmployeeD() {}

    public EmployeeD(String id, String name, String lastname, String phone, String bornNum, String bornDate, String pkid, String email,
                     String userType, String duid, String inscomp, String town, String street, String number, String from, String childrenUnder,
                     String childrenOver, String part, String retirement, String invalidity) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.bornNum = bornNum;
        this.bornDate = bornDate;
        this.pkID = pkid;
        this.email = email;
        this.userType = userType;
        this.duID = duid;
        this.insComp = inscomp;
        this.town = town;
        this.street = street;
        this.number = number;
        this.from = from;
        this.childrenUnder = childrenUnder;
        this.childrenOver = childrenOver;
    }

    public EmployeeD(String id, String name, String lastname, String phone, String bornNum, String bornDate) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.bornNum = bornNum;
        this.bornDate = bornDate;
    }

    public void setAccountInfo(String email, String userType) {
        this.email = email;
        this.userType = userType;
    }

    public void setImportantInfo(String town, String street, String number, String from, String childrenUnder, String childrenOver, String retirement, String invalidity) {
        this.town = town;
        this.street = street;
        this.number = number;
        this.from = from;
        this.childrenUnder = childrenUnder;
        this.childrenOver = childrenOver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBornNum() {
        return bornNum;
    }

    public void setBornNum(String bornNum) {
        this.bornNum = bornNum;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getChildrenUnder() {
        return childrenUnder;
    }

    public void setChildrenUnder(String childrenUnder) {
        this.childrenUnder = childrenUnder;
    }

    public String getChildrenOver() {
        return childrenOver;
    }

    public void setChildrenOver(String childrenOver) {
        this.childrenOver = childrenOver;
    }

    public ArrayList<RelationOV> getRelations() {
        return relations;
    }

    public void setRelations(ArrayList<RelationOV> relations) {
        this.relations = relations;
    }

    public String getPkID() {
        return pkID;
    }

    public void setPkID(String pkID) {
        this.pkID = pkID;
    }

    public String getDuID() {
        return duID;
    }

    public void setDuID(String duID) {
        this.duID = duID;
    }

    public String getInsComp() {
        return insComp;
    }

    public void setInsComp(String insComp) {
        this.insComp = insComp;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getLastLogIn() {
        return lastLogIn;
    }

    public void setLastLogIn(String lastLogIn) {
        this.lastLogIn = lastLogIn;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
