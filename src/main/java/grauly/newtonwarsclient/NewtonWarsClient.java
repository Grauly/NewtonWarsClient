package grauly.newtonwarsclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NewtonWarsClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NewtonWarsClient.class.getResource("newtonwars-main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 900);
        stage.setTitle("Newtonwars");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}