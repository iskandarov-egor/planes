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

    private boolean isRunning = false;
    private final static int NANO_IN_SECOND = 1000000000;
    private final SceneImpl scene;

    public MyGLRenderer(SceneImpl scene) {
        //debug
        Log.d("hey", "MyGLRenderer called");
        if(scene == null) throw new NullPointerException("scene");

        this.scene = scene;
        physStepTime = NANO_IN_SECOND / scene.getPhysicsFPS();
        graphStepTime = NANO_IN_SECOND / scene.getGraphicsFPS();
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

    private boolean drawCalled = false; //debug
    public void onDrawFrame(GL10 gl) {
        //debug
        if(!drawCalled) {
            Log.d("hey", "onDrawFrame first called");
            drawCalled = true;
        }

        if (isRunning) {
            now = System.nanoTime();
            dt += Math.min(NANO_IN_SECOND, (now - last));
            if (dt > graphStepTime) {
                while (dt > physStepTime) {
                    dt -= physStepTime;
                    scene.onPhysicsFrame();
                }
                scene.onGraphicsFrame();
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
        scene.onScreenChanged(width, height);
    }

    public static int shaderCounter = 0;

}
