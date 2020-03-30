package application.gui;

import application.models.PositionD;
import application.models.RelationD;
import application.models.WageD;
import javafx.scene.layout.VBox;

public interface ControllerIntHours
{
    public void setChoosenRelation(RelationD relationD);
    public RelationD getChoosenRelation();
    public void setChoosenWage(WageD wageD);
    public WageD getChoosenWage();
    public void setRelationElements();
    public void setWageElements();
    public void removeHoursBox(VBox vb, ControllerAddHoursBox controllerAddHoursBox);


}
