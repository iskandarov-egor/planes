package com.example.planes.Utils;

/**
 * Created by egor on 02.07.15.
 */
public class MathHelper {
    public static float dist(float x1, float y1, float x2, float y2){
        return (float)Math.hypot(x2-x1, y2-y1);
    }
    public static float dist(Vector v1, Vector v2){
        return (float)Math.hypot(v1.x - v2.x, v1.y - v2.y);
    }
    public static Vector vectorSum(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y);
    }
    public static float PI2 = (float)Math.PI*2;
    public static float PI = (float)Math.PI;

    public static void rotate(Vector point, float centerX, float centerY, float angle, float R) {
        double x1 = point.x - centerX;
        double y1 = point.y - centerY;

        point.x = (float)(x1 * Math.cos(angle) - y1 * Math.sin(angle)) + centerX;
        point.y = (float)(x1 * Math.sin(angle) + y1 * Math.cos(angle)) + centerY;
    }
}
