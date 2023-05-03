package Controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;


import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LobbyController {


	String loggedInUserName;
	String dbID;
	
	
	 private DatabaseConnection dbConnection;

	    public LobbyController() {
	        try {
	            dbConnection = new DatabaseConnection("jdbc:mysql://localhost:3306/TestDB", "root", "");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	@FXML
	private ColorPicker cP; // Farbauswahl-Element aus der FXML-Datei

	@FXML
	private AnchorPane anchorPane; // Das AnchorPane-Element aus der FXML-Datei
	@FXML
	private Button SinglePlayerButton;
	@FXML
	private Button MultiplayerButton;
	@FXML
	private Button SettingsButton;
	@FXML
	private Button Highscores;
	@FXML
	private Button CloseButton;
	@FXML
	private Toggle ToggleButton;
	@FXML
	private Label UserName; // Begrüßungslabel
	@FXML
	private ImageView Avatar; // Avatar für den User
	@FXML
	private Button SaveSettingsButton;

	// Methode, die aufgerufen wird, wenn eine neue Farbe ausgewählt wird
	@FXML
	private void colorChange(ActionEvent event) {
		Color color = cP.getValue(); // Die ausgewählte Farbe

		// Setzen der Hintergrundfarbe der Szene auf die ausgewählte Farbe
		anchorPane.setStyle("-fx-background-color: " + toRgbString(color) + ";");
	}

	// Hilfsmethode, um die Farbe in ein RGB-Format umzuwandeln
	private String toRgbString(Color color) {
		return "rgb(" + ((int) (color.getRed() * 255)) + ", " + ((int) (color.getGreen() * 255)) + ", "
				+ ((int) (color.getBlue() * 255)) + ")";
	}

	@FXML
	private void FontChange(ActionEvent event) {

		List<String> fontNames = Arrays.asList("Arial", "Helvetica", "Times New Roman", "Courier New", "Verdana",
				"Georgia");
		String randomFontName = fontNames.get((int) (Math.random() * fontNames.size()));

		for (Node node : anchorPane.getChildren()) {
			if (node instanceof Button) {
				((Button) node).setFont(Font.font(randomFontName, FontWeight.NORMAL, 14));

			}
			if (node instanceof Label) {
				((Label) node).setFont(Font.font(randomFontName, FontWeight.NORMAL, 14));
			}
		}
	}

	
	 @FXML
	    public void setLoggedInUserName(String userName) {
			UserName.setText("Hallo " + userName + "!");
	        try {
	            Connection connection = dbConnection.getConnection();
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT * FROM PLAYER WHERE name = '" + userName + "'");
	            if (resultSet.next()) {
	                String dbID = resultSet.getString("id");
	                this.dbID = dbID;
	                // ...
	            
	            // Hier werden die Parameter aus der Tabelle UserSettings in der mySQL DB
				// geladen
				// FontType, FontSize und der Avatar
	            resultSet = statement.executeQuery("SELECT * FROM UserSettings WHERE UserID = '" + dbID + "'");
				if (resultSet.next()) {
					String fontType = resultSet.getString("FontType");
					int fontSize = resultSet.getInt("FontSize");
					String backgroundColor = resultSet.getString("Backgroundcolor");
					// Bei neuen Einträgen in die DB ist der Avatar noch NULL daher wird hier eine
					// Überprüfung gemacht ob der Wert NULL ist wenn ja lade ein Standardbild
					if (resultSet.getBlob("Avatar") == null) {
						InputStream inputStream = getClass().getResourceAsStream("/res/UserAvatar/PlayerIcon1.png");
						Image image = new Image(inputStream);
						Avatar.setImage(image);
					} else {
						Blob avatar = resultSet.getBlob("Avatar");
						InputStream inputStream = avatar.getBinaryStream();
						Image image = new Image(inputStream);

						// Weist das Bild der ImageView zu

						Avatar.setImage(image);
					}

					for (Node node : anchorPane.getChildren()) {
						if (node instanceof Button) {
							((Button) node).setFont(Font.font(fontType, FontWeight.NORMAL, fontSize));
						}
					}

					anchorPane.setStyle("-fx-background-color: " + backgroundColor + ";");
				}
	            
	        }} catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public void close() {
	        try {
	            dbConnection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	 
	    @FXML
	    private void saveSettings(ActionEvent event) throws IOException {
	        Connection connection = null;
	        PreparedStatement pstmt = null;
	        try {
	            connection = dbConnection.getConnection();
	            String fontType = ((Button) SinglePlayerButton).getFont().getFamily();
	            int fontSize = (int) ((Button) SinglePlayerButton).getFont().getSize();
	            Color color = cP.getValue();
	            String colorValue = String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
	                                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	            String updateQuery = "UPDATE UserSettings SET FontType = ?, FontSize = ?, Backgroundcolor = ? WHERE UserID = ?";
	            pstmt = connection.prepareStatement(updateQuery);
	            pstmt.setString(1, fontType);
	            pstmt.setInt(2, fontSize);
	            pstmt.setString(3,colorValue);
	            pstmt.setString(4, dbID);
	            pstmt.executeUpdate();
	            
	            
	            
	            // Execute the SQL statement
	            int rowsUpdated = pstmt.executeUpdate();

	            // Erfolgsmeldung anzeigen
	            System.out.println(rowsUpdated + " Einstellungen wurden gespeichert!");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            // Ressourcen freigeben
	            try {
	                if (pstmt != null)
	                    pstmt.close();
	                if (connection != null)
	                    connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }


	 
		
	


	@FXML
	private void handleCloseButton(ActionEvent event) {
		// Alert anzeigen und Benutzer fragen, ob er das Spiel wirklich beenden möchte
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Spiel beenden");
		alert.setHeaderText("Space-Type beenden ?");
		alert.setContentText("Möchten Sie das Spiel wirklich beenden?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			// Schließen der Stage
			Stage stage = (Stage) CloseButton.getScene().getWindow();
			stage.close();
		}
	}
	
	@FXML
	private void handleHighscoreButton(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("highscores.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
