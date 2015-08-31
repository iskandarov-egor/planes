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
    private final Engine engine;
    public static int surfaceVersion = 0;

    MyGLRenderer(Engine engine, float pfps) {
        Log.d("hey", "MyGLRenderer called");
        this.engine = engine;
        setPhysicsFPS(pfps);
    }

    public void setGraphicsFPS(float fps) {

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

    public void runIfNotRunning(){
        last = System.nanoTime();
        isRunning = true;
    }

    public static GameLoop1.FpsMeter gfpsMeter = new GameLoop1.FpsMeter();
    public static GameLoop1.FpsMeter pfpsMeter = new GameLoop1.FpsMeter();


    public void onDrawFrame(GL10 gl) {
        if(!isRunning) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            return;
        }
        long now = System.nanoTime();
        final long dt = Math.min(NANO_IN_SECOND, (now - last));
        last = now;
        long physDt = dt;

        int maxDiv = 3;
        int count = 0;
        while (physDt > physStepTime && count < maxDiv) {
            physDt -= physStepTime;
            pfpsMeter.before();
            engine.onPhysicsFrame((float) NANO_IN_SECOND / physStepTime);
            count++;
        }
        if (physDt != 0 && count < maxDiv) {
            engine.onPhysicsFrame((float) NANO_IN_SECOND / physDt);
            physDt = 0;
        } else if (count == maxDiv) {
            Log.d("PERF", "count == maxDiv!");
            engine.onPhysicsFrame((float) NANO_IN_SECOND / physDt);
            physDt = 0;
        }


        gfpsMeter.before();
        engine.onGraphicsGrame((float)NANO_IN_SECOND / dt);
        gfpsMeter.after();


        if (GameLoop1.FpsMeter.ready()) {
            float gr = gfpsMeter.result();

            Log.d("PERF", "Pfps : " + String.valueOf(pfpsMeter.result()) + " Gfps : " + String.valueOf(gr));
            Log.d("PERF", "MAX Pfps : " + String.valueOf(pfpsMeter.max()) + " MAX Gfps : " + String.valueOf(MyGLRenderer
                    .gfpsMeter.max()));
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        if(height <= 0 || width <= 0) return;
        engine.onScreenChanged(width, height);
    }


    public boolean isRunning() {
        return isRunning;
    }

    public void run() {
        if(isRunning()) throw new RuntimeException("already running");
        runIfNotRunning();
    }

    public void pause() {
        isRunning = false;
    }
}
