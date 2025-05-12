package controllers;

import entities.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.ServiceCart;
import services.ServiceProduct;
import controllers.CartView;
import utils.Cart;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserProduct {

    @FXML private FlowPane flowPaneProducts;

    private final ServiceProduct serviceProduct = new ServiceProduct();
    @FXML private Button btnViewCart;


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
    public void initialize() {
        try {
            List<Product> products = serviceProduct.afficher();

            for (Product product : products) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCard.fxml"));
                Node card = loader.load();

                ProductCard controller = loader.getController();
                controller.setProduct(product);

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

            Stage stage = new Stage();
            stage.setTitle(product.getNom());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}