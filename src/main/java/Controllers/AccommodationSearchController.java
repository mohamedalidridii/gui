package Controllers;

import Models.Accommodation;
import Services.AccommodationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class AccommodationSearchController {
    @FXML
    private TableColumn<Accommodation, Integer> idColumn;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField minPriceField;

    @FXML
    private TextField maxPriceField;

    @FXML
    private TableView<Accommodation> resultsTable;

    @FXML
    private TableColumn<Accommodation, String> nameColumn;
    @FXML
    private TableColumn<Accommodation, String> locationColumn;
    @FXML
    private TableColumn<Accommodation, String> typeColumn;
    @FXML
    private TableColumn<Accommodation, Integer> roomsColumn;
    @FXML
    private TableColumn<Accommodation, Double> priceColumn;

    private final AccommodationService accommodationService;

    public AccommodationSearchController() {
        // Properly initialize the service with the DAO
        this.accommodationService = new AccommodationService();
    }

    @FXML
    public void initialize() {
        // Set up accommodation types
        typeComboBox.setItems(FXCollections.observableArrayList("Hotel", "Hostel", "Apartment", "Villa"));

        // Set up table columns with proper property factories
        idColumn.setCellValueFactory(new PropertyValueFactory<>("AccommodationId"));

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        locationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        roomsColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAvailableRooms()).asObject());
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPricePerNight()).asObject());

        // Add price formatting to price column
        priceColumn.setCellFactory(column -> new TableCell<>() {
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

        // Load all accommodations initially
        loadAllAccommodations();
    }

    private void loadAllAccommodations() {
        List<Accommodation> allAccommodations = accommodationService.getAllAccommodations();
        resultsTable.setItems(FXCollections.observableArrayList(allAccommodations));
    }


    @FXML
    private void onSearch() {
        String location = locationField.getText().trim();
        String type = typeComboBox.getValue();

        // Step 1: Initial search
        List<Accommodation> results = accommodationService.search(location, type);

        if (results == null) {
            results = FXCollections.observableArrayList();
        }

        // Step 2: Optional price filtering
        try {
            boolean minSet = !minPriceField.getText().isEmpty();
            boolean maxSet = !maxPriceField.getText().isEmpty();

            if (minSet || maxSet) {
                double min = minSet ? Double.parseDouble(minPriceField.getText().trim()) : 0;
                double max = maxSet ? Double.parseDouble(maxPriceField.getText().trim()) : Double.MAX_VALUE;

                results = results.stream()
                        .filter(a -> a.getPricePerNight() >= min && a.getPricePerNight() <= max)
                        .toList();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid numeric values for price.").showAndWait();
            return;
        }

        resultsTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void onClearFilters() {
        locationField.clear();
        typeComboBox.setValue(null);
        minPriceField.clear();
        maxPriceField.clear();
        loadAllAccommodations();
    }

    @FXML
    private void onSortByPrice() {
        ObservableList<Accommodation> currentItems = resultsTable.getItems();
        List<Accommodation> sortedItems = accommodationService.sortByPrice(currentItems);
        resultsTable.setItems(FXCollections.observableArrayList(sortedItems));
    }

    @FXML
    private void onBookSelected() {
        Accommodation selectedAccommodation = resultsTable.getSelectionModel().getSelectedItem();

        if (selectedAccommodation == null) {
            // Show an alert that no accommodation is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Accommodation Selected");
            alert.setContentText("Please select an accommodation to book.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/accommodation_booking.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected accommodation
            AccommodationBookingController controller = loader.getController();
            controller.setAccommodation(selectedAccommodation);

            // Switch to the accommodation booking scene
            Stage stage = (Stage) resultsTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Accommodation Booking");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show error message to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Could not open the accommodation booking window. Please try again.");
            alert.showAndWait();
        }
    }
    /**
     * Returns to the main menu when the user clicks the "Return to Main Menu" button
     */
    @FXML
    private void onReturnToMain(javafx.event.ActionEvent event) {
        try {
            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            Parent root = loader.load();

            // Create a new scene with the main menu
            Scene scene = new Scene(root);

            // Set the scene on the stage
            stage.setScene(scene);
            stage.setTitle("Travel Booking System - Main Menu");
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Failed to return to main menu");
            alert.setContentText("An error occurred while trying to load the main menu: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }




}