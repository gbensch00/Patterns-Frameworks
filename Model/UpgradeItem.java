package Model;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class UpgradeItem {
    private double xPos;
    private double yPos;
    private int width;
    private int height;
    private Image upgradeImage;

    public UpgradeItem(double xPos, double yPos, int width, int height, Image upgradeImage) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.upgradeImage = upgradeImage;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getUpgradeImage() {
        return upgradeImage;
    }

    public void move() {
        xPos -= 3;
    }

    public boolean isColliding(Player player) {
    	// Berechnung der Begrenzungsrechtecke
        Rectangle2D thisBounds = new Rectangle2D(xPos, yPos, width, height);
        Rectangle2D playerBounds = new Rectangle2D(
            player.getTranslateX(), 
            player.getTranslateY(), 
            player.getFitWidth(), 
            player.getFitHeight()
        );

        // Überprüfung, ob die Begrenzungsrechtecke sich überschneiden
        return thisBounds.intersects(playerBounds);
    }
}