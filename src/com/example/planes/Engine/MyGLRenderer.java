package com.example.planes.Engine;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.example.planes.Engine.Sprite.OpenGLShape;
import com.example.planes.Engine.Sprite.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by name on 08.07.15.
 */
final class MyGLRenderer implements GLSurfaceView.Renderer {
    // todo background color
    private boolean isRunning = false;
    private final static int NANO_IN_SECOND = 1000000000;
    private final SceneImpl scene;
    private Runnable onGraphicsFrameCallback = null;
    private Runnable onPhysicsFrameCallback = null;

    public MyGLRenderer(SceneImpl scene) {
        //debug
        Log.d("hey", "MyGLRenderer called");
        if(scene == null) throw new NullPointerException("scene");

        this.scene = scene;
        physStepTime = 60;
        graphStepTime = 60;
    }

    public void setGraphicsFPS(float fps) {
        graphStepTime = (long)(NANO_IN_SECOND / fps);
    }

    public void setPhysicsFPS(float fps) {
        physStepTime = (long)(NANO_IN_SECOND / fps);
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.d("hey", "onSurfaceCreated called");
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        OpenGLShape.onSurfaceCreated();
    }

    long now, dt = 0;
    long last = 0;
    long physStepTime;
    long graphStepTime;
    public void run(){
        //debug
        if(isRunning) throw new RuntimeException("already running");

        now = 0;
        dt = 0;
        last = System.nanoTime();
        isRunning = true;
    }
    private int c = 0;
    private boolean drawCalled = false; //debug
    public void onDrawFrame(GL10 gl) {
        //debug
        if(!drawCalled) {
            Log.d("hey", "onDrawFrame first called");
            drawCalled = true;
        }
        c++;
        if(c > 211111) {

            Log.d("hey", "onDrawFrame running");
            c = 0;
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
                    scene.onPhysicsFrame();
                }
                scene.onGraphicsFrame();
                if(onGraphicsFrameCallback != null) onGraphicsFrameCallback.run();
            }
            last = now;
        }
    }

    public void pause(){
        //debug
        if(!isRunning) throw new RuntimeException("was not running");

        isRunning = false;
    }

    public void setOnGraphicsFrameCallback(Runnable onGraphicsFrameCallback) {
        //debug
        if(onGraphicsFrameCallback == null) throw new NullPointerException("bug!");

        this.onGraphicsFrameCallback = onGraphicsFrameCallback;
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        scene.getViewport().onScreenChanged(width, height);
        scene.onScreenChanged(width, height);
    }

    public static int shaderCounter = 0;

}
