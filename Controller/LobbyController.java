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
import javafx.scene.control.TextInputDialog;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Model.GameModel;
import View.GameView;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class LobbyController {

	String loggedInUserName;
	String PlayerTwoName = "guest";
	String dbID;
	private DatabaseConnection dbConnection;
	String resolution;
	int width;
	int height;

	String DBURL = "jdbc:mysql://localhost:3307/TestDB";
	String DBUser = "root";
	String DBPassword = "";

	private UserSettingsDAO userSettingsDAO;

	// private UserDAO userDAO;

	public LobbyController() {
		try {
			dbConnection = new DatabaseConnection("jdbc:mysql://localhost:3307/TestDB", "root", "");
			Connection con = DriverManager.getConnection(DBURL, DBUser, DBPassword);
			userSettingsDAO = new UserSettingsDAOImpl(con);
			// userDAO = new UserDAOImpl(con);

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

	@FXML
	public void setLoggedInUserName(User userName) {
		UserName.setText("Hallo " + userName.getUsername() + "!");
		this.loggedInUserName = userName.getUsername();

		try {
			// User user = userDAO.getUserByName(userName);
			String userID = String.valueOf(userName.getId());

			UserSettings userSettings = userSettingsDAO.getUserSettingsByUserId(userID);

			// lediglich Fehlerüberprüfung kann später gelöscht werden
			if (userSettings == null) {
				System.out.println(userSettings);
			}

			if (userSettings != null) {
				String fontType = userSettings.getFontType();
				String fontSize = userSettings.getFontSize();
				String backgroundColor = userSettings.getBackgroundColor();
				String resolution = userSettings.getResolution();
				this.dbID = String.valueOf(userSettings.getUserId());
				System.out.println("ID lautet " + this.dbID);

				String[] dimensions = resolution.split("x");
				this.width = Integer.parseInt(dimensions[0]);
				this.height = Integer.parseInt(dimensions[1]);

				Color bgC = Color.valueOf(backgroundColor);
				// Bei neuen Einträgen in die DB ist der Avatar noch NULL daher wird hier eine
				// Überprüfung gemacht ob der Wert NULL ist wenn ja lade ein Standardbild
				if (userSettings.getSavedAvatar() == null) {
					InputStream inputStream = getClass().getResourceAsStream("/res/UserAvatar/PlayerIcon1.png");
					Image image = new Image(inputStream);
					Avatar.setImage(image);
				} else {

					byte[] avatarBytes = userSettings.getSavedAvatar();
					ByteArrayInputStream inputStream = new ByteArrayInputStream(avatarBytes);
					Image image = new Image(inputStream);
					Avatar.setImage(image);
				}

				for (Node node : anchorPane.getChildren()) {
					if (node instanceof Button) {
						((Button) node).setFont(Font.font(fontType));
					}
				}

				Stop[] stops = new Stop[] { new Stop(0, bgC), new Stop(1, Color.LIGHTBLUE) };
				LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

				// Setze den Farbverlauf als Hintergrundbild
				BackgroundFill fill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
				Background background = new Background(fill);
				anchorPane.setBackground(background);

			}

		} catch (SQLException e) {
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
	private void handleSinglePlayerButton(ActionEvent event) {
		Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		previousStage.close();
		Stage stage = new Stage();
		GameModel model = new GameModel();
		GameView view = new GameView(width, height,false);
		GameController controller = new GameController(model, view);
		stage.setScene(view.getScene());
		stage.show();
	}

	@FXML
	private void handleMultiPlayerButton(ActionEvent event) {

		// Abfrage zum zweiten Spieler
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Zweiter Spieler");
		alert.setHeaderText(null);
		alert.setContentText("Gibt es einen zweiten Spieler?");
		ButtonType buttonTypeYes = new ButtonType("Ja");
		ButtonType buttonTypeNo = new ButtonType("Nein");
		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

		// Zeigt den Dialog an und wartet auf Benutzerinteraktion
		ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

		if (result == buttonTypeYes) {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Spielername");
			dialog.setHeaderText(null);
			dialog.setContentText("Wie heißt der zweite Spieler?");
			Optional<String> resultName = dialog.showAndWait();
			this.PlayerTwoName = resultName.orElse("guest");

		}

		Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		previousStage.close();
		Stage stage = new Stage();
		GameModel model = new GameModel();
		GameView view = new GameView(800, 600,true);
		// GameView view = new GameView(600,600, this.loggedInUserName,PlayerTwoName);
		GameController controller = new GameController(model, view);
		stage.setScene(view.getScene());
		stage.show();
		System.out.println("Hallo " + PlayerTwoName + " Bist du bereit?");
	}

	@FXML
	private void handleHighscoreButton(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/res/fxml/highscores.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void handleSettingsButton(ActionEvent event) throws IOException, SQLException {

		Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		previousStage.close();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/fxml/settings.fxml"));
		Parent root = loader.load();
		SettingsController settingsController = loader.getController();
		settingsController.setUserName(this.dbID); // Benutzernamen an das FXML-Controller-Objekt übergeben

		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}

}