package application.gui;

import application.gui.employee.PageEmployeeDetails;
import application.gui.employee.PageEmployeeImportant;
import application.httpcomunication.LoggedInUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPain implements Initializable {

    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private String backPage;
    private String arg1, arg2, arg3;


    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/
    public MainPain()
    {
        MainPaneManager.setC(this);
        pageLoader = null;
    }


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    private FXMLLoader pageLoader;
    private Button logoutB;
    @FXML
    private AnchorPane ap;
    @FXML
    private BorderPane bp;
    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7, button8, back;
    @FXML
    private Text user;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.disableButtonsByRole();
        desibleBackPage();
        user.setText(LoggedInUser.getEmail());

    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    public void btn1(MouseEvent mouseEvent)
    {
        loadAnchorPage("employee/PageEmployee");
        menuButtonClicked("b1");
    }

    public void btn2(MouseEvent mouseEvent)
    {

        loadAnchorPage("hours/PageHours");
        menuButtonClicked("b2");
    }

    public void btn3(MouseEvent mouseEvent)
    {
        loadAnchorPage("absence/PageAbsence");
        menuButtonClicked("b3");
    }

    public void btn4(MouseEvent mouseEvent)
    {
        loadAnchorPage("payment/PagePayment");
        menuButtonClicked("b4");
    }

    public void btn5(MouseEvent mouseEvent)
    {
        menuButtonClicked("b5");
    }

    public void btn6(MouseEvent mouseEvent)
    {
        loadScrollPage("firm/PageFirm");
        menuButtonClicked("b6");
    }

    public void btn7(MouseEvent mouseEvent)
    {
        loadScrollPage("legislation/PageLegislation");
        menuButtonClicked("b7");
    }

    public void btn8(MouseEvent mouseEvent)
    {
        menuButtonClicked("b8");
        loadAnchorPage("user/PageUsers");

    }

    public void backClick(MouseEvent mouseEvent)
    {
        if(this.backPage.equals("PageEmployee"))
        {
            loadAnchorPage("employee/PageEmployee");
        }
        else if(this.backPage.equals("PageUsers"))
        {
            loadAnchorPage("user/PageUsers");
        }
        else if(this.backPage.equals("PageFirm"))
        {
            loadScrollPage("firm/PageFirm");
        }
        else if(this.backPage.equals("PageLegislation"))
        {
            loadScrollPage("legislation/PageLegislation");
        }
        else if(this.backPage.equals("PageEmployeeDetails"))
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("employee/PageEmployeeDetails.fxml"));
            l.setControllerFactory(c -> {
                return new PageEmployeeDetails(this.arg1);
            });
            loadScrollPage(l);
        }
        else if(this.backPage.equals("PageUsersDetails"))
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("user/PageUsersDetails.fxml"));
            l.setControllerFactory(c -> {
                return new PageEmployeeDetails(this.arg1);
            });
            loadScrollPage(l);
        }
        else if(this.backPage.equals("PageEmployeeImportant"))
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("employee/PageEmployeeImportant.fxml"));
            l.setControllerFactory(c -> {
                return new PageEmployeeImportant(this.arg1, this.arg2);
            });
            MainPaneManager.getC().loadScrollPage(l);
        }
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/


    private void menuButtonClicked(String button)
    {
        button1.getStyleClass().removeAll("menubuttonclicked");
        button2.getStyleClass().removeAll("menubuttonclicked");
        button3.getStyleClass().removeAll("menubuttonclicked");
        button3.getStyleClass().add("soft-border");
        button4.getStyleClass().removeAll("menubuttonclicked");
        button5.getStyleClass().removeAll("menubuttonclicked");
        button5.getStyleClass().add("soft-border");
        button6.getStyleClass().removeAll("menubuttonclicked");
        button7.getStyleClass().removeAll("menubuttonclicked");
        button7.getStyleClass().add("soft-border");
        button8.getStyleClass().removeAll("menubuttonclicked");

        if(button.equals("b1"))
        {
            button1.getStyleClass().add("menubuttonclicked");
        }
        else if(button.equals("b2"))
        {
            button2.getStyleClass().add("menubuttonclicked");
        }
        else if(button.equals("b3"))
        {
            button3.getStyleClass().removeAll("soft-border");
            button3.getStyleClass().add("menubuttonclicked");
        }
        else if(button.equals("b4"))
        {
            button4.getStyleClass().add("menubuttonclicked");
        }
        else if(button.equals("b5"))
        {
            button5.getStyleClass().removeAll("soft-border");
            button5.getStyleClass().add("menubuttonclicked");
        }
        else if(button.equals("b6"))
        {
            button6.getStyleClass().add("menubuttonclicked");
        }
        else if(button.equals("b7"))
        {
            button7.getStyleClass().removeAll("soft-border");
            button7.getStyleClass().add("menubuttonclicked");
        }
        else if(button.equals("b8"))
        {
            button8.getStyleClass().add("menubuttonclicked");
        }
    }

    public void loadScrollPage(String page)
    {
         try {
            this.pageLoader = new FXMLLoader(getClass().getResource(page+".fxml"));
            ScrollPane newPane = this.pageLoader.load();

            ap.getChildren().removeAll();
            ap.getChildren().setAll(newPane);

            AnchorPane.setBottomAnchor(newPane, 0.0);
            AnchorPane.setLeftAnchor(newPane, 0.0);
            AnchorPane.setRightAnchor(newPane, 0.0);
            AnchorPane.setTopAnchor(newPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadScrollPage(FXMLLoader l)
    {
         try {
             this.pageLoader = l;
            ScrollPane newPane = this.pageLoader.load();

            ap.getChildren().removeAll();
            ap.getChildren().setAll(newPane);

            AnchorPane.setBottomAnchor(newPane, 0.0);
            AnchorPane.setLeftAnchor(newPane, 0.0);
            AnchorPane.setRightAnchor(newPane, 0.0);
            AnchorPane.setTopAnchor(newPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAnchorPage(String page)
    {
         try {
             this.pageLoader = new FXMLLoader(getClass().getResource(page+".fxml"));
             AnchorPane newPane = this.pageLoader.load();

             ap.getChildren().removeAll();
             ap.getChildren().setAll(newPane);

            AnchorPane.setBottomAnchor(newPane, 0.0);
            AnchorPane.setLeftAnchor(newPane, 0.0);
            AnchorPane.setRightAnchor(newPane, 0.0);
            AnchorPane.setTopAnchor(newPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAnchorPage(FXMLLoader l)
    {
         try {
             this.pageLoader = l;
             AnchorPane newPane = this.pageLoader.load();

             ap.getChildren().removeAll();
             ap.getChildren().setAll(newPane);

            AnchorPane.setBottomAnchor(newPane, 0.0);
            AnchorPane.setLeftAnchor(newPane, 0.0);
            AnchorPane.setRightAnchor(newPane, 0.0);
            AnchorPane.setTopAnchor(newPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBackPage(String p)
    {
        this.arg1=null;
        this.arg2=null;
        this.arg3=null;
        this.back.setDisable(false);
        this.backPage = p;
    }

    public void setBackPage(String p, String arg1)
    {
        this.arg1=null;
        this.arg2=null;
        this.arg3=null;
        this.back.setDisable(false);
        this.backPage = p;
        this.arg1 = arg1;
    }

    public void setBackPage(String p, String arg1, String arg2)
    {
        this.arg1=null;
        this.arg2=null;
        this.arg3=null;
        this.back.setDisable(false);
        this.backPage = p;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public void desibleBackPage()
    {
        this.back.setDisable(true);
        this.backPage = null;
    }

    public void logout(MouseEvent mouseEvent) {
        LoggedInUser.logout();
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Prihlasovacie okno");
        primaryStage.setScene(new Scene(root, 464, 294));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void disableButtonsByRole()
    {
        if(LoggedInUser.getRole().equals("admin"))
        {
            loadAnchorPage("user/PageUsers");
            menuButtonClicked("b8");
            button1.setDisable(true);
            button2.setDisable(true);
            button3.setDisable(true);
            button4.setDisable(true);
            button5.setDisable(true);
            button6.setDisable(true);
            button7.setDisable(true);
        }
        else
        {
            loadAnchorPage("employee/PageEmployee");
            menuButtonClicked("b1");
            button8.setDisable(true);
        }
    }


}
