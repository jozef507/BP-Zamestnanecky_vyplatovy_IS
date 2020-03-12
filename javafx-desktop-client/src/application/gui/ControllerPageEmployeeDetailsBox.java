package application.gui;

import application.models.EmployeeD;
import application.models.RelationOV;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControllerPageEmployeeDetailsBox
{

    public Text type;
    public Text origin;
    public Text expire;
    public Text main;
    public Text position;
    public Text place;
    public HBox hb;

    private RelationOV rel;
    private EmployeeD employeeD;

    @FXML
    public void initialize()
    {
        type.setText(this.rel.getType());
        origin.setText(this.rel.getOrigin());
        expire.setText(this.rel.getExpiration());
        main.setText(this.rel.getMain());
        position.setText(this.rel.getPosition());
        place.setText(this.rel.getPlace());
        setStyle();
    }

    public ControllerPageEmployeeDetailsBox(RelationOV relation, EmployeeD employeeD)
    {
        this.rel = relation;
        this.employeeD = employeeD;
    }

    private void setStyle()
    {
        if(!this.rel.getExpiration().equals("aktuálne"))
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.y");
            LocalDate localDate = LocalDate.parse(this.rel.getExpiration(), formatter);
            LocalDate lt = LocalDate.now();

            if(localDate.compareTo(lt)>=0)
            {
                hb.getStyleClass().add("click-box-current");
            }
            else
            {
                hb.getStyleClass().add("click-box");
            }
        }
        else
        {
            hb.getStyleClass().add("click-box-current");
        }


    }

    public void onClicked(MouseEvent mouseEvent)
    {
        FXMLLoader l = new FXMLLoader(getClass().getResource("fxml/"+"page_employee_details_relation"+".fxml"));
        l.setControllerFactory(c -> {
            return new ControllerPageEmployeeDetailsRelation(this.employeeD, this.rel);
        });
        MainPaneManager.getC().loadScrollPage(l);

        MainPaneManager.getC().setBackPage("page_employee_details", this.employeeD.getId());

    }
}
