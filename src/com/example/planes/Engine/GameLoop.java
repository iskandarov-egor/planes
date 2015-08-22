package com.example.planes.Engine;

import android.util.Log;

/**
 * Created by egor on 17.08.15.
 */
class GameLoop extends Thread {
    boolean isRunning = false;
    public GameLoop(Engine engine, float gfps, float pfps) {
        this.engine = engine;
        setGraphicsFPS(gfps);
        setPhysicsFPS(pfps);
    }

    public void setGraphicsFPS(float graphicsFPS) {
        if (graphicsFPS <= 0) throw new IllegalArgumentException("fps");
        Log.d("hey", "GFPS set");

        graphStepTime = (long)(NANO_IN_SECOND / graphicsFPS);
    }

    public void setPhysicsFPS(float physicsFPS) {
        Log.d("hey", "PFPS set");
        if (physicsFPS <= 0) throw new IllegalArgumentException("fps");

        physStepTime = (long)(NANO_IN_SECOND / physicsFPS);
    }

    @Override
    public void run() {
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

    private void loop() {
        //debug
        if(!drawCalled) {
            Log.d("hey", "loop() first called");
            drawCalled = true;
        }

        long now = System.nanoTime();
        long dt = Math.min(NANO_IN_SECOND, (now - last));
        graphDt += dt;
        physDt += dt;

        if(physDt > physStepTime) {
            while (physDt > physStepTime) {
                physDt -= physStepTime;
                pfpsMeter.before();
                engine.onPhysicsFrame(60f);
                pfpsMeter.after();
            }
            if(physDt != 0) {
                pfpsMeter.before();
                engine.onPhysicsFrame((float)NANO_IN_SECOND / physDt);
                pfpsMeter.after();
            }
            physDt = 0;
        }
        if (graphDt > graphStepTime) {
            graphDt = 0;
            lock = false;
            engine.getView().requestRender();
        }

        if(FpsMeter.ready()) {
            Log.d("PERF", "Pfps : "+String.valueOf(pfpsMeter.result())+" Gfps : "+String.valueOf(MyGLRenderer.gfpsMeter
                    .result()));
        }
        last = now;
    }

    @Override
    public synchronized void start() {
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

        private long before;
        private float sum;
        private int count = 0;

        public void before() {
            before = System.nanoTime();
        }

        public void after() {
            sum += ((float) NANO_IN_SECOND / (System.nanoTime() - before));
            count++;
        }

        public static boolean ready() {
            return System.currentTimeMillis() - lastSpam > spamRate;
        }

        public float result() {
            float res = sum / count;
            sum = 0;
            count = 0;
            lastSpam = System.currentTimeMillis();
            return res;
        }
    }
}
