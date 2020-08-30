package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import entity.ObjectsParams;
import entity.SumObjects;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

public class ChartController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BarChart<String, Number> scheduleObjects;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    private SearchObjectsController searchObjectsController;

//    private ArrayList<SumObjects> sumObjects;

    private HashMap<String, Integer> sumObjects = new HashMap<>();

    @FXML
    void initialize() {

    }

    void setChart(ObservableList<ObjectsParams> objectsParams) {
        for (ObjectsParams object : objectsParams) {
            sumObjects.put(object.getFirmName(), 0);
        }

        for (ObjectsParams object : objectsParams) {
            sumObjects.put(object.getFirmName(), sumObjects.get(object.getFirmName()) + 1);
        }
//        for (Map.Entry<String, Integer> entry : sumObjects.entrySet()) {
//            pieChartObjects.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
//        }

//        categoryAxis.setLabel("Количество предложений от фирмы");
        numberAxis.setLabel("Количество предложений от фирмы");
        scheduleObjects.setTitle("Гистограмма занятости");

        XYChart.Series<String, Number> salesObjects = new XYChart.Series<String, Number>();
//        salesObjects.setName("Количество предложений");
        for (Map.Entry<String, Integer> entry : sumObjects.entrySet()) {
            salesObjects.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));
        }

        scheduleObjects.getData().add(salesObjects);
    }

    void setParent(SearchObjectsController searchObjectsController) {
        this.searchObjectsController = searchObjectsController;
    }
}
