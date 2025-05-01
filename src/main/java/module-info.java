module org.example.praktikum_bd_8_3_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires java.sql;


    opens org.example.praktikum_bd_8_3_1 to javafx.fxml;
    exports org.example.praktikum_bd_8_3_1;
}