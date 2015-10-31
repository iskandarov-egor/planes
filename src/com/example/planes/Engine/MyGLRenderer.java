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
    private long graphStepTime;

    MyGLRenderer(Engine engine, float pfps) {
        Log.d("hey", "MyGLRenderer called");
        this.engine = engine;
        setPhysicsFPS(60);
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

    public void runIfNotRunning(){
        last = System.nanoTime();
        isRunning = true;
    }

    public static FpsMeter gfpsMeter = new FpsMeter();
    public static FpsMeter pfpsMeter = new FpsMeter();


    public void onDrawFrame(GL10 gl) {
        if(!isRunning) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            return;
        }
        long now = System.nanoTime();
        final long dt = Math.min(NANO_IN_SECOND, (now - last));
        last = now;

        pfpsMeter.before();
        engine.onPhysicsFrame((float) NANO_IN_SECOND / physStepTime);

        pfpsMeter.after();

        if (dt < physStepTime) {
            try {
                Thread.sleep((long) (1000*((float)(physStepTime - dt)/NANO_IN_SECOND)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        gfpsMeter.before();
        engine.onGraphicsGrame((float) NANO_IN_SECOND / physStepTime);
        gfpsMeter.after();

        if (FpsMeter.ready()) {
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

    static class FpsMeter {
        private static long spamRate = 2000;
        private static long lastSpam = 0;

        private long _before;
        private float sum;
        private int count = 0;
        private float _max = 0;

        public void before() {
            _before = System.nanoTime();
        }

        public void after() {
            float dt = ( (float)(System.nanoTime() - _before) / NANO_IN_SECOND);
            sum += dt;
            if(dt > _max) _max = dt;
            count++;
        }

        public static boolean ready() {
            return System.currentTimeMillis() - lastSpam > spamRate;
        }

        public float result() {
            float res = sum / (spamRate / 1000f);
            sum = 0;
            count = 0;
            lastSpam = System.currentTimeMillis();
            return res;
        }
        public float max() {
            float res = _max;
            _max = 0;
            return res;
        }
    }
}
