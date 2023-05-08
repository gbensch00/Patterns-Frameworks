package View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import Model.Enemy;
import Model.Player;
import Model.SpecialEnemy;
import Model.GameModel;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameView {

    private List<Enemy> enemies;
    private Image enemyImage;
    private ArrayList<SpecialEnemy> specialEnemies = new ArrayList<>();
    private long lastSpecialEnemyTime = 0L;
    private Scene scene;
    private Label scoreLabel = new Label();
    private Player player;
    private GameModel model;
    private ArrayList<ImageView> bullets;
    private Group bulletGroup = new Group();  
    private Pane root;
    private GraphicsContext gc;
    private AnimationTimer animationTimer;
    private long lastEnemyTime = 0;
    private long enemyCreationInterval = 1000000000; // 1 second
    private long startTime = System.nanoTime();
    private int newScore = 0;
    private boolean isGameOver = false; 

    // //Von Tobi am 03.05. eingefügt
    public GameView() {
        Pane root = new Pane();
        player = new Player("/res/enemy/player.png");
        /*this.bullets = bullets;
        Group bulletGroup = new Group();
        bulletGroup.getChildren().addAll(bullets); */
        root.getChildren().addAll(player);
        scene = new Scene(root, 900, 900);
      }

    public GameView(double width, double height) {
        enemyImage = new Image("/res/enemy/Idle.png");
        player = new Player("/res/enemy/player.png");
        //specialEnemy = new Image("/enemy.png");
        model = new GameModel();
        enemies = new ArrayList<>();
        root = new Pane();
        root.getChildren().addAll(player, bulletGroup);
        scene = new Scene(root, width, height);

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        // Hintergrundbild erstellen und der Szene hinzufügen
        ImageView background = new ImageView(new Image("/res/enemy/planet.jpg"));
        root.getChildren().add(0, background); // Hinzufügen als unterstes Element im Pane

        
        // Erstellung des Labels für die Punkte
        Label scoreLabel = new Label("Score:0");
        scoreLabel.setTranslateX(10); // Platzieren des Labels am linken Rand
        scoreLabel.setTranslateY(10); // Platzieren des Labels am oberen Rand
        scoreLabel.setTextFill(Color.WHITE); // Schriftfarbe auf Weiß setzen
        root.getChildren().add(scoreLabel); // Hinzufügen des Labels zum Root-Pane

            // Erstellung des Labels für die verbleibende Zeit
         Label timeLabel = new Label("Time: 60");
        timeLabel.setTranslateX(scene.getWidth() - 100); // Platzieren des Labels am rechten Rand
         timeLabel.setTranslateY(10); // Platzieren des Labels am oberen Rand
         timeLabel.setTextFill(Color.WHITE); // Schriftfarbe auf Weiß setzen
         root.getChildren().add(timeLabel); // Hinzufügen des Labels zum Root-Pane


        // Binden der Punktzahl an das Label
        scoreLabel.textProperty().bind(model.scoreProperty().asString("Punktzahl: %d"));
        //model.setScore(model.getScore()); // aktualisiert den Score-Wert im Controller

        // In der AnimationTimer-Schleife die Position der ImageView kontinuierlich ändern
        animationTimer = new AnimationTimer() {
            private double backgroundOffset = 0;

            @Override
            public void handle(long now) {
                checkCollision();
                //Zeit
                if (!isGameOver) {
                    // ...
                    long gameTime = now - startTime;
                    if (gameTime > 60_000_000_000L) { // 60 Sekunden sind vergangen, das Spiel ist vorbei
                        stop();
                        showAlertWon("Vorbei", "Time's up!");
                    } else {
                        // verbleibende Spielzeit berechnen
                        long remainingTime = 60_000_000_000L - gameTime;
                        int remainingSeconds = (int) (remainingTime / 1_000_000_000);
                        String timeString = String.format("%02d:%02d", remainingSeconds / 60, remainingSeconds % 60);
                        timeLabel.setText("Time: " + timeString); // Zeit-Label aktualisieren
                    }
                    // ...
                }
                // Hintergrundbild nach links bewegen
                backgroundOffset -= 0.2;
                if (backgroundOffset <= -background.getImage().getWidth()) {
                    backgroundOffset = 0;
                }
                background.setTranslateX(backgroundOffset);
                addEnemy(now);
                update();
                render();
            }
        };
        animationTimer.start();
    }

    private void addEnemy(long currentTime) {
        // setze xPos auf rechten Rand
        int xPos = (int) scene.getWidth(); 
        // wähle yPos zufällig zwischen 0 und Fensterhöhe
        int yPos = new Random().nextInt((int) scene.getHeight()); 
        int width = 100;
        int height = 50;
        // setze Geschwindigkeit in horizontaler Richtung auf 5, damit der Gegner nach links fliegt
        Enemy enemy = new Enemy(100, 5, xPos, yPos, width, height); 
    
        // Normale Gegner hinzufügen
        // bis 30 Sekunden alle 1 Sekunde
        if (currentTime - startTime <= 30_000_000_000L) { 
            if (currentTime - lastEnemyTime > enemyCreationInterval) {
                enemies.add(enemy);
                lastEnemyTime = currentTime;
            }
        } else { // ab 30 Sekunden alle 0.5 Sekunden
            if (currentTime - lastEnemyTime > enemyCreationInterval && enemies.size() < 30) {
                enemies.add(enemy);
                lastEnemyTime = currentTime;
                // verkürze Intervall um 100ms
                enemyCreationInterval -= 100_000_000; 
            }
        }
    
        // Speziellen Gegner hinzufügen
        if (currentTime - startTime >= 20_000_000_000L && currentTime - startTime <= 120_000_000_000L) {
            // alle 5 Sekunden und nur maximal 5 spezielle Gegner
            if (currentTime - lastSpecialEnemyTime > 5_000_000_000L && specialEnemies.size() < 5) { 
                // setze xPos auf rechten Rand
                int specialXPos = (int) scene.getWidth(); 
                int specialYPos = (int) (Math.random() * scene.getHeight());
                int specialWidth = 100;
                int specialHeight = 100;
                Image specialEnemyImage = Math.random() < 0.5 ? new Image("/res/enemy/Ship1.png") : new Image("/res/enemy/sp.png");
                SpecialEnemy specialEnemy = new SpecialEnemy(50, 5, specialXPos, specialYPos, specialWidth, specialHeight, specialEnemyImage);
                specialEnemies.add(specialEnemy);
                enemies.add(specialEnemy);
                lastSpecialEnemyTime = currentTime;
            }
        }
    }

  //Diese Methode aktualisiert die Positionen der Feinde (Enemies) in einer Liste (ArrayList) 
  //und entfernt diejenigen, die den linken Bildschirmrand verlassen haben.
    public void update() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.move();
            if (enemy.getXPos() < -100) {
                iterator.remove();
            }
        }     
    }

    public void render() {
        gc.clearRect(0, 0, root.getWidth(), root.getHeight());
    
        for (Enemy enemy : enemies) {
            if (enemy instanceof SpecialEnemy) {
                gc.drawImage(((SpecialEnemy) enemy).getImage(), enemy.getXPos(), enemy.getYPos(), enemy.getWidth(), enemy.getHeight());
            } else {
                gc.drawImage(enemyImage, enemy.getXPos(), enemy.getYPos(), enemy.getWidth(), enemy.getHeight());
            }
        }
    }    

    public Scene getScene() {
        return scene;
    }

    public Player getPlayer() {
        return player;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public void addBullet(ImageView bullet) {
        bullets.add(bullet);
        bulletGroup.getChildren().add(bullet);
    }

    public void addBullet(ImageView bullet, ImageView secondBullet) {
        bullets.add(bullet);
        bulletGroup.getChildren().add(bullet);
        bullets.add(secondBullet);
        bulletGroup.getChildren().add(secondBullet);
    }

    public void stop() {
        animationTimer.stop();
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Bounds getBoundsInParent(ImageView imageView) {
        return imageView.getBoundsInParent();
    }

    public void checkCollision() {
        List<ImageView> bulletsToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<SpecialEnemy> specialEnemiesToRemove = new ArrayList<>();
        boolean specialEnemyHit = false;
    
        // Überprüfe, ob ein Bullet einen normalen Enemy trifft
        for (ImageView bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (getBoundsInParent(bullet).intersects(enemy.getBounds())) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    Media sound = model.getHitSound();
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                }
            }
        }
    
        // Überprüfe, ob ein Bullet den SpecialEnemy trifft
        for (ImageView bullet : bullets) {
            for (SpecialEnemy specialEnemy : specialEnemies) {
                if (getBoundsInParent(bullet).intersects(specialEnemy.getBounds())) {
                    bulletsToRemove.add(bullet);
                    specialEnemiesToRemove.add(specialEnemy);
                    specialEnemyHit = true;
                    Media sound = model.getHitSound();
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                }
            }
        }
    
        bulletGroup.getChildren().removeAll(bulletsToRemove); // entferne Kugeln aus der bulletGroup
        bullets.removeAll(bulletsToRemove); // entferne Kugeln aus der bullets-Liste
        enemies.removeAll(enemiesToRemove);
        specialEnemies.removeAll(specialEnemiesToRemove);
    
        if (specialEnemyHit) {
            model.setScore(model.getScore() + 5); // Punkte für Spezialgegner erhöhen          

        } else if (!enemiesToRemove.isEmpty()) {
            //Die Punktzahl brauche ich für die shoot-Methode im GameController
            newScore = model.getScore() + 1;
            model.setScore(newScore);
        }      
    
        if (isPlayerHit()) {
            stop();
            showAlert("Game Over", "You were hit by an enemy!");
        }
    }
 
    //Trefferbereich wird definiert, der etwas kleiner ist als der Spieler selbst, indem ein Faktor von 0,5 
    //auf die Höhe des Spielers angewendet wird. Die Methode verwendet dann diesen Trefferbereich, um zu überprüfen, 
    //ob er sich mit dem Bereich eines Feindes überschneidet.
    private boolean isPlayerHit() {
        double hitThreshold = player.getBoundsInParent().getHeight() * 0.5;
        Bounds playerBounds = new BoundingBox(
            player.getBoundsInParent().getMinX() + hitThreshold,
            player.getBoundsInParent().getMinY() + hitThreshold,
            player.getBoundsInParent().getWidth() - hitThreshold * 2,
            player.getBoundsInParent().getHeight() - hitThreshold * 2);
        
        for (Enemy enemy : enemies) {
            Bounds enemyBounds = enemy.getBounds();
            if (playerBounds.intersects(enemyBounds)) {
                return true;
            }
        }      
        return false;
    }

    public void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Du bist getroffen!");
            alert.setContentText("Game Over! Deine Punktzahl ist: " + model.getScore());
            alert.showAndWait();
        });
    }

    public void showAlertWon(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Spielende");
            alert.setHeaderText("Du bist ein Champion");
            alert.setContentText("Deine Punktzahl ist: " + model.getScore());
            alert.showAndWait();
        });
    }

    public void setBullets(ArrayList<ImageView> bullets) {
        this.bullets = bullets;
      }
      
      public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
      }

      public long getLastEnemyTime() {
        return lastEnemyTime;
    }
    
    public int getNewScore() {
        return newScore;
    }
}
