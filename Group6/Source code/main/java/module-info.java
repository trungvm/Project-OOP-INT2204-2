module com.example.btloop {
    requires javafx.controls;
    requires javafx.fxml;

    opens oop.bufihofa.classes to javafx.base;
    opens oop.bufihofa.app to javafx.fxml;
    exports oop.bufihofa.app;
}