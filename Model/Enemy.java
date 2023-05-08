package Model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;  

public class Enemy {
private int health;
private int speed;
private int xPos;
private int yPos;
private int width;
private int height;

public Enemy(int health, int speed, int xPos, int yPos, int width, int height) {
    this.health = health;
    this.speed = speed;
    this.xPos = xPos;
    this.yPos = yPos;
    this.width = width;
    this.height = height;
}

public int getHealth() {
    return health;
}

public int getSpeed() {
    return speed;
}

public int getXPos() {
    return xPos;
}

public int getYPos() {
    return yPos;
}

public int getWidth() {
    return width;
}

public int getHeight() {
    return height;
}

public void setYPos(int yPos) {
    this.yPos = yPos;
}

public void move() {
    xPos -= speed;
}

public void takeDamage(int damage) {
    health -= damage;
}
public Bounds getBounds() {
    double x = getXPos();
    double y = getYPos();
    double width = getWidth();
    double height = getHeight();
    return new BoundingBox(x, y, width, height);
}

}