package Controller;

import java.util.ArrayList;
import Model.Enemy;
import Model.GameModel;
import View.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameController {
  private GameModel model;
  private GameView view;
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
    // Lade den Sound für das Schießen

    Scene scene = view.getScene();
    spaceship = view.getPlayer();
    bullets = new ArrayList<>();
    enemies = new ArrayList<>();
    view.setBullets(bullets);
    view.setEnemies(enemies);
  
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

    if(getNewScore() < 5) {
        // Erstelle ein neues ImageView-Objekt für die Kugel
        ImageView bullet = new ImageView("/res/enemy/playerGun1a.png");
        Media sound = model.getshootSound();
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        // Setze die Anfangsposition der Kugel auf der rechten Seite des Raumschiffs
        bullet.setTranslateX(spaceship.getTranslateX() + spaceship.getFitWidth());
        bullet.setTranslateY(spaceship.getTranslateY() + spaceship.getFitHeight() / 2 - bullet.getImage().getHeight() / 2 + 24);

        // Füge das Kugel-ImageView der Liste der Kugeln hinzu
        bullets.add(bullet);
        
        // Übergib das Objekt an die View-Klasse, um es auf dem Bildschirm anzuzeigen
        view.addBullet(bullet);

    } else if(getNewScore() >= 5) {
        ImageView bullet1 = new ImageView("/res/enemy/playerGun1a.png");
        ImageView bullet2 = new ImageView("/res/enemy/playerGun1a.png");
        Media sound = model.getshootSound();
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        // Setze die Anfangsposition der Kugeln auf der rechten Seite des Raumschiffs
        bullet1.setTranslateX(spaceship.getTranslateX() + spaceship.getFitWidth());
        bullet1.setTranslateY(spaceship.getTranslateY() + spaceship.getFitHeight() / 2 - bullet1.getImage().getHeight() / 2 + 16);

        bullet2.setTranslateX(spaceship.getTranslateX() + spaceship.getFitWidth());
        bullet2.setTranslateY(spaceship.getTranslateY() + spaceship.getFitHeight() / 2 - bullet2.getImage().getHeight() / 2 + 32);

        // Füge die Kugel-ImageViews der Liste der Kugeln hinzu
        bullets.add(bullet1);
        bullets.add(bullet2);

        // Übergib die Objekte an die View-Klasse, um sie auf dem Bildschirm anzuzeigen
        view.addBullet(bullet1, bullet2);       
    }
}

public ArrayList<ImageView> getBullets() {
  return bullets;
}

public ArrayList<Enemy> getEnemies() {
  return enemies;
}

public int getScore() {
  return model.scoreProperty().get();
}

public int getNewScore() {
  return view.getNewScore();
}
}