package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {
  private Player player;
  private double velocity = 10;
  //Zeile von gideon 03.05.
  private DoubleProperty health;

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

  //ab hier von Gideon am 03.05.
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