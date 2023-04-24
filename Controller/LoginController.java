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
	private Button devLogin;

	@FXML
	public void userLogin(ActionEvent e) {

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
						Stage stage = new Stage();
						GameModel model = new GameModel();
						GameView view = new GameView();
						GameController controller = new GameController(model, view);
						stage.setScene(view.getScene());
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
	
	/*Für Testzwecke eine DevLogin der die Datenbankabfrage umgeht*/
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
