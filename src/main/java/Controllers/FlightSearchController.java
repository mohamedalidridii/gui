package Controllers;

import Models.Flight;
import Services.FlightService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FlightSearchController {
    @FXML
    private Label resultCountLabel;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private Button bookButton;
    @FXML
    private Button exportButton;
    @FXML
    private TableColumn<Flight, String> colFlightId;
    @FXML
    private TableColumn<Flight, String> colAirline;
    @FXML
    private TableColumn<Flight, String> colDeparture;
    @FXML
    private TableColumn<Flight, String> colDestination;
    @FXML
    private TableColumn<Flight, LocalDate> colDate;
    @FXML
    private TableColumn<Flight, String> colTime;
    @FXML
    private TableColumn<Flight, Double> colPrice;
    @FXML
    private TableColumn<Flight, Void> colActions;
    @FXML
    private TextField departureField;
    @FXML
    private TextField destinationField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField airlineField;
    @FXML
    private TableView<Flight> FlightTable;

    private FlightService flightService;
    private ObservableList<Flight> flightData = FXCollections.observableArrayList();
    private List<Flight> currentSearchResults;

    @FXML
    private void initialize() {
        // Initialize the FlightService with properly constructed instance
        flightService = new FlightService();

        // Configure table columns
        colFlightId.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        colAirline.setCellValueFactory(new PropertyValueFactory<>("airline"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Add sorting options
        sortComboBox.getItems().addAll(
                "Price (Low to High)",
                "Price (High to Low)",
                "Departure (A-Z)",
                "Destination (A-Z)",
                "Date (Earliest First)",
                "Airline (A-Z)"
        );
        sortComboBox.setValue("Price (Low to High)");
        sortComboBox.setOnAction(e -> applySorting());

        // Load all flights initially
        try {
            List<Flight> flights = flightService.getAllFlights();
            updateResultsDisplay(flights);
        } catch (Exception e) {
            showAlert("Data Loading Error", "Failed to load flight data: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        // Configure buttons
        bookButton.setDisable(true);
        exportButton.setDisable(true);

        // Add selection listener to enable/disable buttons
        FlightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            bookButton.setDisable(!hasSelection);
        });

        // Configure double-click to show details
        FlightTable.setRowFactory(tv -> {
            TableRow<Flight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showFlightDetails(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    private void onSearchClick() {
        String departure = departureField.getText().trim();
        String destination = destinationField.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String airline = airlineField.getText().trim();

        List<Flight> results = flightService.searchFlights(
                departure, destination, startDate, endDate, airline
        );

        updateResultsDisplay(results);
        applySorting();
    }

    private void updateResultsDisplay(List<Flight> results) {
        currentSearchResults = results;
        flightData.clear();
        flightData.addAll(results);
        FlightTable.setItems(flightData);

        resultCountLabel.setText(results.size() + " flights found");
        exportButton.setDisable(results.isEmpty());
    }

    private void applySorting() {
        if (currentSearchResults == null || currentSearchResults.isEmpty()) {
            return;
        }

        String sortOption = sortComboBox.getValue();
        List<Flight> sortedList = currentSearchResults;

        switch (sortOption) {
            case "Price (Low to High)":
                sortedList = currentSearchResults.stream()
                        .sorted((f1, f2) -> Double.compare(f1.getPrice(), f2.getPrice()))
                        .collect(Collectors.toList());
                break;
            case "Price (High to Low)":
                sortedList = currentSearchResults.stream()
                        .sorted((f1, f2) -> Double.compare(f2.getPrice(), f1.getPrice()))
                        .collect(Collectors.toList());
                break;
            case "Departure (A-Z)":
                sortedList = currentSearchResults.stream()
                        .sorted((f1, f2) -> f1.getDeparture().compareTo(f2.getDeparture()))
                        .collect(Collectors.toList());
                break;
            case "Destination (A-Z)":
                sortedList = currentSearchResults.stream()
                        .sorted((f1, f2) -> f1.getDestination().compareTo(f2.getDestination()))
                        .collect(Collectors.toList());
                break;
            case "Date (Earliest First)":
                sortedList = currentSearchResults.stream()
                        .sorted((f1, f2) -> f1.getDate().compareTo(f2.getDate()))
                        .collect(Collectors.toList());
                break;
            case "Airline (A-Z)":
                sortedList = currentSearchResults.stream()
                        .sorted((f1, f2) -> f1.getAirline().compareTo(f2.getAirline()))
                        .collect(Collectors.toList());
                break;
        }

        flightData.clear();
        flightData.addAll(sortedList);
    }

    @FXML
    private void onClearClick() {
        departureField.clear();
        destinationField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        airlineField.clear();

        // Reset to showing all flights
        try {
            List<Flight> flights = flightService.getAllFlights();
            updateResultsDisplay(flights);
            applySorting();
        } catch (Exception e) {
            showAlert("Data Loading Error", "Failed to load flight data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onBookButtonClick() {
        Flight selectedFlight = FlightTable.getSelectionModel().getSelectedItem();

        if (selectedFlight == null) {
            // Show an alert that no flight is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Flight Selected");
            alert.setContentText("Please select a flight to book.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/flight_booking.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected flight
            FlightBookingController controller = loader.getController();
            controller.initData(selectedFlight);

            // Switch to the flight booking scene
            Stage stage = (Stage) FlightTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Flight Booking");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show error message to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Could not open the flight booking window. Please try again.");
            alert.showAndWait();
        }

    }

    @FXML
    private void onExportButtonClick() {
        // Implementation for exporting the results
        if (currentSearchResults == null || currentSearchResults.isEmpty()) {
            showAlert("Export Error", "No results to export", Alert.AlertType.WARNING);
            return;
        }

        // Add code here to export the results to CSV or other format
        showAlert("Export Successful", "Flight results exported successfully", Alert.AlertType.INFORMATION);
    }

    private void showFlightDetails(Flight flight) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Flight Details");
        alert.setHeaderText("Details for flight " + flight.getFlightId());

        String details = "Airline: " + flight.getAirline() + "\n" +
                "From: " + flight.getDeparture() + "\n" +
                "To: " + flight.getDestination() + "\n" +
                "Date: " + flight.getDate() + "\n" +
                "Time: " + flight.getTime() + "\n" +
                "Price: $" + flight.getPrice();

        alert.setContentText(details);
        alert.showAndWait();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onBackButtonClick() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
            Stage stage = (Stage) FlightTable.getScene().getWindow();
            stage.setTitle("Travel Booking System");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not return to main screen", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}