package Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {
  private Player player;
  private double velocity = 5;

  public Player(String imagePath) {
    super(new Image(imagePath));
  }

  public void moveUp() {
    setTranslateY(getTranslateY() - velocity);
  }

  public void moveDown() {
    setTranslateY(getTranslateY() + velocity);
  }

  public void moveLeft() {
    setTranslateX(getTranslateX() - velocity);
  }

  public void moveRight() {
    setTranslateX(getTranslateX() + velocity);
  }
}
