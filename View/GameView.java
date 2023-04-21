package View;

import java.util.ArrayList;

import Model.Player;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameView {
  private Scene scene;
  private Label scoreLabel = new Label();
  private Player player;
  private ArrayList<ImageView> bullets;
  private Group bulletGroup;

  public GameView() {
    Pane root = new Pane();
    player = new Player("player.png");
    /*this.bullets = bullets;
    Group bulletGroup = new Group();
    bulletGroup.getChildren().addAll(bullets); */
    root.getChildren().addAll(player);
    scene = new Scene(root, 900, 900);
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

 /*  public void addBullet(ImageView bullet) {
   
    bullets.add(bullet);
    //Ist dasselbe wie if(root instanceof Pane); ist um sicherzustellen dass die Root der erwartete Typ ist, sonst würde das nicht funktionieren; braucht man weil man von einer Pane die getChildren() methode aufrufen kann und man dann dynamisch Inhalte hinzufügen kann, bei einer vBox z.B. aber nicht.
    Pane root = (Pane) scene.getRoot();
    root.getChildren().add(bullet);
  } */
}
