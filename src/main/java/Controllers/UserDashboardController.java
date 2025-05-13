package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class UserDashboardController {
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        // Initialize any necessary components
    }

    @FXML
    private void handleLogout() {
        try {
            // Load the login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewClientEvents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewProfile() {
        // TODO: Implement view profile functionality
    }

    @FXML
    private void editProfile() {
        // TODO: Implement edit profile functionality
    }

    @FXML
    private void browseProducts() {
        // TODO: Implement browse products functionality
    }

    @FXML
    private void viewFavorites() {
        // TODO: Implement view favorites functionality
    }

    @FXML
    private void viewMessages() {
        // TODO: Implement view messages functionality
    }

    @FXML
    private void sendMessage() {
        // TODO: Implement send message functionality
    }

    @FXML
    private void viewHistory() {
        // TODO: Implement view history functionality
    }

    @FXML
    private void viewEvents() {
        // TODO: Implement view events functionality
    }
} 