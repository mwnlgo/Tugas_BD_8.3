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

public class CommissionApplication extends Application {
    private static ArrayList<String[]> rows = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        query();
        TableView tableView = new TableView();

        int i = 0;
        for (String column : new String[]{"full_name", "salary", "commission_pct", "commission", "total_salary"}) {
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
        stage.setTitle("Commissions");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void query() {
        rows.clear();
        try {
            Connection connection = HCPDataSource.getConnection();
            if (connection != null) {
                ResultSet result = connection.createStatement().executeQuery("SELECT first_name || ' ' || last_name, salary, COALESCE(commission_pct, 0), COALESCE(commission_pct, 0) * salary, salary + (COALESCE(commission_pct, 0) * salary) FROM employees");
                while (result.next()) {
                    String[] row = new String[5];
                    for (int i = 1; i <= 5; i++) {
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