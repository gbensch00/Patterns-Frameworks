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

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                addEnemy(now);
                update();
                render();
            }
        };
        animationTimer.start();
    }

    private void addEnemy(long currentTime) {
      if (currentTime - lastEnemyTime > enemyCreationInterval) {
          int xPos = new Random().nextInt(500);
          int yPos = -50;
          Enemy enemy = new Enemy(100, 5, xPos, yPos);
          enemies.add(enemy);
          lastEnemyTime = currentTime;
      }
  }
  
  

    public void update() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.move();
            if (enemy.getYPos() > 600) {
                iterator.remove();
            }
        }
    }

    public void render() {
        gc.clearRect(0, 0, root.getWidth(), root.getHeight());

        for (Enemy enemy : enemies) {
            gc.drawImage(enemyImage, enemy.getXPos(), enemy.getYPos());
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
