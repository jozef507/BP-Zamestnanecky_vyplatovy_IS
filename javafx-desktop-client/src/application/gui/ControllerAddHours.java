package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ControllerAddHours implements ControllerIntHours
{

    public ScrollPane sp;
    public VBox vb;
    public HBox hb;
    public Text name, relEmp, relFrom, relTo, relId, relPlace, relPos, wageForm, wageId, wageLabel, wageUnit ;
    public FlowPane fp_hours;
    public Label label;
    public Button addWage, addHours;

    private ControllerPageHours controllerPageHours;

    private RelationD choosenRelationD;
    private WageD choosenWage;

    private ArrayList<ControllerAddHoursBox> controllerAddHoursBoxes;
    private ArrayList<HoursD> hoursDS;


    public ControllerAddHours(ControllerPageHours controllerPageHours)
    {
        this.controllerPageHours = controllerPageHours;
        this.controllerAddHoursBoxes = new ArrayList<>();
        this.hoursDS = new ArrayList<>();
    }

    public void initialize()
    {


    }




    public void onAddRelationClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/add_hours_chooserelation.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddHoursChooserelation(this);
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

    public void onAddWageClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/add_hours_choosewage.fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddHoursChoosewage(this);
        });
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Výber mzdy pracovného vzťahu");
        primaryStage.setScene(new Scene(root1, 810, 570));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }


    public void onAddHoursClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/"+"add_hours_box"+".fxml"));
        loader.setControllerFactory(c -> {
            return new ControllerAddHoursBox(this);
        });
        VBox newPane = null;
        try {
            newPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ControllerAddHoursBox c = loader.getController();
        this.addHoursBox(newPane, c);
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
            createHours();
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

        controllerPageHours.updateInfo();
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }


    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(choosenRelationD == null)
            flag=false;
        else if(choosenWage == null)
            flag=false;

        if(controllerAddHoursBoxes.size() == 0)
            flag=false;
        for(int i = 0;i<controllerAddHoursBoxes.size();i++)
        {
            flag=controllerAddHoursBoxes.get(i).checkFormular();
            if(!flag)
                break;
        }

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

        for(int i = 0;i<controllerAddHoursBoxes.size();i++)
        {
            flag=controllerAddHoursBoxes.get(i).checkFormularTypes();
            if(!flag) break;
        }

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
        for(int i = 0;i<controllerAddHoursBoxes.size();i++)
        {
            controllerAddHoursBoxes.get(i).setModelsFromInputs();
        }
    }

    private void createHours() throws InterruptedException, IOException, CommunicationException
    {
        for(int i = 0;i<controllerAddHoursBoxes.size();i++)
        {
            controllerAddHoursBoxes.get(i).createHours(this.choosenRelationD, this.choosenWage);
        }
    }

    private void addHoursBox(VBox vbox, ControllerAddHoursBox controllerAddHoursBox)
    {
        this.fp_hours.getChildren().add(vbox);
        this.controllerAddHoursBoxes.add(controllerAddHoursBox);
    }

    public void removeHoursBox(VBox vbox, ControllerAddHoursBox controllerAddHoursBox)
    {
        System.out.println("SP: " +sp.getVvalue());
        this.fp_hours.getChildren().remove(vbox);
        this.controllerAddHoursBoxes.remove(controllerAddHoursBox);
        System.out.println("SP: " +sp.getVvalue());

    }

    @Override
    public void setChoosenRelation(RelationD relationD) {
        this.choosenRelationD = relationD;
        this.choosenWage = null;
        this.controllerAddHoursBoxes.clear();
    }

    @Override
    public RelationD getChoosenRelation() {
        return this.choosenRelationD;
    }

    @Override
    public void setChoosenWage(WageD wageD) {
        this.choosenWage = wageD;
        this.controllerAddHoursBoxes.clear();

    }

    @Override
    public WageD getChoosenWage() {
        return this.choosenWage;
    }

    @Override
    public void setRelationElements() {
        clearWageElements();
        relId.setText(this.choosenRelationD.getId());
        relPlace.setText(this.choosenRelationD.getPlaceName());
        relFrom.setText(this.choosenRelationD.getFrom());
        relEmp.setText(this.choosenRelationD.getEmployeeNameLastname());
        relPos.setText(this.choosenRelationD.getPositionName());
        relTo.setText(this.choosenRelationD.getTo());
        addWage.setDisable(false);
    }

    @Override
    public void setWageElements() {
        wageId.setText(this.choosenWage.getId());
        wageForm.setText(this.choosenWage.getWageFormName());
        wageLabel.setText(this.choosenWage.getLabel());
        wageUnit.setText(this.choosenWage.getWageFormUnit());
        addHours.setDisable(false);
        this.fp_hours.getChildren().clear();
    }

    private void clearWageElements()
    {
        wageId.setText("");
        wageForm.setText("");
        wageLabel.setText("");
        wageUnit.setText("");
        addHours.setDisable(true);
        this.fp_hours.getChildren().clear();
    }


}
