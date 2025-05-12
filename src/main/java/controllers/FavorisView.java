package controllers;

import entities.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceFavoris;

import java.io.IOException;
import java.util.List;

public class FavorisView {
    @FXML private FlowPane flowPaneFavoris;
    @FXML private Button btnClearFavoris;

    public void showFavoris(Stage stage, List<Product> favorisItems) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/favorisView.fxml"));
            VBox root = loader.load();
            
            FavorisView controller = loader.getController();
            controller.displayFavorisItems(favorisItems);
            
            stage.setTitle("Mes Favoris");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void displayFavorisItems(List<Product> favorisItems) {
        try {
            for (Product product : favorisItems) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCard.fxml"));
                Node card = loader.load();
                
                ProductCard controller = loader.getController();
                controller.setProduct(product);
                controller.setFavoriteStatus(true);
                
                controller.setOnToggleFavorite(() -> {
                    ServiceFavoris.removeFromFavoris(product);
                    flowPaneFavoris.getChildren().remove(card);
                });
                
                flowPaneFavoris.getChildren().add(card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleClearFavoris() {
        ServiceFavoris.getFavorisItems().clear();
        flowPaneFavoris.getChildren().clear();
    }
}