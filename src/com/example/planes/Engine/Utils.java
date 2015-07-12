package com.example.planes.Engine;

import android.opengl.Matrix;

/**
 * Created by egor on 11.07.15.
 */
public final class Utils {
    public static final float PI2 = 2*(float)Math.PI;
    public static final float PI = (float)Math.PI;
    public static float mod(float x, float y) {
        x %= y;
        return (x < 0)?x + y:x;
    }
    public static void assertColor(float R, float G, float B) {
        if(R < 0 || G > 1) throw new IllegalArgumentException("0<rgb<1!");
        if(G < 0 || B > 1) throw new IllegalArgumentException("0<rgb<1!");
        if(B < 0 || R > 1) throw new IllegalArgumentException("0<rgb<1!");
    }

    public static float[] idMatrix = new float[16];
    static {
        Matrix.setIdentityM(idMatrix, 0);
    }
}
