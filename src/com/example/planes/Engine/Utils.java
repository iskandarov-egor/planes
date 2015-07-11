package com.example.planes.Engine;

/**
 * Created by egor on 11.07.15.
 */
public final class Utils {
    public static final float PI2 = 2*(float)Math.PI;
    public static float mod(float x, float y) {
        x %= y;
        return (x < 0)?x + y:x;
    }
}
