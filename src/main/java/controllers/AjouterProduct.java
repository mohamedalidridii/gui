package controllers;

import entities.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import services.ServiceProduct;

import java.sql.Date;
import java.sql.SQLException;

public class AjouterProduct {

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
    private TextField txtid;

    @FXML
    private TextField txtidS;

    @FXML
    void btnAjouterProd(ActionEvent event) {

        try {
            String nom = txtNom.getText();
            String description = txtDescription.getText();
            int quantite = Integer.parseInt(txtQt.getText());
            float prix = Float.parseFloat(txtPrix.getText());
            String image = txtImage.getText();

            Date dateCreation = Date.valueOf(java.time.LocalDate.now());
            Date dateModification = Date.valueOf(java.time.LocalDate.now());
            
            Product product = new Product(nom,description,quantite, prix, image, dateCreation,dateModification);
            ServiceProduct serviceProduct = new ServiceProduct();
            serviceProduct.ajouter(product);
            System.out.println("Product Ajouter");
        }catch (SQLException e){
            System.out.println(e);
        }
    }
    @FXML
    void btnModifierProd(ActionEvent event) {
        try {
            // Check if the important fields are not empty
            if (txtid.getText().isEmpty() || txtNomM.getText().isEmpty() || txtDescriptionM.getText().isEmpty() ||
                    txtQtM.getText().isEmpty() || txtPrixM.getText().isEmpty() || txtImageM.getText().isEmpty()) {
                System.out.println("Please fill all fields!");
                return;
            }
            int id = Integer.parseInt(txtid.getText());
            String nom = txtNomM.getText();
            String description = txtDescriptionM.getText();
            int quantite = Integer.parseInt(txtQtM.getText());
            float prix = Float.parseFloat(txtPrixM.getText());
            String image = txtImageM.getText();

            Date dateCreation = Date.valueOf(java.time.LocalDate.now());
            Date dateModification = Date.valueOf(java.time.LocalDate.now());


            Product productMod = new Product(nom,description,quantite, prix, image, dateCreation,dateModification);
            productMod.setId(id);
            System.out.println(productMod);
            ServiceProduct serviceProductMod = new ServiceProduct();
            System.out.println(id);
            serviceProductMod.modifier(productMod);

            System.out.println("Product modifier");

        }catch (SQLException e){
            System.out.println(e);
        }
    }
    @FXML
    void btnSuppProd(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtidS.getText());
            Product prodSupp = new Product();
            prodSupp.setId(id);
            ServiceProduct serviceProductSupp = new ServiceProduct();
            serviceProductSupp.supprimer(prodSupp);
    }catch (SQLException e){
        System.out.println(e);
    }
    }

}

