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
        // Initialize flight service
        flightService = new FlightService();

        // Configure table columns
        colFlightId.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        colAirline.setCellValueFactory(new PropertyValueFactory<>("airline"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Format price column to show currency
        colPrice.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        // Set up action column with a view details button
        colActions.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("View");

            {
                viewButton.setOnAction(event -> {
                    Flight flight = getTableRow().getItem();
                    if (flight != null) {
                        showFlightDetails(flight);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        });

        // Link table to data
        FlightTable.setItems(flightData);

        // Add listener for table selection to enable/disable book button
        FlightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            bookButton.setDisable(newSelection == null);
        });

        // Initialize sort combo box
        sortComboBox.setItems(FXCollections.observableArrayList(
                "Price: Low to High",
                "Price: High to Low",
                "Date & Time",
                "Airline"
        ));

        sortComboBox.setOnAction(event -> applySorting());

        // Disable buttons initially
        exportButton.setDisable(true);
        bookButton.setDisable(true);
    }

    @FXML
    private void onSearchClick() {
        String departure = departureField.getText().trim();
        String destination = destinationField.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String airline = airlineField.getText().trim();

        // Validate required fields
        if (departure.isEmpty() || destination.isEmpty()) {
            showAlert("Missing Information", "Please enter both departure and destination locations.", Alert.AlertType.WARNING);
            return;
        }

        if (startDate == null) {
            showAlert("Missing Information", "Please select a start date.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Perform search based on inputs
            List<Flight> results;

            // Search by route (departure and destination)
            results = FlightService.searchByRoute(departure, destination);

            // Filter by airline if specified
            if (!airline.isEmpty()) {
                results = results.stream()
                        .filter(flight -> flight.getAirline().equalsIgnoreCase(airline))
                        .collect(Collectors.toList());
            }

            // Filter by date range
            if (endDate != null) {
                results = results.stream()
                        .filter(flight -> {
                            LocalDate flightDate = flight.getDate();
                            return !flightDate.isBefore(startDate) && !flightDate.isAfter(endDate);
                        })
                        .collect(Collectors.toList());
            } else {
                // Just filter by start date
                results = results.stream()
                        .filter(flight -> flight.getDate().equals(startDate))
                        .collect(Collectors.toList());
            }

            // Update the current search results
            currentSearchResults = results;

            // Update the UI
            updateResultsDisplay(results);

        } catch (Exception e) {
            showAlert("Error", "An error occurred during search: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateResultsDisplay(List<Flight> results) {
        // Update table
        flightData.clear();
        flightData.addAll(results);

        // Update result count
        resultCountLabel.setText(results.size() + " flights found");

        // Enable/disable export button
        exportButton.setDisable(results.isEmpty());

        // Clear selection and disable book button
        FlightTable.getSelectionModel().clearSelection();
        bookButton.setDisable(true);
    }

    private void applySorting() {
        String sortOption = sortComboBox.getValue();
        if (sortOption == null || currentSearchResults == null || currentSearchResults.isEmpty()) {
            return;
        }

        List<Flight> sortedResults;

        switch (sortOption) {
            case "Price: Low to High":
                sortedResults = flightService.sortByPrice(currentSearchResults);
                break;
            case "Price: High to Low":
                sortedResults = flightService.sortByPrice(currentSearchResults);
                // Reverse the list for high to low
                java.util.Collections.reverse(sortedResults);
                break;
            case "Date & Time":
                sortedResults = flightService.sortByDateTime(currentSearchResults);
                break;
            case "Airline":
                sortedResults = flightService.sortByAirline(currentSearchResults);
                break;
            default:
                return;
        }

        // Update display with sorted results
        flightData.clear();
        flightData.addAll(sortedResults);
    }

    @FXML
    private void onClearClick() {
        // Clear form fields
        departureField.clear();
        destinationField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        airlineField.clear();

        // Clear results
        currentSearchResults = null;
        flightData.clear();
        resultCountLabel.setText("0 flights found");

        // Disable buttons
        exportButton.setDisable(true);
        bookButton.setDisable(true);

        // Reset sort combo box
        sortComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void onBookButtonClick() {
        Flight selectedFlight = FlightTable.getSelectionModel().getSelectedItem();
        if (selectedFlight != null) {
            // Here you would typically navigate to a booking form or dialog
            showAlert("Booking",
                    "Starting booking process for flight " + selectedFlight.getFlightId() +
                            " from " + selectedFlight.getDeparture() +
                            " to " + selectedFlight.getDestination(),
                    Alert.AlertType.INFORMATION);

            // In a real app, you might do something like:
            // navigationService.navigateToBookingView(selectedFlight);
        }
    }

    @FXML
    private void onExportButtonClick() {
        if (currentSearchResults == null || currentSearchResults.isEmpty()) {
            showAlert("Export", "No results to export", Alert.AlertType.WARNING);
            return;
        }

        showAlert("Export Results",
                "Exporting " + currentSearchResults.size() + " flight results to file.",
                Alert.AlertType.INFORMATION);

        // Implement actual export functionality here
        // For example:
        // exportService.exportFlightsToCSV(currentSearchResults, "flight_search_results.csv");
    }

    private void showFlightDetails(Flight flight) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Flight Details");
        alert.setHeaderText("Flight " + flight.getFlightId());

        String content = String.format(
                "Airline: %s\n" +
                        "From: %s\n" +
                        "To: %s\n" +
                        "Date: %s\n" +
                        "Time: %s\n" +
                        "Price: $%.2f",
                flight.getAirline(),
                flight.getDeparture(),
                flight.getDestination(),
                flight.getDate(),
                flight.getTime(),
                flight.getPrice()
        );

        alert.setContentText(content);
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
            // Clear any current search data
            flightData.clear();
            if (currentSearchResults != null) {
                currentSearchResults.clear();
            }

            // Navigate back to the main menu using the current scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the stage from any control in the current scene
            Stage stage = (Stage) departureField.getScene().getWindow();
            stage.setTitle("Travel Booking System - Main Menu");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert("Navigation Error",
                    "Could not return to main menu: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}