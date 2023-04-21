package Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {
  private Player player;
  private double velocity = 10;

  public Player(String imagePath) {
    super(new Image(imagePath));
  }

  public void moveUp() {
    setTranslateY(getTranslateY() - velocity);
    setRotate(0);
  }

  public void moveDown() {
    setTranslateY(getTranslateY() + velocity);
    setRotate(180);
  }

  public void moveLeft() {
    setTranslateX(getTranslateX() - velocity);
    setRotate(-90);
  }

  public void moveRight() {
    setTranslateX(getTranslateX() + velocity);
    setRotate(0);
    setRotate(90);
  }

  
  
}
