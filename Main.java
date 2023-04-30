import java.io.File;
import java.util.ArrayList;

import Controller.GameController;
import Model.GameModel;
import View.GameView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

//public class Main extends Application {
//  public static void main(String[] args) {
//    launch(args);
//  }

//  @Override
//  public void start(Stage primaryStage) throws Exception {
//    FXMLLoader loader = new FXMLLoader(new File("welcome.fxml").toURI().toURL());

//    Parent root = loader.load();
//    Scene scene = new Scene(root);
//    primaryStage.setScene(scene);
//    primaryStage.show();
//  }
//}

public class Main extends Application {
  private ArrayList<ImageView> bullets;
 public static void main(String[] args) {
   launch(args);
 }

 @Override
 public void start(Stage primaryStage) throws Exception {
   GameModel model = new GameModel();
   GameView view = new GameView(600,600);
   GameController controller = new GameController(model, view);

   primaryStage.setScene(view.getScene());
   primaryStage.show();
 }
}