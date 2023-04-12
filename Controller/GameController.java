package Controller;
import Model.GameModel;
import View.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import Model.Player;

public class GameController {
  private GameModel model;
  private GameView view;
  

  public GameController(GameModel model, GameView view) {
    this.model = model;
    this.view = view;
    Scene scene = view.getScene();

    view.getScoreLabel().textProperty().bind(model.scoreProperty().asString());

    scene.setOnKeyPressed(event -> {
      System.out.println("event:" + event.getCode());
      switch (event.getCode()) {
        case W:
          view.getPlayer().moveUp();
          break;
        case S:
          view.getPlayer().moveDown();
          break;
        case A:
          view.getPlayer().moveLeft();
          break;
        case D:
          view.getPlayer().moveRight();
          break;
        default:
          break;
      }
    });

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        model.setScore(model.getScore() + 1);
      }
    };
    timer.start();
  }
}
