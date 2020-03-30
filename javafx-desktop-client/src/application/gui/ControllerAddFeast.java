package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.AbsenceD;
import application.models.RelationD;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class ControllerAddFeast
{

    public ScrollPane sp;
    public VBox vb;
    public HBox hb;
    public TextArea characteristic;
    public TableView<RelationD> tab = new TableView<RelationD>();
    public TableColumn idCol, empCol, plaCol, posCol, fromCol, toCol;
    public Label label;
    public DatePicker date;

    private ControllerPageAbsence controllerPageAbsence;
    private ObservableList<RelationD> choosenRelationDS;
    private AbsenceD absenceD;


    public ControllerAddFeast(ControllerPageAbsence controllerPageAbsence)
    {
        this.controllerPageAbsence = controllerPageAbsence;
        this.choosenRelationDS = FXCollections.observableArrayList();
    }

    public void initialize()
    {
        setDatePicker(date);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        empCol.setCellValueFactory(new PropertyValueFactory<>("employeeNameLastname"));
        plaCol.setCellValueFactory(new PropertyValueFactory<>("placeName"));
        posCol.setCellValueFactory(new PropertyValueFactory<>("positionName"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        tab.setItems(choosenRelationDS);
    }


    public void onAddRelationClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/add_feast_chooserelation.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddFeastChooserelation(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Výber zamestnaneckého pracovného vzťahu");
        primaryStage.setScene(new Scene(root1, 1000, 800));
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
            createFeast();
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

        controllerPageAbsence.updateInfo();
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }


    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(choosenRelationDS.size() == 0)
            flag=false;

        if(date.getValue()==null)
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

        absenceD.setReason("sviatok");
        absenceD.setCharacteristic("štátny sviatok");
        absenceD.setHalf("0");
        absenceD.setFrom(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        absenceD.setTo(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        this.absenceD = absenceD;
    }




    public void setRelationElements() {
        tab.setItems(choosenRelationDS);
    }

    private void createFeast() throws InterruptedException, IOException, CommunicationException
    {
        HttpClientClass ht = new HttpClientClass();
        ht.addParam("from", this.absenceD.getFrom());
        ht.addParam("to", this.absenceD.getTo());
        ht.addParam("half", this.absenceD.getHalf());
        ht.addParam("type", this.absenceD.getReason());
        ht.addParam("char", this.absenceD.getCharacteristic());

        int i = 0;
        for(; i<choosenRelationDS.size(); i++){
            ht.addParam("consid"+i, this.choosenRelationDS.get(i).getConditionsID());
        }
        ht.addParam("num", i+"");
        ht.sendPost("absence/crt_fst", LoggedInUser.getToken(), LoggedInUser.getId());
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


    public ObservableList<RelationD> getChoosenRelationDS() {
        return choosenRelationDS;
    }

    public void setChoosenRelationDS(ObservableList<RelationD> choosenRelationDS) {
        this.choosenRelationDS = choosenRelationDS;
    }
}
