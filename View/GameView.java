package View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Model.Enemy;
import Model.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameView {

    private List<Enemy> enemies = new ArrayList<>();
    private Image enemyImage;
    private Scene scene;
    private Label scoreLabel = new Label();
    private Player player;
    private ArrayList<ImageView> bullets = new ArrayList<>();
    private Group bulletGroup = new Group();

    private Pane root;
    private GraphicsContext gc;
    private AnimationTimer animationTimer;
    private long lastEnemyTime = 0;
    private long enemyCreationInterval = 1000000000; // 1 second

    public GameView(double width, double height) {
        enemyImage = new Image("enemy.png");
        player = new Player("player.png");

        root = new Pane();
        root.getChildren().addAll(player, bulletGroup);
        scene = new Scene(root, width, height);

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        // Hintergrundbild erstellen und der Szene hinzufügen
        ImageView background = new ImageView(new Image("/planet.jpg"));
        root.getChildren().add(0, background); // Hinzufügen als unterstes Element im Pane

        // In der AnimationTimer-Schleife die Position der ImageView kontinuierlich ändern
        animationTimer = new AnimationTimer() {
            private double backgroundOffset = 0;

            @Override
            public void handle(long now) {
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
}
