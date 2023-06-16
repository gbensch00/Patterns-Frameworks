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
 
  private boolean isUpPressed = false;
  private boolean isDownPressed = false;
  private boolean isLeftPressed = false;
  private boolean isRightPressed = false;
  private boolean isUpPressed2 = false;
  private boolean isDownPressed2 = false;
  private boolean isLeftPressed2 = false;
  private boolean isRightPressed2 = false;
  private MediaPlayer shootSound;
  private boolean isGameOver = false;
  private Player player;
  private Player player2;
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
    player = view.getPlayer1();
   
    
    scene.setOnKeyPressed(event -> {
      KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.W) {
        isUpPressed = true;
        player.setVelocityX(-5);
      } else if (keyCode == KeyCode.A) {
       isLeftPressed = true;
        player.setVelocityY(-5); 
      } else if (keyCode == KeyCode.S) {
        isDownPressed = true;
        player.setVelocityX(5);
      } else if (keyCode == KeyCode.D) {
        isRightPressed = true;
        player.setVelocityY(5);
      } else if (keyCode == KeyCode.SPACE) {
        shoot(player);
      }
    });

    scene.setOnKeyReleased(event -> {
      KeyCode keyCode = event.getCode();
     
       if (keyCode == KeyCode.W) {
        isUpPressed = false;
        if (isDownPressed) {
            player.setVelocityX(5);
        } else {
            player.setVelocityX(0);
        }
    } else if (keyCode == KeyCode.A) {
        isLeftPressed = false;
        if (isRightPressed) {
            player.setVelocityY(5);
        } else {
            player.setVelocityY(0);
        }
    } else if (keyCode == KeyCode.S) {
        isDownPressed = false;
        if (isUpPressed) {
            player.setVelocityX(-5);
        } else {
            player.setVelocityX(0);
        }
    } else if (keyCode == KeyCode.D) {
        isRightPressed = false;
        if (isLeftPressed) {
            player.setVelocityY(-5);
        } else {
            player.setVelocityY(0);
        }
       
      }
    });

   if(view.getPlayer2() != null) {
      player2 = view.getPlayer2();

     scene.setOnKeyPressed(event -> {
       KeyCode keyCode = event.getCode();
       if (keyCode == KeyCode.W) {
        isUpPressed = true;
        player.setVelocityX(-5);
      } else if (keyCode == KeyCode.A) {
       isLeftPressed = true;
        player.setVelocityY(-5); 
      } else if (keyCode == KeyCode.S) {
        isDownPressed = true;
        player.setVelocityX(5);
      } else if (keyCode == KeyCode.D) {
        isRightPressed = true;
        player.setVelocityY(5);
      } else if (keyCode == KeyCode.SPACE) {
        shoot(player);
      }
      else if (keyCode == KeyCode.UP) {
        isUpPressed2 = true;
        player2.setVelocityX(-5);
      } else if (keyCode == KeyCode.LEFT) {
       isLeftPressed2 = true;
        player2.setVelocityY(-5); 
      } else if (keyCode == KeyCode.DOWN) {
        isDownPressed2 = true;
        player2.setVelocityX(5);
      } else if (keyCode == KeyCode.RIGHT) {
        isRightPressed2 = true;
        player2.setVelocityY(5);
      } else if (keyCode == KeyCode.ENTER) {
        shoot(player2);
      }
    });

    scene.setOnKeyReleased(event -> {
      KeyCode keyCode = event.getCode();
     if (keyCode == KeyCode.W) {
        isUpPressed = false;
        if (isDownPressed) {
            player.setVelocityX(5);
        } else {
            player.setVelocityX(0);
        }
    } else if (keyCode == KeyCode.A) {
        isLeftPressed = false;
        if (isRightPressed) {
            player.setVelocityY(5);
        } else {
            player.setVelocityY(0);
        }
    } else if (keyCode == KeyCode.S) {
        isDownPressed = false;
        if (isUpPressed) {
            player.setVelocityX(-5);
        } else {
            player.setVelocityX(0);
        }
    } else if (keyCode == KeyCode.D) {
        isRightPressed = false;
        if (isLeftPressed) {
            player.setVelocityY(-5);
        } else {
            player.setVelocityY(0);
        }
       
      }
      else  if (keyCode == KeyCode.UP) {
        isUpPressed2 = false;
        if (isDownPressed2) {
            player2.setVelocityX(5);
        } else {
            player2.setVelocityX(0);
        }
    } else if (keyCode == KeyCode.LEFT) {
        isLeftPressed2 = false;
        if (isRightPressed2) {
            player2.setVelocityY(5);
        } else {
            player2.setVelocityY(0);
        }
    } else if (keyCode == KeyCode.DOWN) {
        isDownPressed2 = false;
        if (isUpPressed2) {
            player2.setVelocityX(-5);
        } else {
            player2.setVelocityX(0);
        }
    } else if (keyCode == KeyCode.RIGHT) {
        isRightPressed2 = false;
        if (isLeftPressed2) {
            player2.setVelocityY(-5);
        } else {
            player2.setVelocityY(0);
        }
       
      }
    }); 
    }

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        checkCollision();
         double newX = player.getTranslateX() + player.getVelocityX();
         double newY = player.getTranslateY() + player.getVelocityY();
         player.setTranslateX(newX);
         player.setTranslateY(newY);
        
         if(view.getPlayer2() != null) {
          double newX2 = player2.getTranslateX() + player2.getVelocityX();
         double newY2 = player2.getTranslateY() + player2.getVelocityY();
         player2.setTranslateX(newX2);
         player2.setTranslateY(newY2);
         }
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

  public void shoot(Player player) {

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
        bullet.setTranslateY(player.getTranslateY() + player.getFitHeight() / 5 - bullet.getImage().getHeight() / 2 + 24);

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

  checkPlayerHit(player);
  if(view.getPlayer2() != null) {
    checkPlayerHit(player2);
  }
  
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

private void checkPlayerHit(Player player) {
  if (isPlayerHit(player)) {
    player.updateHealth(-1, player, "one");

    if (player.getHealth() <= 0) {
      isGameOver = true;
      view.stop();
      view.showAlert("Game Over", "You were hit by an enemy!");
    }

  }
  if (view.getPlayer2() != null) { 
    if (isPlayerHit(player2)) {
      player.updateHealth(-1, player2, "two");
      view.updateSecondHealthBar(player.getHealth2());
      if (player.getHealth2() <= 0) {
      isGameOver = true;
      view.stop();
      view.showAlert("Game Over", "You were hit by an enemy!");
    }
    }
  }
} 

private boolean isPlayerHit(Player player) {
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