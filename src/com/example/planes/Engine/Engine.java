package com.example.planes.Engine;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.planes.Engine.Scene.Scene;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by egor on 15.07.15.
 */
public class Engine {
    private Scene scene;
    private MyGLSurfaceView view;
    private MyGLRenderer gLRenderer;

    public Engine() {
        Log.d("hey", "Engine()");

        scene = new Scene();
        gLRenderer = new MyGLRenderer(this);
    }

    public View createView(Context context) {
        Log.d("hey", "Engine.createView called");
        view = new MyGLSurfaceView(context, gLRenderer, this);
        return view;
    }

    private boolean running = false;

    public void run() {
        //if (view == null) throw new RuntimeException("run called before view assignment");
        if(running) throw new RuntimeException("engine already running");
        running = true;
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

        //process touch events
        Event e = touchQueue.poll();
        while(e != null) {
            if(!scene.onTouchEvent(e.action, e.x, e.y, e.id, e.vid)) {
                //listener.onTouchEvent(e); //todo
            }
            e = touchQueue.poll();
        }
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
        if(running && !gLRenderer.isRunning()) gLRenderer.run();
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

    BlockingQueue<Event> touchQueue = new ArrayBlockingQueue<Event>(1, true);
    public boolean onTouchEvent(MotionEvent e, int vid) {
        final int pointerIndex = (e.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = e.getPointerId(pointerIndex);
        float x = e.getX(pointerIndex);
        float y = e.getY(pointerIndex);

        try {
            touchQueue.put(new Event(e.getAction(), pointerId, x, y, vid));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return true;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private static class Event {
        int action;
        int id, vid;
        float x, y;

        public Event(int action, int id,float x, float y, int vid) {
            this.action = action;
            this.id = id;
            this.x = x;
            this.vid = vid;
            this.y = y;
        }
    }
}