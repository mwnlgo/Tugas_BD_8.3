package org.example.praktikum_bd_8_3_1;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeApplication extends Application {
    private static ArrayList<String> columns = new ArrayList<>();
    private static ArrayList<String[]> rows = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        query();
        TableView tableView = new TableView();

        int i = 0;
        for (String column : columns) {
            TableColumn<String[], String> tableColumn = new TableColumn<>(column);
            final int finalI = i;
            tableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[finalI]));
            tableView.getColumns().add(tableColumn);
            i++;
        }

        ObservableList<String[]> data = FXCollections.observableArrayList(rows);
        tableView.setItems(data);

        VBox vbox = new VBox(tableView);
        Scene scene = new Scene(vbox);
        stage.setTitle("Employees");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void query() {
        columns.clear();
        rows.clear();
        try {
            Connection connection = HCPDataSource.getConnection();
            if (connection != null) {
                ResultSet columnNames = connection.createStatement().executeQuery("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'employees'");
                int columnCount = 0;
                while (columnNames.next()) {
                    columnCount++;
                    String columnName = columnNames.getString("COLUMN_NAME");
                    columns.add(columnName);
                }
                ResultSet result = connection.createStatement().executeQuery("SELECT * FROM employees");
                while (result.next()) {
                    String[] row = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = result.getString(i);
                    }
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}