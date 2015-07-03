package Utils;

/**
 * Created by egor on 02.07.15.
 */
public class MathHelper {
    public static double dist(double x1, double y1, double x2, double y2){
        return Math.hypot(x2-x1, y2-y1);
    }
    public static double dist(Vector v1, Vector v2){
        return Math.hypot(v1.x - v2.x, v1.y - v2.y);
    }
    public static Vector vectorSum(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y);
    }
}
