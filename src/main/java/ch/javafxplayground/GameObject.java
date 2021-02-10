package ch.javafxplayground;

public class GameObject {
    // Position
    private double x;
    private double y;
    // Geschwindigkeit
    private double vx;
    private double vy;

    public void update(double dt) {
        setX(getX() + getVx() * dt);
        setY(getY() + getVy() * dt);
        setVx(0);
        setVy(0);
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
