package com.example.planes.Engine;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

/**
 * Created by egor on 12.07.15.
 */

public final class Engine {
    static SceneImpl scene;
    private static MyGLSurfaceView view;
    private static MyGLRenderer gLRenderer;
    private static float physicsFPS = 60;
    private static float graphicsFPS = 60;
    static {
        Log.d("hey", "Engine static init");
        scene = new SceneImpl();
        gLRenderer = new MyGLRenderer();
    }

    public static CollisionManager getCollisionManager() {
        return scene.getCollisionManager();
    }

    public static View getView(Context context) {
        if(view != null) throw new RuntimeException("view was already assigned");
        view = new MyGLSurfaceView(context, gLRenderer);
        return view;
    }
    public static Scene getScene() {
        return scene;
    }

    public static void run(){
        if(view == null) throw new RuntimeException("run called before view assignment");

        Log.d("hey", "Engine.run called");
        gLRenderer.run();
    }

    public static void pause() {
        gLRenderer.pause();
    }

    public static void onPause() {
        view.onPause();
    }

    public static void onResume() {
        view.onResume();
    }

    public static void setGraphicsFPS(float graphicsFPS) {
        if(graphicsFPS <= 0) throw new IllegalArgumentException("fps");
        Log.d("hey", "GFPS set");
        gLRenderer.setGraphicsFPS(graphicsFPS);
    }

    // todo не влияет на скорость и вращение объектов
    public static void setPhysicsFPS(float physicsFPS) {
        Log.d("hey", "PFPS set");
        if(physicsFPS <= 0) throw new IllegalArgumentException("fps");
        gLRenderer.setPhysicsFPS(physicsFPS);
    }

    public static float getPhysicsFPS() {
        return physicsFPS;
    }

    public static float getGraphicsFPS() {
        return graphicsFPS;
    }

    public static void setOnGraphicsFrameCallback(Runnable callback){
        gLRenderer.setOnGraphicsFrameCallback(callback);
    }

    static TouchEventListener touchEventListener;

    public static void setTouchEventListener(TouchEventListener touchEventListener) {
        Engine.touchEventListener = (touchEventListener);
    }

    public static void onGraphicsFrame() {
        scene.onGraphicsFrame(graphicsFPS);
    }

    static void onPhysicsFrame() {
        scene.onPhysicsFrame(physicsFPS);
    }

    static void onScreenChanged(int width, int height) {
        // напоминание: если будет много сцен, то им тоже надо будет сообщить
        scene.onScreenChanged(width, height);
    }
}

