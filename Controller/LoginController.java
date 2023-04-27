package Controller;

import java.awt.Color;
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
import Controller.GameController;
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

	String url = "jdbc:mysql://localhost:3306/TestDB";
	String user = "root";
	String password = "";
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

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
			try {
				con = DriverManager.getConnection(this.url, this.user, this.password);
				stmt = con.createStatement();
				rs = stmt.executeQuery("SELECT * FROM PLAYER");
				while (rs.next()) {

					String name = rs.getString("name");
					String userpassword = rs.getString("password");

					if (userID.equals(name) && password.equals(password)) {
						System.out.println("Login erfolgreich");
						System.out.println(userID + " " + name + " " + userpassword + " " + password);

						// Login Stage wird geschlossen
						Stage previousStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
						previousStage.close();

						// Game Stage wird geöffnet
						/*
						 * Stage stage = new Stage(); GameModel model = new GameModel(); GameView view =
						 * new GameView(); GameController controller = new GameController(model, view);
						 * stage.setScene(view.getScene()); stage.show();
						 */

						FXMLLoader loader = new FXMLLoader(getClass().getResource("lobby.fxml"));
						Parent root = loader.load();
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.show();

					}

					else {
						System.out.println("Login nicht erfolgreich");
					}

				}

			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stmt != null)
						stmt.close();
					if (con != null)
						con.close();
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}

			System.out.println("Database Connection Closed");

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
	public void createNewUser(ActionEvent event) throws IOException {
		if (event.getSource() == RegisterButton) {
			String userName = RegisterUserNameField.getText();
			String password = String.valueOf(RegisterUserPasswordField.getText());
			try {
				con = DriverManager.getConnection(this.url, this.user, this.password);
				stmt = con.createStatement();
				// Überprüfung, ob Nutzername bereits vergeben ist
				String selectQuery = "SELECT * FROM PLAYER WHERE name = '" + userName + "'";
				ResultSet rs = stmt.executeQuery(selectQuery);
				if (rs.next()) { // Wenn es bereits einen Eintrag mit dem Nutzernamen gibt
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Nutzername bereits vergeben");
					alert.setHeaderText(null);
					alert.setContentText("Der Nutzername " + userName + " ist bereits vergeben.");
					alert.showAndWait();
					return; // Methode beenden, wenn Nutzername bereits vergeben ist
				}
				String insertQuery = "INSERT INTO PLAYER (name, password) VALUES ('" + userName + "', '" + password
						+ "')";
				stmt.executeUpdate(insertQuery);
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

			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					if (con != null)
						con.close();
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}

			System.out.println("Database Connection Closed");
		}
	}

	/* Für Testzwecke eine DevLogin der die Datenbankabfrage umgeht */
	@FXML
	public void devLogin(ActionEvent event) throws IOException {
		Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		previousStage.close();
		Stage stage = new Stage();
		GameModel model = new GameModel();
		GameView view = new GameView();
		GameController controller = new GameController(model, view);
		stage.setScene(view.getScene());
		stage.show();
	}

}
