package controllers;

import entities.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ServiceCategory;
import java.sql.SQLException;
import java.util.List;

public class GestionCategory {


    @FXML
    private TableColumn<GestionCategory, String> colDescCat;

    @FXML
    private TableColumn<GestionCategory, String> colNomCat;

    @FXML
    private Tab tabModCat;

    @FXML
    private TableView<Category> tableView;

    @FXML
    private TextField txtDescriptionCat;

    @FXML
    private TextField txtDescriptionMCat;

    @FXML
    private TextField txtNomCat;

    @FXML
    private TextField txtNomMCat;

    @FXML
    private TextField txtidMCat;

    @FXML
    private TabPane tabPaneCat;

    @FXML
    private TextField txtidSCat;

    @FXML
    private Button btnAjouterCatid;

    private ServiceCategory serviceCategory = new ServiceCategory();

    private GestionProd parentController;

    public void setParentController(GestionProd parentController) {
        this.parentController = parentController;
    }

    @FXML
    void btnAjouterCat(ActionEvent event) {
        try {

            String nom = txtNomCat.getText().trim();
            String description = txtDescriptionCat.getText().trim();


            if (nom.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
                return;
            }
            Category category = new Category(nom,description);
            serviceCategory.ajouter(category);
            showAlert(Alert.AlertType.INFORMATION, "Succès","Category ajouté avec succès !");
            clearFields();
            if (parentController != null) {
                parentController.refreshComboBoxCat();
            }
            Stage stage = (Stage) btnAjouterCatid.getScene().getWindow();
            stage.close();

        }catch (SQLException e){
            System.out.println(e);
        }
    }
    private void clearFields() {
        txtNomCat.clear();
        txtDescriptionCat.clear();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void btnSuppCat(ActionEvent event) {
        try {

            Category selectedCategory = tableView.getSelectionModel().getSelectedItem();
            if (selectedCategory == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une Category à supprimer !");
                return;
            }


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");


            if (alert.showAndWait().get() == ButtonType.OK) {
                ServiceCategory serviceCatSupp = new ServiceCategory();
                serviceCatSupp.supprimer(selectedCategory);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Category supprimée avec succès !");


                refreshTable();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    private void refreshTable() throws SQLException {
        List<Category> l = serviceCategory.afficher();
        ObservableList<Category> obe = FXCollections.observableList(l);
        tableView.setItems(obe);
    }
    @FXML
    void initialize() throws SQLException {
        System.out.println("after initialize");
        List<Category> l=serviceCategory.afficher();
        ObservableList<Category> obe= FXCollections.observableList(l);

        tableView.setItems(obe);
        colNomCat.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescCat.setCellValueFactory(new PropertyValueFactory<>("desc"));

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Category selectedCategory = tableView.getSelectionModel().getSelectedItem();
                if (selectedCategory != null) {
                    txtidMCat.setText(String.valueOf(selectedCategory.getId()));
                    txtNomMCat.setText(selectedCategory.getNom());
                    txtDescriptionMCat.setText(selectedCategory.getDesc());


                    txtidSCat.setText(String.valueOf(selectedCategory.getId()));
                    System.out.println("Selected Category ID: " + selectedCategory.getId());
                }
            }
        });


    }
}
