module org.example.slutprojektfe {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.slutprojektfe to javafx.fxml;
    exports org.example.slutprojektfe;
}