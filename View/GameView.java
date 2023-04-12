package View;

import Model.Player;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameView {
  private Scene scene;
  private Label scoreLabel = new Label();
  private Player player;

  public GameView() {
    VBox root = new VBox();
    player = new Player("player.png");
    root.getChildren().addAll(player);
    scene = new Scene(root, 1800, 1800);
  }

  public Scene getScene() {
    return scene;
  }

  public Player getPlayer() {
    return player;
  }

  public Label getScoreLabel() {
    return scoreLabel;
  }
}
