package application.gui.hours;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.models.*;
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

import java.io.IOException;
import java.util.ArrayList;

public class AddHours implements HoursInterface
{

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private PageHours pageHours;
    private RelationD choosenRelationD;
    private WageD choosenWage;
    private ArrayList<AddHoursBox> addHoursBoxes;
    private ArrayList<HoursD> hoursDS;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public AddHours(PageHours pageHours)
    {
        this.pageHours = pageHours;
        this.addHoursBoxes = new ArrayList<>();
        this.hoursDS = new ArrayList<>();
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    private void createHours() throws InterruptedException, IOException, CommunicationException
    {
        for(int i = 0; i< addHoursBoxes.size(); i++)
        {
            addHoursBoxes.get(i).createHours(this.choosenRelationD, this.choosenWage);
        }
    }

    @Override
    public void setChoosenRelation(RelationD relationD) {
        this.choosenRelationD = relationD;
        this.choosenWage = null;
        this.addHoursBoxes.clear();
    }

    @Override
    public RelationD getChoosenRelation() {
        return this.choosenRelationD;
    }

    @Override
    public void setChoosenWage(WageD wageD) {
        this.choosenWage = wageD;
        this.addHoursBoxes.clear();

    }

    @Override
    public WageD getChoosenWage() {
        return this.choosenWage;
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    public ScrollPane sp;
    public VBox vb;
    public HBox hb;
    public Text name, relEmp, relFrom, relTo, relId, relPlace, relPos, wageForm, wageId, wageLabel, wageUnit ;
    public FlowPane fp_hours;
    public Label label;
    public Button addWage, addHours;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    public void initialize()
    {


    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void onAddRelationClick(MouseEvent mouseEvent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddHoursChooserelation.fxml"));
        loader.setControllerFactory(c -> {
            return new AddHoursChooserelation(this);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddHoursChoosewage.fxml"));
        loader.setControllerFactory(c -> {
            return new AddHoursChoosewage(this);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddHoursBox.fxml"));
        loader.setControllerFactory(c -> {
            return new AddHoursBox(this);
        });
        VBox newPane = null;
        try {
            newPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddHoursBox c = loader.getController();
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

        pageHours.updateInfo();
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
        else if(choosenWage == null)
            flag=false;

        if(addHoursBoxes.size() == 0)
            flag=false;
        for(int i = 0; i< addHoursBoxes.size(); i++)
        {
            flag= addHoursBoxes.get(i).checkFormular();
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

        for(int i = 0; i< addHoursBoxes.size(); i++)
        {
            flag= addHoursBoxes.get(i).checkFormularTypes();
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
        for(int i = 0; i< addHoursBoxes.size(); i++)
        {
            addHoursBoxes.get(i).setModelsFromInputs();
        }
    }

    private void addHoursBox(VBox vbox, AddHoursBox addHoursBox)
    {
        this.fp_hours.getChildren().add(vbox);
        this.addHoursBoxes.add(addHoursBox);
    }

    public void removeHoursBox(VBox vbox, AddHoursBox addHoursBox)
    {
        System.out.println("SP: " +sp.getVvalue());
        this.fp_hours.getChildren().remove(vbox);
        this.addHoursBoxes.remove(addHoursBox);
        System.out.println("SP: " +sp.getVvalue());

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
