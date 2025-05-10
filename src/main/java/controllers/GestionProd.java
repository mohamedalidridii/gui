package controllers;

import entities.Category;
import entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceCategory;
import services.ServiceProduct;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class GestionProd {

    public Label subname32;
    @FXML
    private Button btns;

    @FXML
    private Button btns1;

    @FXML
    private Button btns11;

    @FXML
    private Tab tabAjout;

    @FXML
    private Tab tabMod;

    @FXML
    private TabPane prod;

    @FXML
    private TableColumn<Product, Date> colDC;

    @FXML
    private TableColumn<Product, Date> colDM;

    @FXML
    private TableColumn<Product, String> colDesc;

    @FXML
    private TableColumn<Product, String> colNom;

    @FXML
    private TableColumn<Product, Double> colPrix;

    @FXML
    private TableColumn<Product, Integer> colQt;

    @FXML
    private Label subname1;

    @FXML
    private Label subname11;

    @FXML
    private Label subname111;

    @FXML
    private Label subname1111;

    @FXML
    private Label subname2;

    @FXML
    private Label subname21;

    @FXML
    private Label subname3;

    @FXML
    private Label subname31;

    @FXML
    private Label subname6;

    @FXML
    private Label subname61;

    @FXML
    private TableView<Product> tableView;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtDescriptionM;

    @FXML
    private TextField txtImage;

    @FXML
    private TextField txtImageM;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtNomM;

    @FXML
    private TextField txtPrix;

    @FXML
    private TextField txtPrixM;

    @FXML
    private TextField txtQt;

    @FXML
    private TextField txtQtM;

    @FXML
    private TextField txtidM;

    @FXML
    private TextField txtidS;

    @FXML
    private ComboBox<String> comboCategory;

    @FXML
    private Button categoryInter;

    private ServiceProduct serviceProduct = new ServiceProduct();
    private ServiceCategory serviceCategory = new ServiceCategory();

    @FXML
    void btnAjouterProd(ActionEvent event) {

        try {

            String nom = txtNom.getText().trim();
            String description = txtDescription.getText().trim();
            String qtStr = txtQt.getText().trim();
            String prixStr = txtPrix.getText().trim();
            String image = txtImage.getText().trim();

            if (nom.isEmpty() || description.isEmpty() || qtStr.isEmpty() || prixStr.isEmpty() || image.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
                return;
            }
            int quantite;
            double prix;

            try {
                quantite = Integer.parseInt(qtStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur","La quantité doit être des nombres valides !");
                return;
            }
            try {

                prix = Double.parseDouble(prixStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur","Le Prix doit être des nombres valides !");
                return;
            }
            if (quantite < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur","La Quantité doit être positifs !");
                return;
            }if (prix < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur","Le Prix doivent être positifs !");
                return;
            }
            Date dateCreation = Date.valueOf(java.time.LocalDate.now());
            Date dateModification = Date.valueOf(java.time.LocalDate.now());



            Product product = new Product(nom,description,quantite, prix, image, dateCreation,dateModification);
            serviceProduct.ajouter(product);
            showAlert(Alert.AlertType.INFORMATION, "Succès","Produit ajouté avec succès !");
            clearFields();
            refreshTable();
        }catch (SQLException e){
            System.out.println(e);
        }

    }
    private void clearFields() {
        txtNom.clear();
        txtDescription.clear();
        txtQt.clear();
        txtPrix.clear();
        txtImage.clear();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void btnModifierProd(ActionEvent event) {
        int id = Integer.parseInt(txtidM.getText());
        String nom = txtNomM.getText().trim();
        String description = txtDescriptionM.getText().trim();
        String qtStr = txtQtM.getText().trim();
        String prixStr = txtPrixM.getText().trim();
        String image = txtImageM.getText().trim();
        try {
            // Check if the important fields are not empty
            if (txtidM.getText().isEmpty() || txtNomM.getText().isEmpty() || txtDescriptionM.getText().isEmpty() ||
                    txtQtM.getText().isEmpty() || txtPrixM.getText().isEmpty() || txtImageM.getText().isEmpty()) {
                return;
            }


            Date dateCreation = Date.valueOf(java.time.LocalDate.now());
            Date dateModification = Date.valueOf(java.time.LocalDate.now());

            if (nom.isEmpty() || description.isEmpty() || qtStr.isEmpty() || prixStr.isEmpty() || image.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
                return;
            }
            int quantite;
            double prix;

            try {
                quantite = Integer.parseInt(qtStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur","La quantité doit être des nombres valides !");
                return;
            }
            try {

                prix = Double.parseDouble(prixStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur","Le Prix doit être des nombres valides !");
                return;
            }
            if (quantite < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur","La Quantité doit être positifs !");
                return;
            }if (prix < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur","Le Prix doivent être positifs !");
                return;
            }



            Product productMod = new Product(nom,description,quantite, prix, image, dateCreation,dateModification);
            productMod.setId(id);
            System.out.println(productMod);
            ServiceProduct serviceProductMod = new ServiceProduct();
            System.out.println(id);
            serviceProductMod.modifier(productMod);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Produit modifier avec succès !");

            refreshTable();
        }catch (SQLException e){
            System.out.println(e);
        }
    }
    @FXML
    void btnSuppProd(ActionEvent event) {
        try {

            Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un produit à supprimer !");
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");


            if (alert.showAndWait().get() == ButtonType.OK) {
                ServiceProduct serviceProductSupp = new ServiceProduct();
                serviceProductSupp.supprimer(selectedProduct);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Produit supprimé avec succès !");

                refreshTable();

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    private void refreshTable() throws SQLException {
        List<Product> l = serviceProduct.afficher();
        ObservableList<Product> obe = FXCollections.observableList(l);
        tableView.setItems(obe);
    }
    @FXML
    void initialize() throws SQLException {

        List<Product> listedProducts=serviceProduct.afficher();
        List<Category> listedCategories=serviceCategory.afficher();
        List<String> listedcategoryNames = listedCategories.stream()
                .map(Category::getNom)
                .toList();
        ObservableList<Product> obse = FXCollections.observableList(listedProducts);
        ObservableList<String> obeCatName= FXCollections.observableArrayList(listedcategoryNames);
        ObservableList<Category> obeCat= FXCollections.observableArrayList(listedCategories);

        tableView.setItems(obse);
        comboCategory.setItems(obeCatName);
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQt.setCellValueFactory(new PropertyValueFactory<>("qt"));
        colDC.setCellValueFactory(new PropertyValueFactory<>("cTime"));
        colDM.setCellValueFactory(new PropertyValueFactory<>("mTime"));

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    txtidM.setText(String.valueOf(selectedProduct.getId()));
                    txtNomM.setText(selectedProduct.getNom());
                    txtDescriptionM.setText(selectedProduct.getDesc());
                    txtQtM.setText(String.valueOf(selectedProduct.getQt()));
                    txtPrixM.setText(String.valueOf(selectedProduct.getPrice()));
                    txtImageM.setText(selectedProduct.getImage());

                    txtidS.setText(String.valueOf(selectedProduct.getId()));
                    System.out.println("Selected Product ID: " + selectedProduct.getId());
                }
            }
        });
        tableView.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Product clickedProduct = row.getItem();
                    fillModifierFields(clickedProduct);
                    prod.getSelectionModel().select(tabMod);
                }
            });
            return row;
        });

    }
    private void fillModifierFields(Product product) {
        txtidM.setText(String.valueOf(product.getId()));
        txtNomM.setText(product.getNom());
        txtDescriptionM.setText(product.getDesc());
        txtQtM.setText(String.valueOf(product.getQt()));
        txtPrixM.setText(String.valueOf(product.getPrice()));
        txtImageM.setText(product.getImage());
    }

    @FXML
    void categoryComboBox(ActionEvent event) {
        String selectedCategoryName = comboCategory.getSelectionModel().getSelectedItem();


    }
    @FXML
    void OncategoryInter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/category.fxml"));
            Parent root = loader.load();
            GestionCategory catController = loader.getController();
            catController.setParentController(this);
            Stage newStage = new Stage();
            newStage.setTitle("Gestion des catégories");
            newStage.setScene(new Scene(root));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshComboBoxCat() throws SQLException {
        List<Category> categories = serviceCategory.afficher();
        ObservableList<String> categoryNames = FXCollections.observableArrayList();

        for (Category category : categories) {
            categoryNames.add(category.getNom());
        }
        comboCategory.setItems(categoryNames);
    }

    @FXML
    void onbuttonSelectimg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez une image");

        // Filter only image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Open the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(null); // Use your Stage if needed

        if (selectedFile != null) {
            txtImage.setText(selectedFile.toURI().toString());
        }

    }    @FXML
    void onbuttonModifyimg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez une image");

        // Filter only image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Open the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(null); // Use your Stage if needed

        if (selectedFile != null) {
            txtImageM.setText(selectedFile.getAbsolutePath());
        }

    }
    @FXML
    void btnAffichageModern(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/userProduct.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Produits - Vue Utilisateur");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

