package com.example.planes.Engine;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.planes.Engine.Scene.Scene;

/**
 * Created by egor on 15.07.15.
 */
public class Engine {
    private Scene scene;
    private MyGLSurfaceView view;
    private MyGLRenderer gLRenderer;

    public Engine() {
        Log.d("hey", "activity oncreate");

        scene = new Scene();
        gLRenderer = new MyGLRenderer(this);
    }

    public View createView(Context context) {
        Log.d("hey", "Engine.createView called");
        view = new MyGLSurfaceView(context, gLRenderer, this);
        return view;
    }

    public void run() {
        //if (view == null) throw new RuntimeException("run called before view assignment");

        Log.d("hey", "Engine.run called");
        gLRenderer.run();
    }

    public void setGraphicsFPS(float graphicsFPS) {
        if (graphicsFPS <= 0) throw new IllegalArgumentException("fps");
        Log.d("hey", "GFPS set");

        gLRenderer.setGraphicsFPS(graphicsFPS);
    }

    public void setPhysicsFPS(float physicsFPS) {
        Log.d("hey", "PFPS set");
        if (physicsFPS <= 0) throw new IllegalArgumentException("fps");

        gLRenderer.setPhysicsFPS(physicsFPS);
    }



    public void onGraphicsGrame(float graphicsFPS) {
        scene.onGraphicsFrame(graphicsFPS);
        if(listener != null) listener.onGraphicsFrame(graphicsFPS);
    }

    public void onPhysicsFrame(float physicsFPS) {
        scene.onPhysicsFrame(physicsFPS);
        if(listener != null) listener.onPhysicsFrame(physicsFPS);
    }

    void onScreenChanged(int width, int height) {
        // напоминание: если будет много сцен, то им тоже надо будет сообщить
        scene.onScreenChanged(width, height);
    }

    public void onResume() {
        view.onResume();
    }

    public void onPause() {
        gLRenderer.pause();
        view.onPause();
    }

    public Scene getScene() {
        return scene;
    }

    public MyGLSurfaceView getView() {
        return view;
    }

    EngineEventsListener listener;
    public void setEventsListener(EngineEventsListener listener) {
        this.listener = listener;
    }

    public boolean onTouchEvent(MotionEvent e) {
        if(scene.onTouchEvent(e)) return true;
        return listener.onTouchEvent(e);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}