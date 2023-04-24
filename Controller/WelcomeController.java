package Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WelcomeController {

    @FXML
    private Button hilfeButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button registButton;

    @FXML
    void hilfeButtonAction(ActionEvent event) {

    }

    @FXML
    void loginButtonAction(ActionEvent event) {

    }

    @FXML
    void registButtonAction(ActionEvent event) throws IOException {

        registrierung();
    }

    public void registrierung () throws IOException{
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/registration.fxml"));
    
        Pane root = (Pane)fxmlLoader.load();
    
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Registrierung");
        stage.setResizable(false);
        stage.show();
    }
    

}
