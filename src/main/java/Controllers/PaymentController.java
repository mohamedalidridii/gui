package Controllers;

import DAO.AccommodationDAO;
import Models.*;
import Services.AccommodationService;
import Services.FlightService;
import Services.PaymentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentController {

    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> idColumn;
    @FXML private TableColumn<Payment, Double> amountColumn;
    @FXML private TableColumn<Payment, String> methodColumn;
    @FXML private TableColumn<Payment, String> statusColumn;
    @FXML private TableColumn<Payment, LocalDate> dateColumn;
    @FXML private TableColumn<Payment, Integer> flightBookingColumn;
    @FXML private TableColumn<Payment, Integer> accommodationBookingColumn;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField bookingIdField;
    @FXML private ToggleGroup bookingTypeToggle;
    @FXML private RadioButton flightBookingRadio;
    @FXML private RadioButton accommodationBookingRadio;
    @FXML private Label totalAmountLabel;

    private PaymentService paymentService = new PaymentService();
    private ObservableList<Payment> observablePayments;
    private final FlightService flightService ;

    public PaymentController() {
        // Initialize the PaymentService as you already do
        this.paymentService = new PaymentService();

        // Initialize FlightService with a FlightDAO instance
        this.flightService = new FlightService();
    }



    @FXML
    public void initialize() {
        // Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        methodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        flightBookingColumn.setCellValueFactory(new PropertyValueFactory<>("flightBookingId"));
        accommodationBookingColumn.setCellValueFactory(new PropertyValueFactory<>("accommodationBookingId"));

        // Populate status filter
        statusFilterComboBox.getItems().addAll("SUCCESS", "PENDING", "REFUNDED", "FAILED");
        statusFilterComboBox.setValue(null);

        loadAllPayments();
    }

    @FXML
    private void loadAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        observablePayments = FXCollections.observableArrayList(payments);
        paymentTable.setItems(observablePayments);
        updateTotalAmount();
    }

    @FXML
    private void filterByStatus(ActionEvent event) {
        String status = statusFilterComboBox.getValue();
        List<Payment> filtered = observablePayments.stream()
                .filter(p -> p.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
        paymentTable.setItems(FXCollections.observableArrayList(filtered));
        updateTotalAmount();
    }

    @FXML
    private void filterByDateRange(ActionEvent event) {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start == null || end == null) {
            showAlert("Validation Error", "Please select both start and end dates.");
            return;
        }
        List<Payment> filtered = observablePayments.stream()
                .filter(p -> !p.getPaymentDate().isBefore(start) && !p.getPaymentDate().isAfter(end))
                .collect(Collectors.toList());
        paymentTable.setItems(FXCollections.observableArrayList(filtered));
        updateTotalAmount();
    }

    @FXML
    private void filterByBookingId(ActionEvent event) {
        String idText = bookingIdField.getText().trim();
        if (idText.isEmpty()) {
            showAlert("Validation Error", "Please enter a booking ID.");
            return;
        }
        boolean isFlight = flightBookingRadio.isSelected();
        List<Payment> filtered = observablePayments.stream()
                .filter(p -> isFlight ? p.getFlightBookingId() == Integer.parseInt(idText)
                        : p.getAccommodationBookingId() == Integer.parseInt(idText))
                .collect(Collectors.toList());
        paymentTable.setItems(FXCollections.observableArrayList(filtered));
        updateTotalAmount();
    }

    @FXML
    private void updatePaymentStatus(ActionEvent event) {
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        String newStatus = statusFilterComboBox.getValue();
        if (selected == null || newStatus == null) {
            showAlert("Validation Error", "Select a payment and a status.");
            return;
        }
        selected.setStatus(newStatus);
        paymentService.updatePayment(selected);
        loadAllPayments();
    }

    @FXML
    private void processRefund(ActionEvent event) {
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "No payment selected.");
            return;
        }
        selected.setStatus("REFUNDED");
        paymentService.updatePayment(selected);
        loadAllPayments();
    }

    @FXML
    private void deletePayment(ActionEvent event) {
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "No payment selected.");
            return;
        }
        paymentService.deletePayment(selected.getPaymentId());
        loadAllPayments();
    }

    private void showPaymentDetails(Payment payment) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Details");
        alert.setHeaderText("Payment ID: " + payment.getPaymentId());
        alert.setContentText(
                "Amount: " + payment.getAmount() + "\n" +
                        "Method: " + payment.getPaymentMethod() + "\n" +
                        "Status: " + payment.getStatus() + "\n" +
                        "Date: " + payment.getPaymentDate()
        );
        alert.showAndWait();
    }

    private void updateTotalAmount() {
        double total = paymentTable.getItems().stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        totalAmountLabel.setText(String.format("Total: $%.2f", total));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    AccommodationDAO accommodationDAO = new AccommodationDAO();
    AccommodationService accommodationService = new AccommodationService();
    private double calculateAccommodationPrice(AccommodationBooking booking) {
        // Get the accommodation details using the ID from the booking
        AccommodationService accommodationService = new AccommodationService();
        Optional<Accommodation> accommodationOpt = accommodationService.getAccommodation(booking.getAccommodationId());

        if (!accommodationOpt.isPresent()) {
            return 0.0; // Return 0 if accommodation not found
        }

        Accommodation accommodation = accommodationOpt.get();
        double pricePerNight = accommodation.getPricePerNight();

        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());

        // Calculate total price (price per night × number of nights)
        return pricePerNight * nights;
    }


    public void initPaymentForBooking(Object booking, String bookingType) {
        // Logic to calculate amount and add a new payment record
        int bookingId;
        double amount;
        String method = "CARD"; // default, or prompt user
        if ("FLIGHT".equalsIgnoreCase(bookingType) && booking instanceof FlightBooking) {
            FlightBooking flightBooking = (FlightBooking) booking;
            bookingId = flightBooking.getId();

            // We need to retrieve the Flight object using the flightId from the FlightBooking
            // assuming you have a FlightService similar to AccommodationService
            Flight flight = FlightService.getFlight(flightBooking.getFlightId())
                    .orElseThrow(() -> new IllegalArgumentException("Flight not found"));

            amount = flight.getPrice() * flightBooking.getNumPassengers();
        } else if ("ACCOMMODATION".equalsIgnoreCase(bookingType) && booking instanceof AccommodationBooking) {
            bookingId = ((AccommodationBooking) booking).getAccommodationBookingId(); // Fixed: use accommodationBookingId instead of accommodationId
            amount = calculateAccommodationPrice((AccommodationBooking) booking);
        } else {
            throw new IllegalArgumentException("Invalid booking type");
        }

        Payment payment = new Payment();
        if ("FLIGHT".equalsIgnoreCase(bookingType)) {
            payment.setFlightBookingId(bookingId);
            payment.setAccommodationBookingId(0);
        } else {
            payment.setFlightBookingId(0);
            payment.setAccommodationBookingId(bookingId);
        }

        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus("PAID"); // Set initial status

        paymentService.makePayment(payment);
        loadAllPayments();
    }
}
