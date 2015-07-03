package Utils;

/**
 * Created by egor on 02.07.15.
 */
public class Vector {
    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vector) {
        x = vector.x;
        y = vector.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vector getOrt() {
        double len = getLength();
        return new Vector(x / len, y / len);
    }

    public double getLength() {
        return Math.hypot(x, y);
    }
    public void add(Vector vector) {
        x += vector.x;
        y += vector.y;
    }
    public void multiply(double by) {
        x *= by;
        y *= by;
    }

    public void add(double dx, double dy) {
        x += dx;
        y += dy;
    }
}
