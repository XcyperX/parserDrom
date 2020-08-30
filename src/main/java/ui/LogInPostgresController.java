package ui;

import SQL.SQLqueries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LogInPostgresController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginPostgres;

    @FXML
    private TextField passwordPostgres;

    @FXML
    private Button log_InForPostgresButton;

    private SQLqueries sqLqueries;
//Функция авторизации в системе PostgreSQL
    @FXML
    void onLog_InForPostgresButtonClick(ActionEvent event) throws IOException, SQLException {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(loginPostgres.getText());
        arrayList.add(passwordPostgres.getText());
        sqLqueries.exportToJSON(arrayList);
        sqLqueries.setLoginAndPassword(loginPostgres.getText(), passwordPostgres.getText());
        sqLqueries.checkDB();
        Stage mainWindow = (Stage) log_InForPostgresButton.getScene().getWindow();
        mainWindow.close();
    }

    @FXML
    void initialize() {

    }

    public void setParent(SQLqueries sqLqueries) {
        this.sqLqueries = sqLqueries;
    }
}
