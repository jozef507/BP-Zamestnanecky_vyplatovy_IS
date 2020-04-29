package application.gui.payment.create;


import application.core.PaymentManager;
import application.gui.payment.PagePayment;
import application.models.PaymentD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class PaneCreate
{

    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS------------------------------------------------------*/
    private PagePayment pagePayment;
    private PaymentManager paymentManager;

    private boolean wasAveragePage;
    private boolean wasWorkDayTimePage;
    /*----------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS---------------------------------------------------*/

    public PaneCreate(PagePayment pagePayment, PaymentD paymentD) {
        this.pagePayment = pagePayment;
        this.paymentManager = new PaymentManager(this, paymentD.getMonthID());
        this.wasAveragePage = false;
    }
    /*----------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------GETTERS/SETTERS-------------------------------------------------*/

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public boolean isWasAveragePage() {
        return wasAveragePage;
    }

    public void setWasAveragePage(boolean wasAveragePage) {
        this.wasAveragePage = wasAveragePage;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------METHODS-----------------------------------------------------*/


    /*----------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI FIELDS----------------------------------------------------*/
    @FXML
    private Text name;
    @FXML
    private Text lastname;
    @FXML
    private Text bornnumber;
    @FXML
    private Text borndate;
    @FXML
    private Text inscompany;
    @FXML
    private Text relationtype;
    @FXML
    private Text regularwage;
    @FXML
    private Text from;
    @FXML
    private Text to;
    @FXML
    private Text place;
    @FXML
    private Text position;
    @FXML
    private Text weektime;
    @FXML
    private Text apweektime;
    @FXML
    private Text uniformtime;
    @FXML
    private Text daytime;
    @FXML
    private Text months;
    @FXML
    private Text fund;
    @FXML
    private Text basecomponent;
    @FXML
    private Text dynamiccomponent;
    @FXML
    private Text surcharges;
    @FXML
    private Text compensations;
    @FXML
    private Text others;
     @FXML
    private Text deficit;
    @FXML
    private Text grosswage;
    @FXML
    private Text assessmentbasis;
    @FXML
    private Text leviessocialinsurence;
    @FXML
    private Text leviesinsurencecompany;
    @FXML
    private Text partialTaxBase;
    @FXML
    private Text taxablewage;
    @FXML
    private Text advances;
    @FXML
    private Text taxbonus;
    @FXML
    private Text netwage;
    @FXML
    private Text wagededuction;
    @FXML
    private Text topay;
    @FXML
    private Text workprice;
    @FXML
    private Text eleviessocialinsurence;
    @FXML
    private Text eleviesinsurencecompany;
    @FXML
    private Text averageWage;
    @FXML
    private Text dayAssessmentBasis;
    @FXML
    private AnchorPane anchorpane;

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------GUI GETTERS/SETTERS----------------------------------------------*/

    public Text getFund() {
        return fund;
    }

    public Text getBasecomponent() {
        return basecomponent;
    }

    public Text getDynamiccomponent() {
        return dynamiccomponent;
    }

    public Text getSurcharges() {
        return surcharges;
    }

    public Text getCompensations() {
        return compensations;
    }

    public Text getOthers() {
        return others;
    }

    public Text getGrosswage() {
        return grosswage;
    }

    public Text getAssessmentbasis() {
        return assessmentbasis;
    }

    public Text getLeviessocialinsurence() {
        return leviessocialinsurence;
    }

    public Text getLeviesinsurencecompany() {
        return leviesinsurencecompany;
    }

    public Text getPartialTaxBase() {
        return partialTaxBase;
    }

    public Text getTaxablewage() {
        return taxablewage;
    }

    public Text getAdvances() {
        return advances;
    }

    public Text getTaxbonus() {
        return taxbonus;
    }

    public Text getNetwage() {
        return netwage;
    }

    public Text getWagededuction() {
        return wagededuction;
    }

    public Text getTopay() {
        return topay;
    }

    public Text getWorkprice() {
        return workprice;
    }

    public Text getEleviessocialinsurence() {
        return eleviessocialinsurence;
    }

    public Text getEleviesinsurencecompany() {
        return eleviesinsurencecompany;
    }

    public Text getAverageWage() {
        return averageWage;
    }

    public Text getDaytime() {return daytime;}

    public Text getDayAssessmentBasis() {
        return dayAssessmentBasis;
    }

    public Text getDeficit() {
        return deficit;
    }

    public boolean isWasWorkDayTimePage() {
        return wasWorkDayTimePage;
    }

    public void setWasWorkDayTimePage(boolean wasWorkDayTimePage) {
        this.wasWorkDayTimePage = wasWorkDayTimePage;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        setLeftPanelTexts();
        setFristPage();


    }

    private void setLeftPanelTexts()
    {
        name.setText(paymentManager.getEmployeeConditionsManager().getEmployeeD().getName());
        lastname.setText(paymentManager.getEmployeeConditionsManager().getEmployeeD().getLastname());
        borndate.setText(paymentManager.getEmployeeConditionsManager().getEmployeeD().getBornDate());
        bornnumber.setText(paymentManager.getEmployeeConditionsManager().getEmployeeD().getBornNum());
        inscompany.setText(paymentManager.getEmployeeConditionsManager().getImportantD().getInsComp());

        relationtype.setText(paymentManager.getEmployeeConditionsManager().getRelationD().getType());
        regularwage.setText(getBooleanViewString(paymentManager.getEmployeeConditionsManager()
                .getWageDS().get(0).getPayWay()));
        from.setText(paymentManager.getEmployeeConditionsManager().getRelationD().getFrom());
        to.setText(paymentManager.getEmployeeConditionsManager().getRelationD().getTo());

        place.setText(paymentManager.getEmployeeConditionsManager().getPlaceD().getName());
        position.setText(paymentManager.getEmployeeConditionsManager().getPositionD().getName());

        weektime.setText(getStringForNextconditionsTime(paymentManager.getEmployeeConditionsManager()
                .getNextConditionsD().getWeekTime()));
        apweektime.setText(getStringForNextconditionsTime(paymentManager.getEmployeeConditionsManager()
                .getNextConditionsD().getApWeekTime()));
        uniformtime.setText(getBooleanViewString(paymentManager.getEmployeeConditionsManager()
                .getNextConditionsD().getIsWeekTimeUniform()));
        daytime.setText(getStringForNextconditionsTime(paymentManager.getEmployeeConditionsManager()
                .getNextConditionsD().getDayTime()));

        String monthsS = "";
        for (int i = paymentManager.getEmployeeConditionsManager().getMonthDS().size()-1; i>=0; i--)
        {
            if(i!=paymentManager.getEmployeeConditionsManager().getMonthDS().size()-1)
                monthsS = monthsS + ", ";

            monthsS = monthsS + paymentManager.getEmployeeConditionsManager().getMonthDS().get(i).getMonthNumber()
                    +"/"+ paymentManager.getEmployeeConditionsManager().getYearDS().get(i).getYearNumber();
        }
        months.setText(monthsS);
    }

    private void setFristPage()
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageRecapitulation.fxml"));
        l.setControllerFactory(c -> {
            return new PageFund(this);
        });
        this.loadAnchorPage(l);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS---------------------------------------------------*/
    public void loadAnchorPage(String page)
    {
        try {
            FXMLLoader pageLoader = new FXMLLoader(getClass().getResource(page));
            AnchorPane newPane = pageLoader.load();

            anchorpane.getChildren().removeAll();
            anchorpane.getChildren().setAll(newPane);

            AnchorPane.setBottomAnchor(newPane, 0.0);
            AnchorPane.setLeftAnchor(newPane, 0.0);
            AnchorPane.setRightAnchor(newPane, 0.0);
            AnchorPane.setTopAnchor(newPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAnchorPage(FXMLLoader l)
    {
        try {
            FXMLLoader pageLoader = l;
            AnchorPane newPane = pageLoader.load();

            anchorpane.getChildren().removeAll();
            anchorpane.getChildren().setAll(newPane);

            AnchorPane.setBottomAnchor(newPane, 0.0);
            AnchorPane.setLeftAnchor(newPane, 0.0);
            AnchorPane.setRightAnchor(newPane, 0.0);
            AnchorPane.setTopAnchor(newPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS---------------------------------------------------*/
    private String getBooleanViewString(String st)
    {
        if(st==null) return null;
        return st.equals("0") ? "nie" : "áno";
    }

    private String getStringForNextconditionsTime(String string)
    {
        return string==null ? null : string+" hod";
    }

}

