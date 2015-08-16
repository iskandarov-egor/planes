package com.example.planes.Engine;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.example.planes.Engine.Scene.Sprite;

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

    private long now, dt = 0;
    private long last = 0;
    private long physStepTime;
    private long graphStepTime;
    public void run(){
        //debug
        if(isRunning) throw new RuntimeException("already running");

        now = 0;
        dt = 0;
        last = System.nanoTime();
        isRunning = true;
        fpsLastSpam = System.currentTimeMillis();
        gfpsCount = 0;
        pfpsCount = 0;
        gfpsSum = 0;
        pfpsSum = 0;
    }
    private int c = 0;
    private boolean drawCalled = false; //debug

    private float gfpsSum = 0;
    private float pfpsSum = 0;
    private long fpsSpamRate = 2000;
    private long fpsLastSpam = 0;
    private int gfpsCount = 0;
    private int pfpsCount = 0;


    public void onDrawFrame(GL10 gl) {
        //debug
        if(!drawCalled) {
            Log.d("hey", "onDrawFrame first called");
            drawCalled = true;
        }


        if (isRunning) {
            now = System.nanoTime();
            dt += Math.min(NANO_IN_SECOND, (now - last));
//            try {
//                Thread.sleep((long) ((float)graphStepTime/NANO_IN_SECOND*1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            if (dt > graphStepTime) {
                while (dt > physStepTime) {
                    dt -= physStepTime;
                    long b = System.nanoTime();
                    engine.onPhysicsFrame(60f);
                    pfpsSum += ((float)NANO_IN_SECOND / (System.nanoTime() - b));
                    pfpsCount++;
                }
                long b = System.nanoTime();
                engine.onGraphicsGrame(60f);
                gfpsSum += ((float)NANO_IN_SECOND / (System.nanoTime() - b));
                gfpsCount++;
                if((System.currentTimeMillis() - fpsLastSpam > fpsSpamRate)) {
                    Log.d("PERF", "Pfps : "+String.valueOf(pfpsSum / pfpsCount)+" Gfps : "+String.valueOf(gfpsSum /
                            gfpsCount));
                    pfpsSum = 0;
                    gfpsSum = 0;
                    gfpsCount = 0;
                    pfpsCount = 0;
                    fpsLastSpam = System.currentTimeMillis();
                }
            }
            last = now;
        }
    }

    public void pause(){
        //debug
        if(!isRunning) throw new RuntimeException("was not running");

        isRunning = false;
    }



    public void onSurfaceChanged(GL10 unused, int width, int height) {
        engine.onScreenChanged(width, height);
    }


    public boolean isRunning() {
        return isRunning;
    }
}
