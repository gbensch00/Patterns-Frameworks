package Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class HealthBar implements PropertyChangeListener {

  private Pane heartsPane;
  private Player player;

  public HealthBar(Player player, Pane heartsPane) {
    this.player = player;
    this.heartsPane = heartsPane;
    player.addPropertyChangeListener(this);
  }
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    
    if (evt.getPropertyName().equals("health")) {
      int oldValue = (int) evt.getOldValue();
      int newValue = (int) evt.getNewValue();
      if (oldValue > newValue) {
        heartsPane.getChildren().clear();
        for (int i = 0; i < newValue; i++) {
          ImageView heart = new ImageView(new Image("heart.png"));

            heart.setTranslateX(10 + (i * 10));
            heart.setTranslateY(10);

            heartsPane.getChildren().add(heart);
        }
      }
    }
  }
  
}
