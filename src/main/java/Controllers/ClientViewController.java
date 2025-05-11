package Controllers;

import entities.Event;
import entities.GenreEvent;
import entities.Location;
import entities.StatusEvent;
import entities.TypeEvent;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import services.ServiceEvent;
import Repositories.EventRepository;
import services.WeatherService;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientViewController implements Initializable {

    @FXML private TextField searchField;
    @FXML private ComboBox<TypeEvent> filterTypeComboBox;
    @FXML private ComboBox<GenreEvent> filterGenreComboBox;
    @FXML private ComboBox<String> filterPriceComboBox;
    @FXML private FlowPane eventsFlowPane;

    private final ServiceEvent serviceEvent = new ServiceEvent();
    private final EventRepository eventRepository = new EventRepository();
    private final WeatherService weatherService = new WeatherService();
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize filter comboboxes
        filterTypeComboBox.getItems().addAll(TypeEvent.values());
        filterGenreComboBox.getItems().addAll(GenreEvent.values());
        filterPriceComboBox.getItems().addAll(
            "All Prices",
            "Under 100",
            "100-500",
            "500-1000",
            "Over 1000"
        );

        // Load initial events
        loadEvents();

        // Add listeners for filters
        searchField.textProperty().addListener((obs, oldVal, newVal) -> loadEvents());
        filterTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> loadEvents());
        filterGenreComboBox.valueProperty().addListener((obs, oldVal, newVal) -> loadEvents());
        filterPriceComboBox.valueProperty().addListener((obs, oldVal, newVal) -> loadEvents());
    }

    private void loadEvents() {
        eventsFlowPane.getChildren().clear();
        List<Event> events = serviceEvent.list_Event();

        // Apply filters
        if (filterTypeComboBox.getValue() != null) {
            events = events.stream()
                .filter(e -> e.getTypeEvent() == filterTypeComboBox.getValue())
                .toList();
        }

        if (filterGenreComboBox.getValue() != null) {
            events = events.stream()
                .filter(e -> e.getGenreEvent() == filterGenreComboBox.getValue())
                .toList();
        }

        if (filterPriceComboBox.getValue() != null && !filterPriceComboBox.getValue().equals("All Prices")) {
            events = events.stream()
                .filter(e -> {
                    float price = e.getFinalPrice();
                    return switch (filterPriceComboBox.getValue()) {
                        case "Under 100" -> price < 100;
                        case "100-500" -> price >= 100 && price <= 500;
                        case "500-1000" -> price > 500 && price <= 1000;
                        case "Over 1000" -> price > 1000;
                        default -> true;
                    };
                })
                .toList();
        }

        // Create cards for each event
        for (Event event : events) {
            VBox card = createEventCard(event);
            eventsFlowPane.getChildren().add(card);
        }
    }

    private VBox createEventCard(Event event) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(250);
        card.setPrefHeight(300);

        // Image
        ImageView imageView = new ImageView();
        if (event.getLocation() != null && event.getLocation().getImages() != null) {
            try {
                Image image = new Image(event.getLocation().getImages());
                imageView.setImage(image);
                imageView.setFitWidth(230);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(false);
            } catch (Exception e) {
                // If image loading fails, use a placeholder
                imageView.setStyle("-fx-background-color: #cccccc;");
                imageView.setFitWidth(230);
                imageView.setFitHeight(200);
            }
        }

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setStyle("-fx-background-color: #000000;");

        // Event name and price container
        HBox namePriceContainer = new HBox(10);
        namePriceContainer.setAlignment(Pos.CENTER);
        
        // Event name
        Text nameText = new Text(event.getName());
        nameText.setFont(Font.font("System", FontWeight.BOLD, 14));
        nameText.setWrappingWidth(150);
        
        // Price label
        Text priceText = new Text(String.format("$%.2f", event.getFinalPrice()));
        priceText.setFont(Font.font("System", FontWeight.BOLD, 16));
        priceText.setFill(Color.BLACK);

        namePriceContainer.getChildren().addAll(nameText, priceText);

        // Add components to card
        card.getChildren().addAll(imageContainer, namePriceContainer);

        // Add hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999999; -fx-border-radius: 5; -fx-background-radius: 5;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;"));

        // Add click handler to show event details and increment views
        card.setOnMouseClicked(e -> {
            // Show details and increment views on single click
            try {
                serviceEvent.add_vues(event);
                showEventDetails(event);
            } catch (Exception ex) {
                System.err.println("Error incrementing views: " + ex.getMessage());
            }
        });

        return card;
    }

    private void showEventDetails(Event event) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Event Details");
        dialog.setHeaderText(event.getName());

        // Create the main content container
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));

        // Create image section
        if (event.getLocation() != null && event.getLocation().getImages() != null) {
            try {
                ImageView imageView = new ImageView(new Image(event.getLocation().getImages()));
                imageView.setFitWidth(400);
                imageView.setFitHeight(300);
                imageView.setPreserveRatio(true);
                mainContent.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }

        // Create two columns for details
        HBox detailsContainer = new HBox(20);
        
        // Left column for event details
        GridPane eventGrid = new GridPane();
        eventGrid.setHgap(10);
        eventGrid.setVgap(10);
        eventGrid.setPadding(new Insets(10));

        // Add event details to the grid
        int row = 0;
        eventGrid.add(new Label("Name:"), 0, row);
        eventGrid.add(new Label(event.getName()), 1, row++);

        eventGrid.add(new Label("Description:"), 0, row);
        eventGrid.add(new Label(event.getDescription()), 1, row++);

        eventGrid.add(new Label("Start Date:"), 0, row);
        eventGrid.add(new Label(event.getStartDate().toString()), 1, row++);

        eventGrid.add(new Label("End Date:"), 0, row);
        eventGrid.add(new Label(event.getEndDate().toString()), 1, row++);

        eventGrid.add(new Label("Price:"), 0, row);
        eventGrid.add(new Label(String.format("$%.2f", event.getFinalPrice())), 1, row++);

        eventGrid.add(new Label("Type:"), 0, row);
        eventGrid.add(new Label(event.getTypeEvent().toString()), 1, row++);

        eventGrid.add(new Label("Category:"), 0, row);
        eventGrid.add(new Label(event.getGenreEvent().toString()), 1, row++);

        eventGrid.add(new Label("Status:"), 0, row);
        eventGrid.add(new Label(event.getStatusEvent().toString()), 1, row++);

        eventGrid.add(new Label("Visa Required:"), 0, row);
        eventGrid.add(new Label(event.isVisa() ? "Yes" : "No"), 1, row++);

        eventGrid.add(new Label("Participants:"), 0, row);
        eventGrid.add(new Label(event.getNbParticipant() + "/" + event.getMaxParticipants()), 1, row++);

        eventGrid.add(new Label("Views:"), 0, row);
        eventGrid.add(new Label(String.valueOf(event.getVuesNb())), 1, row++);

        eventGrid.add(new Label("Fidelity Points:"), 0, row);
        eventGrid.add(new Label(String.valueOf(event.getFidelityPoints())), 1, row++);

        if (event.getPromotionRate() > 0) {
            eventGrid.add(new Label("Promotion Rate:"), 0, row);
            eventGrid.add(new Label(String.format("%.1f%%", event.getPromotionRate())), 1, row++);

            eventGrid.add(new Label("Final Price:"), 0, row);
            eventGrid.add(new Label(String.format("$%.2f", event.getFinalPrice())), 1, row++);
        }

        // Right column for location details
        GridPane locationGrid = new GridPane();
        locationGrid.setHgap(10);
        locationGrid.setVgap(10);
        locationGrid.setPadding(new Insets(10));

        if (event.getLocation() != null) {
            row = 0;
            locationGrid.add(new Label("Location Details"), 0, row);
            locationGrid.add(new Label(""), 1, row++);

            locationGrid.add(new Label("Country:"), 0, row);
            locationGrid.add(new Label(event.getLocation().getCountry()), 1, row++);

            locationGrid.add(new Label("Description:"), 0, row);
            locationGrid.add(new Label(event.getLocation().getDescription()), 1, row++);

            locationGrid.add(new Label("Visa Required:"), 0, row);
            locationGrid.add(new Label(event.getLocation().isVisa() ? "Yes" : "No"), 1, row++);

            if (event.getLocation().getTransportaion() != null && !event.getLocation().getTransportaion().isEmpty()) {
                locationGrid.add(new Label("Transportation:"), 0, row);
                locationGrid.add(new Label(String.join(", ", event.getLocation().getTransportaion())), 1, row++);
            }

            // Add weather forecast section
            locationGrid.add(new Label("Weather Forecast:"), 0, row);
            VBox weatherBox = new VBox(5);
            weatherBox.setPadding(new Insets(5));
            weatherBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
            
            // Get weather forecast
            List<String> weatherForecast = weatherService.getWeatherForecast(
                event.getLocation(),
                event.getStartDate(),
                event.getEndDate()
            );
            
            for (String forecast : weatherForecast) {
                Label forecastLabel = new Label(forecast);
                forecastLabel.setWrapText(true);
                weatherBox.getChildren().add(forecastLabel);
            }
            
            locationGrid.add(weatherBox, 1, row++);
        }

        // Add both grids to the details container
        detailsContainer.getChildren().addAll(eventGrid, locationGrid);
        mainContent.getChildren().add(detailsContainer);

        // Add Join Event button if user is logged in
        if (currentUser != null) {
            Button joinButton = new Button("Join Event");
            joinButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
            joinButton.setOnAction(e -> {
                try {
                    eventRepository.addParticipant(currentUser, event);
                    showSuccess("Success", "You have successfully joined the event!");
                    dialog.close();
                } catch (Exception ex) {
                    showError("Error", "Failed to join event: " + ex.getMessage());
                }
            });
            mainContent.getChildren().add(joinButton);
        }

        // Add the main content to the dialog
        dialog.getDialogPane().setContent(mainContent);

        // Add a close button
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Show the dialog
        dialog.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 