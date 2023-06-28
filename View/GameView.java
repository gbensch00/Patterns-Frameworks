package View;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Controller.LobbyController;
import Model.Enemy;
import Model.Player;
import Model.Server;
import Model.SpecialEnemy;
import Model.UpgradeItem;
import Model.User;
import Model.GameModel;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;

public class GameView {

    private Image enemyImage;
    private List<SpecialEnemy> specialEnemies = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private long lastSpecialEnemyTime = 0L;
    private Scene scene;
    private Player player;
    private Player player1;
    private Player player2;
    private GameModel model;
    private Group bulletGroup = new Group();  
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
    private HealthBar healthBar2;
    private String PlayerName1;
    private String PlayerName2 = "guest";
    private User User1;
    private List<UpgradeItem> upgradeItems = new ArrayList<>();


    //Konstruktor 2 Gideon Multiplayer
    public GameView(double width, double height, boolean multiplayer, User Player1, String Player2) {
    	this.User1 = Player1;
        this.PlayerName1 = User1.getUsername();
        this.PlayerName2 = Player2;
    	
        enemyImage = new Image("/res/enemy/Idle.png");
        player1 = new Player("/res/enemy/player1.png", startingHealth, "one");
        player1.setRotate(180);
        player1.setTranslateY(200);
        player1.setFitHeight(100);
        player1.setFitWidth(100);

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
           player2 = new Player("/res/enemy/player2.png", startingHealth, "two");
        player2.setRotate(180);
        player2.setTranslateY(400);
        player2.setFitHeight(100);
        player2.setFitWidth(100);
       
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
            healthBar2 =  new HealthBar(player2, heartsPane2);
             
        }
        
        scene = new Scene(root, width, height);
        hitSound = new MediaPlayer(model.getHitSound());
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        // Hintergrundbild erstellen und der Szene hinzufügen
        ImageView background = new ImageView(new Image("/res/enemy/planet.jpg"));
        root.getChildren().add(0, background); // Hinzufügen als unterstes Element im Pane
        background.setFitHeight(height);
        
        // Erstellung des Labels für die Punkte
        scoreLabel = new Label("Score:0");
    
        scoreLabel.setTextFill(Color.WHITE); // Schriftfarbe auf Weiß setzen      
        scoreLabel.setLayoutY(10); // Platzieren des Labels am oberen Rand
        scoreLabel.setPrefWidth(width);
        scoreLabel.setAlignment(Pos.CENTER); 
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
        if (Math.random() < 0.00025) { //  Chance, ein Upgrade-Item hinzuzufügen
            Image upgradeImage = new Image("/res/enemy/upgrade3.png");
            UpgradeItem upgradeItem = new UpgradeItem(xPos, yPos, width, height, upgradeImage);
            upgradeItems.add(upgradeItem);
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
        Iterator<UpgradeItem> upgradeItemIterator = upgradeItems.iterator();
        while (upgradeItemIterator.hasNext()) {
            UpgradeItem upgradeItem = upgradeItemIterator.next();
            upgradeItem.move();
            if (upgradeItem.getXPos() < -100) {
                upgradeItemIterator.remove();
            } else if (upgradeItem.isColliding(this.player1)) {
                upgradeItemIterator.remove();
                addGameTime(15_000_000_000L);
            }
        }

    }
    
    public void addGameTime(long nanoSeconds) {
        startTime += nanoSeconds;
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
        for (UpgradeItem upgradeItem : upgradeItems) {
            gc.drawImage(upgradeItem.getUpgradeImage(), upgradeItem.getXPos(), upgradeItem.getYPos(), upgradeItem.getWidth(), upgradeItem.getHeight());
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
            animationTimer.stop();
            // Consume "SPACE"-Event
            alert.getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    event.consume();
                }
            });
            try {
				updateHighscore();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            alert.showAndWait();
            try {
				backToMainMenu();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }

    public void showAlertWon(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Spielende");
            alert.setHeaderText("Du bist ein Champion");
            alert.setContentText("Deine Punktzahl ist: " + model.getScore());
            animationTimer.stop();
            
            // Consume "SPACE"-Event
            alert.getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    event.consume();
                }
            });
            
            try {
                updateHighscore();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            alert.showAndWait();
            try {
                backToMainMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    
    public void backToMainMenu() throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/fxml/cockpit.fxml"));
		Parent root = loader.load();
		LobbyController lobbyController = loader.getController();

		lobbyController.setLoggedInUserName(this.User1);
		Stage lobbyStage = new Stage();
		lobbyStage.setScene(new Scene(root));
		lobbyStage.show();

		// Schließe die Login-Stage
		Stage gameStage = (Stage) scene.getWindow();
		gameStage.close();
    }
    
    public void  updateHighscore() throws SQLException{
    	Server.updateHSc(PlayerName1, model.getScore());
    	System.out.println("Dein Punktestand wurde auf " +model.getScore() + " gesetzt");
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