package Controllers;

import entities.DestinationRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import services.FlaskConnector;

import java.io.IOException;

public class DreamDestinationController {


    @FXML
    public void initialize() {


        moodComboBox.getItems().addAll(
                "Adventurous", "Affordable", "Balanced", "Calm", "Charming", "Coastal",
                "Cultural", "Diverse", "Elegant", "Energetic", "Exotic", "Historic",
                "Laid-back", "Lively", "Luxury", "Modern", "Mysterious", "Peaceful", "Quaint",
                "Quiet", "Relaxed", "Romantic", "Scenic", "Spiritual", "Sunny", "Tranquil",
                "Tropical", "Vibrant", "Wellness", "Wild"
        );

        familyToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                familyToggle.setStyle("-fx-background-color: #81c784; -fx-text-fill: white;");
            } else {
                familyToggle.setStyle("-fx-background-color: #e57373; -fx-text-fill: white;");
            }
        });




    }

    @FXML
    private TextField budgetField;

    @FXML
    private Spinner<Integer> durationSpinner;

    @FXML
    private ToggleButton familyToggle;

    @FXML
    private ComboBox<String> moodComboBox;

    @FXML
    private Slider physicalitySlider;

    @FXML
    private Label resultLabel;

    @FXML
    private Slider securitySlider;

    @FXML
    private Button submitButton;

    @FXML
    private Spinner<Double> tempSpinner;

    @FXML
    void handleSubmit(ActionEvent event) {

        String mood=moodComboBox.getValue();
        int phsicality=(int)physicalitySlider.getValue();
        int security=(int) securitySlider.getValue();

        int bugdet=Integer.parseInt(budgetField.getText());
        double temp=Double.parseDouble(tempSpinner.getValue().toString());
        int duration = ((Number) durationSpinner.getValue()).intValue();
        String  familyFriendly=(familyToggle.isSelected() )?"Yes":"No";

        DestinationRequest request=new DestinationRequest(mood,phsicality,familyFriendly,bugdet,  security,  temp,  duration);
        System.out.println(request.getFamilyFriendly());

        try {
            String response= FlaskConnector.getConnection(request);
            resultLabel.setText(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}