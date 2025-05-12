package Controllers;

import com.sun.net.httpserver.HttpServer;
import entities.Authentification;
import entities.LoginRequest;
import entities.User;
import entities.UserDraft;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class LoginController {
    private Stage stage;
    private Scene scene;
    UserDraft userDraft=new UserDraft();


    public int loginAttempts=0;



    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    void handleLogin(ActionEvent event) {

        String email = emailField.getText();
        String password = passwordField.getText();

        LoginRequest loginRequest = new LoginRequest(email, password);

        Authentification authentification = new Authentification(loginRequest);

        try {
            if (authentification.authentification()){
                Alert alert =new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Login Successful");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }

    }



    public void handleGoogleLogin(ActionEvent actionEvent) {
        try{
            String clientId="";
            String redirectUri="http://localhost:8888/callback";
            String scope="openid email profile";

            String authurl="https://accounts.google.com/o/oauth2/v2/auth?" +
                    "client_id=" + clientId +
                    "&redirect_uri=" + redirectUri +
                    "&response_type=code" +
                    "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);

            Desktop.getDesktop().browse(new URI(authurl));
            HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
            server.createContext("/callback",exchange ->{
                try{
                    String query = exchange.getRequestURI().getQuery();
                    String code =query.split("=")[1];


                    String html = "<html><body><h1>Login Successful</h1>You may close this tab.</body></html>";
                    exchange.sendResponseHeaders(200, html.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(html.getBytes());
                    os.close();
                    exchangeCodeForToken(code,clientId,redirectUri);


                    Platform.runLater(()->{
                        try {
                            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUserWithGoogle.fxml"));
                            Parent root = loader.load();

                            Scene scene = new Scene(root);
                            AddUserWithGoogleController controller = loader.getController();
                            controller.setUserDraft(userDraft);


                            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                            currentStage.setScene(scene);
                            currentStage.setX(screenBounds.getMinX());
                            currentStage.setY(screenBounds.getMinY());
                            currentStage.setWidth(screenBounds.getWidth());
                            currentStage.setHeight(screenBounds.getHeight());
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to load FXML", e);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                server.stop(0);

            } );
            System.out.println(userDraft);

            server.start();


        }
        catch(Exception e){
            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Google Login Failled : "+e.getMessage());
            alert.showAndWait();
            return;
        }

        
    }
    public void exchangeCodeForToken(String code, String clientId, String redirectUri) throws Exception {
        String clientSecret="";
        String body = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&grant_type=authorization_code";


        HttpRequest request =HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response =HttpClient.newHttpClient()
                .send(request,HttpResponse.BodyHandlers.ofString());
        String responseBody=response.body();

        JSONObject obj=new JSONObject(responseBody);
        String accessToken=obj.getString("access_token");
        fetchUserInfo(accessToken);



    }

    private void fetchUserInfo(String accessToken) throws Exception {
        boolean succes=false;

        HttpRequest userInfoRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/oauth2/v2/userinfo"))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        HttpResponse<String> userInfoResponse = HttpClient.newHttpClient()
                .send(userInfoRequest, HttpResponse.BodyHandlers.ofString());

        String userInfoJson = userInfoResponse.body();
        System.out.println("User info: " + userInfoJson);
        JSONObject obj=new JSONObject(userInfoJson);

        userDraft.setEmail(obj.getString("email"));
        userDraft.setFirstName(obj.getString("given_name"));
        userDraft.setLastName(obj.getString("family_name"));
        succes=true;




    }


    public void handleForgotPassword(javafx.event.ActionEvent actionEvent) {
        loadFxml("/ForgottenPassword.fxml",actionEvent);
    }

    @FXML
    public void handleSignUp(ActionEvent actionEvent) {
       loadFxml("/Adduser.fxml",actionEvent);
    }

    private void loadFxml(String fxml, ActionEvent actionEvent) {
        try {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            currentStage.setScene(scene);
            currentStage.setX(screenBounds.getMinX());
            currentStage.setY(screenBounds.getMinY());
            currentStage.setWidth(screenBounds.getWidth());
            currentStage.setHeight(screenBounds.getHeight());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML", e);
        }
    }

}