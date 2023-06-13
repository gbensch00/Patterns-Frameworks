import java.sql.SQLException;
import java.util.ArrayList;

import Controller.GameController;
import Model.GameModel;
import Model.Server;
import View.GameView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {
	private ArrayList<ImageView> bullets;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Thread serverThread = new Thread(() -> {
			try {
				Server.startServer();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		serverThread.setDaemon(true);
		serverThread.start();

		Parent root = FXMLLoader.load(getClass().getResource("/res/fxml/main.fxml"));
		Scene scene = new Scene(root);

		// Weitere Anpassungen für die Szene...

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}