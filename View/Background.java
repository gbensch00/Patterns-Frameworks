
package View;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Background {

    private Image backgroundImage;
    private double xPosition;
    private double yPosition;
    private double speed;

    public Background(String imagePath, double speed) {
        this.backgroundImage = new Image(imagePath);
        this.speed = speed;
        this.xPosition = 0;
        this.yPosition = 0;
    }

    public void move(double elapsedTime) {
        double movement = speed * elapsedTime;
        xPosition -= movement;

        if (xPosition <= -backgroundImage.getWidth()) {
            xPosition = 0;
        }
    }

    public void render(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        gc.drawImage(backgroundImage, xPosition, yPosition, canvasWidth, canvasHeight);
        if (xPosition <= 0) {
            gc.drawImage(backgroundImage, xPosition + backgroundImage.getWidth(), yPosition, canvasWidth, canvasHeight);
        }
    }
}