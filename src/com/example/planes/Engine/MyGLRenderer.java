package com.example.planes.Engine;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.Sprite;
import com.example.planes.Game.Models.Cloud;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by name on 08.07.15.
 */
public final class MyGLRenderer implements GLSurfaceView.Renderer {
    private boolean isRunning = false;
    private final static int NANO_IN_SECOND = 1000000000;
    private Engine engine;
    public static int surfaceVersion = 0;

    MyGLRenderer(Engine engine) {
        Log.d("hey", "MyGLRenderer called");
        this.engine = engine;
    }

    public void setGraphicsFPS(float fps) {
        graphStepTime = (long)(NANO_IN_SECOND / fps);
    }

    public void setPhysicsFPS(float fps) {
        physStepTime = (long)(NANO_IN_SECOND / fps);
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.d("hey", "onSurfaceCreated called");
        surfaceVersion++;
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        Sprite.onSurfaceCreated();
        TextureManager.onSurfaceCreated();
        engine.listener.onSurfaceCreated();
    }

    private long last = 0;
    private long physStepTime;
    private long graphStepTime;
    private int maxGraphBunch = 0;
    private int maxPhysBunch = 0;
    public void run1(){
        if(true) throw new RuntimeException();
        //debug
        if(isRunning) throw new RuntimeException("already running");





        fpsLastSpam = System.currentTimeMillis();
        gfpsCount = 0;
        pfpsCount = 0;
        gfpsSum = 0;
        pfpsSum = 0;
        graphDt = 0;
        physDt = 0;
        endTime = 0;
        last = System.nanoTime();
        isRunning = true;
    }
    private int c = 0;
    private boolean drawCalled = false; //debug

    private float gfpsSum = 0;
    private float pfpsSum = 0;
    private long fpsSpamRate = 2000;
    private long fpsLastSpam = 0;
    private long graphDt = 0;
    private long physDt = 0;
    private long endTime = 0;
    private int gfpsCount = 0;
    private int pfpsCount = 0;

    float maxdtt = 0;
    float lastpos = 0;
    boolean skipped = false;
    boolean even = false;
    public static GameLoop.FpsMeter gfpsMeter = new GameLoop.FpsMeter();

    public void onDrawFrame(GL10 gl) { // todo queue future?
        //if(Scene.sample != null) Scene.sample.onGraphicsFrame(60f);

        gfpsMeter.before();
        engine.onGraphicsGrame(60);
        gfpsMeter.after();

        GameLoop.lock = true;
    }
    public void onDrawFrame1(GL10 gl) {

    }





    public void onSurfaceChanged(GL10 unused, int width, int height) {
        if(height <= 0 || width <= 0) return;
        engine.onScreenChanged(width, height);
    }


    public boolean isRunning() {
        return isRunning;
    }
}
