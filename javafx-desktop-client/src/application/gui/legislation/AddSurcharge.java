package application.gui.legislation;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.SurchargeTypeD;
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

public class AddSurcharge
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private ArrayList<String> bases;
    private ArrayList<String> names;
    private PageLegislationSurcharge pageLegislationSurcharge;
    private SurchargeTypeD surchargeTypeD;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public AddSurcharge(PageLegislationSurcharge pageLegislationSurcharge) {
        this.pageLegislationSurcharge = pageLegislationSurcharge;
        this.surchargeTypeD = new SurchargeTypeD();
        this.bases = new ArrayList<>();
        this.bases.add("minimálna mzda");
        this.bases.add("priemerná mzda");
        this.bases.add("základná mzda");
        this.names = new ArrayList<>();
        this.names.add("sobota");
        this.names.add("nedeľa");
        this.names.add("noc");
        this.names.add("sviatok");
        this.names.add("nadčas");
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private void createSurcharge() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("name", this.surchargeTypeD.getName());
        ht.addParam("description", this.surchargeTypeD.getDescription());
        ht.addParam("part", this.surchargeTypeD.getPart());
        ht.addParam("base", this.surchargeTypeD.getBase());
        ht.addParam("from", this.surchargeTypeD.getFrom());

        ht.sendPost("surcharge/crt_sur", LoggedInUser.getToken(), LoggedInUser.getId());
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public Button cancel;
    public Button create;
    public DatePicker from;
    public TextField part;
    public TextField description;
    public ComboBox<String> base, name;
    public Label label;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setDatePicker();
        setComboboxes();
        changeFocus();
        setTextfieldLimit(description, 20);
    }

    private void setTextfieldLimit(TextInputControl textArea, int limit)
    {
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length()  <= limit ? change : null));
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

    private void setComboboxes()
    {
        for (String p:this.bases)
        {
            base.getItems().add(p);
        }

        for (String p:this.names)
        {
            name.getItems().add(p);
        }

    }

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                name.getParent().requestFocus();
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

        CustomAlert al = new CustomAlert("Vytvorenie stupňa náročnosti", "Ste si istý že chcete vytvoriť nový stupeň náročnosti?", "", "Áno", "Nie");
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

        this.pageLegislationSurcharge.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(name.getSelectionModel().isEmpty())
            flag=false;
        /*else if(description.getText() == null || description.getText().trim().isEmpty())
            flag=false;*/
        else if(part.getText() == null || part.getText().trim().isEmpty())
            flag=false;

        if(from.getValue()==null)
            flag=false;

        if(base.getSelectionModel().isEmpty())
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

       try {
            double d = Double.parseDouble(part.getText());
            if(!(d>=0 && d<=1)) flag = false;
        } catch (NumberFormatException nfe) {
           flag=false;
        }

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
        this.surchargeTypeD.setName(name.getValue());
        this.surchargeTypeD.setDescription(description.getText());
        this.surchargeTypeD.setPart(part.getText());
        this.surchargeTypeD.setFrom(from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.surchargeTypeD.setBase(base.getValue());
    }





}
