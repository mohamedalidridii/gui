package Controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import services.ServiceSeller;
import services.ServiceUser;

public class DeleteUserController {

    private final ServiceUser serviceUser = new ServiceSeller();

    @FXML
    private TextField txid;

    @FXML
    private Button deleteuser;

    @FXML
    private Label feedbackLabel;

    @FXML
    void deleteUser(ActionEvent event) {
        feedbackLabel.setText(""); // Clear previous messages
        String idText = txid.getText().trim();
        int id;

        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric ID.");
            return;
        }

        try {
            User user = serviceUser.searchUserById(id);
            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "User Not Found", "No user found with ID: " + id);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to delete: " + user.getName() + " " + user.getPrename() + "?");

            ButtonType result = confirm.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                serviceUser.deleteUser(id);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                feedbackLabel.setText("User deleted.");
                clearFields(null);
            } else {
                feedbackLabel.setText("Deletion cancelled.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        txid.clear();
        feedbackLabel.setText("");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
