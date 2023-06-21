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

import Model.DatabaseConnection;
import Model.User;
import Model.UserDAO;
import Model.UserDAOImpl;
import Model.UserSettings;
import Model.UserSettingsDAO;
import Model.UserSettingsDAOImpl;

import java.io.FileInputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

/**
 * Der SettingsController ist verantwortlich für die Steuerung der Einstellungsansicht und die Verarbeitung von Benutzereingaben.
 */
public class SettingsController {

	String loggedInUserName;
	String dbID;
	String savedFontName;
	String savedFontSize;
	File avatarImage;
	byte[] savedAvatar;
	private Connection connection;
	boolean somethingChanged = false;
	Color savedBackgroundColor;
	String savedResolution;

	private UserSettings savedUserSettings;
	private UserSettingsDAO userSettingsDAO;
	private User savedUser;

	/* Tobi: Um das SaveSettings-Problem zu lösen, muss im Server eine Methode geschrieben welche
	 * Put oder SaveUserSettings heißt. 
	 * Das Laden der UserSettings stellt kein Problem dar nur das Speichern aktuell */
	
	
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

	/**
     * Konstruktor für den SettingsController mit einem UserSettingsDAO-Objekt.
     *
     * @param userSettingsDAO Das UserSettingsDAO-Objekt, das für den Zugriff auf die Benutzereinstellungen verwendet wird.
     */
	public SettingsController(UserSettingsDAO userSettingsDAO) {
		this.userSettingsDAO = userSettingsDAO;
	}

	/**
     * Initialisiert die Einstellungsansicht und lädt die Benutzereinstellungen.
     */
	@FXML
	public void initialize() {

		List<String> fontNames = Arrays.asList("Arial", "Helvetica", "Times New Roman", "Courier New", "Verdana", "Speedy",
				"Georgia");
		FontStyle.getItems().addAll(fontNames);

		List<String> fontSizes = Arrays.asList("8", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28",
				"36", "48", "72");
		FontSize.getItems().addAll(fontSizes);

		List<String> windowSizes = Arrays.asList("800x600", "1024x786", "1280x720", "1920x1080");
		WindowSize.getItems().addAll(windowSizes);

	}

	
	/**
     * Setzt den Benutzernamen und lädt die Benutzereinstellungen.
     *
     * @param user Der Benutzer, dessen Benutzernamen und Einstellungen gesetzt werden sollen.
     * @throws SQLException Falls ein Fehler beim Abrufen der Benutzereinstellungen aus der Datenbank auftritt.
     */
	@FXML
	public void setUserName(User user) throws SQLException {
		String userID = String.valueOf(user.getId());
		try {
			UserSettings userSettings =	Model.Server.callUserSettings(userID);
			this.savedUser = user;
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

	/**
     * Behandelt die Auswahl einer neuen Farbe.
     *
     * @param e Das ActionEvent-Objekt des Farbwechsel-Ereignisses.
     */
	@FXML
	private void colorChange(ActionEvent e) {
		this.savedBackgroundColor = cP.getValue(); // Die ausgewählte Farbe

		// Setzen der Hintergrundfarbe der Szene auf die ausgewählte Farbe
		// Erstelle eine Farbpalette mit den gewünschten Farben
		Stop[] stops = new Stop[] { new Stop(0, this.savedBackgroundColor), new Stop(1, Color.LIGHTBLUE) };
		LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

		// Setze den Farbverlauf als Hintergrundbild
		BackgroundFill fill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(fill);
		AnchorPane.setBackground(background);

	}

	/**
     * Speichert die Benutzereinstellungen.
     *
     * @param event Das ActionEvent-Objekt des Speichern-Buttons.
     * @throws IOException Falls ein Fehler beim Speichern der Benutzereinstellungen auftritt.
     */
	@FXML
	private void saveSettings(ActionEvent event) throws IOException {
		
		   try {
		        if (savedAvatar.length > 648576) {  // Überprüfe, ob die Größe des Bildes größer als 600kb ist
		            Alert alert = new Alert(AlertType.ERROR);
		            alert.setTitle("Bild zu groß");
		            alert.setHeaderText(null);
		            alert.setContentText("Das ausgewählte Bild ist zu groß. Bitte wählen Sie ein Bild, das kleiner als 600KB ist.");
		            alert.showAndWait();
		            return;  // Beenden Sie die Methode, ohne die Einstellungen zu speichern
		        }
		
		
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

	/**
     * Behandelt den Klick auf den Zurück-Button.
     *
     * @param e Das ActionEvent-Objekt des Zurück-Buttons.
     * @throws IOException Falls ein Fehler beim Zurückkehren zur Lobby auftritt.
     */
	@FXML
	public void handleBackButton(ActionEvent e) throws IOException {

		// Rückkehr zur Lobby
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/fxml/cockpit.fxml"));
		Parent root = loader.load();
		LobbyController lobbyController = loader.getController();
		lobbyController.setLoggedInUserName(savedUser); // UserObjekt an das FXML-Controller-Objekt übergeben
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		Stage previousStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		previousStage.close();
	}

	/**
     * Behandelt die Änderung der Schriftart.
     *
     * @param event Das ActionEvent-Objekt der Schriftartänderung.
     */
	@FXML
	private void FontChange(ActionEvent event) {

		String FontName = FontStyle.getValue();
		this.savedFontName = FontName;
		int fs = Integer.parseInt(this.savedFontSize);

		for (Node node : AnchorPane.getChildren()) {
			if (node instanceof Button) {
				((Button) node).setFont(Font.font(FontName, FontWeight.NORMAL, fs));
			}
			if (node instanceof Label) {
				((Label) node).setFont(Font.font(FontName, FontWeight.NORMAL, fs));
			}
		}
	}

	/**
     * Behandelt die Änderung der Schriftgröße.
     *
     * @param event Das ActionEvent-Objekt der Schriftgrößenänderung.
     */
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

	/**
     * Behandelt die Änderung der Auflösung.
     *
     * @param event Das ActionEvent-Objekt der Auflösungsänderung.
     */
	@FXML
	private void ResolutionChange(ActionEvent event) {
		this.savedResolution = WindowSize.getValue();
	}

	/**
     * Behandelt die Änderung des Avatars.
     *
     * @param e Das ActionEvent-Objekt der Avataränderung.
     * @throws IOException Falls ein Fehler beim Ändern des Avatars auftritt.
     */
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
	

