package controllers;

import entities.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceProduct;

import java.sql.Date;
import java.sql.SQLException;

public class AjouterProduct {

    @FXML
    private DatePicker DC;

    @FXML
    private DatePicker DM;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtImage;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrix;

    @FXML
    private TextField txtQt;


    @FXML
    void btnAjouterProd(ActionEvent event) {

        try {
            String nom = txtNom.getText();
            String description = txtDescription.getText();
            int quantite = Integer.parseInt(txtQt.getText());
            float prix = Float.parseFloat(txtPrix.getText());
            String image = txtImage.getText();

            Date dateCreation = Date.valueOf(DC.getValue());
            Date dateModification = Date.valueOf(DM.getValue());
            
            Product product = new Product(nom,description,quantite, prix, image, dateCreation,dateModification);
            ServiceProduct serviceProduct = new ServiceProduct();
            serviceProduct.ajouter(product);
            System.out.println("Product Ajouter");
        }catch (SQLException e){
            System.out.println(e);
        }
    }
}

