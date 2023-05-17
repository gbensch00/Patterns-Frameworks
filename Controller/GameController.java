package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Model.Enemy;
import Model.GameModel;
import Model.Player;
import Model.SpecialEnemy;
import View.GameView;
import javafx.animation.AnimationTimer;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class GameController {
  private GameModel model;
  private GameView view;
  private double velocityX = 0;
  private double velocityY = 0;
  private boolean isUpPressed = false;
  private boolean isDownPressed = false;
  private boolean isLeftPressed = false;
  private boolean isRightPressed = false;
  private MediaPlayer shootSound;
  private boolean isGameOver = false;
  private Player player;
  private List<ImageView> bullets;
  public List<Enemy> enemies;
  public List<SpecialEnemy> specialEnemies;

  public GameController(GameModel model, GameView view) {
    
    this.model = model;
    this.view = view;
    // Übergebe das Model-Objekt an die GameView
    view.setModel(model); 
    // Lade den Sound für das Schießen
    shootSound = new MediaPlayer(model.getshootSound());
    Scene scene = view.getScene();
    
    bullets = view.getBulletGroup().getChildren().stream()
            .filter(node -> node instanceof ImageView)
            .map(node -> (ImageView) node)
            .collect(Collectors.toList());
    enemies = view.getEnemies(); 
    specialEnemies = view.getSpecialEnemies();
    // Spieler-Objekt von der GameView erhalten
    player = view.getPlayer(); 
   
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
        checkCollision();
         double newX = player.getTranslateX() + velocityX;
         double newY = player.getTranslateY() + velocityY;
         player.setTranslateX(newX);
         player.setTranslateY(newY);
    
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
    // Übergebe die enemies- und specialEnemies-Listen an die View
  view.setEnemies(enemies);
  view.setSpecialEnemies(specialEnemies);
  }    

  public void shoot() {

    shootSound.stop();
    shootSound.seek(Duration.ZERO);
    shootSound.play();

    int currentScore = model.getScore();
    if (currentScore < 5) {
        // Erstelle ein neues ImageView-Objekt für die Kugel
        ImageView bullet = new ImageView("/res/enemy/playerGun1a.png");
        Media sound = model.getshootSound();
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        // Setze die Anfangsposition der Kugel auf der rechten Seite des Spielers
        bullet.setTranslateX(player.getTranslateX() + player.getFitWidth());
        bullet.setTranslateY(player.getTranslateY() + player.getFitHeight() / 2 - bullet.getImage().getHeight() / 2 + 24);

        // Füge das Kugel-ImageView der Liste der Kugeln hinzu
        bullets.add(bullet);
        // Übergib das Objekt an die View-Klasse, um es auf dem Bildschirm anzuzeigen
        addBullet(bullet);
        
    } else if (currentScore >= 5) {
        ImageView bullet1 = new ImageView("/res/enemy/playerGun1a.png");
        ImageView bullet2 = new ImageView("/res/enemy/playerGun1a.png");
        Media sound = model.getshootSound();
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        // Setze die Anfangsposition der Kugeln auf der rechten Seite des Spielers
        bullet1.setTranslateX(player.getTranslateX() + player.getFitWidth());
        bullet1.setTranslateY(player.getTranslateY() + player.getFitHeight() / 2 - bullet1.getImage().getHeight() / 2 + 16);

        bullet2.setTranslateX(player.getTranslateX() + player.getFitWidth());
        bullet2.setTranslateY(player.getTranslateY() + player.getFitHeight() / 2 - bullet2.getImage().getHeight() / 2 + 32);

        // Füge die Kugel-ImageViews der Liste der Kugeln hinzu
        bullets.add(bullet1);
        bullets.add(bullet2);
        // Übergib die Objekte an die View-Klasse, um sie auf dem Bildschirm anzuzeigen
        addBullet(bullet1, bullet2);
    }
}

public void checkCollision() {
  List<ImageView> bulletsToRemove = new ArrayList<>();
  List<Enemy> enemiesToRemove = new ArrayList<>();
  List<SpecialEnemy> specialEnemiesToRemove = new ArrayList<>();

  checkBulletCollision(bullets, enemies, specialEnemies, bulletsToRemove, enemiesToRemove, specialEnemiesToRemove);

  removeBulletsAndEnemies(bulletsToRemove, enemiesToRemove, specialEnemiesToRemove);

  int scoreIncrease = calculateScoreIncrease(enemiesToRemove, specialEnemiesToRemove);
  updateScore(scoreIncrease);

  checkPlayerHit();
}

private void checkBulletCollision(List<ImageView> bullets, List<Enemy> enemies, List<SpecialEnemy> specialEnemies,
                                List<ImageView> bulletsToRemove, List<Enemy> enemiesToRemove,
                                List<SpecialEnemy> specialEnemiesToRemove) {
  for (ImageView bullet : bullets) {
      boolean bulletCollided = false;

      for (Enemy enemy : enemies) {
          if (getBoundsInParent(bullet).intersects(enemy.getBounds())) {
              bulletCollided = true;
              bulletsToRemove.add(bullet);
              enemiesToRemove.add(enemy);
              break;
          }
      }

      if (!bulletCollided) {
          for (SpecialEnemy specialEnemy : specialEnemies) {
              if (getBoundsInParent(bullet).intersects(specialEnemy.getBounds())) {
                  bulletCollided = true;
                  bulletsToRemove.add(bullet);
                  specialEnemiesToRemove.add(specialEnemy);
                  break;
              }
          }
      }

      if (bulletCollided) {
          break;
      }
  }
}

private void removeBulletsAndEnemies(List<ImageView> bulletsToRemove, List<Enemy> enemiesToRemove,
                                   List<SpecialEnemy> specialEnemiesToRemove) {
  view.getBulletGroup().getChildren().removeAll(bulletsToRemove);
  bullets.removeAll(bulletsToRemove);

  enemies.removeAll(enemiesToRemove);
  specialEnemies.removeAll(specialEnemiesToRemove);
}

private int calculateScoreIncrease(List<Enemy> enemiesToRemove, List<SpecialEnemy> specialEnemiesToRemove) {
  int scoreIncrease = 0;
  if (!enemiesToRemove.isEmpty()) {
      scoreIncrease += enemiesToRemove.size();
  }
  if (!specialEnemiesToRemove.isEmpty()) {
      scoreIncrease += 5;
  }
  return scoreIncrease;
}

private void updateScore(int scoreIncrease) {
  model.setScore(model.getScore() + scoreIncrease);
  view.setScoreLabel(model.getScore());
}

private void checkPlayerHit() {
  if (isPlayerHit()) {
    player.updateHealth(-1);
    
    if (player.getHealth() <= 0) {
      isGameOver = true;
      view.stop();
      view.showAlert("Game Over", "You were hit by an enemy!");
    }
    
  }
} 

private boolean isPlayerHit() {
  double hitThreshold = player.getBoundsInParent().getHeight() * 0.5;
  Bounds playerBoundsInScene = player.localToScene(player.getBoundsInLocal());
  Bounds playerBounds = new BoundingBox(
          playerBoundsInScene.getMinX() + hitThreshold,
          playerBoundsInScene.getMinY() + hitThreshold,
          playerBoundsInScene.getWidth() - hitThreshold * 2,
          playerBoundsInScene.getHeight() - hitThreshold * 2);

  for (Enemy enemy : enemies) {
      Bounds enemyBounds = enemy.getBounds();
      if (playerBounds.intersects(enemyBounds)) {
        enemy.setYPos(-1000);;
          return true;
      }
  }
  return false;
} 

public Bounds getBoundsInParent(ImageView imageView) {
  return imageView.getBoundsInParent();
}

public void addBullet(ImageView bullet) {
  view.getBulletGroup().getChildren().add(bullet);  
}

public void addBullet(ImageView bullet1, ImageView bullet2) {
  view.getBulletGroup().getChildren().addAll(bullet1, bullet2);  
}
}
