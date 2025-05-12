package services;

import entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServiceCart {
    private static final ObservableList<Product> cartItems = FXCollections.observableArrayList();

    public static void addToCart(Product product) {
        cartItems.add(product);
    }

    public static ObservableList<Product> getCartItems() {
        return cartItems;
    }

    public static void clearCart() {
        cartItems.clear();
    }
}