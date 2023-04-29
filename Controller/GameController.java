package Controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Model.Enemy;
import Model.GameModel;
import View.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class GameController {
  private GameModel model;
  private GameView view;
  private Enemy enemy;
  private ImageView spaceship;
  private double velocityX = 0;
  private double velocityY = 0;
  private boolean isUpPressed = false;
  private boolean isDownPressed = false;
  private boolean isLeftPressed = false;
  private boolean isRightPressed = false;
  private ArrayList<ImageView> bullets;
  private ArrayList<Enemy> enemies;


  public GameController(GameModel model, GameView view) {
    this.model = model;
    this.view = view;
    Scene scene = view.getScene();
    spaceship = view.getPlayer();

    bullets = new ArrayList<>();

    view.getScoreLabel().textProperty().bind(model.scoreProperty().asString());

    scene.setOnKeyPressed(event -> {
      KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.W) {
        isUpPressed = true;
        velocityY = -5;
      } else if (keyCode == KeyCode.A) {
        isLeftPressed = true;
        velocityX = -5;
      } else if (keyCode == KeyCode.S) {
        isDownPressed = true;
        velocityY = 5;
      } else if (keyCode == KeyCode.D) {
        isRightPressed = true;
        velocityX = 5;
      } else if (keyCode == KeyCode.SPACE) {
        shoot();
      }
    });

    scene.setOnKeyReleased(event -> {
      KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.W) {
        isUpPressed = false;
        velocityY = isDownPressed ? 5 : 0;
      } else if (keyCode == KeyCode.A) {
        isLeftPressed = false;
        velocityX = isRightPressed ? 5 : 0;
      } else if (keyCode == KeyCode.S) {
        isDownPressed = false;
        velocityY = isUpPressed ? -5 : 0;
      } else if (keyCode == KeyCode.D) {
        isRightPressed = false;
        velocityX = isLeftPressed ? -5 : 0;
      }
    });

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        model.setScore(model.getScore() + 1);
        double newX = spaceship.getTranslateX() + velocityX;
        double newY = spaceship.getTranslateY() + velocityY;
        spaceship.setTranslateX(newX);
        spaceship.setTranslateY(newY);

        ArrayList<ImageView> bulletsToRemove = new ArrayList<>();
        for (ImageView bullet : bullets) {
          bullet.setTranslateX(bullet.getTranslateX() + 10);
          if (bullet.getTranslateY() < -100) {
            bulletsToRemove.add(bullet);
          }
        }
        bullets.removeAll(bulletsToRemove);
        
      }
    };
    timer.start();
  }

  public void shoot() {
    ImageView bullet = new ImageView("playerGun1a.png");
    bullet.setTranslateX(spaceship.getTranslateX() + spaceship.getFitWidth());
    bullet.setTranslateY(spaceship.getTranslateY() + spaceship.getFitHeight() / 2 - bullet.getImage().getHeight() / 2 + 24);
    bullets.add(bullet);
    view.addBullet(bullet);
}

}
