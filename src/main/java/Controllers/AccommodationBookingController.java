package Controllers;

import Models.Accommodation;
import Models.AccommodationBooking;
import Services.AccommodationBookingService;
import DAO.AccommodationBookingDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
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

    private Accommodation selectedAccommodation;
    private final AccommodationBookingService bookingService;

    public AccommodationBookingController() throws SQLException {
        this.bookingService = new AccommodationBookingService(new AccommodationBookingDAO());
    }

    @FXML
    private void initialize() {
        // Setup spinner
        SpinnerValueFactory<Integer> guestsValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        guestsSpinner.setValueFactory(guestsValueFactory);

        setupDatePickers();
        addValidationListeners();

        confirmButton.setDisable(true);
    }

    private void setupDatePickers() {
        LocalDate today = LocalDate.now();

        StringConverter<LocalDate> converter = createDateConverter();
        checkInDatePicker.setConverter(converter);
        checkOutDatePicker.setConverter(converter);

        checkInDatePicker.setValue(today);
        checkOutDatePicker.setValue(today.plusDays(1));

        checkInDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && checkOutDatePicker.getValue() != null &&
                    (newVal.isAfter(checkOutDatePicker.getValue()) || newVal.isEqual(checkOutDatePicker.getValue()))) {
                checkOutDatePicker.setValue(newVal.plusDays(1));
            }
        });
    }

    private StringConverter<LocalDate> createDateConverter() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ?
                        LocalDate.parse(string, dateFormatter) : null;
            }
        };
    }

    private void addValidationListeners() {
        checkInDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateInput());
        checkOutDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateInput());
    }

    private void validateInput() {
        boolean isValid = checkInDatePicker.getValue() != null &&
                checkOutDatePicker.getValue() != null &&
                checkInDatePicker.getValue().isBefore(checkOutDatePicker.getValue());

        confirmButton.setDisable(!isValid);
    }

    public void setAccommodation(Accommodation accommodation) {
        this.selectedAccommodation = accommodation;
        accommodationNameLabel.setText(accommodation.getName());

        LocalDate today = LocalDate.now();
        checkInDatePicker.setValue(today);
        checkOutDatePicker.setValue(today.plusDays(1));

        guestsSpinner.getValueFactory().setValue(1);

        Platform.runLater(() -> guestsSpinner.requestFocus());
    }

    @FXML
    private void onConfirmBooking() {
        if (checkInDatePicker.getValue() == null || checkOutDatePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Check-in and check-out dates are required.");
            return;
        }

        if (checkInDatePicker.getValue().isAfter(checkOutDatePicker.getValue())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Check-out date must be after check-in date.");
            return;
        }

        AccommodationBooking booking = new AccommodationBooking();
        booking.setAccommodationId(selectedAccommodation.getAccommodationId());
        booking.setCheckIn(checkInDatePicker.getValue());
        booking.setCheckOut(checkOutDatePicker.getValue());
        booking.setNumGuests(guestsSpinner.getValue());

        long days = java.time.temporal.ChronoUnit.DAYS.between(
                booking.getCheckIn(), booking.getCheckOut());
        double totalPrice = selectedAccommodation.getPricePerNight() * days;

        try {
            boolean success = bookingService.addBooking(booking);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Booking Confirmed",
                        "Your accommodation booking has been confirmed successfully!\n" +
                                "Total Price: $" + String.format("%.2f", totalPrice));
                navigateAfterBooking(booking);
            } else {
                showAlert(Alert.AlertType.ERROR, "Booking Failed",
                        "There was an error processing your booking. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void navigateAfterBooking(AccommodationBooking booking) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/payment.fxml"));
            Parent root = loader.load();

            PaymentController paymentController = loader.getController();
            paymentController.initPaymentForBooking(booking, "ACCOMMODATION");

            Scene scene = new Scene(root);
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Payment");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not navigate to the next screen. Error: " + e.getMessage());
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/accommodation_search.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) accommodationNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Accommodation Search");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Could not return to search form. Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
