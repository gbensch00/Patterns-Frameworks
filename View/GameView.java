package View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import Model.Enemy;
import Model.Player;
import Model.SpecialEnemy;
import Model.GameModel;
import Model.HealthBar;
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
import javafx.scene.media.MediaPlayer;

public class GameView {

    private Image enemyImage;
    private List<SpecialEnemy> specialEnemies = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private long lastSpecialEnemyTime = 0L;
    private Scene scene;
    private Player player;
    public Player player1;
    public Player player2;
    private GameModel model;
    public Group bulletGroup = new Group();  
    private Pane root;
    private GraphicsContext gc;
    private AnimationTimer animationTimer;
    private long lastEnemyTime = 0;
    private long enemyCreationInterval = 1000000000; // 1 second
    private long startTime = System.nanoTime();
    private boolean isGameOver = false; 
    private MediaPlayer hitSound;
    private Label scoreLabel;
    private int startingHealth = 3;
    private ArrayList<ImageView> hearts = new ArrayList<>();
    private ArrayList<ImageView> hearts2 = new ArrayList<>();
    private Pane heartsPane;
    private Pane heartsPane2;
    private HealthBar healthBar;
    private String PlayerName1;
    private String PlayerName2 = "guest";

    //Konstruktor 1 Tobi
    public GameView(double width, double height, String playername) {
        
    	this.PlayerName1 = playername;
    	System.out.println("Hello " + playername + " ready to kick some asses ?");
    	
       	
    	enemyImage = new Image("/res/enemy/Idle.png");
        player = new Player("/res/enemy/player.png", startingHealth);
        player.setRotate(90);
        model = new GameModel();

        for (int i = 0; i < startingHealth; i++) {
            ImageView heart = new ImageView(new Image("/res/oberflaechen/heart.png"));

            heart.setTranslateX(10 + (i * 10));
            heart.setTranslateY(10);

            hearts.add(heart);
        }
        heartsPane = new Pane();
        heartsPane.getChildren().addAll(hearts);
        root = new Pane();
        root.getChildren().addAll(player, bulletGroup);
        root.getChildren().addAll(heartsPane);
        healthBar = new HealthBar(player, heartsPane);
        scene = new Scene(root, width, height);
        hitSound = new MediaPlayer(model.getHitSound());
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

        // In der AnimationTimer-Schleife die Position der ImageView kontinuierlich ändern
        animationTimer = new AnimationTimer() {
            private double backgroundOffset = 0;

            @Override
            public void handle(long now) {
                
                //Zeit
                if (!isGameOver) {
                
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

    //Konstruktor 2 Gideon Multiplayer
    public GameView(double width, double height, boolean multiplayer) {
        enemyImage = new Image("/res/enemy/Idle.png");
        player1 = new Player("/res/enemy/player.png", startingHealth);
        player1.setRotate(90);
        player1.setTranslateY(200);

        model = new GameModel();
        enemies = new ArrayList<>();
        for (int i = 0; i < startingHealth; i++) {
            ImageView heart = new ImageView(new Image("/res/oberflaechen/heart.png"));
            heart.setTranslateX(10 + (i * 10));
            heart.setTranslateY(10);
            hearts.add(heart);
        }
        heartsPane = new Pane();
        heartsPane.getChildren().addAll(hearts);
       
        root = new Pane();
        root.getChildren().addAll(player1, bulletGroup);
        root.getChildren().addAll(heartsPane);
        healthBar = new HealthBar(player1, heartsPane);
        if (multiplayer == true) {
            player2 = new Player("/res/enemy/player.png", startingHealth);
            player2.setRotate(90);
            player2.setTranslateY(300);
            for (int i = 0; i < startingHealth; i++) {
            ImageView heart2 = new ImageView(new Image("/res/oberflaechen/heart.png"));

            heart2.setTranslateX(10 + (i * 10));
            heart2.setTranslateY(40);

            hearts2.add(heart2);
        }
            heartsPane2 = new Pane();
            heartsPane2.getChildren().addAll(hearts2);
            root.getChildren().addAll(player2);
            root.getChildren().addAll(heartsPane2);
        }
        scene = new Scene(root, width, height);
        hitSound = new MediaPlayer(model.getHitSound());
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        // Hintergrundbild erstellen und der Szene hinzufügen
        ImageView background = new ImageView(new Image("/res/enemy/planet.jpg"));
        root.getChildren().add(0, background); // Hinzufügen als unterstes Element im Pane

        
        // Erstellung des Labels für die Punkte
        scoreLabel = new Label("Score:0");
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

        // In der AnimationTimer-Schleife die Position der ImageView kontinuierlich ändern
        animationTimer = new AnimationTimer() {
            private double backgroundOffset = 0;

            @Override
            public void handle(long now) {
                
                //Zeit
                if (!isGameOver) {
                
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
        int xPos = (int) scene.getWidth();
        int yPos = new Random().nextInt((int) scene.getHeight());
        int width = 100;
        int height = 50;
    
        // Normale Gegner hinzufügen
        if (currentTime - startTime <= 30_000_000_000L) {
            if (currentTime - lastEnemyTime > enemyCreationInterval) {
                addNormalEnemy(xPos, yPos, width, height);
            }
        } else {
            if (currentTime - lastEnemyTime > enemyCreationInterval && enemies.size() < 30) {
                addNormalEnemy(xPos, yPos, width, height);
                enemyCreationInterval -= 100_000_000;
            }
        }
    
        // Spezielle Gegner hinzufügen
        if (currentTime - startTime >= 20_000_000_000L && currentTime - startTime <= 120_000_000_000L) {
            if (currentTime - lastSpecialEnemyTime > 5_000_000_000L && specialEnemies.size() < 5) {
                addSpecialEnemy(xPos, width, height);
            }
        }
    }
    
    private void addNormalEnemy(int xPos, int yPos, int width, int height) {
        Enemy enemy = new Enemy(100, 5, xPos, yPos, width, height);
        enemies.add(enemy);
        lastEnemyTime = System.nanoTime();
    }
    
    private void addSpecialEnemy(int xPos, int width, int height) {
        int specialXPos = xPos;
        int specialYPos = (int) (Math.random() * scene.getHeight());
        int specialWidth = 100;
        int specialHeight = 100;
        Image specialEnemyImage = Math.random() < 0.5 ? new Image("/res/enemy/Ship1.png") : new Image("/res/enemy/sp.png");
        SpecialEnemy specialEnemy = new SpecialEnemy(50, 5, specialXPos, specialYPos, specialWidth, specialHeight, specialEnemyImage);
        specialEnemies.add(specialEnemy);
        enemies.add(specialEnemy);
        lastSpecialEnemyTime = System.nanoTime();
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
public void updateSecondHealthBar(int health) {

    int heartsToRemove = hearts2.size() - health;
    if (heartsToRemove > 0) {
        hearts2.subList(hearts2.size() - heartsToRemove, hearts2.size()).clear();
        heartsPane2.getChildren().clear();
        heartsPane2.getChildren().addAll(hearts2);
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

    public void stop() {
        animationTimer.stop();
    }


    public Bounds getBoundsInParent(ImageView imageView) {
        return imageView.getBoundsInParent();
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

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }
    
    public void setSpecialEnemies(List<SpecialEnemy> specialEnemies) {
        this.specialEnemies = specialEnemies;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
    
    public List<SpecialEnemy> getSpecialEnemies() {
        return specialEnemies;
    }
    
    public Group getBulletGroup() {
        return bulletGroup;
    }
    
    public Scene getScene() {
        return scene;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }


    public void setScoreLabel(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void setModel(GameModel model) {
        this.model = model;
    }
}