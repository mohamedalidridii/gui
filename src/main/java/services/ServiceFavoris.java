package services;

import entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServiceFavoris {
    private static final ObservableList<Product> favorisItems = FXCollections.observableArrayList();

    public static void addToFavoris(Product product) {
        // Vérifier si le produit existe déjà dans les favoris
        if (!favorisItems.contains(product)) {
            favorisItems.add(product);
        }
    }

    public static void removeFromFavoris(Product product) {
        favorisItems.remove(product);
    }

    public static boolean isFavorite(Product product) {
        return favorisItems.contains(product);
    }

    public static ObservableList<Product> getFavorisItems() {
        return favorisItems;
    }
}