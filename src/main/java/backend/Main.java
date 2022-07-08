package backend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) {
        MainApp.main(args);
    }

    public static class MainApp extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/hello-view.fxml"));
            Scene scene = new Scene(loader.load());

            stage.setTitle("JCalculator");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }

    }
}
