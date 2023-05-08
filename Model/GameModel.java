package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.media.Media;

public class GameModel {
  public IntegerProperty score = new SimpleIntegerProperty(0);
  private Media hitSound;
  private Media shootSound;

  public GameModel() {
    hitSound = new Media(getClass().getResource("/res/Sounds/treffer.mp3").toString());
    shootSound = new Media(getClass().getResource("/res/Sounds/shoot.mp3").toString());
}
public Media getshootSound() {
  return shootSound;
}

public Media getHitSound() {
  return hitSound;
}

  public int getScore() {
    return score.get();
  }

  public void setScore(int score) {
    this.score.set(score);
}

  public IntegerProperty scoreProperty() {
    return score;
  }


}