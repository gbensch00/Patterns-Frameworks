package Controller;

import java.sql.SQLException;
import Model.UserRegistration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class RegistrationController {
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView imageView;

    @FXML
    private Button registerButton;

    Image picture;

    @FXML
    void handleRegisterButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        String name = nameField.getText();
        String password = passwordField.getText();
        UserRegistration newUser = new UserRegistration(name, password, picture, 1);

        // Update User
        newUser.setName(nameField.getText());
        newUser.setPassword(passwordField.getText());

        // User speichern in der Datenbank
        newUser.saveToDatabase();

        // Close - Fenster schließen
        registerButton.getScene().getWindow().hide();

        // Meldung anzeigen
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrierung erfolgreich");
        alert.setHeaderText(null);
        alert.setContentText("Die Registrierung war erfolgreich.");
        alert.showAndWait();
    }

    @FXML
    void handleCancelButtonAction(ActionEvent event) {
        // Abbruchlogik
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }
    

}
