package application.gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerPageFirm
{


    @FXML
    public void initialize() throws IOException, InterruptedException
    {
        MainPaneManager.getC().desibleBackPage();
    }

    public void onFormClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_firm_form"+".fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onPositionClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_firm_position"+".fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }

    public void onPlaceClick(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_firm_place"+".fxml"));
        MainPaneManager.getC().loadAnchorPage(l);
    }
}
