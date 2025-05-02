package Controllers;

import entities.Event;
import entities.GenreEvent;
import entities.StatusEvent;
import entities.TypeEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import jdk.jfr.EventType;
import services.ServiceEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class EventController implements Initializable {

    @FXML private TextField searchField;
    @FXML private ListView<String> eventListView;

    @FXML private TextField titleField;
    @FXML private DatePicker StartDateField;
    @FXML private DatePicker EndDateField;
    @FXML private TextField locationField;
    @FXML private TextField priceField;
    @FXML private TextArea descriptionArea;
    @FXML private ImageView eventImageView;

    @FXML private RadioButton nationalRadio;
    @FXML private RadioButton internationalRadio;

    @FXML private CheckBox seminarCheckBox;
    @FXML private CheckBox cruiseCheckBox;
    @FXML private CheckBox travelCheckBox;
    @FXML private CheckBox afterWorkCheckBox;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ServiceEvent serviceEvent = new ServiceEvent();
    private ToggleGroup eventTypeGroup = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            nationalRadio.setToggleGroup(eventTypeGroup);
            internationalRadio.setToggleGroup(eventTypeGroup);

            loadEventList();

            addButton.setOnAction(e -> handleAddEvent());
        } catch (Exception e) {
            System.err.println("Error initializing EventController: " + e.getMessage());
            // Show an error message to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Connection Error");
            alert.setContentText("Could not connect to the database. Please check your database connection and try again.");
            alert.showAndWait();
        }
    }

    private void handleAddEvent() {
        String title = titleField.getText();
        LocalDate start = StartDateField.getValue();
        LocalDate end = EndDateField.getValue();
        String location = locationField.getText();
        String price = priceField.getText();
        String description = descriptionArea.getText();

        TypeEvent eventType = nationalRadio.isSelected() ? TypeEvent.NATIONAL :
                internationalRadio.isSelected() ? TypeEvent.INTERNATIONAL : null;

        List<String> categories = new ArrayList<>();
        if (seminarCheckBox.isSelected()) categories.add("SEMINAR");
        if (cruiseCheckBox.isSelected()) categories.add("CRUISE");
        if (travelCheckBox.isSelected()) categories.add("TRAVEL");
        if (afterWorkCheckBox.isSelected()) categories.add("AFTER_WORK");

        Event newEvent = new Event();
        newEvent.setName(title);
        newEvent.setStartDate(start);
        newEvent.setEndDate(end);
        newEvent.setDescription(location);
        newEvent.setPrice(Float.parseFloat(price));
        newEvent.setDescription(description);
        newEvent.setTypeEvent(eventType);
        newEvent.setGenreEvent(GenreEvent.CRUISE);
        newEvent.setDuration(1);
        newEvent.setFidelityPoints(5);
        newEvent.setDeleted(false);
        newEvent.setFinalPrice(Float.parseFloat(price));
        newEvent.setIdCreator(2);
        newEvent.setMaxParticipants(50);
        newEvent.setPromotionRate(0);
        newEvent.setVisa(false);
        newEvent.setStatusEvent(StatusEvent.AVAILABLE);
        newEvent.setVuesNb(0);

        serviceEvent.create(newEvent);
        loadEventList();
        clearForm();
    }

    private void loadEventList() {
        try {
            eventListView.getItems().clear();
            List<Event> events = serviceEvent.list_Event();
            if (events != null) {
                for (Event e : events) {
                    eventListView.getItems().add(e.getName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading event list: " + e.getMessage());
            eventListView.getItems().add("Error loading events");
        }
    }

    private void clearForm() {
        titleField.clear();
        StartDateField.setValue(null);
        EndDateField.setValue(null);
        locationField.clear();
        priceField.clear();
        descriptionArea.clear();
        nationalRadio.setSelected(false);
        internationalRadio.setSelected(false);
        seminarCheckBox.setSelected(false);
        cruiseCheckBox.setSelected(false);
        travelCheckBox.setSelected(false);
        afterWorkCheckBox.setSelected(false);
    }
}
