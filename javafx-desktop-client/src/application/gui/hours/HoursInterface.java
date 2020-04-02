package application.gui.hours;

import application.models.RelationD;
import application.models.WageD;
import javafx.scene.layout.VBox;

public interface HoursInterface
{
    public void setChoosenRelation(RelationD relationD);
    public RelationD getChoosenRelation();
    public void setChoosenWage(WageD wageD);
    public WageD getChoosenWage();
    public void setRelationElements();
    public void setWageElements();
    public void removeHoursBox(VBox vb, AddHoursBox addHoursBox);


}
