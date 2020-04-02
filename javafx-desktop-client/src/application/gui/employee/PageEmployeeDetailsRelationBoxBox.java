package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.ConditionsD;
import application.models.NextConditionsD;
import application.models.WageD;
import application.models.WageFormD;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class PageEmployeeDetailsRelationBoxBox
{

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private WageD wageD;
    private ArrayList<WageFormD> wageFormDS;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public PageEmployeeDetailsRelationBoxBox(WageD wageD, ArrayList<WageFormD> wageFormDS) {
        this.wageD = wageD;
        this.wageFormDS = wageFormDS;
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private String getWageForm(String id)
    {
        for(int i = 0; i<this.wageFormDS.size(); i++)
        {
            if(this.wageFormDS.get(i).getId().equals(id))
                return this.wageFormDS.get(i).toComboboxString();
        }
        return this.wageFormDS.get(0).toComboboxString();
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public Text form;
    public Text way;
    public Text employee;
    public Text time;
    public Text tarif;
    public Text date;
    public Text label, emergency;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize()
    {
        this.setTexts();
    }

    private void setTexts()
    {
        form.setText(this.getWageForm(this.wageD.getWageFormID()));
        way.setText(this.wageD.getPayWay());
        employee.setText(this.wageD.getEmployeeEnter());
        time.setText(this.wageD.getTimeImportant());
        tarif.setText(this.wageD.getTarif());
        date.setText(this.wageD.getPayDate());
        label.setText(this.wageD.getLabel());
        emergency.setText(this.wageD.getEmergencyImportant());
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/





}
