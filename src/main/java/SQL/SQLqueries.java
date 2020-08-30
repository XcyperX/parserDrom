package SQL;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.LogInPostgresController;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class SQLqueries {
    private static final String FILE_NAME = "data.json";
    private String login = "";
    private String password = "";

    public void checkDB() throws SQLException, IOException {
        readToJSON();
        if (getDBConnectionTest() == null) {
            showLoginPostgres();
        } else if (getDBConnection() == null){
            createDatabase();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/SearchObjectsMenu.fxml"));
            primaryStage.setTitle("Начальное меню");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } else if (getDBConnection() != null) {
            readToJSON();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/SearchObjectsMenu.fxml"));
            primaryStage.setTitle("Начальное меню");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    private Connection getDBConnectionTest() {
        try {
            Connection dbConnection = null;
            dbConnection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/",  this.login, this.password);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println("Ошибочка (((");
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Connection getDBConnection() {
        try {
            Connection dbConnection = null;
            dbConnection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/parser_drom", this.login, this.password);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void createDatabase() throws SQLException, IOException {
        Connection dbConnection = null;
        Statement statement = null;
        String queries = "CREATE DATABASE parser_drom";

        try {
            dbConnection = getDBConnectionTest();
            statement = dbConnection.createStatement();
            // выполнить SQL запрос
            statement.executeUpdate(queries);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
        createDatabaseTable();
    }

    public void createDatabaseTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        String createTableEmployee = "CREATE TABLE \"history_data\" (\n" +
                                    "\t\"id\" serial NOT NULL,\n" +
                                    "\t\"id_object\" TEXT NOT NULL,\n" +
                                    "\tCONSTRAINT \"employee_data_pk\" PRIMARY KEY (\"id\")\n" +
                                    ") WITH (\n" +
                                    "  OIDS=FALSE\n" +
                                    ");\n";

        try {
//            Создание таблиц
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(createTableEmployee);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public ObservableList<String> selectHistory() throws SQLException {
        readToJSON();
        Connection dbConnection = null;
        Statement statement = null;
        ObservableList<String> historyList = FXCollections.observableArrayList();
        String queries = "SELECT id_object " +
                "FROM history_data";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            // выполнить SQL запрос
            ResultSet resultSet = statement.executeQuery(queries);
            while (resultSet.next()) {
                historyList.add(resultSet.getString("id_object"));
            }
            return historyList;
        } catch (SQLException e) {
            return null;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public void insertHistory(String name) throws SQLException {
        readToJSON();
        Connection dbConnection = null;
        Statement statement = null;
        String insertEmployeeData = ("INSERT INTO history_data (id_object) \n" +
                "VALUES ('%s')");
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            // выполнить SQL запрос
            statement.executeUpdate(String.format(insertEmployeeData, name));
            statement.close();
            dbConnection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public void showLoginPostgres() throws IOException {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/LogInPostgresMenu.fxml"));
            root = loader.load();
            Stage loginPostgres = new Stage();
            loginPostgres.setTitle("Авторизация PostgreSQL");
            loginPostgres.initModality(Modality.APPLICATION_MODAL);
            loginPostgres.setScene(new Scene(root));
            LogInPostgresController logInPostgresController = loader.getController();
            logInPostgresController.setParent(this);
            loginPostgres.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportToJSON(ArrayList<String> settings) throws IOException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(settings);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(FILE_NAME);
            fileOutputStream.write(jsonString.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readToJSON() {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(FILE_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            ArrayList dataItems = gson.fromJson(streamReader, ArrayList.class);
            if (dataItems != null) {
                this.login = dataItems.get(0).toString();
                this.password = dataItems.get(1).toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setLoginAndPassword(String login, String password) {
        this.login = login;
        this.password = password;
    }
}


