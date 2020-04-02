package application.gui.legislation;


import application.gui.MainPaneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class PageLegislation
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
    public void onLevelClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageLegislationLevel.fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onMinimumClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageLegislationMinimum.fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onSurchargeClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageLegislationSurcharge.fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onLevyClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("PageLegislationLevy.fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }
}
