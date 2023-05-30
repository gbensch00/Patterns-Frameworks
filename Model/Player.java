package Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Observer;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView implements PropertyChangeListener {
  private double velocityX = 0;
  private double velocityY = 0;
  //Zeile von gideon 03.05.
  private int health;
  private PropertyChangeSupport propertyChangeSupport;

  public Player(String imagePath, int startingHealth) {
    super(new Image(imagePath));
    this.health = startingHealth;
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public int getHealth() {
    return health;
  }

  public double getVelocityX() {
    return velocityY;
  }

  public double getVelocityY() {
    return velocityX;
  }

  public void setVelocityX(double velocityX) {
    this.velocityX = velocityX;
  }
  public void setVelocityY(double velocityY) {
    this.velocityY = velocityY;
  }
  

  public void updateHealth(int healthChange) {
    int oldHealth = this.health;
    this.health += healthChange;
    System.out.println("this.health: " + this.health);
    propertyChangeSupport.firePropertyChange("health", oldHealth, health);
  }
    
   // Methods to register and unregister PropertyChangeListeners
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'propertyChange'");
    }

}
