package controllers;

import entities.Reclamation;
import services.ServiceReclamation;
import utils.ChatBot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;

import entities.Avis;
import services.ServiceAvis;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionAvis {

    @FXML private TextField AvisAjouterIdUser;
    @FXML private TextField AvisAjouterIdVoyage;
    @FXML private TextField AvisAjouterNote;
    @FXML private TextArea AvisAjouterComm;
    @FXML private Button AvisAjouterButton;
    @FXML private Label AvisAjouterLabel;
    ServiceAvis serviceAvis = new ServiceAvis();
    Avis avis = new Avis();
    @FXML TableColumn<Avis, Integer> avisIdCol;
    @FXML TableColumn<Avis, Integer> avisIdUserCol;
    @FXML TableColumn<Avis, Integer> avisIdVoyageCol;
    @FXML TableColumn<Avis, Integer> avisNoteCol;
    @FXML TableColumn<Avis, String> avisCommCol;
    @FXML private TextField AvisListerId;
    @FXML private TableColumn<Reclamation, Integer> ReclamationIdCol;
    @FXML private TableColumn<Reclamation, Integer> ReclamationIdUserCol;
    @FXML private TableColumn<Reclamation, Integer> ReclamationIdVoyageCol;
    @FXML private TableColumn<Reclamation, String> ReclamationCommCol;
    @FXML private TableView<Reclamation> reclamationTable;
    @FXML
    public void initialize(){

        chatboxTextArea.appendText("Assistant virtuel : Bonjour!\n");

        avisIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        avisIdUserCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        avisIdVoyageCol.setCellValueFactory(new PropertyValueFactory<>("idVoyage"));
        avisNoteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        avisCommCol.setCellValueFactory(new PropertyValueFactory<>("commentaire"));

        ObservableList<Avis> salleObservableList = FXCollections.observableArrayList(serviceAvis.getAllAvis());
        avisTable.setItems(salleObservableList);

        avisTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Avis selected = avisTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    int selectedId = selected.getId();
                    AvisListerId.setText(String.valueOf(selectedId));
                    AvisModifierID.setText(String.valueOf(selectedId));
                }
            }
        });

        ReclamationIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ReclamationIdUserCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        ReclamationIdVoyageCol.setCellValueFactory(new PropertyValueFactory<>("idVoyage"));
        ReclamationCommCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        ObservableList<Reclamation> reclamationObservableList = FXCollections.observableArrayList(serviceReclamation.getAllReclamations());
        reclamationTable.setItems(reclamationObservableList);

        reclamationTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Reclamation selected = reclamationTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    int selectedId = selected.getId();
                    ReclamationListerId.setText(String.valueOf(selectedId));
                    ReclamationModifierID.setText(String.valueOf(selectedId));
                }
            }
        });


        avisRechercher.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Avis> avisArrayList = new ArrayList<>();
            for(Avis a : serviceAvis.getAllAvis()){
                if ((""+a.getId()+a.getIdUser()+a.getIdVoyage()+a.getCommentaire()).toLowerCase().contains(newValue.toLowerCase())){
                    avisArrayList.add(a);
                }
            }
            ObservableList<Avis> avisObservableListR = FXCollections.observableArrayList(avisArrayList);
            avisTable.setItems(avisObservableListR);
        });
        reclamationRechercher.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Reclamation> reclamationArrayList = new ArrayList<>();
            for(Reclamation a : serviceReclamation.getAllReclamations()){
                if ((""+a.getId()+a.getIdUser()+a.getIdVoyage()+a.getMessage()).toLowerCase().contains(newValue.toLowerCase())){
                    reclamationArrayList.add(a);
                }
            }
            ObservableList<Reclamation> reclamationObservableListR = FXCollections.observableArrayList(reclamationArrayList);
            reclamationTable.setItems(reclamationObservableListR);
        });

    }
    @FXML
    public void avisAjouter(ActionEvent event) {
        try{
            avis.setIdUser(Integer.parseInt(AvisAjouterIdUser.getText()));
            avis.setIdVoyage(Integer.parseInt(AvisAjouterIdVoyage.getText()));
            avis.setNote(Integer.parseInt(AvisAjouterNote.getText()));
            avis.setCommentaire(AvisAjouterComm.getText());
            ServiceAvis sa = new ServiceAvis();
            sa.addAvis(avis);
            AvisAjouterLabel.setText("Avis ajouter avec l'id "+avis.getId());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            AvisAjouterLabel.setText("Erreur lors de l'ajout!");
        }
    }
    @FXML private TextField AvisModifierID;
    @FXML private TextField AvisModifierIdUser;
    @FXML private TextField AvisModifierIdVoyage;
    @FXML private TextField AvisModifierNote;
    @FXML private TextArea AvisModifierComm;
    @FXML private Label AvisModifierAffichage;
    @FXML
    public void avisSelect(ActionEvent event) {
        try {
            avis=serviceAvis.getAvis(Integer.parseInt(AvisModifierID.getText()));
            if(avis!=null){
                AvisModifierAffichage.setText("Salle N°"+avis.getId()+" selectionnée!");
                AvisModifierIdUser.setText(avis.getIdUser()+"");
                AvisModifierIdVoyage.setText(avis.getIdVoyage()+"");
                AvisModifierNote.setText(avis.getNote()+"");
                AvisModifierComm.setText(avis.getCommentaire());
            } else {
                AvisModifierAffichage.setText("Avis introuvable!");
                return;
            }
        } catch (NumberFormatException ex) {
            AvisModifierAffichage.setText("Veuillez entrer un nombre!");
            return;
        }

    }
    @FXML
    public void supprimerAvis(ActionEvent event) {
        try {
            if (avis.getId()==Integer.parseInt(AvisModifierID.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet élément ?");
                alert.setContentText("Cette action est irréversible.");

                ButtonType ouiButton = new ButtonType("Oui");
                ButtonType nonButton = new ButtonType("Non");

                alert.getButtonTypes().setAll(ouiButton, nonButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ouiButton) {
                    serviceAvis.deleteAvis(avis.getId());
                    AvisModifierAffichage.setText("Élément supprimé!");
                } else {
                    AvisModifierAffichage.setText("Suppression annulée.");
                }
            }
        } catch (NumberFormatException e){
            AvisModifierAffichage.setText("Veuillez selectionner un avis avant de la supprimer!");
            return;
        }
    }
    @FXML
    public void avisModifier(ActionEvent event){
        try {
            if (avis.getId()==Integer.parseInt(AvisModifierID.getText())){

                if(AvisModifierIdUser.getText().isEmpty()||AvisModifierIdVoyage.getText().isEmpty()||AvisModifierNote.getText().isEmpty()){
                    AvisModifierAffichage.setText("Veuillez remplir tous les champs!");
                    return;
                }
                avis.setIdUser(Integer.parseInt(AvisModifierIdUser.getText()));
                avis.setIdVoyage(Integer.parseInt(AvisModifierIdVoyage.getText()));
                avis.setNote(Integer.parseInt(AvisModifierNote.getText()));
                avis.setCommentaire(AvisModifierComm.getText());
                System.out.println(avis);
                if(serviceAvis.updateAvis(avis)){
                    AvisModifierAffichage.setText("L'avis N°"+avis.getId()+" a ete modifie avec succès!");
                    return;
                }
                AvisModifierAffichage.setText("Erreur lors de la modification de l'avis!");
            }
        } catch (NumberFormatException e){
            AvisModifierAffichage.setText("Veuillez selectionner un avis avant de le modifier!");
            return;
        }
    }

    @FXML
    private TableView<Avis> avisTable;
    @FXML
    public void avisLister(ActionEvent event){
        ObservableList<Avis> avisObservableList = FXCollections.observableArrayList(serviceAvis.getAllAvis());
        avisTable.setItems(avisObservableList);
    }
    @FXML
    private TextArea chatboxTextArea;
    @FXML
    private TextField chatboxInput;
    @FXML
    public void envoyerMessage(ActionEvent event) {
        chatboxTextArea.appendText("Vous : " +chatboxInput.getText()+"\n");
        chatboxTextArea.appendText("Assistant virtuel : " + ChatBot.sendMessage("Vous etes un assistant dans une agence de voyage repondez sur cette question en francais precisament et sans parler trop : "+chatboxInput.getText())+"\n");
        chatboxInput.clear();

    }
    ServiceReclamation serviceReclamation = new ServiceReclamation();
    Reclamation reclamation = new Reclamation();
    @FXML private TextField ReclamationAjouterIdUser;
    @FXML private TextField ReclamationAjouterIdVoyage;
    @FXML private TextField ReclamationAjouterNote;
    @FXML private TextArea ReclamationAjouterComm;
    @FXML private Label ReclamationAjouterLabel;
    @FXML private TextField ReclamationListerId;
    @FXML
    public void reclamationAjouter(ActionEvent event){
        try{
            reclamation.setIdUser(Integer.parseInt(ReclamationAjouterIdUser.getText()));
            reclamation.setIdVoyage(Integer.parseInt(ReclamationAjouterIdVoyage.getText()));
            reclamation.setMessage(ReclamationAjouterComm.getText());
            serviceReclamation.addReclamation(reclamation);
            ReclamationAjouterLabel.setText("Reclamation ajouter avec l'id "+reclamation.getId());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            ReclamationAjouterLabel.setText("Erreur lors de l'ajout!");
        }
    }

    @FXML public void reclamationLister(ActionEvent event){
        ObservableList<Reclamation> reclamationObservableList = FXCollections.observableArrayList(serviceReclamation.getAllReclamations());
        reclamationTable.setItems(reclamationObservableList);
    }
    @FXML private TextField ReclamationModifierID;
    @FXML private TextField ReclamationModifierIdUser;
    @FXML private TextField ReclamationModifierIdVoyage;
    @FXML private TextArea ReclamationModifierComm;
    @FXML private Label ReclamationModifierAffichage;
    @FXML public void reclamationSelect(ActionEvent event){
        try {
            reclamation=serviceReclamation.getReclamation(Integer.parseInt(ReclamationModifierID.getText()));
            if(reclamation!=null){
                ReclamationModifierAffichage.setText("Reclamation N°"+reclamation.getId()+" selectionnée!");
                ReclamationModifierIdUser.setText(reclamation.getIdUser()+"");
                ReclamationModifierIdVoyage.setText(reclamation.getIdVoyage()+"");
                ReclamationModifierComm.setText(reclamation.getMessage());
            } else {
                ReclamationModifierAffichage.setText("Reclamation introuvable!");
                return;
            }
        } catch (NumberFormatException ex) {
            ReclamationModifierAffichage.setText("Veuillez entrer un nombre!");
            return;
        }
    }
    @FXML public void supprimerReclamation(ActionEvent event){
        try {
            if (reclamation.getId()==Integer.parseInt(ReclamationModifierID.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet élément ?");
                alert.setContentText("Cette action est irréversible.");

                ButtonType ouiButton = new ButtonType("Oui");
                ButtonType nonButton = new ButtonType("Non");

                alert.getButtonTypes().setAll(ouiButton, nonButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ouiButton) {
                    serviceReclamation.deleteReclamation(reclamation.getId());
                    ReclamationModifierAffichage.setText("Élément supprimé!");
                } else {
                    ReclamationModifierAffichage.setText("Suppression annulée.");
                }
            }
        } catch (NumberFormatException e){
            ReclamationModifierAffichage.setText("Veuillez selectionner une reclamation avant de la supprimer!");
            return;
        }
    }
    @FXML public void reclamationModifier(ActionEvent event){
        try {
            if (reclamation.getId()==Integer.parseInt(ReclamationModifierID.getText())){

                if(ReclamationModifierIdUser.getText().isEmpty()||ReclamationModifierIdVoyage.getText().isEmpty()){
                    AvisModifierAffichage.setText("Veuillez remplir tous les champs!");
                    return;
                }
                reclamation.setIdUser(Integer.parseInt(ReclamationModifierIdUser.getText()));
                reclamation.setIdVoyage(Integer.parseInt(ReclamationModifierIdVoyage.getText()));
                reclamation.setMessage(ReclamationModifierComm.getText());
                if(serviceReclamation.updateReclamation(reclamation)){
                    ReclamationModifierAffichage.setText("La reclamation N°"+reclamation.getId()+" a ete modifie avec succès!");
                    return;
                }
                ReclamationModifierAffichage.setText("Erreur lors de la modification de la reclamation!");
            }
        } catch (NumberFormatException e){
            ReclamationModifierAffichage.setText("Veuillez selectionner une reclamation avant de le modifier!");
            return;
        }
    }
    @FXML private TextField avisRechercher;
    @FXML private TextField reclamationRechercher;


}
