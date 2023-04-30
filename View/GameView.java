package View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import Model.Enemy;
import Model.Player;
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

public class GameView {

    private List<Enemy> enemies;
    private Image enemyImage;
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
    private double startingHealth = 3;
    private ArrayList<ImageView> hearts = new ArrayList<>();
    private Pane heartsPane;

    public GameView(double width, double height) {
        enemyImage = new Image("/Idle.png");
        player = new Player("/player.png", startingHealth);
        player.setRotate(90);
        model = new GameModel();

        for (int i = 0; i < startingHealth; i++) {
            ImageView heart = new ImageView(new Image("heart.png"));

            heart.setTranslateX(10 + (i * 10));
            heart.setTranslateY(10);

            hearts.add(heart);
        }
        heartsPane = new Pane();
        heartsPane.getChildren().addAll(hearts);
        root = new Pane();
        root.getChildren().addAll(player, bulletGroup);
        root.getChildren().addAll(heartsPane);
        scene = new Scene(root, width, height);

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        // Hintergrundbild erstellen und der Szene hinzufügen
        ImageView background = new ImageView(new Image("/planet.jpg"));
        root.getChildren().add(0, background); // Hinzufügen als unterstes Element im Pane

        // Erstellung des Labels für die Punkte
        Label scoreLabel = new Label("Score:0");
        scoreLabel.setTranslateX(10); // Platzieren des Labels am linken Rand
        scoreLabel.setTranslateY(10); // Platzieren des Labels am oberen Rand
        scoreLabel.setTextFill(Color.WHITE); // Schriftfarbe auf Weiß setzen
        root.getChildren().add(scoreLabel); // Hinzufügen des Labels zum Root-Pane

        // Binden der Punktzahl an das Label
        scoreLabel.textProperty().bind(model.scoreProperty().asString("Punktzahl: %d"));

        // In der AnimationTimer-Schleife die Position der ImageView kontinuierlich ändern
        animationTimer = new AnimationTimer() {
            private double backgroundOffset = 0;

            @Override
            public void handle(long now) {
                checkCollision();
                // Hintergrundbild nach links bewegen
                backgroundOffset -= 1;
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
      if (currentTime - lastEnemyTime > enemyCreationInterval) {
          int xPos = (int) scene.getWidth(); // setze xPos auf rechten Rand
          int yPos = new Random().nextInt((int) scene.getHeight()); // wähle yPos zufällig zwischen 0 und Fensterhöhe
          int width = 100;
          int height = 50;
          Enemy enemy = new Enemy(100, 5, xPos, yPos, width, height); // setze Geschwindigkeit in horizontaler Richtung auf 5, damit der Gegner nach links fliegt
          enemies.add(enemy);
          lastEnemyTime = currentTime;
      }
  }

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
            gc.drawImage(enemyImage, enemy.getXPos(), enemy.getYPos(), enemy.getWidth(), enemy.getHeight());
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
        for (ImageView bullet : bullets) {
            for (Enemy enemy : enemies) {
               
                if (getBoundsInParent(bullet).intersects(enemy.getBounds())) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                }
            }
        }
        bulletGroup.getChildren().removeAll(bulletsToRemove); // entferne Kugeln aus der bulletGroup
        bullets.removeAll(bulletsToRemove); // entferne Kugeln aus der bullets-Liste
        enemies.removeAll(enemiesToRemove);
        
        if (!enemiesToRemove.isEmpty()) {
            model.setScore(model.getScore() + 1);
        }

        if (isPlayerHit()) {
            player.decreaseHealth(1);
            hearts.remove(hearts.size() - 1);
            heartsPane.getChildren().remove(heartsPane.getChildren().size() - 1);
            System.out.println(player.getHealth().getValue());
            if (player.getHealth().getValue() == 0) {
                stop();
                showAlert("Game Over", "You were hit by an enemy!");
            }
            
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
                enemies.remove(enemy);
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

    public void setBullets(ArrayList<ImageView> bullets) {
        this.bullets = bullets;
      }
      
      public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
      }
}
