package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main application class for the Travel Booking System.
 * Handles the initialization of the JavaFX application.
 */
public class Main extends Application {

    private static final String MAIN_FXML_PATH = "/Main.fxml";
    private static final String CSS_PATH = "/application.css";
    private static final String APP_TITLE = "Travel Booking System";

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main FXML file
            URL fxmlUrl = getClass().getResource(MAIN_FXML_PATH);
            if (fxmlUrl == null) {
                showErrorAlert("Resource Error", "Missing FXML File",
                        "Cannot find " + MAIN_FXML_PATH + " in the classpath");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Create the scene
            Scene scene = new Scene(root);

            // Add stylesheet if available
            URL cssUrl = getClass().getResource(CSS_PATH);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("Warning: CSS file not found at " + CSS_PATH);
            }

            // Configure the primary stage
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setMinWidth(800);  // Optional: Set minimum dimensions
            primaryStage.setMinHeight(600); // Optional: Set minimum dimensions

            // Optional: Add application icon
            // URL iconUrl = getClass().getResource("/images/app-icon.png");
            // if (iconUrl != null) {
            //     primaryStage.getIcons().add(new Image(iconUrl.toExternalForm()));
            // }

            // Show the stage
            primaryStage.show();

        } catch (IOException e) {
            showErrorAlert("Loading Error", "Failed to initialize application",
                    "Error details: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showErrorAlert("Application Error", "An unexpected error occurred",
                    "Error details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows an error alert with the specified title, header, and content
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Main method that launches the JavaFX application
     */
    public static void main(String[] args) {
        launch(args);
    }
}

