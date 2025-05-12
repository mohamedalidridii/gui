package Controllers;

import entities.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceLocation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LocationController implements Initializable {

    @FXML private TableView<Location> locationTableView;
    @FXML private TableColumn<Location, String> countryColumn;
    @FXML private TableColumn<Location, String> descriptionColumn;
    @FXML private TableColumn<Location, Boolean> visaColumn;
    @FXML private TableColumn<Location, String> imageColumn;

    @FXML private TextField countryField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox visaCheckBox;
    @FXML private ImageView imagePreview;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private TextField searchField;

    private final ServiceLocation serviceLocation = new ServiceLocation();
    private Location selectedLocation = null;
    private String currentImagePath = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Setup TableView columns
            countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            visaColumn.setCellValueFactory(new PropertyValueFactory<>("visa"));
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

            // Setup search field listener
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                String searchTerm = newValue.trim();
                if (!searchTerm.isEmpty()) {
                    List<Location> searchResults = serviceLocation.dynamicSearch(searchTerm);
                    locationTableView.setItems(FXCollections.observableArrayList(searchResults));
                } else {
                    loadLocationList();
                    // Reload all locations if search is empty
                }
            });

            // Load initial data
            loadLocationList();

            // Setup selection handler for TableView
            locationTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedLocation = newSelection;
                    populateForm(selectedLocation);
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                } else {
                    selectedLocation = null;
                    clearForm();
                    editButton.setDisable(true);
                    deleteButton.setDisable(true);
                }
            });

            // Initially disable edit and delete buttons
            editButton.setDisable(true);
            deleteButton.setDisable(true);

        } catch (Exception e) {
            System.err.println("Error initializing LocationController: " + e.getMessage());
            showError("Database Connection Error", "Could not connect to the database. Please check your database connection and try again.");
        }
    }

    @FXML
    public void handleAddLocation() {
        try {
            Location newLocation = createLocationFromForm();
            serviceLocation.create(newLocation);
            loadLocationList();
            clearForm();
        } catch (Exception e) {
            showError("Error adding location", e.getMessage());
        }
    }

    @FXML
    public void handleEditLocation() {
        if (selectedLocation != null) {
            try {
                Location updatedLocation = createLocationFromForm();
                updatedLocation.setIdLocation(selectedLocation.getIdLocation());
                serviceLocation.update(updatedLocation);
                loadLocationList();
                clearForm();
                selectedLocation = null;
                editButton.setDisable(true);
                deleteButton.setDisable(true);
            } catch (Exception e) {
                showError("Error updating location", e.getMessage());
            }
        }
    }

    @FXML
    public void handleDeleteLocation() {
        if (selectedLocation != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Delete Location");
            confirmDialog.setContentText("Are you sure you want to delete this Location: " + selectedLocation.getCountry() + "?");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    serviceLocation.delete(selectedLocation.getIdLocation());
                    loadLocationList();
                    clearForm();
                    selectedLocation = null;
                    editButton.setDisable(true);
                    deleteButton.setDisable(true);
                } catch (Exception e) {
                    showError("Error deleting location", e.getMessage());
                }
            }
        }
    }

    @FXML
    public void navigateToEvents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainEventView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setTitle("Event Management");
            stage.setScene(scene);
        } catch (IOException e) {
            showError("Navigation Error", "Could not load the Event view: " + e.getMessage());
        }
    }

    @FXML
    public void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Location Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(addButton.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Create images directory if it doesn't exist
                Path imagesDir = Paths.get("src/main/resources/images");
                if (!Files.exists(imagesDir)) {
                    Files.createDirectories(imagesDir);
                }

                // Copy the selected file to the images directory
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path targetPath = imagesDir.resolve(fileName);
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image saved to: " + targetPath.toAbsolutePath());

                // Update the image preview
                Image image = new Image(targetPath.toUri().toString());
                if (!image.isError()) {
                    imagePreview.setImage(image);
                    // Store the relative path
                    currentImagePath = "images/" + fileName;
                    System.out.println("Image preview updated with path: " + currentImagePath);
                } else {
                    System.out.println("Failed to load image preview");
                    showError("Error", "Failed to load image preview");
                }

            } catch (IOException e) {
                System.err.println("Error uploading image: " + e.getMessage());
                e.printStackTrace();
                showError("Error uploading image", e.getMessage());
            }
        }
    }

    @FXML
    public void handleSearch() {
        // This method is no longer needed as search is now automatic
    }

    private Location createLocationFromForm() {
        Location location = new Location();
        location.setCountry(countryField.getText());
        location.setDescription(descriptionArea.getText());
        location.setVisa(visaCheckBox.isSelected());
        location.setImage(currentImagePath);
        return location;
    }

    private void populateForm(Location location) {
        countryField.setText(location.getCountry());
        descriptionArea.setText(location.getDescription());
        visaCheckBox.setSelected(location.isVisa());
        
        if (location.getImage() != null && !location.getImage().isEmpty()) {
            try {
                System.out.println("Attempting to load image from path: " + location.getImage());
                String imagePath = location.getImage();
                
                // Try loading from file system first
                File imageFile = new File("src/main/resources/" + imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    if (!image.isError()) {
                        imagePreview.setImage(image);
                        currentImagePath = location.getImage();
                        System.out.println("Successfully loaded image from file system");
                        return;
                    }
                }
                
                // If file system loading fails, try loading from resources
                try {
                    Image image = new Image(getClass().getResourceAsStream("/" + imagePath));
                    if (!image.isError()) {
                        imagePreview.setImage(image);
                        currentImagePath = location.getImage();
                        System.out.println("Successfully loaded image from resources");
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Failed to load image from resources: " + e.getMessage());
                }
                
                // If both attempts fail, clear the image
                System.out.println("Failed to load image from both file system and resources");
                imagePreview.setImage(null);
                currentImagePath = null;
                
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                e.printStackTrace();
                imagePreview.setImage(null);
                currentImagePath = null;
            }
        } else {
            System.out.println("No image path available for location");
            imagePreview.setImage(null);
            currentImagePath = null;
        }
    }

    private void loadLocationList() {
        try {
            ObservableList<Location> locations = FXCollections.observableArrayList(serviceLocation.getAllLocations());
            locationTableView.setItems(locations);
        } catch (Exception e) {
            System.err.println("Error loading location list: " + e.getMessage());
            showError("Error loading locations", e.getMessage());
        }
    }

    private void clearForm() {
        countryField.clear();
        descriptionArea.clear();
        visaCheckBox.setSelected(false);
        imagePreview.setImage(null);
        currentImagePath = null;
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 