package services;

import entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SmsService;

public class ServiceCart {
    private static final ObservableList<Product> cartItems = FXCollections.observableArrayList();
    private static String userPhoneNumber = "+16073043341"; // À remplacer par le numéro de l'utilisateur connecté

    public static void addToCart(Product product) {
        cartItems.add(product);
        
        // Envoyer une notification SMS
        SmsService.sendCartNotification(userPhoneNumber, product.getNom(), product.getPrice());
    }

    public static ObservableList<Product> getCartItems() {
        return cartItems;
    }

    public static void clearCart() {
        cartItems.clear();
    }
    
    // Méthode pour définir le numéro de téléphone de l'utilisateur
    public static void setUserPhoneNumber(String phoneNumber) {
        userPhoneNumber = phoneNumber;
    }
}
