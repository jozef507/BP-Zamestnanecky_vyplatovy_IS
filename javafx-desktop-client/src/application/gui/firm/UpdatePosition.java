package application.gui;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.JsonArrayClass;
import application.httpcomunication.LoggedInUser;
import application.models.LevelD;
import application.models.PlaceD;
import application.models.PositionD;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerUpdatePosition
{
    public Button cancel;
    public Button create;
    public Label label;
    public ComboBox level;


    private ControllerPageFirmPosition controllerPageFirmPosition;
    private PositionD positionD;

    private ArrayList<LevelD> levelDS;

    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        setComboboxes();
    }

    public ControllerUpdatePosition(ControllerPageFirmPosition controllerPageFirmPosition, PositionD positionD) {
        this.controllerPageFirmPosition = controllerPageFirmPosition;
        this.positionD = new PositionD();
        this.positionD.setId(positionD.getId());
        try {
            this.levelDS = this.getLevels();
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
    }

    public void cancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;

        CustomAlert al = new CustomAlert("Vytvorenie formy mzdy", "Ste si istý že chcete vytvoriť novú pracovnú pozíciu?", "", "Áno", "Nie");
        if(!al.showWait()) return;

        this.setModelsFromInputs();
        try {
            this.updatePosition();
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

        this.controllerPageFirmPosition.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

       if(level.getSelectionModel().isEmpty())
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené alebo nesprávne vyplnené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private void setModelsFromInputs()
    {
        String choosenLevel = level.getValue().toString();
        for(LevelD l:this.levelDS)
        {
            if(choosenLevel.equals(l.getComboboxString()))
                this.positionD.setLevelID(l.getId());
        }
    }

    private void updatePosition() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("positionid", this.positionD.getLevelID());
        ht.addParam("levelid", this.positionD.getId());

        ht.sendPost("position/upd_pos", LoggedInUser.getToken(), LoggedInUser.getId());
    }

    private ArrayList<LevelD> getLevels() throws IOException, InterruptedException, CommunicationException {
        ArrayList<LevelD> levels = new ArrayList<>();
        HttpClientClass ht = new HttpClientClass();
        ht.sendGet("position/lvls", LoggedInUser.getToken(), LoggedInUser.getId());

        JsonArrayClass json = new JsonArrayClass(ht.getRespnseBody());
        for(int i=0; i<json.getSize(); i++)
        {
            LevelD levelD = new LevelD();
            levelD.setId(json.getElement(i, "id"));
            levelD.setNumber(json.getElement(i, "cislo_stupna"));
            levelD.setCaracteristic(json.getElement(i, "charakteristika"));
            levelD.setFrom(json.getElement(i, "platnost_od"));
            levelD.setTo(json.getElement(i, "platnost_do"));
            levels.add(levelD);
        }
        return levels;
    }

    private void setComboboxes()
    {
        for (LevelD l:this.levelDS)
        {
            level.getItems().add(l.getComboboxString());
        }
    }




}
