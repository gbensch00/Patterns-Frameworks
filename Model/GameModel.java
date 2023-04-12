package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameModel {
  private IntegerProperty score = new SimpleIntegerProperty(0);

  public int getScore() {
    return score.get();
  }

  public void setScore(int newScore) {
    score.set(newScore);
  }

  public IntegerProperty scoreProperty() {
    return score;
  }
}
