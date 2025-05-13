package Controllers;

import Models.AccommodationBooking;
import Models.FlightBooking;
import DAO.AccommodationBookingDAO;
import DAO.FlightBookingDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Controller for managing user bookings including flight and accommodation bookings.
 * Allows users to view, manage and cancel their bookings.
 */
public class BookingController {

    @FXML
    private TableView<Optional<FlightBooking>> flightBookingsTable;

    @FXML
    private TableColumn<FlightBooking, Integer> flightBookingIdColumn;

    @FXML
    private TableColumn<FlightBooking, String> flightNumberColumn;

    @FXML
    private TableColumn<FlightBooking, String> flightOriginColumn;

    @FXML
    private TableColumn<FlightBooking, String> flightDestinationColumn;

    @FXML
    private TableColumn<FlightBooking, String> flightDepartureColumn;

    @FXML
    private TableColumn<FlightBooking, Double> flightPriceColumn;

    @FXML
    private TableColumn<FlightBooking, String> flightStatusColumn;

    @FXML
    private TableView<Optional<AccommodationBooking>> accommodationBookingsTable;

    @FXML
    private TableColumn<AccommodationBooking, Integer> accommodationBookingIdColumn;

    @FXML
    private TableColumn<AccommodationBooking, String> accommodationNameColumn;

    @FXML
    private TableColumn<AccommodationBooking, String> accommodationLocationColumn;

    @FXML
    private TableColumn<AccommodationBooking, String> accommodationCheckInColumn;

    @FXML
    private TableColumn<AccommodationBooking, String> accommodationCheckOutColumn;

    @FXML
    private TableColumn<AccommodationBooking, Double> accommodationPriceColumn;

    @FXML
    private TableColumn<AccommodationBooking, String> accommodationStatusColumn;

    private FlightBookingDAO flightBookingDAO;
    private AccommodationBookingDAO accommodationBookingDAO;
    private Stage stage;
    private Scene scene;
    private int userId; // Will store the currently logged-in user's ID

    /**
     * Initializes the controller and loads user bookings
     */
    @FXML
    public void initialize() {
        try {
            flightBookingDAO = new FlightBookingDAO();
            accommodationBookingDAO = new AccommodationBookingDAO();

            // Initialize table columns for flight bookings
            flightBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
            flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
            flightOriginColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));
            flightDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
            flightDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
            flightPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            flightStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            // Initialize table columns for accommodation bookings
            accommodationBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
            accommodationNameColumn.setCellValueFactory(new PropertyValueFactory<>("accommodationName"));
            accommodationLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            accommodationCheckInColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
            accommodationCheckOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
            accommodationPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            accommodationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            // Get the current user ID
            userId = getCurrentUserId();

            // Load bookings for the current user
            loadFlightBookings();
            loadAccommodationBookings();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database: " + e.getMessage());
        }
    }

    /**
     * Gets the current user ID from the session
     * @return the user ID
     */
    private int getCurrentUserId() {
        // This would typically retrieve the user ID from a session management system
        // For now, returning a placeholder value
        return 1; // Replace with actual implementation
    }

    /**
     * Loads all flight bookings for the current user
     */
    private void loadFlightBookings() {
        Optional<FlightBooking> bookings = flightBookingDAO.getById(userId);
        ObservableList<Optional<FlightBooking>> observableBookings = FXCollections.observableArrayList(bookings);
        flightBookingsTable.setItems(observableBookings);
    }

    /**
     * Loads all accommodation bookings for the current user
     */
    private void loadAccommodationBookings() {
        Optional<AccommodationBooking> bookings = accommodationBookingDAO.getById(userId);
        ObservableList<Optional<AccommodationBooking>> observableBookings = FXCollections.observableArrayList(bookings);
        accommodationBookingsTable.setItems(observableBookings);
    }

    /**
     * Handles viewing flight booking details
     * @param event The action event triggering this method
     */
    @FXML
    private void viewFlightDetails(ActionEvent event) {
        Optional<FlightBooking> selectedBooking = flightBookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a flight booking to view.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/FlightBookingDetails.fxml"));
            Parent root = loader.load();

            // Pass the selected booking to the details controller
            // FlightBookingDetailsController controller = loader.getController();
            // controller.setBookingDetails(selectedBooking);

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Flight Booking Details");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Failed to open booking details: " + e.getMessage());
        }
    }

    /**
     * Handles viewing accommodation booking details
     * @param event The action event triggering this method
     */
    @FXML
    private void viewAccommodationDetails(ActionEvent event) {
        Optional<AccommodationBooking> selectedBooking = accommodationBookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an accommodation booking to view.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AccommodationBookingDetails.fxml"));
            Parent root = loader.load();

            // Pass the selected booking to the details controller
            // AccommodationBookingDetailsController controller = loader.getController();
            // controller.setBookingDetails(selectedBooking);

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Accommodation Booking Details");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Failed to open booking details: " + e.getMessage());
        }
    }

    /**
     * Handles cancellation of flight bookings
     * @param event The action event triggering this method
     */
    @FXML
    private void cancelFlightBooking(ActionEvent event) {
        Optional<FlightBooking> selectedBooking = flightBookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a flight booking to cancel.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancellation");
        confirmAlert.setHeaderText("Cancel Flight Booking");
        confirmAlert.setContentText("Are you sure you want to cancel this flight booking? This action cannot be undone.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            flightBookingDAO.getById(selectedBooking.get().getId());
            showAlert(Alert.AlertType.INFORMATION, "Booking Cancelled",
                    "Your flight booking has been successfully cancelled.");
            loadFlightBookings(); // Refresh the table
        }
    }

    /**
     * Handles cancellation of accommodation bookings
     * @param event The action event triggering this method
     */
    @FXML
    private void cancelAccommodationBooking(ActionEvent event) {
        Optional<AccommodationBooking> selectedBooking = accommodationBookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an accommodation booking to cancel.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancellation");
        confirmAlert.setHeaderText("Cancel Accommodation Booking");
        confirmAlert.setContentText("Are you sure you want to cancel this accommodation booking? This action cannot be undone.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            String string = accommodationBookingDAO.toString(selectedBooking.get().getAccommodationBookingId());
            showAlert(Alert.AlertType.INFORMATION, "Booking Cancelled",
                    "Your accommodation booking has been successfully cancelled.");
            loadAccommodationBookings(); // Refresh the table
        }
    }

    /**
     * Navigates back to the home screen
     * @param event The action event triggering this method
     */
    @FXML
    private void backToHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Travel Booking System - Main Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Failed to navigate to home screen: " + e.getMessage());
        }
    }

    /**
     * Shows an alert dialog with the specified type, title, and message
     * @param alertType The type of alert to show
     * @param title The title of the alert
     * @param message The message to display in the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}