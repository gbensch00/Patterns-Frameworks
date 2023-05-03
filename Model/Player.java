package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {
  private Player player;
  private double velocity = 10;
  private DoubleProperty health;

  public Player(String imagePath, double startingHealth) {
    super(new Image(imagePath));
    this.health = new SimpleDoubleProperty(startingHealth);
  }

  public DoubleProperty getHealth() {
    return health;
  }
  
  public void decreaseHealth(double amount) {
    health.set(health.get() - amount);
  }

  public void increaseHealth(double amount) {
    health.set(health.get() + amount);
  }

}
