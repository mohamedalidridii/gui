package controllers;

import entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.io.File;

public class ProductCard {
    private Runnable onAddToCart;

    @FXML private ImageView imgProduct;
    @FXML private Label lblName;
    @FXML private Label lblPrice;
    public void setOnAddToCart(Runnable onAddToCart) {
        this.onAddToCart = onAddToCart;
    }

    @FXML
    void handleAddToCart(ActionEvent event) {
        if (onAddToCart != null) {
            onAddToCart.run();
        }
    }
    public void setProduct(Product product) {
        lblName.setText(product.getNom());
        lblPrice.setText(String.format("%.2f DT", product.getPrice()));

        // Load image from file path
        File file = new File(product.getImage());
        if (file.exists()) {
            imgProduct.setImage(new Image(file.toURI().toString()));
        } else {
            System.out.println("Image not found: " + product.getImage());
        }
    }
}