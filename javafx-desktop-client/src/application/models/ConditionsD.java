package application.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConditionsD
{
    private String id, from, to, relationID, positionID, nextConditionsID,
        taxFree, taxBonus, disabled, retirement, invalidity40, invalidity70,
        premature, exemption, bank, bankPart, iban;

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

    public String getFrom() {  return from;  }

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


    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankPart() {
        return bankPart;
    }

    public void setBankPart(String bankPart) {
        this.bankPart = bankPart;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTaxFree() {
        return taxFree;
    }

    public void setTaxFree(String taxFree) {
        this.taxFree = taxFree;
    }

    public String getTaxBonus() {
        return taxBonus;
    }

    public void setTaxBonus(String taxBonus) {
        this.taxBonus = taxBonus;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getRetirement() {
        return retirement;
    }

    public void setRetirement(String retirement) {
        this.retirement = retirement;
    }

    public String getInvalidity40() {
        return invalidity40;
    }

    public void setInvalidity40(String invalidity40) {
        this.invalidity40 = invalidity40;
    }

    public String getInvalidity70() {
        return invalidity70;
    }

    public void setInvalidity70(String invalidity70) {
        this.invalidity70 = invalidity70;
    }

    public String getPremature() {
        return premature;
    }

    public void setPremature(String premature) {
        this.premature = premature;
    }

    public String getExemption() {
        return exemption;
    }

    public void setExemption(String exemption) {
        this.exemption = exemption;
    }

    public String getBank() {
        return bank;
    }
}
