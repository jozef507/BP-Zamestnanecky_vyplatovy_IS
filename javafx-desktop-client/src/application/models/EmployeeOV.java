package application.models;

import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;

import java.io.IOException;
import java.util.*;

public class EmployeeOV
{
    private int id;
    private String name;
    private String lastname;
    private String phonenumber;
    private String bornnumber;
    private String borndate;
    private String login;
    private int actrelat;
    private ArrayList<String> workRelations;
    private ArrayList<String> places;

    public EmployeeOV(int id, String name, String lastName, String phonenumber, String bornnumber, String borndate, String login, int actrelat)
    {
        this.id  = id;
        this.name  = name;
        this.lastname  = lastName;
        this.phonenumber  = phonenumber;
        this.bornnumber  = bornnumber;
        this.borndate  = borndate;
        this.login  = login;
        this.actrelat  = actrelat;
        this.workRelations = new ArrayList<>();
        this.places = new ArrayList<>();
    }

    public EmployeeOV() {
        this.workRelations = new ArrayList<>();
        this.places = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setBornnumber(String bornnumber) {
        this.bornnumber = bornnumber;
    }

    public void setBorndate(String borndate) {
        this.borndate = borndate;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setWorkRelations(ArrayList<String> workRelations) {
        this.workRelations = workRelations;
    }

    public void setPlaces(ArrayList<String> places) {
        this.places = places;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getLastname()
    {
        return this.lastname;
    }

    public String getPhonenumber()
    {
        return this.phonenumber;
    }

    public String getBornnumber()
    {
        return this.bornnumber;
    }

    public String getBorndate()
    {
        return this.borndate;
    }

    public String getLogin()
    {
        return this.login;
    }

    public int getActrelat()
    {
        return this.actrelat;
    }

    public ArrayList<String> getWorkRelations()
    {
        return this.workRelations;
    }

    public ArrayList<String> getPlaces()
    {
        return this.places;
    }

    public void addWorkRelation(String workRelation)
    {
        this.workRelations.add(workRelation);
    }

    public void addPlace(String place)
    {
        this.places.add(place);
    }

    public void setActrelat(int actrelat) {
        this.actrelat = actrelat;
    }

    public void setLists() throws IOException, InterruptedException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.sendGet("employee/relations_places_details/"+this.id, LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        for (int i = 0; i<json.getSize(); i++)
        {
            this.workRelations.add(json.getElement(i, "typ"));
            this.places.add(json.getElement(i,"nazov"));
        }
    }

    public String toString()
    {
        String a;
        a=this.id+" "+this.name+" "+this.lastname+" "+this.login;
        return a;
    }
}
