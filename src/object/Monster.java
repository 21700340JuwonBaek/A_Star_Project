package object;

public class Monster {
    int x = -1;
    int y = -1;
    private int checks = 0;
    private int length = 0;

    public int getChecks() {
        return checks;
    }

    public int getLength() {
        return length;
    }

    public Monster(){};

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
