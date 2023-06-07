package Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import View.GameView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class HealthBar implements PropertyChangeListener {

  private Pane heartsPane;
   private Pane heartsPane2;
   private Player player;
  private GameView view;

  public HealthBar(Player player, Pane heartsPane) {
    this.player = player;
    this.heartsPane = heartsPane;
    this.heartsPane2 = heartsPane2;
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
    }else
     if (evt.getPropertyName().equals("health2")) {
      int oldValue = (int) evt.getOldValue();
      int newValue = (int) evt.getNewValue();
     //funktioniert nicht weil die View nicht übergeben wird, aber ist eigentlich nicht der Sinn einer Observable, dass alles extern gemacht wird... mal sehen
      //view.updateSecondHealthBar(newValue);
    }
  }
  
}
