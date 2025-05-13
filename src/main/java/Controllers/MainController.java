package Controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.Objects;

/**
 * Main controller responsible for navigation between different scenes
 * in the Travel Booking application.
 */
public class MainController {

    private Stage stage;
    private Scene scene;

    /**
     * Opens the flight search screen
     */
    @FXML
    private void openFlightSearch(ActionEvent event) {
        loadScene(event, "/flightsearch.fxml", "Search Flights");
    }

    /**
     * Opens the accommodation search screen
     */
    @FXML
    private void openAccommodationSearch(ActionEvent event) {
        loadScene(event, "/accommodation_search.fxml", "Search Accommodations");
    }

    /**
     * Opens the bookings list screen
     */
    @FXML
    private void openBookings(ActionEvent event) {
        loadScene(event, "/booking_list.fxml", "My Bookings");
    }

    /**
     * Opens the payment history screen
     */
    @FXML
    private void openPayments(ActionEvent event) {
        loadScene(event, "/payment.fxml", "Payment History");
    }

    /**
     * Opens the accommodation booking screen
     */
    @FXML
    private void openAccommodationBooking(ActionEvent event) {
        loadScene(event, "/accommodation_booking.fxml", "Book Accommodation");
    }

    /**
     * Opens the flight booking screen
     */
    @FXML
    private void openFlightBooking(ActionEvent event) {
        loadScene(event, "/flight_booking.fxml", "Book Flight");
    }

    /**
     * Returns to the main menu
     */
    @FXML
    private void goToMainMenu(ActionEvent event) {
        loadScene(event, "/Main.fxml", "Travel Booking System");
    }

    /**
     * Handles the logout action
     */
    @FXML
    private void logout(ActionEvent event) {
        // Implement logout functionality here if needed
        // For example, clearing session data or user credentials

        // Return to main screen or login screen
        loadScene(event, "/Main.fxml", "Travel Booking System");
    }

    /**
     * Loads a new scene in the same window
     *
     * @param event The action event that triggered this method
     * @param fxmlPath The path to the FXML file to load
     * @param title The title for the window
     */
    private void loadScene(Event event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not load scene: " + fxmlPath, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens a scene in a new window
     *
     * @param fxmlPath The path to the FXML file to load
     * @param title The title for the new window
     */
    public void openNewWindow(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage newStage = new Stage();
            newStage.setTitle(title);
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not open new window: " + fxmlPath, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows an error alert with the specified title, header and content
     *
     * @param title The title of the alert
     * @param header The header text of the alert
     * @param content The content text of the alert
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        // Initialize any components or data needed by the main controller
    }
}
