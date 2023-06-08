package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class HighscoreScene {

	@FXML
	private Button BackButton;
	@FXML
	private GridPane HighScores;

	@FXML
	public void initialize() {
		try {
			HighscoreController highscoreController = new HighscoreController();
			ResultSet resultSet = highscoreController.getAllHighscores();
			int row = 0;
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				int highscore = resultSet.getInt("highscore");
				Label nameLabel = new Label(name);
				Label highscoreLabel = new Label(String.valueOf(highscore));
				HighScores.add(nameLabel, 0, row);
				HighScores.add(highscoreLabel, 1, row);
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