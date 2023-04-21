import java.util.ArrayList;

import Controller.GameController;
import Model.GameModel;
import View.GameView;
import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {
   private ArrayList<ImageView> bullets;
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    GameModel model = new GameModel();
    GameView view = new GameView();
    GameController controller = new GameController(model, view);

    primaryStage.setScene(view.getScene());
    primaryStage.show();
  }
}
