package application.gui;

import application.models.RelationOV;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ControllerPageEmployeeDetailsBox
{

    public Text type;
    public Text origin;
    public Text expire;
    public Text main;
    public Text position;
    public Text place;

    private RelationOV rel;

    @FXML
    public void initialize()
    {
        type.setText(this.rel.getType());
        origin.setText(this.rel.getOrigin());
        expire.setText(this.rel.getExpiration());
        main.setText(this.rel.getMain());
        position.setText(this.rel.getPosition());
        place.setText(this.rel.getPlace());
    }

    public ControllerPageEmployeeDetailsBox(RelationOV relation)
    {
        this.rel = relation;
    }
}
