package application.gui.absence;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.AbsenceD;
import application.models.RelationD;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddAbsence
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/

    private PageAbsence pageAbsence;
    private RelationD choosenRelationD;
    private AbsenceD absenceD;

    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/

    public AddAbsence(PageAbsence pageAbsence)
    {
        this.pageAbsence = pageAbsence;
    }

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/

    public void setChoosenRelation(RelationD relationD)
    {
        this.choosenRelationD = relationD;
    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/

    public ScrollPane sp;
    public VBox vb;
    public HBox hb;
    public Text relEmp, relFrom, relTo, relId, relPlace, relPos ;
    public TextArea characteristic;
    public DatePicker date, from, to;
    public Label label;
    public ComboBox<String> reason;
    public CheckBox one, range, half;

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/

    public void initialize()
    {
        setTextareaLimit(characteristic, 1024);
        setDatePicker(date);
        setDatePicker(from);
        setDatePicker(to);
        setCommboBoxes();
        setCheckBoxes();
    }

    private void setTextareaLimit(TextArea textArea, int limit)
    {
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= limit ? change : null));
    }

    private void setDatePicker(DatePicker datePicker)
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
        datePicker.setConverter(converter);
        datePicker.setPromptText("D.M.RRRR");
    }

    private void setCommboBoxes()
    {
        reason.getItems().addAll(
                "PN",
                "OČR",
                "dovolenka",
                "náhradné voľno",
                "prekážka z dôvodu všeobecného záujmu",
                "dôležitá osovná prekážka",
                "prekážka na strane zamestnávateľa",
                "iný"
        );
    }

    private void setCheckBoxes()
    {
        one.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    date.setDisable(false);
                    half.setDisable(false);
                    range.setSelected(false);
                } else {
                    date.getEditor().clear();
                    half.setSelected(false);
                    date.setDisable(true);
                    half.setDisable(true);
                }
            }
        });

        range.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    from.setDisable(false);
                    to.setDisable(false);
                    one.setSelected(false);
                } else {
                    from.getEditor().clear();
                    to.getEditor().clear();
                    from.setDisable(true);
                    to.setDisable(true);
                }
            }
        });
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/

    public void onAddRelationClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAbsenceChooserelation.fxml"));
        loader.setControllerFactory(c -> {
            return new AddAbsenceChooserelation(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Výber zamestnaneckého pracovného vzťahu");
        primaryStage.setScene(new Scene(root1, 810, 570));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    public void cancel(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }

    public void create(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Pridanie odpracovaných hodín", "Ste si istý že chcete pridať odpracované hodiny?", "", "Áno", "Nie");
        if(!al.showWait())
            return;

        this.setModelsFromInputs();

        try {
            createAbsence();
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

        pageAbsence.updateInfo();
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(choosenRelationD == null)
            flag=false;

        if(!one.isSelected() && !range.isSelected())
            flag=false;
        else if(!reason.isDisable() && reason.getSelectionModel().isEmpty())
            flag=false;
        else if(!characteristic.isDisable() && (characteristic.getText() == null || characteristic.getText().trim().isEmpty()))
            flag=false;
        else if(!date.isDisable() && date.getValue()==null)
            flag=false;
        else if(!from.isDisable() && from.getValue()==null)
            flag=false;
        else if(!to.isDisable() && to.getValue()==null)
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

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return flag;
        }
        return flag;

    }

    private void setModelsFromInputs()
    {
        AbsenceD absenceD = new AbsenceD();
        absenceD.setReason(reason.getSelectionModel().getSelectedItem());
        absenceD.setCharacteristic(characteristic.getText());

        if (!date.isDisable() && !half.isDisable()) {
            absenceD.setFrom(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            absenceD.setTo(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            absenceD.setHalf((half.isSelected() ? "1" : "0"));
        }else if(!from.isDisable() && !to.isDisable()){
            absenceD.setFrom(from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            absenceD.setTo(to.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            absenceD.setHalf("0");
        }else {
            label.setText("Neočakávana chyba!");
            label.setVisible(true);
        }
        this.absenceD = absenceD;
    }

    public void setRelationElements()
    {
        relId.setText(this.choosenRelationD.getId());
        relPlace.setText(this.choosenRelationD.getPlaceName());
        relFrom.setText(this.choosenRelationD.getFrom());
        relEmp.setText(this.choosenRelationD.getEmployeeNameLastname());
        relPos.setText(this.choosenRelationD.getPositionName());
        relTo.setText(this.choosenRelationD.getTo());
    }

    private void createAbsence() throws InterruptedException, IOException, CommunicationException
    {
        HttpClientClass ht = new HttpClientClass();
        ht.addParam("consid", this.choosenRelationD.getConditionsID());
        ht.addParam("from", this.absenceD.getFrom());
        ht.addParam("to", this.absenceD.getTo());
        ht.addParam("half", this.absenceD.getHalf());
        ht.addParam("type", this.absenceD.getReason());
        ht.addParam("char", this.absenceD.getCharacteristic());
        ht.sendPost("absence/crt_bsnc", LoggedInUser.getToken(), LoggedInUser.getId());
    }


}

/*---------------------------------------------------------------------------------------*/
/*----------------------------------------FIELDS-----------------------------------------*/


/*---------------------------------------------------------------------------------------*/
/*-------------------------------------CONSTRUCTORS--------------------------------------*/


/*---------------------------------------------------------------------------------------*/
/*----------------------------------------METHODS----------------------------------------*/



/*---------------------------------------------------------------------------------------*/
/*--------------------------------------GUI FIELDS---------------------------------------*/


/*---------------------------------------------------------------------------------------*/
/*----------------------------------GUI INITIALIZATIONS----------------------------------*/


/*---------------------------------------------------------------------------------------*/
/*--------------------------------------GUI METHODS--------------------------------------*/


/*---------------------------------------------------------------------------------------*/
/*--------------------------------------GUI HELPERS--------------------------------------*/

