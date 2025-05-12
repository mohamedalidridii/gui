package Controllers;

import entities.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceSeller;
import services.ServiceUser;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SearchUsersController {
    int removeuser=0;
    User user;

    private final ServiceUser serviceUser = new ServiceSeller();

    @FXML
    private TableColumn<User, Integer> cage;
    @FXML
    private TableColumn<User, LocalDateTime> ccreatedat;
    @FXML
    private TableColumn<User, String> cemail;
    @FXML
    private TableColumn<User, Integer> cid;
    @FXML
    private TableColumn<User, LocalDateTime> clastlogin;
    @FXML
    private TableColumn<User, String> clastname;
    @FXML
    private TableColumn<User, String> clocation;
    @FXML
    private TableColumn<User, String> cname;
    @FXML
    private TableColumn<User, Integer> cnumber;
    @FXML
    private TableColumn<User, String> cpassword;
    @FXML
    private TableColumn<User, String> ctype;



    @FXML
    private TextField txtnumber;


    @FXML
    private TextField txtlocation;

    @FXML
    private TableView<User> readUserTAbleview;
    @FXML
    private TextField txtemail;
    @FXML
    private TextField txtlastname;
    @FXML
    private TextField txtname;

    @FXML
    public void initialize() {
        txtname.textProperty().addListener((observable, oldValue, newValue) ->{
                if(!newValue.isEmpty()) combinedSearch();});
        txtlastname.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()) combinedSearch();});
        txtemail.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()) combinedSearch();});
        txtnumber.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d+") && !newValue.isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter a valid number");
                    alert.showAndWait();
                    txtnumber.clear();
                }
                else combinedSearch();

        });
        txtlocation.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()) combinedSearch();});


        readUserTAbleview.setRowFactory(userTableView -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    user = row.getItem();

                }

            });
            return row;
                });



    }

    public void combinedSearch() {
        String name = txtname.getText().trim();
        String lastName = txtlastname.getText().trim();
        String email = txtemail.getText().trim();
        String number = txtnumber.getText().trim();
        String location = txtlocation.getText().trim();


        try {
            ArrayList<User> users = new ArrayList<>();
            users=serviceUser.combinedSearch(name,lastName ,location, email ,number);


            showUsers(users);
        } catch (SQLException e) {
            showErrorAlert(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void showUsers(ArrayList<User> a) {
        ObservableList<User> list = FXCollections.observableArrayList(a);
        readUserTAbleview.setItems(list);
        cid.setCellValueFactory(new PropertyValueFactory<>("id"));
        cname.setCellValueFactory(new PropertyValueFactory<>("name"));
        clastname.setCellValueFactory(new PropertyValueFactory<>("prename"));
        cage.setCellValueFactory(new PropertyValueFactory<>("age"));
        cnumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        cpassword.setCellValueFactory(new PropertyValueFactory<>("passwordHashed"));
        cemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        clastlogin.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
        ccreatedat.setCellValueFactory(new PropertyValueFactory<>("createdat"));
        clocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        ctype.setCellValueFactory(new PropertyValueFactory<>("role"));
        System.out.println("Users loaded: " + list.size());
    }

    @FXML
    void searchbutton(ActionEvent event) {
        String name = txtname.getText().trim();
        String lastName = txtlastname.getText().trim();
        String email = txtemail.getText().trim();
        String number = txtnumber.getText().trim();
        String location = txtlocation.getText().trim();

        if(name.isEmpty() && lastName.isEmpty() && email.isEmpty() && number.isEmpty() && location.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid Searching Criteria");
            alert.showAndWait();
            return;
        }


        try {
            ArrayList<User> users = new ArrayList<>();
            users=serviceUser.combinedSearch(name,lastName ,location, email ,number);


            showUsers(users);
        } catch (SQLException e) {
            showErrorAlert(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void removeUser(ActionEvent actionEvent) {
        try {
            serviceUser.deleteUser(user.getId());
            combinedSearch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
