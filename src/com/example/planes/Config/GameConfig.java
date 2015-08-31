package com.example.planes.Config;

/**
 * Created by egor on 02.07.15.
 */
public class GameConfig {
    public static final int ROUND_COUNT = 1;
    public static final int WORLD_WIDTH = 2222;
    public static final int WORLD_HEIGHT = 1111;
    public static final int FPS = 60;
    public static final int PHYSICS_FPS = 60;


    public static final float bulletDamage = 1;
    public static final float smallPlaneHealth = 3;
    public static final float cloudSpeedMin = 0.05f;
    public static final float cloudSpeedMax = 0.2f;
    public static final int TYPE_BT = 463675;


    public static float worldCeiling = 1.5f;
    public static float worldPeriod = 2f;
    public static int cloudsMin = 14;
    public static int backCloudsMin = 5;
    public static int cloudsMax = 16;

//    public static final int TYPE_NO_BT = 607;
//    public static final int TYPE_BT_SERVER = 87364;
//    public static final int TYPE_BT_CLIENT = -87364;
    public static final int noBtEnemies = 1;
//    public static int type = TYPE_NO_BT;

    public static final int numPlayersTypeNoBt = 2;
    public static final boolean collisions = true;
    public static final float cameraDistance = 0.8f;
    public static final float bulletSpeed = 3f;
    public static final float bulletPath = worldPeriod;
    public static final float planeHealth = 3;
    public static final boolean immortality = false;
    public static final float maxCameraDistance = 1;
}
