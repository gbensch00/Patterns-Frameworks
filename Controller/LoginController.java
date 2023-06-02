package Controller;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Model.GameModel;
import View.GameView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

	/*
	 * Tobi: Um eine Verbindung mit der DB aufgebaut zu bekommen benötigt man einen
	 * sql connector, das ist eine Lib die man extra hinzufügen muss
	 * (mysql-connector.java-8.0.25.jar) zu finden im Ordner Extra Lib
	 */

	String DBURL = "jdbc:mysql://localhost:3306/TestDB";
	String DBUser = "root";
	String DBPassword = "";
	Statement stmt = null;
	ResultSet rs = null;

	private String loggedInUserName = "guest";
	private UserDAO userDAO;

	public LoginController() {
		// Code zur Initialisierung der Verbindung zur Datenbank
		try {
			Connection con = DriverManager.getConnection(DBURL, DBUser, DBPassword);
			userDAO = new UserDAOImpl(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public LoginController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@FXML
	private Button LoginButton;
	@FXML
	private Button resetButton;
	@FXML
	private TextField userIDField;
	@FXML
	private PasswordField userPasswordField;
	@FXML
	private Button createUserAccount;
	@FXML
	private Button abbrechenCreateUserAccount;
	@FXML
	private TextField RegisterUserNameField;
	@FXML
	private PasswordField RegisterUserPasswordField;
	@FXML
	private Button RegisterButton;

	@FXML
	private Button devLogin;

	@FXML
	public void userLogin(ActionEvent e) throws IOException {

		if (e.getSource() == LoginButton) {
			String userID = userIDField.getText();
			String password = String.valueOf(userPasswordField.getText());

			/*
			 * Tobi: Verbindet sich mit der lokalen DB und überprüft LEIDER noch jeden
			 * Eintrag einzeln Wollte ich gerne noch anpassen, dass sobald der gültige
			 * Nutzer gefunden wurde dann auch der Login passiert
			 */
			// Aufruf des DAO, um den Benutzer abzurufen
			User user = userDAO.getUserByName(userID);

			if (user != null && password.equals(user.getPassword())) {
				// Benutzer gefunden und Passwort stimmt überein

				loggedInUserName = user.getUsername(); // Benutzername speichern
				
				// Login Stage wird geschlossen
				Stage previousStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
				previousStage.close();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("cockpit.fxml"));
				Parent root = loader.load();
				LobbyController lobbyController = loader.getController();
				lobbyController.setLoggedInUserName(user); // Benutzernamen an das FXML-Controller-Objekt
																// übergeben
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Falsches Password oder Nutzer nicht gefunden");
				alert.setHeaderText(null);
				alert.setContentText("Falsches Password oder Nutzer nicht gefunden");
				alert.showAndWait();
			}

		}

	}

	@FXML
	public void reset(ActionEvent e) {

		if (e.getSource() == resetButton) {
			userIDField.setText("");
			userPasswordField.setText("");
		}

	}

	@FXML
	public void switchTocreateUser(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("createrUser.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		// ((Node) (event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	public void switchToLoginScreen(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	public void createNewUser(ActionEvent event) throws IOException, SQLException {
		if (event.getSource() == RegisterButton) {
			String userName = RegisterUserNameField.getText();
			String password = String.valueOf(RegisterUserPasswordField.getText());

			User newUser = new User(userName, password); // Erstelle ein neues User-Objekt

			userDAO.createUser(newUser); // Aufruf der createUser-Methode des UserDAO
			System.out.println("Nutzer erstellt");

			// Erfolgs-Alert anzeigen
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nutzer erstellt");
			alert.setHeaderText(null);
			alert.setContentText("Nutzer wurde erfolgreich erstellt");
			final ActionEvent finalEvent = event; // Event in endgültige Variable umwandeln
			alert.setOnHidden(evt -> {
				Stage previousStage = (Stage) ((Node) finalEvent.getSource()).getScene().getWindow();
				previousStage.close();
			});
			alert.showAndWait();
		}
	}

	/* Für Testzwecke eine DevLogin der die Datenbankabfrage umgeht */
	@FXML
	public void devLogin(ActionEvent event) throws IOException {
		Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		previousStage.close();
		Stage stage = new Stage();
		GameModel model = new GameModel();
		GameView view = new GameView(600, 600, this.loggedInUserName);
		GameController controller = new GameController(model, view);
		stage.setScene(view.getScene());
		stage.show();
	}

}
