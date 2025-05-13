package Controllers;

import Entities.Tag;
import Entities.Destination;
import services.ServiceDestination;
import services.ServiceTag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.ArrayList;
import java.util.Optional;

public class Gestion {
    ServiceDestination serviceDestination = new ServiceDestination();
    ServiceTag serviceTag = new ServiceTag();
    Destination destination = new Destination();
    Tag tag = new Tag();

    @FXML
    private TextField locationListerId;
    @FXML
    private TextField locationRechercher;
    @FXML
    private TextField tagRechercher;

    @FXML
    public void initialize() {
        locationIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        locationNomCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        ObservableList<Destination> destinationObservableList = FXCollections.observableArrayList(serviceDestination.getAllDestinations());
        locationTable.setItems(destinationObservableList);

        tagIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tagNomCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        locationTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Destination selected = locationTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    int selectedId = selected.getId();
                    locationListerId.setText(String.valueOf(selectedId));
                    locationModifierId.setText(String.valueOf(selectedId));
                }
            }
        });

        ObservableList<Tag> tagObservableList = FXCollections.observableArrayList(serviceTag.getAllTag());
        tagTable.setItems(tagObservableList);

        tagTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Tag selected = tagTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    int selectedId = selected.getId();
                    tagListerId.setText(String.valueOf(selectedId));
                    tagModifierId.setText(String.valueOf(selectedId));
                }
            }
        });

        locationRechercher.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Destination> destinationArrayList = new ArrayList<>();
            for (Destination d : serviceDestination.getAllDestinations()) {
                if (("" + d.getId() + d.getName() + d.getAddress()).toLowerCase().contains(newValue.toLowerCase())) {
                    destinationArrayList.add(d);
                }
            }
            ObservableList<Destination> filteredList = FXCollections.observableArrayList(destinationArrayList);
            locationTable.setItems(filteredList);
        });

        tagRechercher.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Tag> tagArrayList = new ArrayList<>();
            for (Tag a : serviceTag.getAllTag()) {
                if (("" + a.getId() + a.getName()).toLowerCase().contains(newValue.toLowerCase())) {
                    tagArrayList.add(a);
                }
            }
            ObservableList<Tag> tagObservableListR = FXCollections.observableArrayList(tagArrayList);
            tagTable.setItems(tagObservableListR);
        });
        if (mapView == null) {
            mapView = new WebView();
        }
        loadGoogleMap();
    }

    @FXML private TextField locationAjouterNom;
    @FXML private TextField locationAjouterAddress;
    @FXML private Label locationAjouterLabel;

    @FXML
    public void locationAjouter(ActionEvent event) {
        if (locationAjouterNom.getText().isEmpty() || locationAjouterAddress.getText().isEmpty()) {
            locationAjouterLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        Destination destination = new Destination(locationAjouterNom.getText(), locationAjouterAddress.getText());
        if (serviceDestination.addDestination(destination)) {
            locationAjouterLabel.setText("Destination added with id : " + destination.getId());
        } else {
            locationAjouterLabel.setText("Error while adding destination");
        }
    }

    @FXML private TableView<Destination> locationTable;
    @FXML private TableColumn<Destination, Integer> locationIdCol;
    @FXML private TableColumn<Destination, String> locationNomCol;
    @FXML private TableColumn<Destination, String> locationAddressCol;

    @FXML
    public void locationLister(ActionEvent event) {
        ObservableList<Destination> destinationObservableList = FXCollections.observableArrayList(serviceDestination.getAllDestinations());
        locationTable.setItems(destinationObservableList);
        goToLocation(40.7128f, -74.0060f);  // Example for New York City coordinates
    }

    @FXML private Label locationNomLabel;
    @FXML private Label locationAddressLabel;
    @FXML private TextField locationModifierNom;
    @FXML private TextField locationModifierAddress;
    @FXML private TextField locationModifierId;

    public void locationSelect(ActionEvent event) {
        try {
            destination = serviceDestination.getDestination(Integer.parseInt(locationModifierId.getText()));
            if (destination != null) {
                locationNomLabel.setText(destination.getName());
                locationAddressLabel.setText(destination.getAddress());
                locationModifierNom.setText(destination.getName());
                locationModifierAddress.setText(destination.getAddress());
            } else {
                locationModifierAffichage.setText("Destination inexistante!");
                locationNomLabel.setText("");
                locationAddressLabel.setText("");
                locationModifierNom.setText("");
                locationModifierAddress.setText("");
            }
        } catch (NumberFormatException e) {
            locationModifierAffichage.setText("Veuillez entrer un nombre!");
        }
    }

    @FXML private Label locationModifierAffichage;

    @FXML
    public void locationDelete(ActionEvent event) {
        try {
            if (destination.getId() == Integer.parseInt(locationModifierId.getText())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet élément ?");
                alert.setContentText("Cette action est irréversible.");

                ButtonType ouiButton = new ButtonType("Oui");
                ButtonType nonButton = new ButtonType("Non");

                alert.getButtonTypes().setAll(ouiButton, nonButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ouiButton) {
                    serviceDestination.deleteDestination(destination.getId());
                    locationModifierAffichage.setText("Élément supprimé!");
                } else {
                    locationModifierAffichage.setText("Suppression annulée.");
                }
            }
        } catch (NumberFormatException e) {
            locationModifierAffichage.setText("Veuillez selectionner une destination avant de le supprimer!");
        }
    }

    @FXML
    public void locationModify(ActionEvent event) {
        try {
            if (destination.getId() == Integer.parseInt(locationModifierId.getText())) {
                if (locationModifierNom.getText().isEmpty() || locationModifierAddress.getText().isEmpty()) {
                    locationModifierAffichage.setText("Veuillez remplir tous les champs!");
                    return;
                }
                destination.setName(locationModifierNom.getText());
                destination.setAddress(locationModifierAddress.getText());
                if (serviceDestination.updateDestination(destination)) {
                    locationModifierAffichage.setText("La destination N°" + destination.getId() + " a été modifiée avec succès!");
                } else {
                    locationModifierAffichage.setText("Erreur lors de la modification de la destination!");
                }
            }
        } catch (NumberFormatException e) {
            locationModifierAffichage.setText("Veuillez selectionner une destination avant de le modifier!");
        }
    }

    @FXML private TextField tagAjouterNom;
    @FXML private Label tagAjouterLabel;

    @FXML
    public void tagAjouter(ActionEvent event) {
        if (tagAjouterNom.getText().isEmpty()) {
            tagAjouterLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        tag = new Tag(tagAjouterNom.getText());
        if (serviceTag.addTag(tag)) {
            tagAjouterLabel.setText("Tag added with id : " + tag.getId());
        } else {
            tagAjouterLabel.setText("Error while adding tag");
        }
    }

    @FXML private TextField tagModifierId;
    @FXML private Label tagNomLabel;
    @FXML private TextField tagModifierNom;
    @FXML private Label tagModifierAffichage;

    @FXML
    public void tagSelect(ActionEvent event) {
        try {
            tag = serviceTag.getTag(Integer.parseInt(tagModifierId.getText()));
            if (tag != null) {
                tagNomLabel.setText(tag.getName());
                tagModifierNom.setText(tag.getName());
            } else {
                tagModifierAffichage.setText("Tag inexistant!");
                tagNomLabel.setText("");
                tagModifierNom.setText("");
            }
        } catch (NumberFormatException e) {
            tagModifierAffichage.setText("Veuillez entrer un nombre!");
        }
    }

    @FXML
    public void tagDelete(ActionEvent event) {
        try {
            if (tag.getId() == Integer.parseInt(tagModifierId.getText())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet élément ?");
                alert.setContentText("Cette action est irréversible.");

                ButtonType ouiButton = new ButtonType("Oui");
                ButtonType nonButton = new ButtonType("Non");

                alert.getButtonTypes().setAll(ouiButton, nonButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ouiButton) {
                    serviceTag.deleteTag(tag.getId());
                    tagModifierAffichage.setText("Élément supprimé!");
                } else {
                    tagModifierAffichage.setText("Suppression annulée.");
                }
            }
        } catch (NumberFormatException e) {
            tagModifierAffichage.setText("Veuillez selectionner un tag avant de le supprimer!");
        }
    }

    @FXML
    public void tagModify(ActionEvent event) {
        try {
            if (tag.getId() == Integer.parseInt(tagModifierId.getText())) {
                if (tagModifierNom.getText().isEmpty()) {
                    tagModifierAffichage.setText("Veuillez remplir tous les champs!");
                    return;
                }
                tag.setName(tagModifierNom.getText());
                if (serviceTag.updateTag(tag)) {
                    tagModifierAffichage.setText("Le tag N°" + tag.getId() + " a été modifié avec succès!");
                } else {
                    tagModifierAffichage.setText("Erreur lors de la modification du tag!");
                }
            }
        } catch (NumberFormatException e) {
            tagModifierAffichage.setText("Veuillez selectionner un tag avant de le modifier!");
        }
    }

    @FXML TableView<Tag> tagTable;
    @FXML TableColumn<Tag, Integer> tagIdCol;
    @FXML TableColumn<Tag, String> tagNomCol;
    @FXML private TextField tagListerId;

    @FXML
    public void tagLister(ActionEvent event) {
        ObservableList<Tag> tagObservableList = FXCollections.observableArrayList(serviceTag.getAllTag());
        tagTable.setItems(tagObservableList);
    }
    @FXML
    private WebView mapView;
    private void loadGoogleMap() {
        String html = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>Building Map</title>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
        <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
        <style>
            #map { height: 400px; width: 100%; }
            body { margin: 0; padding: 0; }
        </style>
    </head>
    <body>
    <div id="map"></div>
    <script>
        // Initialize the map with a default center (Geneva)
        var map = L.map('map').setView([33.8869, 9.5375], 13);

        // Add OpenStreetMap tiles
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
            maxZoom: 19
        }).addTo(map);

        // Make the map object available globally
        window.map = map;
    </script>
    </body>
    </html>
    """;

        // Get the WebEngine from the WebView
        WebEngine webEngine = mapView.getEngine();
        webEngine.loadContent(html);
    }

    // Function to move the map to the given coordinates (lat, lng)
    public void goToLocation(float lat, float lng) {
        WebEngine webEngine = mapView.getEngine();

        // This JavaScript function will move the map's center to the given lat/lng.
        String script = String.format("map.setView([%f, %f], 15);", lat, lng);

        // Execute the script in the WebView to move the map to the specified coordinates.
        webEngine.executeScript(script);
    }
}
