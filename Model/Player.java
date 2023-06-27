package Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView implements PropertyChangeListener {
  private double velocityX;
  private double velocityY;
  private int health;
  private PropertyChangeSupport propertyChangeSupport;
  private String playerSelector;
  

  public Player(String imagePath, int startingHealth, String playerSelector) {
    super(new Image(imagePath));
    this.health = startingHealth;
    this.playerSelector = playerSelector;
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public int getHealth() {
    return health;
  }


  public void setHealth(int newHealth) {
    this.health = newHealth;
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

  public String getPlayerSelector() {
    return playerSelector;
  }
  
  public void setVelocityY(double velocityY) {
    this.velocityY = velocityY;
  }

  public void updateHealth(int healthChange, Player player, String playerSelector) {
    int oldHealth = this.health;
    this.health += healthChange;
      propertyChangeSupport.firePropertyChange("health", oldHealth, health);
    
  }
    
   // Methoden für PropertyChangeListener, werden benötigt
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