package View;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import Controller.HighscoreController;
import Model.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Blob;
import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class HighscoreScene {

	@FXML
	private Button BackButton;
	@FXML
	private GridPane HighScores;

	@FXML
	public void initialize() {
	    try {
	        ResultSet resultSet = Server.getHSc();
	        int row = 0;
	        while (resultSet.next()) {
	            String name = resultSet.getString("name");
	            int highscore = resultSet.getInt("highscore");
	            Blob avatarBlob = resultSet.getBlob("Avatar");

	            // Konvertiere Blob in Image
	            InputStream is = avatarBlob.getBinaryStream();
	            Image avatarImage = new Image(is);

	            // Erstelle UI-Komponenten
	            Label nameLabel = new Label(name);
	            nameLabel.setStyle("-fx-font-size: 20px");
	            Label highscoreLabel = new Label(String.valueOf(highscore));
	            highscoreLabel.setStyle("-fx-font-size: 20px");
	            ImageView avatarView = new ImageView(avatarImage);
	            
	            // Setze die Größe des ImageView
	            avatarView.setFitHeight(35);  // Setze die Höhe auf 25px
	            avatarView.setFitWidth(35);   // Setze die Breite auf 25px
	            
	    

	            // Füge sie dem GridPane hinzu
	            HighScores.add(nameLabel, 1, row);
	            HighScores.add(highscoreLabel, 2, row);
	            HighScores.add(avatarView, 0, row);  // Füge das Avatar-Bild in einer neuen Spalte hinzu
	            
	            // Prüfe, ob eine neue Zeile hinzugefügt wurde und setze die RowConstraints entsprechend
	            if (HighScores.getRowConstraints().size() <= row) {
	                RowConstraints lastRowConstraints = HighScores.getRowConstraints().get(HighScores.getRowConstraints().size() - 1);
	                HighScores.getRowConstraints().add(lastRowConstraints);
	            }


	            row++;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	@FXML
	public void handleBackButton(ActionEvent event) {
		Stage stage = (Stage) BackButton.getScene().getWindow();
		stage.close();
	}
}