package controllers;

import entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ProductDetails {

    @FXML private ImageView imgProduct;
    @FXML private Label lblName;
    @FXML private Label lblPrice;
    @FXML private TextArea txtDescription;
    @FXML private Button btnAddToCart;

    public void setProduct(Product product) {
        lblName.setText(product.getNom());
        lblPrice.setText(String.format("%.2f DT", product.getPrice()));
        txtDescription.setText(product.getDesc());

        File file = new File(product.getImage());
        if (file.exists()) {
            imgProduct.setImage(new Image(file.toURI().toString()));
        }
    }
}