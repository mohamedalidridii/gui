package Controllers;

import DAO.FlightBookingDAO;
import Models.Flight;
import Models.FlightBooking;
import Services.FlightBookingService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FlightBookingController {
    @FXML
    private Label flightIdLabel;
    @FXML
    private Label departureLabel;
    @FXML
    private Label destinationLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;

    @FXML
    private DatePicker bookingDatePicker;
    @FXML
    private Spinner<Integer> passengerCountSpinner;

    @FXML
    private Button confirmBookingButton;
    @FXML
    private Button backButton;

    private Flight selectedFlight;
    private FlightBookingService bookingService;

    @FXML
    private void initialize() {
        // Set up date picker with current date as default
        bookingDatePicker.setValue(LocalDate.now());

        // Configure passenger count spinner (1-10 passengers)
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        passengerCountSpinner.setValueFactory(valueFactory);

        // Initialize the booking service
        try {
            bookingService = new FlightBookingService(new FlightBookingDAO());
        } catch (java.sql.SQLException e) {
            showAlert("Database Error", "Could not connect to the database", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        // Set up button actions
        confirmBookingButton.setOnAction(event -> onConfirmBookingClick());
        backButton.setOnAction(event -> onBackButtonClick());
    }

    /**
     * Initialize controller with flight data
     */
    public void initData(Flight flight) {
        this.selectedFlight = flight;

        // Populate flight information labels
        flightIdLabel.setText(String.valueOf(flight.getFlightId()));
        departureLabel.setText(flight.getDeparture());
        destinationLabel.setText(flight.getDestination());
        dateLabel.setText(flight.getDate().toString());
        timeLabel.setText(flight.getTime().toString());
    }

    /**
     * Validate the booking form
     */
    private boolean validateForm() {
        if (bookingDatePicker.getValue() == null) {
            showAlert("Validation Error", "Please select a booking date", Alert.AlertType.ERROR);
            return false;
        }

        // Check if booking date is not before flight date
        if (selectedFlight.getDate() != null &&
                bookingDatePicker.getValue().isAfter(selectedFlight.getDate())) {
            showAlert("Validation Error", "Booking date cannot be after flight date", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    /**
     * Handle confirm booking button click
     */
    @FXML
    private void onConfirmBookingClick() {
        if (!validateForm()) {
            return;
        }

        try {
            // Create new flight booking
            FlightBooking booking = new FlightBooking();
            booking.setFlightId(selectedFlight.getFlightId());
            booking.setBookingDate(bookingDatePicker.getValue());
            booking.setNumPassengers(passengerCountSpinner.getValue());

            // Add booking to database
            boolean success = bookingService.addBooking(booking);

            if (success) {
                showAlert("Success", "Booking confirmed successfully!", Alert.AlertType.INFORMATION);
                // Close current window or navigate to confirmation screen
                closeWindow();
            } else {
                showAlert("Error", "Failed to create booking", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handle back button click
     */
    @FXML
    private void onBackButtonClick() {
        closeWindow();
    }

    /**
     * Close the current window
     */
    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}