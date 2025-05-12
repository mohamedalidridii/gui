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
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class AccommodationSearchController {

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
        Accommodation selected = resultsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Check if the accommodation is actually available
            if (accommodationService.isAvailable(selected.getAccommodationId())) {
                // TODO: Open AccommodationBooking.fxml and pass the selected accommodation
                System.out.println("Proceeding to book: " + selected.getName());
            } else {
                new Alert(Alert.AlertType.WARNING, "This accommodation has no available rooms currently.").showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select an accommodation to book.").showAndWait();
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