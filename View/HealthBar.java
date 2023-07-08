package View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import Model.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class HealthBar implements PropertyChangeListener {

  private Pane heartsPane;
  private Player player;
  private int x;

  public HealthBar(Player player, Pane heartsPane) {
    this.player = player;
    this.heartsPane = heartsPane;
    player.addPropertyChangeListener(this);
  }
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    
    if (evt.getPropertyName().equals("health")) {
      int newValue = (int) evt.getNewValue();
     if(player.getPlayerSelector() == "one") {
      x = 10;
     } else if(player.getPlayerSelector() == "two") {
      x = 40;
     }
      player.setHealth(newValue);
      heartsPane.getChildren().clear();
      for (int i = 0; i < newValue; i++) {
            ImageView heart = new ImageView(new Image("/res/oberflaechen/heart.png"));

            heart.setTranslateX(10 + (i * 10));
            heart.setTranslateY(x);

            heartsPane.getChildren().add(heart);
        }
  }
}
}