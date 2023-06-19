package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.StringWriter;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import Model.Server;
import Model.User;
import Model.UserDAO;
import View.GameView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

	/*
	 * Tobi: Um eine Verbindung mit der DB aufgebaut zu bekommen benötigt man einen
	 * sql connector, das ist eine Lib die man extra hinzufügen muss
	 * (mysql-connector.java-8.0.25.jar) zu finden im Ordner Extra Lib
	 * 
	 * 
	 * String DBURL = "jdbc:mysql://localhost:3306/TestDB"; String DBUser = "root";
	 * String DBPassword = ""; Statement stmt = null; ResultSet rs = null;
	 */
	private String defUser = "test99";
	private String loggedInUserName = "guest";
	private UserDAO userDAO;
	private User loggedInUser;
	private boolean sendSuccessful = false;
	private boolean createUserSuccesful = false;

	public LoginController() {
		// Code zur Initialisierung der Verbindung zur Datenbank
		/*
		 * try { Connection con = DriverManager.getConnection(DBURL, DBUser,
		 * DBPassword); userDAO = new UserDAOImpl(con); } catch (SQLException e) {
		 * e.printStackTrace(); }
		 */
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
	private Button mp;

	@FXML
	public void userLogin(ActionEvent e) throws IOException {
		if (e.getSource() == LoginButton) {
			String userID = userIDField.getText();
			String password = String.valueOf(userPasswordField.getText());

			// Erstelle ein XML-Dokument für die Authentifizierungsdaten
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.newDocument();

				// Erstelle das <Auth> Element
				Element authElement = doc.createElement("Auth");
				doc.appendChild(authElement);

				// Füge <Username> und <Password> Elemente hinzu
				Element usernameElement = doc.createElement("Username");
				usernameElement.setTextContent(userID);
				authElement.appendChild(usernameElement);

				Element passwordElement = doc.createElement("Password");
				passwordElement.setTextContent(password);
				authElement.appendChild(passwordElement);

				// Konvertiere das XML-Dokument in einen String
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				StringWriter writer = new StringWriter();
				transformer.transform(new DOMSource(doc), new StreamResult(writer));
				String xmlString = writer.getBuffer().toString();
				System.out.println(xmlString);

				// Sende das XML an den Server
				// Prüfe ob String erfolgreich übertragen wurde
				sendSuccessful = sendXMLToServer(xmlString);

				System.out.println(sendSuccessful);

				// wenn true lade cockpit.fxml schließe den rest
				if (sendSuccessful) {
					// XML erfolgreich an den Server gesendet
					// Lade Lobby.fxml
					// Da erfolgreich frage das Nutzerdaten Objekt am Server ab
					loggedInUser = Server.callUser(userID);

					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/fxml/cockpit.fxml"));
						Parent root = loader.load();
						LobbyController lobbyController = loader.getController();

						lobbyController.setLoggedInUserName(this.loggedInUser);
						Stage lobbyStage = new Stage();
						lobbyStage.setScene(new Scene(root));
						lobbyStage.show();

						// Schließe die Login-Stage
						Stage loginStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
						loginStage.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Falsches Password oder Nutzer nicht gefunden");
					alert.setHeaderText(null);
					alert.setContentText("Falsches Password oder Nutzer nicht gefunden");
					alert.showAndWait();
				}
			}

			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static boolean sendXMLToServer(String xmlString) {
		try {
			Socket clientSocket = new Socket("localhost", 1234); // Verbindung zum Server herstellen
			OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
			writer.write(xmlString); // XML an den Server senden
			writer.flush();
			clientSocket.shutdownOutput(); // Verbindung schließen
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String response = reader.readLine();

			clientSocket.close(); // Verbindung schließen

			return "Authentication successful".equals(response);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean sendNewUserXMLToServer(String xmlString) {
		try {
			Socket clientSocket = new Socket("localhost", 1234); // Verbindung zum Server herstellen
			OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
			writer.write(xmlString); // XML an den Server senden
			writer.flush();
			clientSocket.shutdownOutput(); // Verbindung schließen
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String response = reader.readLine();

			clientSocket.close(); // Verbindung schließen

			return "Nutzer erfolgreich erstellt".equals(response);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
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

		Parent root = FXMLLoader.load(getClass().getResource("/res/fxml/createrUser.fxml"));
		Scene scene = new Scene(root);
		Stage currentStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		currentStage.setScene(scene);
	}

	@FXML
	public void switchToLoginScreen(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/res/fxml/main.fxml"));
		Scene scene = new Scene(root);
		Stage currentStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		currentStage.setScene(scene);
	}

	@FXML
	public void createNewUser(ActionEvent event) throws IOException, SQLException {
		if (event.getSource() == RegisterButton) {
			String userName = RegisterUserNameField.getText();
			String password = String.valueOf(RegisterUserPasswordField.getText());

			// User newUser = new User(userName, password); // Erstelle ein neues
			// User-Objekt

			// Erstelle das XML-Dokument mit den Nutzerdaten
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.newDocument();

				// Erstelle das Root-Element "User"
				Element userElement = doc.createElement("NewUser");
				doc.appendChild(userElement);

				// Erstelle das Element "Username" und füge es dem Root-Element hinzu
				Element usernameElement = doc.createElement("Username");
				usernameElement.setTextContent(userName);
				userElement.appendChild(usernameElement);

				// Erstelle das Element "Password" und füge es dem Root-Element hinzu
				Element passwordElement = doc.createElement("Password");
				passwordElement.setTextContent(password);
				userElement.appendChild(passwordElement);

				// Konvertiere das XML-Dokument in einen String
				StringWriter writer = new StringWriter();
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.transform(new DOMSource(doc), new StreamResult(writer));
				String xmlString = writer.toString();

				// Sende das XML an den Server
				createUserSuccesful = sendNewUserXMLToServer(xmlString);
				if (createUserSuccesful) {
					// userDAO.createUser(newUser); // Aufruf der createUser-Methode des UserDAO
					System.out.println("Nutzer erstellt");

					// Erfolgs-Alert anzeigen
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Nutzer erstellt");
					alert.setHeaderText(null);
					alert.setContentText("Nutzer wurde erfolgreich erstellt");
					alert.showAndWait();
					switchToLoginScreen(event);
				} else {
					// FEHLERMELDUNG
					System.out.println("Fehler bei der Nutzererstellung");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Nutzer konte nicht erstellt werden");
					alert.setHeaderText(null);
					alert.setContentText("Nutzer existiert bereits, bitte einen anderen Nutzernamen wählen");
					alert.showAndWait();
					RegisterUserNameField.setText("");
					RegisterUserPasswordField.setText("");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/* Für Testzwecke eine DevLogin der die Datenbankabfrage umgeht */
	@FXML
	public void devLogin(ActionEvent event) throws IOException {
		Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		previousStage.close();
		Stage stage = new Stage();
		GameModel model = new GameModel();
		GameView view = new GameView(800, 800, false, this.defUser, this.loggedInUserName);
		GameController controller = new GameController(model, view);
		stage.setScene(view.getScene());
		stage.show();
	}

	public void mp(ActionEvent event) throws IOException {
		Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		previousStage.close();
		Stage stage = new Stage();
		GameModel model = new GameModel();
		GameView view = new GameView(800, 800, true, this.defUser, this.loggedInUserName);
		GameController controller = new GameController(model, view);
		stage.setScene(view.getScene());
		stage.show();
	}

}