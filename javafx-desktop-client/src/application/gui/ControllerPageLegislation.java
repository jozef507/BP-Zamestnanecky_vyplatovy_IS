package application.gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ControllerPageLegislation
{


    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        MainPaneManager.getC().desibleBackPage();
    }

    public void onLevelClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_legislation_level"+".fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onMinimumClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_legislation_minimum"+".fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onSurchargeClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_legislation_surcharge"+".fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onLevyClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_legislation_levy"+".fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }
}
