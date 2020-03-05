package application.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class CustomAlert
{
    private Alert alert;
    ButtonType foo, bar;

    public CustomAlert(String type, String title, String headerText)
    {
        this.alert = new Alert(getType(type));
        this.alert.setTitle(title);
        this.alert.setHeaderText(headerText);
        this.alert.show();
        this.toTop();
    }

    public CustomAlert(String title, String headerText, String content, String button1, String button2)
    {
         foo = new ButtonType(button1, ButtonBar.ButtonData.OK_DONE);
         bar = new ButtonType(button2, ButtonBar.ButtonData.CANCEL_CLOSE);
        this.alert = new Alert(Alert.AlertType.CONFIRMATION, content, foo, bar);
        this.alert.setTitle(title);
        this.alert.setHeaderText(headerText);

    }

    public CustomAlert(String type, String title, String headerText, String text)
    {
        this.alert = new Alert(getType(type));
        this.alert.setTitle(title);
        this.alert.setHeaderText(headerText);
        this.alert.setContentText(text);
        this.alert.show();
        this.toTop();
    }

    private Alert.AlertType getType(String type)
    {
        if(type.equals("error"))
            return Alert.AlertType.ERROR;
        else if(type.equals("warning"))
            return Alert.AlertType.WARNING;
        else if(type.equals("confirmation"))
            return Alert.AlertType.CONFIRMATION;
        else if(type.equals("information"))
            return Alert.AlertType.INFORMATION;
        return Alert.AlertType.NONE;
    }

    private void toTop()
    {
        Stage stage = (Stage) this.alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
    }

    public Alert getAlert()
    {
        return this.alert;
    }

    public boolean showWait()
    {
        Optional<ButtonType> result = alert.showAndWait();
        return foo.equals(result.get());
    }
}
