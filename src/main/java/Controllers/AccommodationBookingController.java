package Controllers;

import Models.Accommodation;
import Models.AccommodationBooking;
import Services.AccommodationBookingService;
import Services.AccommodationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccommodationBookingController {

    @FXML
    private Label accommodationNameLabel;
    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;
    @FXML
    private Spinner<Integer> guestsSpinner;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    private Accommodation selectedAccommodation;
    private final AccommodationBookingService bookingService;

    public AccommodationBookingController() throws SQLException {
        this.bookingService = new AccommodationBookingService();
    }

    @FXML
    private void initialize() {
        setupDatePickers();
        setupGuestsSpinner();
        addValidationListeners();
        confirmButton.setDisable(true);
    }

    private void setupDatePickers() {
        // Set current date as default
        checkInDatePicker.setValue(LocalDate.now());
        checkOutDatePicker.setValue(LocalDate.now().plusDays(1));

        // Don't allow past dates
        checkInDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        checkOutDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(checkInDatePicker.getValue().plusDays(1)));
            }
        });

        // When check-in date changes, update check-out date constraints
        checkInDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (checkOutDatePicker.getValue().isBefore(newVal.plusDays(1))) {
                checkOutDatePicker.setValue(newVal.plusDays(1));
            }
        });
    }

    private void setupGuestsSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        guestsSpinner.setValueFactory(valueFactory);
    }

    private void addValidationListeners() {
        // Add listeners to validate input when values change
        checkInDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateInput());
        checkOutDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateInput());
        guestsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> validateInput());
    }

    private void validateInput() {
        boolean isValid = checkInDatePicker.getValue() != null &&
                checkOutDatePicker.getValue() != null &&
                !checkOutDatePicker.getValue().isBefore(checkInDatePicker.getValue().plusDays(1)) &&
                guestsSpinner.getValue() != null &&
                guestsSpinner.getValue() > 0 &&
                selectedAccommodation != null;

        confirmButton.setDisable(!isValid);
    }

    public void setAccommodation(Accommodation accommodation) {
        this.selectedAccommodation = accommodation;
        accommodationNameLabel.setText(accommodation.getName());
        validateInput();
    }

    @FXML
    private void onConfirmBooking() {
        try {
            // Validate that the accommodation exists
            if (selectedAccommodation == null) {
                showAlert(Alert.AlertType.ERROR,
                        "Invalid Accommodation",
                        "No accommodation selected for booking.");
                return;
            }

            // Check if the accommodationId exists in the database
            AccommodationService accommodationService = new AccommodationService();
            boolean accommodationExists = accommodationService.getAccommodation(selectedAccommodation.getAccommodationId()).isPresent();

            if (!accommodationExists) {
                showAlert(Alert.AlertType.ERROR,
                        "Invalid Accommodation",
                        "The selected accommodation does not exist in the database.");
                return;
            }

            // Create the booking object that matches our model
            AccommodationBooking booking = new AccommodationBooking(
                    selectedAccommodation.getAccommodationId(),
                    checkInDatePicker.getValue(),
                    checkOutDatePicker.getValue(),
                    guestsSpinner.getValue()
            );

            // Print debug info
            System.out.println("Creating booking with accommodationId: " + selectedAccommodation.getAccommodationId());

            // Save using service
            var savedBooking = bookingService.createBooking(booking);

            if (savedBooking.isPresent()) {
                showAlert(Alert.AlertType.INFORMATION,
                        "Booking Confirmed",
                        "Your accommodation has been booked successfully!");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR,
                        "Booking Failed",
                        "Unable to complete your booking. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Error",
                    "An error occurred: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        accommodationNameLabel.getScene().getWindow().hide();
    }
}