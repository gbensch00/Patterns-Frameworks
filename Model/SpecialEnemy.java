package Model;

import javafx.scene.image.Image;

public class SpecialEnemy extends Enemy {
    private Image image;
    private int xVelocity;
    private double yVelocity;
    private int rotate; 

    public SpecialEnemy(int health, int speed, int xPos, int yPos, int width, int height, Image image) {
        super(health, speed, xPos, yPos, width, height);
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }
}


