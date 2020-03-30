package application.models;

import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;

import java.io.IOException;
import java.util.ArrayList;

public class RelationOV
{
    private String id, type, origin, expiration, nextDemandsID, main, position, place;


    public RelationOV(String id, String type, String origin, String expiration, String nextdemands, String position, String place) {
        this.id = id;
        this.type = type;
        this.origin = origin;
        if(expiration==null)
            this.expiration = "";
        else
            this.expiration = expiration;
        this.nextDemandsID = nextdemands;
        this.position = position;
        this.place = place;
    }

    public String getNextDemandsID() {
        return nextDemandsID;
    }

    public void setNextDemandsID(String nextDemandsID) {
        this.nextDemandsID = nextDemandsID;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getMain() {
        return main;
    }

    public void setMainByNextDemands() throws InterruptedException, IOException, CommunicationException {
        if(this.nextDemandsID!=null)
        {
            HttpClientClass ht = new HttpClientClass();
            ht.sendGet("relation/nxt_dmnds_d/"+this.nextDemandsID, LoggedInUser.getToken(), LoggedInUser.getId());
            JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
            String m = json.getElement(0, "je_hlavny_pp");
            if(m.equals("0"))
                this.main="nie";
            else
                this.main="áno";
        }
        else
        {
            this.main="nie";
        }
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
