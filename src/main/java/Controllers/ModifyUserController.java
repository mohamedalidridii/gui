package Controllers;

import entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import services.AccessLevel;
import services.ServiceSeller;
import services.ServiceUser;

import java.sql.SQLException;

public class ModifyUserController {
    private final ServiceUser serviceUser=new ServiceSeller();
    public int updateid;


    @FXML
    private Label passportidlabel;
    @FXML
    private Label storenamelabel;


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
    private TextField txtemail;

    @FXML
    private TextField txtlastname;

    @FXML
    private TextField txtlocation;

    @FXML
    private TextField txtname;

    @FXML
    private TextField txtnumber;

    @FXML
    private TextField txtpassport;

    @FXML
    private TextField txtpassword;


    @FXML
    private TextField txtconfirmpassword;

    @FXML
    private ProgressBar strengthBar;

    @FXML
    private Label strengthLabel;

    @FXML
    private TextField txtstorename;

    @FXML
    private ToggleGroup userType;


    @FXML
    private ToggleGroup usersex;

    @FXML
    private Button Searchbutton;

    @FXML
    private TextField txtid;

    @FXML
    private void initialize() {

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 18);
        spinbage.setValueFactory(valueFactory);
        spinbage.getValueFactory().setValue(18);

        userType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue ==radiotypetraveller) {
                txtpassport.setVisible(true);
                txtpassport.setManaged(true);
                passportidlabel.setVisible(true);
                txtstorename.setVisible(false);
                txtstorename.setManaged(false);
                storenamelabel.setVisible(false);
            } else if (newValue == radiotypeseller) {
                txtstorename.setVisible(true);
                txtstorename.setManaged(true);
                storenamelabel.setVisible(true);

                txtpassport.setVisible(false);
                txtpassport.setManaged(false);
                passportidlabel.setVisible(false);

            }
        });

        Tooltip tooltip = new Tooltip("Password must be at least 8 characters\nand contain uppercase, lowercase, digit, and special character.");
        Tooltip.install(txtpassword, tooltip);
        tooltip.setShowDelay(Duration.seconds(0.5));

        // Set the Tooltip to the password field
        Platform.runLater(() -> Tooltip.install(txtpassword, tooltip));


        Tooltip emailTooltip=new Tooltip("Email must be like" +"\n"+ "example@example.com");
        Tooltip.install(txtemail, emailTooltip);
        emailTooltip.setShowDelay(Duration.seconds(0.5));
        Platform.runLater(() -> Tooltip.install(txtemail, emailTooltip));



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
    void Searchbutton(ActionEvent event) {
        int id =Integer.parseInt(txtid.getText());
        User user= null;
        try {
            user = serviceUser.searchUserById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User not found");
            alert.showAndWait();
            return;
        }

        txtname.setText(user.getName());
        updateid = user.getId();
        txtlastname.setText(user.getPrename());
        txtlocation.setText(user.getLocation());
        txtemail.setText(user.getEmail());
        txtnumber.setText(String.valueOf(user.getNumber()));
        spinbage.getValueFactory().setValue(user.getAge());
        if (user.getGender().equals("Male")) radioMale.setSelected(true);
        else radiofemale.setSelected(true);
        if (user.getRole().equals(Role.Seller)) {
            radiotypeseller.setSelected(true);
            txtstorename.setVisible(true);
            txtstorename.setManaged(true);
            txtstorename.setText(((Seller)user).getStoreName());
        }
        else if(user.getRole().equals(Role.Traveller)) {
            radiotypetraveller.setSelected(true);

            txtpassport.setText(((Traveller)user).getPassportNumber());
        }

    }


    @FXML
    void modifyuserbutton(ActionEvent event) {
        RadioButton sex = (RadioButton) usersex.getSelectedToggle();
        String selectedSex="";
        User newUser = null;
        if ( sex!=null){
            if (sex==radioMale){
                selectedSex="Male";
            }
            else if (sex==radiofemale){
                selectedSex="Female";
            }
        }



        String name = txtname.getText();
        String lastname = txtlastname.getText();
        String location = txtlocation.getText();
        String email = txtemail.getText();
        String password = txtpassword.getText();
        String confirmPassword = txtconfirmpassword.getText();
        int age =spinbage.getValue();
        int number=0;

        if(name.isEmpty() || lastname.isEmpty() || location.isEmpty() || email.isEmpty() || password.isEmpty() ||txtnumber.getText().length()==0
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
        if (name.matches(".*[1-9].*")||lastname.matches(".*[1-9].*")){
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Name and Prename cannot contain numbers");
            alert.showAndWait();
            return;
        }

        if (name.matches(".* .*")||lastname.matches(".* .*")){
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Name and Prename cannot contain spaces");
            alert.showAndWait();
            return;
        }
        if (email.trim().isEmpty() || email.contains(" ") || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Email");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid email address without spaces.");
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




        RadioButton usertype = (RadioButton) userType.getSelectedToggle();
        if (usertype==radiotypeseller){
            String storename = txtstorename.getText();
            if(storename.isEmpty() || storename.matches(".*[1-9].*")){
                Alert alert =new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Store name can only contain characters");
                alert.showAndWait();
                return;
            }
            newUser =new Seller(updateid,name,age,selectedSex,email,number,lastname,password,lastname,storename);

        }
        else if (usertype==radiotypetraveller){
            String passport = txtpassport.getText();
            if (passport.isEmpty()){
                Alert alert =new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Password can not be empty");
                alert.showAndWait();
                return;
            }
            newUser=new Traveller(updateid,name,age,selectedSex,email,number,lastname,password,location,passport);
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
            admin.updateUser(newUser);
        } catch (SQLException e) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }


    }

}
