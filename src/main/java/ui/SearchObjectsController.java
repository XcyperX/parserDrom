package ui;

import SQL.SQLqueries;
import entity.ObjectsParams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;

public class SearchObjectsController {

    @FXML
    private ListView<String> historyListView;

    @FXML
    private TextField vendorCodeTextField;

    @FXML
    private TextField nameFirmTextField;

    @FXML
    private TableView<ObjectsParams> tableObjects;

    @FXML
    private TableColumn<ObjectsParams, String> numberObject;

    @FXML
    private TableColumn<ObjectsParams, String> nameObject;

    @FXML
    private TableColumn<ObjectsParams, String> firmObject;

    @FXML
    private TableColumn<ObjectsParams, Float> priceObject;

    private int tempHash = 0;

//Слушатель кнопки, который проверяет заполенность полей и на их основе вызывает определенные функции
    @FXML
    void onSearchButtonClick(ActionEvent event) throws IOException, SQLException {
        SQLqueries sqLqueries = new SQLqueries();
//        Если есть только id
        if (!vendorCodeTextField.getText().isEmpty() && nameFirmTextField.getText().isEmpty()) {
            Document document = Jsoup.connect("https://baza.drom.ru/oem/" + vendorCodeTextField.getText()).get();
            if (this.tempHash != document.hashCode()) {
                    searchVendorCode(document);
                    this.tempHash = document.hashCode();
                    sqLqueries.insertHistory(vendorCodeTextField.getText());
                    loadHistory();
                }
//        Если есть id и название фирмы
        } else if (!vendorCodeTextField.getText().isEmpty() && !nameFirmTextField.getText().isEmpty()) {
            Document document = Jsoup.connect("https://baza.drom.ru/oem/" + vendorCodeTextField.getText()).get();
            if (this.tempHash != document.hashCode()) {
                searchFirmNameAndVendorCode(document);
                this.tempHash = document.hashCode();
                sqLqueries.insertHistory(vendorCodeTextField.getText());
                loadHistory();
            }
        }
    }

    @FXML
    void onShowChartButtonClick(ActionEvent event) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ChartMenuController.fxml"));
            root = loader.load();
            Stage directorMenu = new Stage();
            directorMenu.setTitle("Графики");
            directorMenu.setScene(new Scene(root));
            ChartController chartController = loader.getController();
            chartController.setParent(this);
            chartController.setChart(tableObjects.getItems());
            directorMenu.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//Парсинг данных при использовании только id
    void searchVendorCode(Document listObjects) {
        if (!listObjects.select(".-exact.bull-item.bull-item_inline").isEmpty()) {
            Elements matches = listObjects.select(".-exact.bull-item.bull-item_inline");
            ObservableList<ObjectsParams> objectsParamsObservableList = FXCollections.observableArrayList();
            int i = 1;
            for (Element match : matches) {
                ObjectsParams objectsParams = new ObjectsParams();
                objectsParams.setId(i);
                objectsParams.setNameObject(match.selectFirst(".bulletinLink.bull-item__self-link.auto-shy").text());
                if (match.selectFirst(".bull-item__annotation-row.manufacturer") != null) {
                    objectsParams.setFirmName(match.selectFirst(".bull-item__annotation-row.manufacturer").text());
                } else {
                    objectsParams.setFirmName("-");
                }
                if (match.selectFirst(".price-per-quantity__price") != null) {
                    objectsParams.setPriceObject(Float.parseFloat(match.selectFirst(".price-per-quantity__price").text().replace("₽", "").replace(" ", "")));
                } else if (match.selectFirst(".price-block__final-price.finalPrice.tooltip-element.tooltip-s") != null) {
                    objectsParams.setPriceObject(Float.parseFloat(match.selectFirst(".price-block__final-price.finalPrice.tooltip-element.tooltip-s").text().replace("₽", "").replace(" ", "")));
                } else {
                    objectsParams.setPriceObject(0);
                }
                i++;
                objectsParamsObservableList.add(objectsParams);
            }
            loadTable(objectsParamsObservableList);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ничего не найдено!!");
            alert.show();
        }
    }
//Парсинг данных при использовании id и названия фирмы
    void searchFirmNameAndVendorCode(Document listObjects) {
        if (!listObjects.select(".-exact.bull-item.bull-item_inline").isEmpty()) {
            ObservableList<ObjectsParams> objectsParamsObservableList = FXCollections.observableArrayList();
            Elements matches = listObjects.select(".-exact.bull-item.bull-item_inline");
            int i = 1;
            for (Element match : matches) {
                ObjectsParams objectsParams = new ObjectsParams();
                objectsParams.setId(i);
                objectsParams.setNameObject(match.selectFirst(".bulletinLink.bull-item__self-link.auto-shy").text());
                if (match.selectFirst(".bull-item__annotation-row.manufacturer") != null) {
                    objectsParams.setFirmName(match.selectFirst(".bull-item__annotation-row.manufacturer").text());
                } else {
                    objectsParams.setFirmName("-");
                }
                if (match.selectFirst(".price-per-quantity__price") != null) {
                    objectsParams.setPriceObject(Float.parseFloat(match.selectFirst(".price-per-quantity__price").text().replace("₽", "").replace(" ", "")));
                } else if (match.selectFirst(".price-block__final-price.finalPrice.tooltip-element.tooltip-s") != null) {
                    objectsParams.setPriceObject(Float.parseFloat(match.selectFirst(".price-block__final-price.finalPrice.tooltip-element.tooltip-s").text().replace("₽", "").replace(" ", "")));
                } else {
                    objectsParams.setPriceObject(0);
                }
                i++;
                objectsParamsObservableList.add(objectsParams);
            }
            objectsParamsObservableList.removeIf(j -> !j.getFirmName().equals(nameFirmTextField.getText()));
            if (objectsParamsObservableList.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Ничего не найдено!!");
                alert.show();
            } else {
                loadTable(objectsParamsObservableList);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ничего не найдено!!");
            alert.show();
        }
    }
//Функция загрузки объектов в таблицу
    void loadTable(ObservableList<ObjectsParams> objects){
        tableObjects.refresh();
        numberObject.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameObject.setCellValueFactory(new PropertyValueFactory<>("nameObject"));
        firmObject.setCellValueFactory(new PropertyValueFactory<>("firmName"));
        priceObject.setCellValueFactory(new PropertyValueFactory<>("priceObject"));
        tableObjects.setItems(objects);
    }

    @FXML
    void initialize() throws SQLException {
        loadHistory();
        historyList();
    }
//Функция загрузки объектов в историю
    void loadHistory() throws SQLException {
        SQLqueries sqLqueries = new SQLqueries();
        ObservableList<String> carData = FXCollections.observableArrayList(sqLqueries.selectHistory());
        historyListView.setItems(carData);
    }
//Слушатель, который при двойном клике на id из списка, вызывает функцию парсинга, для этого id
    void historyList() {
        historyListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    Document document = null;
                    try {
                        vendorCodeTextField.setText(historyListView.getSelectionModel()
                                .getSelectedItem());
                        document = Jsoup.connect("https://baza.drom.ru/oem/" + historyListView.getSelectionModel()
                                .getSelectedItem()).get();
                        searchVendorCode(document);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}