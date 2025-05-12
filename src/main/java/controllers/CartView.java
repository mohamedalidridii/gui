package controllers;

import entities.Product;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class CartView {

    public void showCart(Stage primaryStage, List<Product> cartItems) {
        // Root layout
        BorderPane root = new BorderPane();

        // Top: Title
        Label title = new Label("🛒 Your Cart");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(10));
        root.setTop(top);

        // Center: List of products
        VBox cartBox = new VBox(10);
        cartBox.setPadding(new Insets(10));

        if (cartItems.isEmpty()) {
            cartBox.getChildren().add(new Label("Your cart is empty."));
        } else {
            for (Product product : cartItems) {
                HBox productRow = new HBox(10);
                Label nameLabel = new Label("🧾 " + product.getNom());
                Label priceLabel = new Label("💵 " + product.getPrice() + " DT");
                Label idLabel = new Label("🆔 ID: " + product.getId());

                productRow.getChildren().addAll(nameLabel, priceLabel, idLabel);
                cartBox.getChildren().add(productRow);
            }
        }

        ScrollPane scrollPane = new ScrollPane(cartBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        // Bottom: Buttons
        Button checkoutBtn = new Button("✅ Proceed to Checkout");
        Button clearBtn = new Button("🗑️ Clear Cart");

        HBox bottom = new HBox(10, checkoutBtn, clearBtn);
        bottom.setPadding(new Insets(10));
        bottom.setStyle("-fx-alignment: center;");
        root.setBottom(bottom);

        // Button logic
        checkoutBtn.setOnAction(e -> {
            System.out.println("Proceeding to checkout...");
            // Add your checkout logic here
        });

        clearBtn.setOnAction(e -> {
            cartItems.clear(); // or CartService.clearCart()
            showCart(primaryStage, cartItems); // Refresh the view
        });

        // Scene setup
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Cart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}