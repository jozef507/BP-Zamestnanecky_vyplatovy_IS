package application.gui.payment.create;

import application.models.SurchargeTypeD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class PageLevies {
    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------FIELDS-----------------------------------------------------*/
    private PaneCreate paneCreate;


    /*---------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------CONSTRUCTORS--------------------------------------------------*/

    public PageLevies(PaneCreate paneCreate) {
        this.paneCreate = paneCreate;
        paneCreate.getPaymentManager().getNetWageManager().process();
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------METHODS-----------------------------------------------------*/


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI FIELDS---------------------------------------------------*/
    @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private Label label;

    @FXML
    private Text assBas0;
    @FXML
    private Text assBas1;
    @FXML
    private Text assBas2;
    @FXML
    private Text assBas3;
    @FXML
    private Text assBas4;
    @FXML
    private Text er0;
    @FXML
    private Text er1;
    @FXML
    private Text er2;
    @FXML
    private Text er3;
    @FXML
    private Text er4;
    @FXML
    private Text eeSum;
    @FXML
    private Text erSum;
    @FXML
    private Text assBas5;
    @FXML
    private Text assBas6;
    @FXML
    private Text assBas7;
    @FXML
    private Text ee0;
    @FXML
    private Text ee1;
    @FXML
    private Text ee2;
    @FXML
    private Text ee3;
    @FXML
    private Text ee4;
    @FXML
    private Text ee5;
    @FXML
    private Text ee6;
    @FXML
    private Text ee7;
    @FXML
    private Text er5;
    @FXML
    private Text er6;
    @FXML
    private Text er7;
    @FXML
    private Text erSum0;
    @FXML
    private Text erSum1;
    @FXML
    private Text eeSum0;
    @FXML
    private Text eeSum6;
    @FXML
    private Text eeSum1;
    @FXML
    private Text eeSum3;
    @FXML
    private Text eeSum2;
    @FXML
    private Text eeSum4;
    @FXML
    private Text eeSum5;
    @FXML
    private Text erSum2;

    /*---------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------GUI INITIALIZATIONS----------------------------------------------*/
    @FXML
    private void initialize()
    {
        setElementsWithInitialData();
    }

    private void setElementsWithInitialData()
    {
        assBas0.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesHealthInsuranceManager().getHealth().getAssessmentBasis() + " €");
        assBas1.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getSickness().getAssessmentBasis() + " €");
        assBas2.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getRetirement().getAssessmentBasis() + " €");
        assBas3.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getDisability().getAssessmentBasis() + " €");
        assBas4.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getUnemployment().getAssessmentBasis() + " €");
        assBas5.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getAccident().getAssessmentBasis() + " €");
        assBas6.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getGuarantee().getAssessmentBasis() + " €");
        assBas7.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getSolidarity().getAssessmentBasis() + " €");

        ee0.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesHealthInsuranceManager().getHealth().getEmployeeSum() + " €");
        ee1.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getSickness().getEmployeeSum() + " €");
        ee2.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getRetirement().getEmployeeSum() + " €");
        ee3.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getDisability().getEmployeeSum() + " €");
        ee4.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getUnemployment().getEmployeeSum() + " €");
        ee5.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getAccident().getEmployeeSum() + " €");
        ee6.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getGuarantee().getEmployeeSum() + " €");
        ee7.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getSolidarity().getEmployeeSum() + " €");

        er0.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesHealthInsuranceManager().getHealth().getEmployerSum() + " €");
        er1.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getSickness().getEmployerSum() + " €");
        er2.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getRetirement().getEmployerSum() + " €");
        er3.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getDisability().getEmployerSum() + " €");
        er4.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getUnemployment().getEmployerSum() + " €");
        er5.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getAccident().getEmployerSum() + " €");
        er6.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getGuarantee().getEmployerSum() + " €");
        er7.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getSolidarity().getEmployerSum() + " €");

        eeSum.setText(paneCreate.getPaymentManager().getNetWageManager().getEmployeeLeviesTotal() + " €");
        erSum.setText(paneCreate.getPaymentManager().getNetWageManager().getEmployerLeviesTotal() + " €");

        erSum0.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesHealthInsuranceManager().getEmployerSumTotal() + " €");
        erSum1.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getEmployerSumTotal() + " €");
        erSum2.setText(paneCreate.getPaymentManager().getNetWageManager().getWorkPrice() + " €");

        eeSum0.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesHealthInsuranceManager().getEmployeeSumTotal() + " €");
        eeSum1.setText(paneCreate.getPaymentManager().getNetWageManager().getLeviesSocialInsuranceManager().getEmployeeSumTotal() + " €");
        eeSum2.setText(paneCreate.getPaymentManager().getNetWageManager().getPartialTaxBase() + " €");
        eeSum3.setText(paneCreate.getPaymentManager().getNetWageManager().getTaxableWage() + " €");
        eeSum4.setText(paneCreate.getPaymentManager().getNetWageManager().getIncomeTaxAdvance() + " €");
        eeSum5.setText(paneCreate.getPaymentManager().getNetWageManager().getTaxBonusForChild() + " €");
        eeSum6.setText(paneCreate.getPaymentManager().getNetWageManager().getNetWage() + " €");



    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI METHODS--------------------------------------------------*/

    @FXML
    private void onBackClick(MouseEvent mouseEvent)
    {
        setBackPage();
    }

    @FXML
    private void onNextClick(MouseEvent mouseEvent)
    {
        setPaneElements();
        setNextPage();
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------GUI HELPERS--------------------------------------------------*/

    private void setPaneElements()
    {
        paneCreate.getAssessmentbasis().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getAssessmentBasis().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        paneCreate.getLeviessocialinsurence().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getLeviesSocialInsuranceManager().getEmployeeSumTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        paneCreate.getLeviesinsurencecompany().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getLeviesHealthInsuranceManager().getEmployeeSumTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");

        paneCreate.getPartialTaxBase().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getPartialTaxBase().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        paneCreate.getTaxablewage().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getTaxableWage().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        paneCreate.getAdvances().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getIncomeTaxAdvance().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        paneCreate.getTaxbonus().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getTaxBonusForChild().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        paneCreate.getNetwage().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getNetWage().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");

        paneCreate.getWorkprice().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getWorkPrice().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        paneCreate.getEleviesinsurencecompany().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getLeviesHealthInsuranceManager().getEmployerSumTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
        paneCreate.getEleviessocialinsurence().setText(paneCreate.getPaymentManager().getNetWageManager()
                .getLeviesSocialInsuranceManager().getEmployerSumTotal().setScale(2, RoundingMode.HALF_UP).toPlainString() + " €");
    }

    private void setBackPage()
    {

        FXMLLoader l = new FXMLLoader(getClass().getResource("PageMinimalWageDeficit.fxml"));
        l.setControllerFactory(c -> {
            return new PageMinimalWageDeficit(paneCreate);
        });
        paneCreate.loadAnchorPage(l);

    }

    private void setNextPage()
    {
       FXMLLoader l = new FXMLLoader(getClass().getResource("PageDeductions.fxml"));
        l.setControllerFactory(c -> {
            return new PageDeductions(paneCreate);
        });
        paneCreate.loadAnchorPage(l);
    }


}
