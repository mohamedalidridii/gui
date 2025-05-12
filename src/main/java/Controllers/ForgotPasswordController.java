package Controllers;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.ServiceSeller;
import services.ServiceUser;
import utils.EmailSender;

import java.io.IOException;
import java.sql.SQLException;

public class ForgotPasswordController {
    ServiceUser serviceUser=new ServiceSeller();
    String validcode="";
    String email="";




    @FXML
    private Label codeErrorLabel;

    @FXML
    private TextField codeField;

    @FXML
    private VBox codePane;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private TextField emailField;

    @FXML
    private VBox emailPane;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private ProgressBar strengthBar;

    @FXML
    private Label strengthLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private VBox resetPane;

    @FXML
    private Label successLabel;


    @FXML
    void initialize() {



        Tooltip tooltip = new Tooltip("Password must be at least 8 characters\nand contain uppercase, lowercase, digit, and special character.");
        Tooltip.install(newPasswordField, tooltip);
        tooltip.setShowDelay(Duration.seconds(0.5));
        Platform.runLater(() -> Tooltip.install(newPasswordField, tooltip));

        newPasswordField.textProperty().addListener((observable, oldValue, newValue)  -> {
            updatePasswordStrength(newValue);

        });
    }
    private void updatePasswordStrength(String password) {
        int strength=passwordStrengthScore(password);
        double progress=strength/4.0;
        strengthBar.setProgress(progress);
        switch (strength) {
            case 0,1 ->{
                strengthLabel.setText("weak");
                strengthLabel.setStyle("-fx-text-fill: red;");
                strengthBar.setStyle("-fx-accent: red;");
            }
            case 2 -> {
                strengthLabel.setText("Moderate");
                strengthLabel.setStyle("-fx-text-fill: orange;");
                strengthBar.setStyle("-fx-accent: orange;");
            }
            case 3 -> {
                strengthLabel.setText("Strong");
                strengthLabel.setStyle("-fx-text-fill: blue;");
                strengthBar.setStyle("-fx-accent: blue;");
            }
            case 4 -> {
                strengthLabel.setText("Very Strong");
                strengthLabel.setStyle("-fx-text-fill: green;");
                strengthBar.setStyle("-fx-accent: green;");
            }
        }
    }





    public int passwordStrengthScore(String password) {
        int score = 0;
        if (password.length() > 8) score++;
        if (password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) score++;
        return score;
    }




        @FXML
    void handleChangePassword(ActionEvent event) {
        String password = newPasswordField.getText();
        String confirmPasswordFieldText = confirmPasswordField.getText();

        if(passwordStrengthScore(password)<3){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Enter a valid password");
            alert.showAndWait();
            return;
        }
        if(!confirmPasswordFieldText.equals(password)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match");
            alert.showAndWait();
            return;
        }
        try{
            serviceUser.changePassword(email,password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Password changed");
            alert.showAndWait();
            return;
        }
        catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }


    }

    @FXML
    void handleSendCode(ActionEvent event) {
        email = emailField.getText();
        boolean exists=false;
        try{
            exists=serviceUser.emailExists(email);
            System.out.println(1);
        }
        catch(SQLException e) {
            emailErrorLabel.setText(e.getMessage());
        }
        if (exists){
            try {
                validcode=EmailSender.sendEmail(email);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Enter the 6 digit code sent to "+email);
                alert.showAndWait();

                emailPane.setVisible(false);
                emailPane.setManaged(false);

                codePane.setVisible(true);
                codePane.setManaged(true);

            } catch (MessagingException e) {
                emailErrorLabel.setText(e.getMessage());
            }


        }


    }

    @FXML
    void handleVerifyCode(ActionEvent event) {

        String enteredCode=codeField.getText();
        if(enteredCode.length()==0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid code");
            alert.showAndWait();
            return;
        }
        if (validcode.equals(enteredCode)){

            codePane.setVisible(false);
            codePane.setManaged(false);

            resetPane.setVisible(true);
            resetPane.setManaged(true);
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml")); // Adjust path if needed
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
