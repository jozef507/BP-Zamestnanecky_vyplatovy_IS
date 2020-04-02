package application.gui.firm;


import application.gui.MainPaneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;

public class PageFirm
{


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        MainPaneManager.getC().desibleBackPage();
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void onFormClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageFirmForm.fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onPositionClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageFirmPosition.fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onPlaceClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageFirmPlace.fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }
}
