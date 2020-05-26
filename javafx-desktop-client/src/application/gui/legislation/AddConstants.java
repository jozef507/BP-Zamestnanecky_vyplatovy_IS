package application.gui.legislation;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.SurchargeTypeD;
import application.models.WageConstantsD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AddConstants
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private PageLegislationConstants pageLegislationConstants;
    private WageConstantsD wageConstantsD;



    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public AddConstants(PageLegislationConstants pageLegislationConstants) {
        this.pageLegislationConstants = pageLegislationConstants;
        this.wageConstantsD = new WageConstantsD();

    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private void createSurcharge() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("basicWeekTime", this.wageConstantsD.getBasicWeekTime());
        ht.addParam("maxAssessmentBasis", this.wageConstantsD.getMaxAssessmentBasis());
        ht.addParam("minAssessmentBasis", this.wageConstantsD.getMinAssessmentBasis());
        ht.addParam("maxDayAssessmentBasis", this.wageConstantsD.getMaxDayAssessmentBasis());
        ht.addParam("taxBonusUnder6", this.wageConstantsD.getTaxBonusUnder6());
        ht.addParam("taxBonusOver6", this.wageConstantsD.getTaxBonusOver6());
        ht.addParam("nonTaxablePart", this.wageConstantsD.getNonTaxablePart());
        ht.addParam("subsistenceMinimumForAdvances", this.wageConstantsD.getSubsistenceMinimumForAdvances());
        ht.addParam("nightFrom", this.wageConstantsD.getNightFrom());
        ht.addParam("nightTo", this.wageConstantsD.getNightTo());
        ht.addParam("maxAssessmentBasisOP", this.wageConstantsD.getMaxAssessmentBasisOP());
        ht.addParam("maxOP", this.wageConstantsD.getMaxOP());
        ht.addParam("limitOV", this.wageConstantsD.getLimitOV());
        ht.addParam("from", this.wageConstantsD.getFrom());

        ht.sendPost("wageconstants/crt_cons", LoggedInUser.getToken(), LoggedInUser.getId());
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public Button cancel;
    public Button create;
    public DatePicker from;
    public Label label;
    @FXML
    private TextField maxDayAssessmentBasis;
    @FXML
    private TextField maxAssessmentBasis;
    @FXML
    private TextField basicWeekTime;
    @FXML
    private TextField minAssessmentBasis;
    @FXML
    private TextField taxBonusUnder6;
    @FXML
    private TextField taxBonusOver6;
    @FXML
    private TextField nonTaxablePart;
    @FXML
    private TextField subsistenceMinimumForAdvances;
    @FXML
    private TextField nightFrom;
    @FXML
    private TextField nightTo;
    @FXML
    private TextField maxAssessmentBasisOP;
    @FXML
    private TextField maxOP;
    @FXML
    private TextField limitOV;



    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setDatePicker();
        changeFocus();
    }

    private void setDatePicker()
    {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("d.M.yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        from.setConverter(converter);
        from.setPromptText("D.M.RRRR");
    }


    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        maxDayAssessmentBasis.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                maxDayAssessmentBasis.getParent().requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Vytvorenie mzdových konštant", "Ste si istý že chcete vytvoriť nový záznam mzdových konštátnt?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        this.setModelsFromInputs();
        try {
            this.createSurcharge();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return;
        }

        this.pageLegislationConstants.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(maxDayAssessmentBasis.getText() == null || maxDayAssessmentBasis.getText().trim().isEmpty())
            flag=false;
        else if(maxAssessmentBasis.getText() == null || maxAssessmentBasis.getText().trim().isEmpty())
            flag=false;
       else if(basicWeekTime.getText() == null || basicWeekTime.getText().trim().isEmpty())
            flag=false;
       else if(minAssessmentBasis.getText() == null || minAssessmentBasis.getText().trim().isEmpty())
            flag=false;
       else if(taxBonusUnder6.getText() == null || taxBonusUnder6.getText().trim().isEmpty())
            flag=false;
       else if(taxBonusOver6.getText() == null || taxBonusOver6.getText().trim().isEmpty())
            flag=false;
       else if(nonTaxablePart.getText() == null || nonTaxablePart.getText().trim().isEmpty())
            flag=false;
       else if(subsistenceMinimumForAdvances.getText() == null || subsistenceMinimumForAdvances.getText().trim().isEmpty())
            flag=false;
       else if(nightFrom.getText() == null || nightFrom.getText().trim().isEmpty())
            flag=false;
       else if(nightTo.getText() == null || nightTo.getText().trim().isEmpty())
            flag=false;
       else if(maxAssessmentBasisOP.getText() == null || maxAssessmentBasisOP.getText().trim().isEmpty())
            flag=false;
       else if(maxOP.getText() == null || maxOP.getText().trim().isEmpty())
            flag=false;
       else if(limitOV.getText() == null || limitOV.getText().trim().isEmpty())
            flag=false;

        if(from.getValue()==null)
            flag=false;


        if(!flag)
        {
            System.out.println("Nevyplené údaje.");
            label.setText("Nevyplené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        if(from.getValue()==null)
            flag=false;

        if(maxDayAssessmentBasis.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(maxAssessmentBasis.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(basicWeekTime.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(minAssessmentBasis.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(taxBonusUnder6.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(taxBonusOver6.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(nonTaxablePart.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(subsistenceMinimumForAdvances.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(nightFrom.getText().matches("^((0?[0-9])|(1[0-9])|(2[0-3])):(00|15|30|45)$"))
            flag=false;
        else if(nightTo.getText().matches("^((0?[0-9])|(1[0-9])|(2[0-3])):(00|15|30|45)$"))
            flag=false;
        else if(maxAssessmentBasisOP.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(maxOP.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;
        else if(limitOV.getText().matches("^\\+?-?\\d+(\\.\\d+)?$"))
            flag=false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return false;
        }
        return flag;

    }

    private void setModelsFromInputs()
    {
        this.wageConstantsD.setBasicWeekTime(maxDayAssessmentBasis.getText());
        this.wageConstantsD.setMaxAssessmentBasis(maxAssessmentBasis.getText());
        this.wageConstantsD.setMinAssessmentBasis(basicWeekTime.getText());
        this.wageConstantsD.setMaxDayAssessmentBasis(minAssessmentBasis.getText());
        this.wageConstantsD.setTaxBonusUnder6(taxBonusUnder6.getText());
        this.wageConstantsD.setTaxBonusOver6(taxBonusOver6.getText());
        this.wageConstantsD.setNonTaxablePart(nonTaxablePart.getText());
        this.wageConstantsD.setSubsistenceMinimumForAdvances(subsistenceMinimumForAdvances.getText());
        this.wageConstantsD.setNightFrom(nightFrom.getText());
        this.wageConstantsD.setNightTo(nightTo.getText());
        this.wageConstantsD.setMaxAssessmentBasisOP(maxAssessmentBasisOP.getText());
        this.wageConstantsD.setMaxOP(maxOP.getText());
        this.wageConstantsD.setLimitOV(limitOV.getText());
        this.wageConstantsD.setFrom(from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }





}
