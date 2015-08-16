package com.example.planes.Config;

import com.example.planes.Game.Models.Plane;

/**
 * Created by egor on 02.07.15.
 */
public abstract class Config {
    public static final float btnMargin = 0.2f;
    public static final float btnRadius = 0.15f;
    public static final float planeHeight = 0.15f;
    public static final float bulletHeight = 0.05f;
    public static final float planeDx = 0;
    public static final float planeDy = 0;
    public static final float cloudHeight = 0.2f;

//    public static final float[][] planePolyX = {0.346f, 1.531f, 1.531f, 1.115f, 0.577f, 0.515f, 0.008f, -0.315f};
//    public static final float[] planePolyY = {-0.131f, -0.177f, 0.269f, 0.208f, 0.346f, 0.208f, 0.208f, 0.5f};

    public static final float[][] planePolyX = {{-1.031f, -0.185f, 1.0f, 1.0f}, { 0.585f, 0.046f, -0.015f},
                                    { -0.523f, -0.846f, -1.031f}};
    public static final float[][] planePolyY = {{0.162f, -0.131f, -0.177f, 0.269f}, {0.208f, 0.346f, 0.208f},
                                    {0.208f, 0.5f, 0.162f}};

}