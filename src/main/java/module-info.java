module grauly.newtonwarsclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens grauly.newtonwarsclient to javafx.fxml;
    exports grauly.newtonwarsclient;
}