package Controllers;

import entities.Event;
import entities.GenreEvent;
import entities.Location;
import entities.StatusEvent;
import entities.TypeEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import services.ServiceEvent;
import services.ServiceLocation;
import services.PDFService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class EventController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Event> eventTableView;
    @FXML private TableColumn<Event, String> nameColumn;
    @FXML private TableColumn<Event, String> descriptionColumn;
    @FXML private TableColumn<Event, LocalDate> startDateColumn;
    @FXML private TableColumn<Event, LocalDate> endDateColumn;

    @FXML private TextField titleField;
    @FXML private DatePicker StartDateField;
    @FXML private DatePicker EndDateField;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private TextField priceField;
    @FXML private TextField maxParticipantsField;
    @FXML private TextField fidelityPointsField;
    @FXML private TextArea descriptionArea;
    @FXML private ImageView eventImageView;

    @FXML private RadioButton nationalRadio;
    @FXML private RadioButton internationalRadio;

    @FXML private RadioButton seminarRadio;
    @FXML private RadioButton cruiseRadio;
    @FXML private RadioButton travelRadio;
    @FXML private RadioButton afterWorkRadio;

    @FXML private RadioButton visaRequiredRadio;
    @FXML private RadioButton visaNotRequiredRadio;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button changePromotionButton;
    @FXML private Button updateStatusButton;
    @FXML private Button postponeEventButton;
    @FXML private Button exportButton;
    @FXML private Label errorLabel;

    private final ServiceEvent serviceEvent = new ServiceEvent();
    private final ServiceLocation serviceLocation = new ServiceLocation();
    private final ToggleGroup eventTypeGroup = new ToggleGroup();
    private final ToggleGroup categoryGroup = new ToggleGroup();
    private final ToggleGroup visaGroup = new ToggleGroup();
    private Event selectedEvent = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Setup TableView columns
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

            // Set column titles
            nameColumn.setText("Event Name");
            descriptionColumn.setText("Description");
            startDateColumn.setText("Start Date");
            endDateColumn.setText("End Date");

            // Setup location ComboBox
            locationComboBox.setItems(FXCollections.observableArrayList(serviceLocation.getAllLocations()));
            locationComboBox.setCellFactory(param -> new ListCell<Location>() {
                @Override
                protected void updateItem(Location location, boolean empty) {
                    super.updateItem(location, empty);
                    if (empty || location == null) {
                        setText(null);
                    } else {
                        setText(location.getCountry());
                    }
                }
            });
            locationComboBox.setButtonCell(new ListCell<Location>() {
                @Override
                protected void updateItem(Location location, boolean empty) {
                    super.updateItem(location, empty);
                    if (empty || location == null) {
                        setText(null);
                    } else {
                        setText(location.getCountry());
                    }
                }
            });

            // Setup radio button groups
            nationalRadio.setToggleGroup(eventTypeGroup);
            internationalRadio.setToggleGroup(eventTypeGroup);

            seminarRadio.setToggleGroup(categoryGroup);
            cruiseRadio.setToggleGroup(categoryGroup);
            travelRadio.setToggleGroup(categoryGroup);
            afterWorkRadio.setToggleGroup(categoryGroup);

            visaRequiredRadio.setToggleGroup(visaGroup);
            visaNotRequiredRadio.setToggleGroup(visaGroup);

            // Load initial data
            loadEventList();

            // Setup click handler for TableView
            eventTableView.setOnMouseClicked(mouseEvent -> {
                Event clickedEvent = eventTableView.getSelectionModel().getSelectedItem();
                if (clickedEvent != null) {
                    if (mouseEvent.getClickCount() == 2) {
                        // Double click - show event details
                        showEventDetails(clickedEvent);
                    } else {
                        // Single click - select event and populate form
                        selectedEvent = clickedEvent;
                        populateForm(selectedEvent);
                        editButton.setDisable(false);
                        deleteButton.setDisable(false);
                        changePromotionButton.setDisable(false);
                        updateStatusButton.setDisable(false);
                        postponeEventButton.setDisable(false);
                    }
                }
            });

            // Setup button handlers
            addButton.setOnAction(e -> handleAddEvent());
            editButton.setOnAction(e -> handleEditEvent());
            deleteButton.setOnAction(e -> handleDeleteEvent());
            changePromotionButton.setOnAction(e -> handleChangePromotion());
            updateStatusButton.setOnAction(e -> handleUpdateStatus());
            postponeEventButton.setOnAction(e -> handlePostponeEvent());
            exportButton.setOnAction(e -> handleExport());

            // Initially disable buttons
            editButton.setDisable(true);
            deleteButton.setDisable(true);
            changePromotionButton.setDisable(true);
            updateStatusButton.setDisable(true);
            postponeEventButton.setDisable(true);
            exportButton.setDisable(false);

        } catch (Exception e) {
            System.err.println("Error initializing EventController: " + e.getMessage());
            showError("Database Connection Error", "Could not connect to the database. Please check your database connection and try again.");
        }
    }

    @FXML
    public void handleAddEvent() {
        if (validateForm()) {
            try {
                Event newEvent = createEventFromForm();
                serviceEvent.create(newEvent);
                loadEventList();
                clearForm();
                errorLabel.setText("");
            } catch (Exception e) {
                showError("Error adding event", e.getMessage());
            }
        }
    }

    @FXML
    public void handleEditEvent() {
        if (selectedEvent != null && validateForm()) {
            try {
                Event updatedEvent = createEventFromForm();
                updatedEvent.setIdEvent(selectedEvent.getIdEvent());
                serviceEvent.update(updatedEvent, selectedEvent.getIdEvent());
                loadEventList();
                clearForm();
                selectedEvent = null;
                editButton.setDisable(true);
                deleteButton.setDisable(true);
                errorLabel.setText("");
            } catch (Exception e) {
                showError("Error updating event", e.getMessage());
            }
        }
    }

    @FXML
    public void handleDeleteEvent() {
        if (selectedEvent != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Delete Location");
            confirmDialog.setContentText("Are you sure you want to delete this Location: " + selectedEvent.getIdEvent() + "?");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            try {
                serviceEvent.delete(selectedEvent);
                loadEventList();
                clearForm();
                selectedEvent = null;
                editButton.setDisable(true);
                deleteButton.setDisable(true);
                errorLabel.setText("");
            } catch (Exception e) {
                showError("Error deleting event", e.getMessage());
            }
        }
    }

    @FXML
    public void navigateToLocation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/location.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) eventTableView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToClientView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) eventTableView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Validate dates
        if (StartDateField.getValue() == null || EndDateField.getValue() == null) {
            errorLabel.setText("Please select both start and end dates");
            return false;
        }
        if (StartDateField.getValue().isAfter(EndDateField.getValue())) {
            errorLabel.setText("Start date must be before end date");
            return false;
        }

        // Validate price
        try {
            float price = Float.parseFloat(priceField.getText());
            if (price < 0) {
                errorLabel.setText("Price must be a positive number");
                return false;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Price must be a valid number");
            return false;
        }

        // Validate max participants
        try {
            int maxParticipants = Integer.parseInt(maxParticipantsField.getText());
            if (maxParticipants <= 0) {
                errorLabel.setText("Max participants must be a positive number");
                return false;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Max participants must be a valid number");
            return false;
        }

        // Validate fidelity points
        try {
            int fidelityPoints = Integer.parseInt(fidelityPointsField.getText());
            if (fidelityPoints < 0) {
                errorLabel.setText("Fidelity points must be a non-negative number");
                return false;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Fidelity points must be a valid number");
            return false;
        }

        // Validate required fields
        if (titleField.getText().trim().isEmpty()) {
            errorLabel.setText("Title is required");
            return false;
        }
        if (locationComboBox.getValue() == null) {
            errorLabel.setText("Please select a location");
            return false;
        }
        if (eventTypeGroup.getSelectedToggle() == null) {
            errorLabel.setText("Please select an event type");
            return false;
        }
        if (categoryGroup.getSelectedToggle() == null) {
            errorLabel.setText("Please select a category");
            return false;
        }
        if (visaGroup.getSelectedToggle() == null) {
            errorLabel.setText("Please select visa requirement");
            return false;
        }

        return true;
    }

    private Event createEventFromForm() {
        Event event = new Event();
        event.setName(titleField.getText());
        event.setDescription(descriptionArea.getText());
        event.setStartDate(StartDateField.getValue());
        event.setEndDate(EndDateField.getValue());
        event.setPrice(Float.parseFloat(priceField.getText()));
        event.setLocation(locationComboBox.getValue());
        event.setVisa(visaRequiredRadio.isSelected());
        event.setMaxParticipants(Integer.parseInt(maxParticipantsField.getText()));
        event.setFidelityPoints(Integer.parseInt(fidelityPointsField.getText()));

        // Set event type
        if (nationalRadio.isSelected()) {
            event.setTypeEvent(TypeEvent.NATIONAL);
        } else if (internationalRadio.isSelected()) {
            event.setTypeEvent(TypeEvent.INTERNATIONAL);
        }

        // Set genre
        if (seminarRadio.isSelected()) {
            event.setGenreEvent(GenreEvent.SEMINAR);
        } else if (cruiseRadio.isSelected()) {
            event.setGenreEvent(GenreEvent.CRUISE);
        } else if (travelRadio.isSelected()) {
            event.setGenreEvent(GenreEvent.TRAVEL);
        } else if (afterWorkRadio.isSelected()) {
            event.setGenreEvent(GenreEvent.AFTER_WORK);
        }

        // Set initial values
        event.setDeleted(false);
        event.setNbParticipant(0);
        event.setVuesNb(0);
        event.setPromotionRate(0);
        event.setFinalPrice(event.getPrice());
        event.setStatusEvent(StatusEvent.AVAILABLE);

        return event;
    }

    private void populateForm(Event event) {
        titleField.setText(event.getName());
        descriptionArea.setText(event.getDescription());
        StartDateField.setValue(event.getStartDate());
        EndDateField.setValue(event.getEndDate());
        priceField.setText(String.valueOf(event.getPrice()));
        maxParticipantsField.setText(String.valueOf(event.getMaxParticipants()));
        fidelityPointsField.setText(String.valueOf(event.getFidelityPoints()));
        locationComboBox.setValue(event.getLocation());
        visaRequiredRadio.setSelected(event.isVisa());
        visaNotRequiredRadio.setSelected(!event.isVisa());

        // Set event type
        if (event.getTypeEvent() == TypeEvent.NATIONAL) {
            nationalRadio.setSelected(true);
        } else if (event.getTypeEvent() == TypeEvent.INTERNATIONAL) {
            internationalRadio.setSelected(true);
        }

        // Set genre
        switch (event.getGenreEvent()) {
            case SEMINAR:
                seminarRadio.setSelected(true);
                break;
            case CRUISE:
                cruiseRadio.setSelected(true);
                break;
            case TRAVEL:
                travelRadio.setSelected(true);
                break;
            case AFTER_WORK:
                afterWorkRadio.setSelected(true);
                break;
        }
    }

    private void loadEventList() {
        try {
            List<Event> events = serviceEvent.list_Event();
            eventTableView.setItems(FXCollections.observableArrayList(events));
        } catch (Exception e) {
            System.err.println("Error loading event list: " + e.getMessage());
            showError("Error loading events", e.getMessage());
        }
    }

    private void clearForm() {
        titleField.clear();
        StartDateField.setValue(null);
        EndDateField.setValue(null);
        locationComboBox.setValue(null);
        priceField.clear();
        maxParticipantsField.clear();
        fidelityPointsField.clear();
        descriptionArea.clear();
        eventTypeGroup.selectToggle(null);
        categoryGroup.selectToggle(null);
        visaGroup.selectToggle(null);
        errorLabel.setText("");
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showEventDetails(Event event) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Event Details");
        dialog.setHeaderText(event.getName());

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add event details to the grid
        grid.add(new Label("Name:"), 0, 0);
        grid.add(new Label(event.getName()), 1, 0);

        grid.add(new Label("Description:"), 0, 1);
        grid.add(new Label(event.getDescription()), 1, 1);

        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(new Label(event.getStartDate().toString()), 1, 2);

        grid.add(new Label("End Date:"), 0, 3);
        grid.add(new Label(event.getEndDate().toString()), 1, 3);

        grid.add(new Label("Price:"), 0, 4);
        grid.add(new Label(String.format("%.2f", event.getPrice())), 1, 4);

        grid.add(new Label("Location:"), 0, 5);
        grid.add(new Label(event.getLocation().getCountry()), 1, 5);

        grid.add(new Label("Type:"), 0, 6);
        grid.add(new Label(event.getTypeEvent().toString()), 1, 6);

        grid.add(new Label("Category:"), 0, 7);
        grid.add(new Label(event.getGenreEvent().toString()), 1, 7);

        grid.add(new Label("Status:"), 0, 8);
        grid.add(new Label(event.getStatusEvent().toString()), 1, 8);

        grid.add(new Label("Visa Required:"), 0, 9);
        grid.add(new Label(event.isVisa() ? "Yes" : "No"), 1, 9);

        grid.add(new Label("Participants:"), 0, 10);
        grid.add(new Label(event.getNbParticipant() + "/" + event.getMaxParticipants()), 1, 10);

        grid.add(new Label("Views:"), 0, 11);
        grid.add(new Label(String.valueOf(event.getVuesNb())), 1, 11);

        grid.add(new Label("Fidelity Points:"), 0, 12);
        grid.add(new Label(String.valueOf(event.getFidelityPoints())), 1, 12);

        if (event.getPromotionRate() > 0) {
            grid.add(new Label("Promotion Rate:"), 0, 13);
            grid.add(new Label(String.format("%.1f%%", event.getPromotionRate())), 1, 13);

            grid.add(new Label("Final Price:"), 0, 14);
            grid.add(new Label(String.format("%.2f", event.getFinalPrice())), 1, 14);
        }

        // Add the grid to the dialog
        dialog.getDialogPane().setContent(grid);

        // Add a close button
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Show the dialog
        dialog.showAndWait();
    }

    @FXML
    private void handleChangePromotion() {
        if (selectedEvent == null) return;

        Dialog<Float> dialog = new Dialog<>();
        dialog.setTitle("Change Promotion");
        dialog.setHeaderText("Enter new promotion rate for " + selectedEvent.getName());

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField promotionField = new TextField();
        promotionField.setPromptText("Enter promotion rate (0-100)");
        grid.add(new Label("Promotion Rate (%):"), 0, 0);
        grid.add(promotionField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Add buttons
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        // Convert the result to a float when the confirm button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                try {
                    float rate = Float.parseFloat(promotionField.getText());
                    if (rate >= 0 && rate <= 100) {
                        return rate;
                    }
                } catch (NumberFormatException e) {
                    // Invalid number format
                }
            }
            return null;
        });

        Optional<Float> result = dialog.showAndWait();
        result.ifPresent(rate -> {
            try {
                selectedEvent.setPromotionRate(rate);
                selectedEvent.setFinalPrice(selectedEvent.getPrice() * (1 - rate / 100));
                serviceEvent.update(selectedEvent, selectedEvent.getIdEvent());
                loadEventList(); // Refresh the table
                populateForm(selectedEvent); // Update the form
            } catch (Exception ex) {
                showError("Error updating promotion", ex.getMessage());
            }
        });
    }

    @FXML
    private void handleUpdateStatus() {
        if (selectedEvent == null) return;

        Dialog<StatusEvent> dialog = new Dialog<>();
        dialog.setTitle("Update Status");
        dialog.setHeaderText("Select new status for " + selectedEvent.getName());

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<StatusEvent> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(StatusEvent.values());
        statusComboBox.setValue(selectedEvent.getStatusEvent());
        grid.add(new Label("Status:"), 0, 0);
        grid.add(statusComboBox, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Add buttons
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        // Convert the result to a StatusEvent when the confirm button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                return statusComboBox.getValue();
            }
            return null;
        });

        Optional<StatusEvent> result = dialog.showAndWait();
        result.ifPresent(status -> {
            try {
                selectedEvent.setStatusEvent(status);
                serviceEvent.update(selectedEvent, selectedEvent.getIdEvent());
                loadEventList(); // Refresh the table
                populateForm(selectedEvent); // Update the form
            } catch (Exception ex) {
                showError("Error updating status", ex.getMessage());
            }
        });
    }

    @FXML
    private void handlePostponeEvent() {
        if (selectedEvent == null) return;

        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Postpone Event");
        dialog.setHeaderText("Select new dates for " + selectedEvent.getName());

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker startDatePicker = new DatePicker(selectedEvent.getStartDate());
        DatePicker endDatePicker = new DatePicker(selectedEvent.getEndDate());
        
        grid.add(new Label("New Start Date:"), 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(new Label("New End Date:"), 0, 1);
        grid.add(endDatePicker, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Add buttons
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        // Convert the result to a LocalDate when the confirm button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
                    if (startDatePicker.getValue().isBefore(endDatePicker.getValue())) {
                        return startDatePicker.getValue();
                    }
                }
            }
            return null;
        });

        Optional<LocalDate> result = dialog.showAndWait();
        result.ifPresent(newStartDate -> {
            try {
                selectedEvent.setStartDate(newStartDate);
                selectedEvent.setEndDate(endDatePicker.getValue());
                serviceEvent.update(selectedEvent, selectedEvent.getIdEvent());
                loadEventList(); // Refresh the table
                populateForm(selectedEvent); // Update the form
            } catch (Exception ex) {
                showError("Error postponing event", ex.getMessage());
            }
        });
    }

    @FXML
    private void handleExport() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
            fileChooser.setInitialFileName("events_report.pdf");
            
            File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());
            if (file != null) {
                List<Event> eventsToExport;
                Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
                
                if (selectedEvent != null) {
                    // Export only the selected event
                    eventsToExport = List.of(selectedEvent);
                } else {
                    // Export all events
                    eventsToExport = eventTableView.getItems();
                }
                
                PDFService.generateEventsPDF(eventsToExport, file.getAbsolutePath());
                
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("PDF Export Successful");
                alert.setContentText("The events have been exported to: " + file.getAbsolutePath());
                alert.showAndWait();
            }
        } catch (Exception e) {
            errorLabel.setText("Error exporting to PDF: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }
}
