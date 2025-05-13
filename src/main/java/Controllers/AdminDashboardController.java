package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class AdminDashboardController {
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
    private void navigateToSystemSettings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainEventView.fxml"));
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
    private void navigateToUserManagement(ActionEvent event) {
        // TODO: Implement user management navigation
    }

    @FXML
    private void viewUserStats(ActionEvent event) {
        // TODO: Implement user statistics view
    }

    @FXML
    private void navigateToProductManagement(ActionEvent event) {
        // TODO: Implement product management navigation
    }

    @FXML
    private void navigateToCategoryManagement(ActionEvent event) {
        // TODO: Implement category management navigation
    }

    @FXML
    private void generateReports(ActionEvent event) {
        // TODO: Implement report generation
    }

    @FXML
    private void viewAnalytics(ActionEvent event) {
        // TODO: Implement analytics view
    }
} 