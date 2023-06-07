package Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView implements PropertyChangeListener {
  private double velocityX = 0;
  private double velocityY = 0;
  //Zeile von gideon 03.05.
  private int health;
  private int health2;
  private PropertyChangeSupport propertyChangeSupport;
  

  public Player(String imagePath, int startingHealth) {
    super(new Image(imagePath));
    this.health = startingHealth;
    this.health2 = startingHealth;
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public int getHealth() {
    return health;
  }


  public void setHealth(int newHealth) {
    this.health = newHealth;
  }

  public int getHealth2() {
    return health2;
  }

  public void setHealth2(int newHealth) {
    this.health2 = newHealth;
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
  

  public void updateHealth(int healthChange, Player player, String playerSelector) {
    if (playerSelector == "one") {
    int oldHealth = this.health;
    System.out.println("before: " + this.health);
    this.health += healthChange;
     System.out.println("before: " + this.health);
      propertyChangeSupport.firePropertyChange("health", oldHealth, health);
    } else if (playerSelector == "two") {
      int oldHealth = this.health2;
    System.out.println("before: " + this.health2);
    this.health2 += healthChange;
     System.out.println("before: " + this.health2);
     propertyChangeSupport.firePropertyChange("health2", oldHealth, health2);
    }    
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
