package Controllers;

import entities.User;
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
    private TableView<User> readUserTAbleview;
    @FXML
    private TextField txtemail;
    @FXML
    private TextField txtlastname;
    @FXML
    private TextField txtname;

    @FXML
    public void initialize() {
        txtname.textProperty().addListener((observable, oldValue, newValue) -> combinedSearch());
        txtlastname.textProperty().addListener((observable, oldValue, newValue) -> combinedSearch());
        txtemail.textProperty().addListener((observable, oldValue, newValue) -> combinedSearch());
    }

    public void combinedSearch() {
        String name = txtname.getText().trim();
        String lastname = txtlastname.getText().trim();
        String email = txtemail.getText().trim();

        try {
            ArrayList<User> users = new ArrayList<>();

            if (!email.isEmpty()) {
                users = serviceUser.searchUserByEmail(email);
            } else if (!name.isEmpty() && !lastname.isEmpty()) {
                users = serviceUser.searchUserByNameLastName(name, lastname);
            } else if (!name.isEmpty()) {
                users = serviceUser.searchUserByName(name);
            } else if (!lastname.isEmpty()) {
                users = serviceUser.searchUserByLastName(lastname);
            } else {
                return; // no criteria entered
            }

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
        String email = txtemail.getText().trim();
        String lastname = txtlastname.getText().trim();
        String name = txtname.getText().trim();

        if (email.isEmpty() && lastname.isEmpty() && name.isEmpty()) {
            showErrorAlert("Please enter a search criteria");
            return;
        }

        if (!email.isEmpty()) {
            try {
                ArrayList<User> users = serviceUser.searchUserByEmail(email);
                showUsers(users);
            } catch (SQLException e) {
                showErrorAlert(e.getMessage());
            }
        } else {
            combinedSearch();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
