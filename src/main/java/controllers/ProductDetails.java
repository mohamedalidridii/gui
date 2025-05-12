package controllers;

import entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import services.ServiceCart;
import services.ServiceFavoris;

public class ProductDetails {

    @FXML private ImageView imgProduct;
    @FXML private Label lblName;
    @FXML private Label lblPrice;
    @FXML private TextArea txtDescription;
    @FXML private Button btnAddToCart;
    @FXML private Button btnFavorite;
    
    private Product currentProduct;

    @FXML
    private void initialize() {
        btnAddToCart.setOnAction(e -> {
            ServiceCart.addToCart(currentProduct);
            System.out.println("Added to cart: " + currentProduct.getNom());
        });
        
        btnFavorite.setOnAction(e -> {
            if (ServiceFavoris.isFavorite(currentProduct)) {
                ServiceFavoris.removeFromFavoris(currentProduct);
            } else {
                ServiceFavoris.addToFavoris(currentProduct);
            }
            setFavoriteStatus(ServiceFavoris.isFavorite(currentProduct));
        });
    }

    public void setFavoriteStatus(boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setText("★ Retirer des favoris");
        } else {
            btnFavorite.setText("☆ Ajouter aux favoris");
        }
    }

    public void setProduct(Product product) {
        this.currentProduct = product;
        
        lblName.setText(product.getNom());
        lblPrice.setText(String.format("%.2f DT", product.getPrice()));
        txtDescription.setText(product.getDesc());

        File file = new File(product.getImage());
        if (file.exists()) {
            imgProduct.setImage(new Image(file.toURI().toString()));
        }
        
        btnAddToCart.setOnAction(e -> {
            System.out.println("Before adding — Cart size: " + ServiceCart.getCartItems().size());
            ServiceCart.addToCart(product);
            System.out.println("Added to cart: " + product.getNom());
            System.out.println("After adding — Cart size: " + ServiceCart.getCartItems().size());
        });
    }
}