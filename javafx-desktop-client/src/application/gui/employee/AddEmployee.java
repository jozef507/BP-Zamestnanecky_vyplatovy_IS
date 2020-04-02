package application.gui.employee;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;
import application.httpcomunication.HttpClientClass;
import application.httpcomunication.LoggedInUser;
import application.models.EmployeeD;
import application.models.ImportantD;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddEmployee
{
    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------FIELDS-----------------------------------------*/
    private PageEmployee pageEmployee;
    private EmployeeD employeeD;
    private ImportantD importantD;

    /*---------------------------------------------------------------------------------------*/
    /*-------------------------------------CONSTRUCTORS--------------------------------------*/


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------------METHODS----------------------------------------*/
    public AddEmployee(PageEmployee pageEmployee) {
        this.pageEmployee = pageEmployee;
    }
    private void createEmployee() throws InterruptedException, IOException, CommunicationException {
        HttpClientClass ht = new HttpClientClass();

        ht.addParam("name", this.employeeD.getName());
        ht.addParam("lastname", this.employeeD.getLastname());
        ht.addParam("phone", this.employeeD.getPhone());
        ht.addParam("bornnum", this.employeeD.getBornNum());
        ht.addParam("borndate", this.employeeD.getBornDate());

        ht.addParam("inscomp", this.importantD.getInsComp());
        ht.addParam("town", this.importantD.getTown());
        ht.addParam("street", this.importantD.getStreet());
        ht.addParam("num", this.importantD.getNum());
        ht.addParam("childunder", this.importantD.getChildUnder());
        ht.addParam("childover", this.importantD.getChildOver());
        ht.addParam("part", this.importantD.getPart());
        ht.addParam("retirement", this.importantD.getRetirement());
        ht.addParam("invalidity", this.importantD.getInvalidity());
        ht.addParam("begin", this.importantD.getFrom());
        ht.addParam("end", this.importantD.getTo());


        ht.sendPost("employee/crt_emp", LoggedInUser.getToken(), LoggedInUser.getId());


    }



    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI FIELDS---------------------------------------*/
    @FXML private TextField phone;
    @FXML private DatePicker borndate;
    @FXML private TextField bornnumber;
    @FXML private TextField lastname;
    @FXML private TextField name;
    @FXML private Button create;
    @FXML private TextField insCompF, townF, streetF, numF, childUnderF,
            childOverF;
    @FXML private ComboBox partF, retirementF, invalidityF;
    @FXML private Button cancel;
    @FXML private Label label;
    @FXML private DatePicker begin;
    @FXML private VBox vb;


    /*---------------------------------------------------------------------------------------*/
    /*----------------------------------GUI INITIALIZATIONS----------------------------------*/
    @FXML
    public void initialize() throws IOException, InterruptedException {
        this.setDatePicker();
        this.setComboBoxes();
        label.setVisible(false);
        this.changeFocus();
        this.setTextfieldLimit(name, 255);
        this.setTextfieldLimit(lastname, 255);
        this.setTextfieldLimit(phone, 10);
        this.setTextfieldLimit(bornnumber, 10);

        this.setTextfieldLimit(insCompF, 255);
        this.setTextfieldLimit(townF, 255);
        this.setTextfieldLimit(streetF, 255);
        this.setTextfieldLimit(numF, 255);
        this.setTextfieldLimit(childOverF, 2);
        this.setTextfieldLimit(childUnderF, 2);
    }

    private void setTextfieldLimit(TextField textArea, int limit)
    {
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= limit ? change : null));
    }

    private void setDatePicker()
    {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("d.M.yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        begin.setConverter(converter);
        begin.setPromptText("D.M.RRRR");
        borndate.setConverter(converter);
        borndate.setPromptText("D.M.RRRR");
    }

    private void setComboBoxes()
    {
        partF.getItems().addAll(
                "áno",
                "nie"
        );

        retirementF.getItems().addAll(
                "áno",
                "nie"
        );

        invalidityF.getItems().addAll(
                "áno",
                "nie"
        );
    }

    private void changeFocus()
    {
        final SimpleBooleanProperty firstTime = new SimpleBooleanProperty(true);
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                vb.requestFocus();
                firstTime.setValue(false);
            }
        });
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI METHODS--------------------------------------*/
    @FXML private void cancelClick(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML private void createClick(MouseEvent mouseEvent)
    {
        if(!this.checkFormular()) return;
        if(!this.checkFormularTypes()) return;

        CustomAlert al = new CustomAlert("Uloženie zmien dôležitých údajov", "Ste si istý že chcete uložiť zmeny dôležitých údajov pracujúceho?", "", "Áno", "Nie");
        if(!al.showWait())
            return;

        this.setModelsFromInputs();
        try {
            this.createEmployee();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());
            return;
        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
            return;
        }

        pageEmployee.updateInfo();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


    /*---------------------------------------------------------------------------------------*/
    /*--------------------------------------GUI HELPERS--------------------------------------*/

    private boolean checkFormular()
    {
        boolean flag = true;
        label.setVisible(false);

        if(name.getText() == null || name.getText().trim().isEmpty())
            flag=false;
        else if(lastname.getText() == null || lastname.getText().trim().isEmpty())
            flag=false;
        else if(phone.getText() == null || phone.getText().trim().isEmpty())
            flag=false;
        else if(bornnumber.getText() == null || bornnumber.getText().trim().isEmpty())
            flag=false;
        else if(insCompF.getText() == null || insCompF.getText().trim().isEmpty())
            flag=false;
        else if(townF.getText() == null || townF.getText().trim().isEmpty())
            flag=false;
        else if(streetF.getText() == null || streetF.getText().trim().isEmpty())
            flag=false;
        else if(numF.getText() == null || numF.getText().trim().isEmpty())
            flag=false;
        else if(childUnderF.getText() == null || childUnderF.getText().trim().isEmpty())
            flag=false;
        else if(childOverF.getText() == null || childOverF.getText().trim().isEmpty())
            flag=false;

        if(partF.getSelectionModel().isEmpty())
            flag=false;
        else if(retirementF.getSelectionModel().isEmpty())
            flag=false;
        else if(invalidityF.getSelectionModel().isEmpty())
            flag=false;

        else if(begin.getValue()==null)
            flag=false;
        else if(borndate.getValue()==null)
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené alebo nesprávne vyplnené údaje.");
            label.setText("Nevyplené údaje.");
            label.setVisible(true);
            return false;
        }
        return flag;
    }

    private boolean checkFormularTypes()
    {
        boolean flag = true;

        if(!phone.getText().matches("(^0\\d{9}$)|(^[+]421\\d{9}$)|(^[+]420\\d{9}$)|(^\\d{9}$)")) flag=false;
        else if(!bornnumber.getText().matches("(^\\d{10}$)")) flag=false;
        else if(!childOverF.getText().matches("(^\\d\\d?$)")) flag=false;
        else if(!childUnderF.getText().matches("(^\\d\\d?$)")) flag=false;
        else if(!numF.getText().matches("(^\\d*$)")) flag=false;

        if(!flag)
        {
            System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            label.setVisible(true);
            return flag;
        }
        return flag;
        /*try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }*/
    }

    private void setModelsFromInputs()
    {
        EmployeeD employeeD = new EmployeeD();
        employeeD.setName(name.getText());
        employeeD.setLastname(lastname.getText());
        employeeD.setPhone(phone.getText());
        employeeD.setBornNum(bornnumber.getText());
        employeeD.setBornDate(borndate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        ImportantD importantD = new ImportantD();
        importantD.setInsComp(insCompF.getText());
        importantD.setTown(townF.getText());
        importantD.setStreet(streetF.getText());
        importantD.setNum(numF.getText());
        importantD.setChildUnder(childUnderF.getText());
        importantD.setChildOver(childOverF.getText());
        String sPart = partF.getValue().toString();
        if(sPart.equals("áno"))
            importantD.setPart("1");
        else
            importantD.setPart("0");
        String sRetirement = retirementF.getValue().toString();
        if(sRetirement.equals("áno"))
            importantD.setRetirement("1");
        else
            importantD.setRetirement("0");
        String sInvalidity = invalidityF.getValue().toString();
        if(sInvalidity.equals("áno"))
            importantD.setInvalidity("1");
        else
            importantD.setInvalidity("0");
        importantD.setFrom(begin.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        importantD.setTo("NULL");

        this.employeeD = employeeD;
        this.importantD = importantD;
    }













}
