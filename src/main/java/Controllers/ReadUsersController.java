package Controllers;

import entities.Admin;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.AccessLevel;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReadUsersController {

    @FXML
    private TableColumn<User,Integer> cage;

    @FXML
    private TableColumn<User, LocalDateTime> ccreatedat;

    @FXML
    private TableColumn<User,String> cemail;

    @FXML
    private TableColumn<User, Integer> cid;

    @FXML
    private TableColumn<User,LocalDateTime> clastlogin;

    @FXML
    private TableColumn<User,String> clastname;

    @FXML
    private TableColumn<User,String> clocation;

    @FXML
    private TableColumn<User,String> cname;

    @FXML
    private TableColumn<User,Integer> cnumber;

    @FXML
    private TableColumn<User,String> cpassword;


    @FXML
    private TableColumn<User,String> ctype;

    @FXML
    private TableView<User> readUserTAbleview;

    Admin a =new Admin (23,"e",21,"Male","ddffssfd77@gmail",20155144,"Ghazouani","kira1fas12233","Bali", AccessLevel.SuperAdmin);


    @FXML
    private void initialize() {
        ObservableList<User> list = FXCollections.observableArrayList();
        try {
            list.addAll(a.readAll());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

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



}
