package controllers;

import entities.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.ServiceCart;
import services.ServiceFavoris;
import services.ServiceProduct;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserProduct {

    @FXML private FlowPane flowPaneProducts;
    @FXML private Button btnViewCart;
    @FXML private Button btnViewFavoris;

    private final ServiceProduct serviceProduct = new ServiceProduct();

    @FXML
    private void handleViewCart() {
        List<Product> cartItems = ServiceCart.getCartItems();

        System.out.println("Opening cart... current items: ");
        for (Product p : ServiceCart.getCartItems()) {
            System.out.println("- " + p.getNom() + " | Price: " + p.getPrice());
        }
        CartView cartView = new CartView();
        cartView.showCart(new Stage(), ServiceCart.getCartItems());
    }
    
    @FXML
    private void handleViewFavoris() {
        List<Product> favorisItems = ServiceFavoris.getFavorisItems();
        
        System.out.println("Opening favoris... current items: ");
        for (Product p : favorisItems) {
            System.out.println("- " + p.getNom() + " | Price: " + p.getPrice());
        }
        FavorisView favorisView = new FavorisView();
        favorisView.showFavoris(new Stage(), favorisItems);
    }

    @FXML
    public void initialize() {
        try {
            List<Product> products = serviceProduct.afficher();

            for (Product product : products) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCard.fxml"));
                Node card = loader.load();

                ProductCard controller = loader.getController();
                controller.setProduct(product);
                controller.setFavoriteStatus(ServiceFavoris.isFavorite(product));
                
                // Configure le gestionnaire d'événements pour ajouter aux favoris
                controller.setOnToggleFavorite(() -> {
                    if (ServiceFavoris.isFavorite(product)) {
                        ServiceFavoris.removeFromFavoris(product);
                    } else {
                        ServiceFavoris.addToFavoris(product);
                    }
                    controller.setFavoriteStatus(ServiceFavoris.isFavorite(product));
                });

                // Add double-click event
                card.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        showProductDetails(product);
                    }
                });

                flowPaneProducts.getChildren().add(card);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showProductDetails(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductDetails.fxml"));
            Parent root = loader.load();

            ProductDetails controller = loader.getController();
            controller.setProduct(product);
            controller.setFavoriteStatus(ServiceFavoris.isFavorite(product));

            Stage stage = new Stage();
            stage.setTitle(product.getNom());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void configurePhoneNumber() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Configuration SMS");
        dialog.setHeaderText("Entrez votre numéro de téléphone");
        dialog.setContentText("Numéro (format international, ex: +33612345678):");
        
        dialog.showAndWait().ifPresent(phoneNumber -> {
            if (phoneNumber.matches("\\+[0-9]{10,15}")) {
                ServiceCart.setUserPhoneNumber(phoneNumber);
                showAlert(Alert.AlertType.INFORMATION, "Succès",
                         "Votre numéro a été enregistré. Vous recevrez des SMS pour vos achats.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                         "Format de numéro invalide. Utilisez le format international (+33612345678)");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
