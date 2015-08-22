package com.example.planes.Utils;

import java.util.Random;

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

    // повернуть точку вокруг другой точки на угол
    public static void rotate(Vector point, float centerX, float centerY, float angle) {
        float x1 = point.x - centerX;
        float y1 = point.y - centerY;
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        point.x = x1 * cos - y1 * sin + centerX;
        point.y = x1 * sin + y1 * cos + centerY;
    }

    public static float mod(float x, float y) {
        x %= y;
        return (x < 0)?x + y:x;
    }

    public static float modpi2(float x) {
        return mod(x, PI2);
    }

    public static float pullToX(float y, float by, float x) {
        if(y > x){
            y -= by;
            if(y < x) y = x;
        } else {
            y += by;
            if(y > x) y = x;
        }
        return y;
    }

    public static boolean isBetween(float x, float lim1, float lim2) {
        return x > lim1 != x >= lim2;
    }

    static Random rand = new Random();

    public static int randInt(int min, int max) {

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static float rand(float min, float max) {
        return (float) (min + Math.random()*(max - min));
    }
}
