package Controllers;

import entities.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import services.ServiceLocation;
import services.ServiceTag;
import entities.Location;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Material;

public class Gestion {
    ServiceLocation serviceLocation=new ServiceLocation();
    ServiceTag serviceTag=new ServiceTag();
    Location location = new Location();
    Tag tag = new Tag();
    @FXML
    private TextField locationListerId;
    @FXML
    private TextField locationRechercher;
    @FXML
    private TextField tagRechercher;
    @FXML
    public void initialize(){
        locationIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        locationNomCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        ObservableList<Location> localisationObservableList = FXCollections.observableArrayList(serviceLocation.getAllLocation());
        locationTable.setItems(localisationObservableList);

        tagIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tagNomCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        locationTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Location selected = locationTable.getSelectionModel().getSelectedItem();
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
            ArrayList<Location> locationArrayList = new ArrayList<>();
            for(Location a : serviceLocation.getAllLocation()){
                if ((""+a.getId()+a.getName()+a.getAddress()).toLowerCase().contains(newValue.toLowerCase())){
                    locationArrayList.add(a);
                }
            }
            ObservableList<Location> avisObservableListR = FXCollections.observableArrayList(locationArrayList);
            locationTable.setItems(avisObservableListR);
        });

        tagRechercher.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Tag> tagArrayList = new ArrayList<>();
            for(Tag a : serviceTag.getAllTag()){
                if ((""+a.getId()+a.getName()).toLowerCase().contains(newValue.toLowerCase())){
                    tagArrayList.add(a);
                }
            }
            ObservableList<Tag> tagObservableListR = FXCollections.observableArrayList(tagArrayList);
            tagTable.setItems(tagObservableListR);
        });


    }
    @FXML
    private TextField locationAjouterNom;
    @FXML
    private TextField locationAjouterAddress;
    @FXML
    private Label locationAjouterLabel;
    @FXML
    public void locationAjouter(ActionEvent event){
        if(locationAjouterNom.getText().isEmpty()||locationAjouterAddress.getText().isEmpty()){
            locationAjouterLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        Location location = new Location(locationAjouterNom.getText(),locationAjouterAddress.getText());
        if(serviceLocation.addLocation(location)){
            locationAjouterLabel.setText("Location added with id : "+location.getId());
            return;
        }
        locationAjouterLabel.setText("Error while adding location");
    }
    @FXML
    private TableView<Location> locationTable;
    @FXML
    private TableColumn<Location,Integer> locationIdCol;
    @FXML
    private TableColumn<Location,String> locationNomCol;
    @FXML
    private TableColumn<Location,String> locationAddressCol;
    @FXML
    public void locationLister(ActionEvent event){
        ObservableList<Location> locationObservableList = FXCollections.observableArrayList(serviceLocation.getAllLocation());
        locationTable.setItems(locationObservableList);
    }
    @FXML
    private Label locationNomLabel;
    @FXML
    private Label locationAddressLabel;
    @FXML
    private TextField locationModifierNom;
    @FXML
    private TextField locationModifierAddress;
    @FXML
    private TextField locationModifierId;
    public void locationSelect(ActionEvent event){
        try {
            location = serviceLocation.getLocation(Integer.parseInt(locationModifierId.getText()));
            if(location!=null){
                locationNomLabel.setText(location.getName());
                locationAddressLabel.setText(location.getAddress());
                locationModifierNom.setText(location.getName());
                locationModifierAddress.setText(location.getAddress());

            } else {
                locationModifierAffichage.setText("Location inexistant!");
                locationNomLabel.setText("");
                locationAddressLabel.setText("");
                locationModifierNom.setText("");
                locationModifierAddress.setText("");
            }
        } catch (NumberFormatException e) {
            locationModifierAffichage.setText("Veuillez entrer un nombre!");
        }
    }
    @FXML
    private Label locationModifierAffichage;
    @FXML
    public void locationDelete(ActionEvent event){
        try {
            if (location.getId()==Integer.parseInt(locationModifierId.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet élément ?");
                alert.setContentText("Cette action est irréversible.");

                ButtonType ouiButton = new ButtonType("Oui");
                ButtonType nonButton = new ButtonType("Non");

                alert.getButtonTypes().setAll(ouiButton, nonButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ouiButton) {
                    serviceLocation.deleteLocation(location.getId());
                    locationModifierAffichage.setText("Élément supprimé!");
                } else {
                    locationModifierAffichage.setText("Suppression annulée.");
                }
            }
        } catch (NumberFormatException e){
            locationModifierAffichage.setText("Veuillez selectionner une localisation avant de le supprimer!");
            return;
        }

    }
    @FXML
    public void locationModify(ActionEvent event){
        try {
            if (location.getId()==Integer.parseInt(locationModifierId.getText())){

                if(locationModifierNom.getText().isEmpty()||locationModifierAddress.getText().isEmpty()){
                    locationModifierAffichage.setText("Veuillez remplir tous les champs!");
                    return;
                }
                location.setName(locationModifierNom.getText());
                location.setAddress(locationModifierAddress.getText());
                if(serviceLocation.updateLocation(location)){
                    locationModifierAffichage.setText("La localisation N°"+location.getId()+" a etee modifiee avec succès!");
                    return;
                }
                locationModifierAffichage.setText("Erreur lors de la modification de la localisation!");
            }
        } catch (NumberFormatException e){
            locationModifierAffichage.setText("Veuillez selectionner une localisation avant de le modifier!");
            return;
        }
    }

    @FXML
    private TextField tagAjouterNom;
    @FXML
    private Label tagAjouterLabel;
    @FXML
    public void tagAjouter(ActionEvent event){
        if(tagAjouterNom.getText().isEmpty()){
            tagAjouterLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        tag = new Tag(tagAjouterNom.getText());
        if(serviceTag.addTag(tag)){
            tagAjouterLabel.setText("Tag added with id : "+tag.getId());
            return;
        }
        tagAjouterLabel.setText("Error while adding tag");
    }
    @FXML
    private TextField tagModifierId;
    @FXML
    private Label tagNomLabel;
    @FXML
    private TextField tagModifierNom;
    @FXML
    private Label tagModifierAffichage;
    @FXML
    public void tagSelect(ActionEvent event){
        try {
            tag = serviceTag.getTag(Integer.parseInt(tagModifierId.getText()));
            if(tag!=null){
                tagNomLabel.setText(tag.getName());
                tagModifierNom.setText(tag.getName());
            } else {
                tagModifierAffichage.setText("Location inexistant!");
                tagNomLabel.setText("");
                tagModifierNom.setText("");
            }
        } catch (NumberFormatException e) {
            tagModifierAffichage.setText("Veuillez entrer un nombre!");
        }
    }
    @FXML
    public void tagDelete(ActionEvent event){
        try {
            if (tag.getId()==Integer.parseInt(tagModifierId.getText())){
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
        } catch (NumberFormatException e){
            tagModifierAffichage.setText("Veuillez selectionner un tag avant de le supprimer!");
            return;
        }
    }
    @FXML
    public void tagModify(ActionEvent event){
        try {
            if (tag.getId()==Integer.parseInt(tagModifierId.getText())){

                if(tagModifierNom.getText().isEmpty()){
                    tagModifierAffichage.setText("Veuillez remplir tous les champs!");
                    return;
                }
                tag.setName(tagModifierNom.getText());
                if(serviceTag.updateTag(tag)){
                    tagModifierAffichage.setText("Le tag N°"+tag.getId()+" a ete modifie avec succès!");
                    return;
                }
                tagModifierAffichage.setText("Erreur lors de la modification de la tag!");
            }
        } catch (NumberFormatException e){
            tagModifierAffichage.setText("Veuillez selectionner un tag avant de le modifier!");
            return;
        }
    }
    @FXML TableView<Tag> tagTable;
    @FXML TableColumn<Tag,Integer> tagIdCol;
    @FXML TableColumn<Tag,String> tagNomCol;
    @FXML
    private TextField tagListerId;
    @FXML
    public void tagLister(ActionEvent event){
        ObservableList<Tag> tagObservableList = FXCollections.observableArrayList(serviceTag.getAllTag());
        tagTable.setItems(tagObservableList);
    }
}
