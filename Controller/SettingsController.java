package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.io.FileInputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.scene.text.Text;

public class SettingsController {

	String loggedInUserName;
	String dbID;
	String savedFontName;
	String savedFontSize;
	File avatarImage;
	byte[] savedAvatar;
//	byte[] imageBytes;
	private Connection connection;
	boolean somethingChanged = false;
	Color savedBackgroundColor;
	String savedResolution;

	private UserSettings savedUserSettings;
	private UserSettingsDAO userSettingsDAO;
	private User savedUser;

	String DBURL = "jdbc:mysql://localhost:3307/TestDB";
	String DBUser = "root";
	String DBPassword = "";

	@FXML
	Button BackButton;
	@FXML
	Button SaveSettings;
	@FXML
	AnchorPane AnchorPane;
	@FXML
	ComboBox<String> FontStyle;
	@FXML
	ComboBox<String> FontSize;
	@FXML
	ComboBox<String> WindowSize;
	@FXML
	ColorPicker cP;
	@FXML
	Button ChangeAvatarButton;

	private DatabaseConnection dbConnection;

	public SettingsController() {

		// Code zur Initialisierung der Verbindung zur Datenbank
		try {
			Connection con = DriverManager.getConnection(DBURL, DBUser, DBPassword);
			userSettingsDAO = new UserSettingsDAOImpl(con);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public SettingsController(UserSettingsDAO userSettingsDAO) {
		this.userSettingsDAO = userSettingsDAO;
	}

	@FXML
	public void initialize() {

		List<String> fontNames = Arrays.asList("Arial", "Helvetica", "Times New Roman", "Courier New", "Verdana",
				"Georgia");
		FontStyle.getItems().addAll(fontNames);

		List<String> fontSizes = Arrays.asList("8", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28",
				"36", "48", "72");
		FontSize.getItems().addAll(fontSizes);

		List<String> windowSizes = Arrays.asList("800x600", "1024x786", "1280x720", "1920x1080");
		WindowSize.getItems().addAll(windowSizes);

	}

	public void initializeConnection() {
		try {
			connection = DriverManager.getConnection(DBURL, DBUser, DBPassword);
			userSettingsDAO = new UserSettingsDAOImpl(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void setUserName(String userID) throws SQLException {

		try {

			initializeConnection();
			UserDAO userDAO = new UserDAOImpl(connection);
			User user = userDAO.getUserByID(userID);
			this.savedUser = user;

			UserSettings userSettings = userSettingsDAO.getUserSettingsByUserId(userID);
			this.savedUserSettings = userSettings;
			if (userSettings != null) {
				this.savedFontName = userSettings.getFontType();
				this.savedFontSize = String.valueOf(userSettings.getFontSize());
				String bgColorStr = userSettings.getBackgroundColor();
				this.savedBackgroundColor = Color.valueOf(bgColorStr);
				this.savedAvatar = userSettings.getSavedAvatar();
				this.savedResolution = userSettings.getResolution();

				System.out.println("Fontname " + this.savedFontName + " FontSize " + this.savedFontSize + " BGC "
						+ this.savedBackgroundColor + " Avatar " + this.savedAvatar);
			}

			Stop[] stops = new Stop[] { new Stop(0, this.savedBackgroundColor), new Stop(1, Color.LIGHTBLUE) };
			LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

			BackgroundFill fill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
			Background background = new Background(fill);
			AnchorPane.setBackground(background);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Methode, die aufgerufen wird, wenn eine neue Farbe ausgewählt wird
	@FXML
	private void colorChange(ActionEvent e) {
		this.savedBackgroundColor = cP.getValue(); // Die ausgewählte Farbe

		// Setzen der Hintergrundfarbe der Szene auf die ausgewählte Farbe
		// anchorPane.setStyle("-fx-background-color: " + toRgbString(color) + ";");

		// Erstelle eine Farbpalette mit den gewünschten Farben
		Stop[] stops = new Stop[] { new Stop(0, this.savedBackgroundColor), new Stop(1, Color.LIGHTBLUE) };
		LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

		// Setze den Farbverlauf als Hintergrundbild
		BackgroundFill fill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(fill);
		AnchorPane.setBackground(background);

	}

	@FXML
	private void saveSettings(ActionEvent event) throws IOException {
		try {
			String colorValue = String.format("#%02X%02X%02X", (int) (savedBackgroundColor.getRed() * 255),
					(int) (savedBackgroundColor.getGreen() * 255), (int) (savedBackgroundColor.getBlue() * 255));
			savedUserSettings.setBackgroundColor(colorValue);
			savedUserSettings.setSavedAvatar(savedAvatar);
			savedUserSettings.setFontType(savedFontName);
			savedUserSettings.setFontSize(savedFontSize);
			savedUserSettings.setResolution(savedResolution);

			userSettingsDAO.updateUserSettings(savedUserSettings);
			System.out.println("Einstellungen wurden gespeichert!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleBackButton(ActionEvent e) throws IOException {

	
		// Rückkehr zur Lobby
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/fxml/cockpit.fxml"));
		Parent root = loader.load();
		LobbyController lobbyController = loader.getController();
		//lobbyController.setLoggedInUserName(savedUser); // UserObjekt an das FXML-Controller-Objekt übergeben
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		Stage previousStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		previousStage.close();
	}

	@FXML
	private void FontChange(ActionEvent event) {

		String FontName = FontStyle.getValue();
		this.savedFontName = FontName;

		for (Node node : AnchorPane.getChildren()) {
			if (node instanceof Button) {
				((Button) node).setFont(Font.font(FontName, FontWeight.NORMAL, 14));
			}
		}
	}

	@FXML
	private void FontSizeChange(ActionEvent event) {
		String selectedFontSize = FontSize.getValue();
		this.savedFontSize = selectedFontSize;

		for (Node node : AnchorPane.getChildren()) {
			if (node instanceof Button) {
				Font oldFont = ((Button) node).getFont();
				double newFontSize = Double.parseDouble(selectedFontSize);
				((Button) node).setFont(Font.font(oldFont.getFamily(), FontWeight.NORMAL, newFontSize));

			} else if (node instanceof Label) {
				Font oldFont = ((Label) node).getFont();
				double newFontSize = Double.parseDouble(selectedFontSize);
				((Label) node).setFont(Font.font(oldFont.getFamily(), FontWeight.NORMAL, newFontSize));
			}
		}
	}

	@FXML
	private void ResolutionChange(ActionEvent event) {
		this.savedResolution = WindowSize.getValue();
	}

	public void AvatarChange(ActionEvent e) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Avatar Image");
		fileChooser.setInitialDirectory(new File("res/UserAvatar"));
		fileChooser.setInitialFileName("Avatar.png");
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Image Files", "*.png"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			this.avatarImage = selectedFile;
			System.out.println("Datei wurde gespeichert " + this.avatarImage);
			FileInputStream fis = new FileInputStream(avatarImage);
			this.savedAvatar = fis.readAllBytes();
			fis.close();
			System.out.println("Datei wurde gespeichert " + this.savedAvatar);
		}
	}

}