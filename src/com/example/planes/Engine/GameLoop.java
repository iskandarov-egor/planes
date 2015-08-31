package com.example.planes.Engine;

import android.util.Log;
import com.example.planes.Engine.Scene.Scene;

/**
 * Created by egor on 17.08.15.
 */
class GameLoop1 extends Thread {
    boolean isRunning = false;
    float pfps;
    public GameLoop1(Engine engine, float gfps, float pfps) {
        this.engine = engine;
        setGraphicsFPS(gfps);
        setPhysicsFPS(pfps);
        this.pfps = pfps;
    }

    public void setGraphicsFPS(float graphicsFPS) {
        if (graphicsFPS <= 0) throw new IllegalArgumentException("fps");
        Log.d("hey", "GFPS set");

        graphStepTime = (long)(NANO_IN_SECOND / graphicsFPS);
        engine.gLRenderer.setGraphicsFPS(graphicsFPS);
    }

    public void setPhysicsFPS(float physicsFPS) {
        Log.d("hey", "PFPS set");
        if (physicsFPS <= 0) throw new IllegalArgumentException("fps");

        physStepTime = (long)(NANO_IN_SECOND / physicsFPS);
        engine.gLRenderer.setPhysicsFPS(physicsFPS);
    }

    @Override
    public void run() {

        if(true) return;
        waitMeter.before();
        while(isRunning) {
            if(lock) {
                loop();
            }
        }
    }
    public static boolean lock = true;
    final Engine engine;
    boolean drawCalled = false;
    long last = 0;
    long graphDt = 0;
    long physDt = 0;
    long physStepTime = 0;
    long graphStepTime = 0;

    private final static int NANO_IN_SECOND = 1000000000;

    private FpsMeter pfpsMeter = new FpsMeter();
    private FpsMeter logMeter = new FpsMeter();
    public  static FpsMeter waitMeter = new FpsMeter();
    public  static FpsMeter returnMeter = new FpsMeter();

    private boolean requested = false;


    private void loop() {
        returnMeter.after();
        //debug
        if(!drawCalled) {
            Log.d("hey", "loop() first called");
            drawCalled = true;
        }

        long now = System.nanoTime();
        long dt = Math.min(NANO_IN_SECOND, (now - last));
        graphDt += dt;
        physDt += dt;
        boolean skipping = false;

        if(physDt > physStepTime) {
            while (physDt > physStepTime) {
                skipping = true;
                physDt -= physStepTime;
                pfpsMeter.before();
                //engine.onPhysicsFrame(pfps);
                if(Scene.sample != null) Scene.sample.onPhysicsFrame(pfps);
                pfpsMeter.after();
            }
            if(physDt != 0) {
                pfpsMeter.before();
                //engine.onPhysicsFrame((float)NANO_IN_SECOND / physDt);
                if(Scene.sample != null) Scene.sample.onPhysicsFrame((float)NANO_IN_SECOND / physDt);
                pfpsMeter.after();
            }
            physDt = 0;
        }
        long nextFrameEstimate = now + physStepTime;
        if (graphDt > graphStepTime && !skipping) { // && System.nanoTime() < nextFrameEstimate) {
            graphDt = 0;
            lock = false;
            waitMeter.before();
            engine.getView().requestRender();
        }


        last = now;

    }

    @Override
    public synchronized void start() {
        if(getState() != State.NEW) return;
        isRunning = true;
        graphDt = 0;
        physDt = 0;
        last = System.nanoTime();

         super.start();
    }

    public void pause() {
        //debug
        if(!isRunning) throw new RuntimeException("was not running");
        isRunning = false;
    }


    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
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
