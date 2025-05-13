package tests;

import com.sun.javafx.iio.ios.IosDescriptor;
import entities.PythonLauncher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class maintest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Adduser.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add User");
        primaryStage.show();*/

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Avis.fxml"));
        PythonLauncher.launchPython();
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Add User");
        primaryStage.show();


    }
}
