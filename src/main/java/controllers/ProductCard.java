package controllers;

import entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.io.File;

public class ProductCard {
    private Runnable onAddToCart;
    private Runnable onToggleFavorite;
    
    @FXML private ImageView imgProduct;
    @FXML private Label lblName;
    @FXML private Label lblPrice;
    @FXML private Button btnFavorite;
    
    public void setOnAddToCart(Runnable onAddToCart) {
        this.onAddToCart = onAddToCart;
    }
    
    public void setOnToggleFavorite(Runnable onToggleFavorite) {
        this.onToggleFavorite = onToggleFavorite;
    }
    
    public void setFavoriteStatus(boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setText("★"); // Étoile pleine
        } else {
            btnFavorite.setText("☆"); // Étoile vide
        }
    }

    @FXML
    void handleAddToCart(ActionEvent event) {
        if (onAddToCart != null) {
            onAddToCart.run();
        }
    }
    
    @FXML
    void handleToggleFavorite(javafx.event.ActionEvent event) {
        if (onToggleFavorite != null) {
            onToggleFavorite.run();
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