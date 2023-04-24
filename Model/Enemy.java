    package Model;
    
    public class Enemy {
    private int health;
    private int speed;
    private int xPos;
    private int yPos;

    public Enemy(int health, int speed, int xPos, int yPos) {
        this.health = health;
        this.speed = speed;
        this.xPos = xPos;
        this.yPos = yPos;
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

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public void move() {
        yPos += speed;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }
}
