package Controllers;

import entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;


public class AddUserWithGoogleController {
    private UserDraft userDraft;
    String firstName;
    String lastName;
    String email;

    @FXML
    private Button adduserbutton;

    @FXML
    private Label passportidlabel;

    @FXML
    private RadioButton radioMale;

    @FXML
    private RadioButton radiofemale;

    @FXML
    private RadioButton radiotypeseller;

    @FXML
    private RadioButton radiotypetraveller;

    @FXML
    private Spinner<Integer> spinbage;

    @FXML
    private Label storenamelabel;

    @FXML
    private ProgressBar strengthBar;

    @FXML
    private Label strengthLabel;

    @FXML
    private PasswordField txtconfirmpassword;

    @FXML
    private TextField txtlocation;

    @FXML
    private TextField txtnumber;

    @FXML
    private TextField txtpassport;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private TextField txtstorename;

    @FXML
    private ToggleGroup userType;

    @FXML
    private ToggleGroup usersex;

    @FXML
    private void initialize() {
        SpinnerValueFactory<Integer> valueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1);
        spinbage.setValueFactory(valueFactory);
        spinbage.getValueFactory().setValue(18);

        userType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == radiotypetraveller) {
                txtpassport.setVisible(true);
                txtpassport.setManaged(true);
                passportidlabel.setVisible(true);
                txtstorename.setVisible(false);
                txtstorename.setManaged(false);

            }
            else if (newValue == radiotypeseller) {
                txtpassport.setVisible(false);
                txtpassport.setManaged(false);
                passportidlabel.setVisible(false);
                txtstorename.setVisible(true);
                txtstorename.setManaged(true);

            }
        });

        Tooltip tooltip = new Tooltip("Password must be at least 8 characters\nand contain uppercase, lowercase, digit, and special character.");
        Tooltip.install(txtpassword, tooltip);
        tooltip.setShowDelay(Duration.seconds(0.5));
        Platform.runLater(() -> {Tooltip.install(txtpassword, tooltip);});


        txtpassword.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue);
        });


    }

    private void updatePasswordStrength(String password) {
        int strength = getPasswordStrengthScore(password);
        double progress = strength / 4.0;
        strengthBar.setProgress(progress);

        switch (strength) {
            case 0, 1 -> {
                strengthLabel.setText("Weak");
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


    public int getPasswordStrengthScore(String password){
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) score++;
        return score;
    }

    @FXML
    void adduserbutton(ActionEvent event) {
        User user=null;
        int age =spinbage.getValue();
        String selectedSex="";
        if (radioMale.isSelected()) {
            selectedSex="male";

        }
        else if (radiofemale.isSelected()) {
            selectedSex="female";
        }
        int number =0;

        String location = txtlocation.getText();
        String password = txtpassword.getText();
        String confirmPassword = txtconfirmpassword.getText();


        if(location.isEmpty() ||  password.isEmpty() ||txtnumber.getText().length()==0
                ||age==0 ||selectedSex.isEmpty())
        {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
            return;
        }

        if (txtnumber.getText().matches("\\d+") && txtnumber.getText().length()==8 ) {
            number= Integer.parseInt(txtnumber.getText());

        }
        else{
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Number can only contain 8 digits");
            alert.showAndWait();
            return;
        }





        if (getPasswordStrengthScore(password)<3){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Password is too weak");
            alert.showAndWait();
            return;
        }

        if(!password.equals(confirmPassword)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match");
            alert.showAndWait();
            return;

        }



        if(radiotypetraveller.isSelected()){
            String passport = txtpassword.getText();
            if(passport.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Enter Your Passport Id");
                alert.showAndWait();
                return;
            }
            //newUser=new Traveller(15,name,age,selectedSex,email,number,lastname,password,location,passport);

             user=new Traveller(15,firstName,age,selectedSex,email,number,lastName,password,location,passport);
        } else if (radiotypeseller.isSelected()) {
            String storename = txtstorename.getText();
            if(storename.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Enter Your Storename");
                alert.showAndWait();
                return;
            }
             user=new Seller(15,firstName,age,selectedSex,email,number,lastName,password,location,storename);

        }
        else {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Type");
            alert.showAndWait();
            return;
        }
        try {
            Admin admin = new Admin(23,"e",21,"Male","ddfffd77@gmail",20155144,"Ghazouani","kira1fas12233","Bali", AccessLevel.Manager);
            admin.createUser(user);
            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User created successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }


    }


    public void setUserDraft(UserDraft userDraft) {
        this.userDraft = userDraft;

        if(userDraft!=null)
        {
            firstName=userDraft.getFirstName();
            lastName=userDraft.getLastName();
            email=userDraft.getEmail();

        }

    }
    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml")); // Adjust path if needed
            Parent root = loader.load();
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
