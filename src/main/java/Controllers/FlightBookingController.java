package Controllers;

import Models.Flight;
import Models.FlightBooking;
import Services.FlightBookingService;
import DAO.FlightBookingDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class FlightBookingController {

    @FXML private Label flightInfoLabel;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField passengerCountField;
    @FXML private Button bookButton;

    private Flight selectedFlight;
    private final FlightBookingService bookingService = new FlightBookingService(new FlightBookingDAO());

    // Default capacity for flights - adjust as appropriate for your application
    private static final int DEFAULT_FLIGHT_CAPACITY = 200;

    public FlightBookingController() throws SQLException {
    }

    public void setFlight(Flight flight) {
        this.selectedFlight = flight;
        flightInfoLabel.setText("Booking flight to " + flight.getDestination() + " with " + flight.getAirline());
    }

    @FXML
    private void onBookClick() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        int passengers = 1; // Default to 1 passenger

        if (name.isEmpty() || email.isEmpty()) {
            showAlert("Validation Error", "Please enter both name and email.", Alert.AlertType.WARNING);
            return;
        }

        // Parse passenger count if available
        if (passengerCountField != null && !passengerCountField.getText().trim().isEmpty()) {
            try {
                passengers = Integer.parseInt(passengerCountField.getText().trim());
                if (passengers <= 0) {
                    showAlert("Validation Error", "Number of passengers must be positive.", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Please enter a valid number of passengers.", Alert.AlertType.WARNING);
                return;
            }
        }

        // Use default capacity since Flight class doesn't have a getCapacity() method
        int totalCapacity = DEFAULT_FLIGHT_CAPACITY;

        if (!bookingService.isCapacityAvailable(selectedFlight.getFlightId(), passengers, totalCapacity)) {
            showAlert("Booking Error", "Not enough seats available for this flight.", Alert.AlertType.ERROR);
            return;
        }

        FlightBooking booking = new FlightBooking();
        booking.setFlightId(selectedFlight.getFlightId());
        booking.setNumPassengers(passengers);
        booking.setBookingDate(LocalDate.now());

        try {
            boolean success = bookingService.addBooking(booking);
            if (success) {
                showAlert("Success", "Flight booked successfully!", Alert.AlertType.INFORMATION);
                ((Stage) bookButton.getScene().getWindow()).close(); // Close the window
            } else {
                showAlert("Error", "Failed to book flight. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to book flight: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles the cancel button click in the flight booking interface.
     * Clears all input fields and returns to the main interface.
     */
    @FXML
    public void onCancelClick() {
        // Clear all input fields
        nameField.clear();
        emailField.clear();
        passengerCountField.clear();

        // Show confirmation to the user
        showAlert("Booking Cancelled", "Flight booking process has been cancelled.", Alert.AlertType.INFORMATION);

        // Navigate back to Main.fxml
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Main.fxml")));
            Stage stage = (Stage) bookButton.getScene().getWindow();
            stage.setTitle("Travel Booking System");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not return to main interface: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}