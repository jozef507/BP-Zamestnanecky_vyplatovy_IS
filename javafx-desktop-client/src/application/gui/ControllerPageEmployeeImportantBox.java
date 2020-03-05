package application.gui;

import application.models.RelationOV;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ControllerPageEmployeeImportantBox {

    public Text town;
    public Text street;
    public Text num;
    public Text from;
    public Text insComp;
    public Text to;
    public Text children_under;
    public Text children_over;
    public Text retirement;
    public Text invalidity;
    public Text part;

    private String sinsComp;
    private String stown;
    private String sstreet;
    private String snum;
    private String schildren_under;
    private String schildren_over;
    private String spart;
    private String sretirement;
    private String sinvalidity;
    private String sfrom;
    private String sto;



    @FXML
    public void initialize()
    {
        town.setText(stown);
        street.setText(sstreet);
        num.setText(snum);
        from.setText(sfrom);
        insComp.setText(sinsComp);
        to.setText(sto);
        children_under.setText(schildren_under);
        children_over.setText(schildren_over);
        retirement.setText(sretirement);
        invalidity.setText(sinvalidity);
        part.setText(spart);
    }

    public ControllerPageEmployeeImportantBox(String sinsComp, String stown, String sstreet, String snum, String schildren_under, String schildren_over, String spart, String sretirement, String sinvalidity, String sfrom, String sto) {
        this.sinsComp = sinsComp;
        this.stown = stown;
        this.sstreet = sstreet;
        this.snum = snum;
        this.schildren_under = schildren_under;
        this.schildren_over = schildren_over;
        this.spart = spart;
        this.sretirement = sretirement;
        this.sinvalidity = sinvalidity;
        this.sfrom = sfrom;
        this.sto = sto;
    }
}
