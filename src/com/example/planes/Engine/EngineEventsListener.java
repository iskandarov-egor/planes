package com.example.planes.Engine;

import android.view.MotionEvent;

/**
 * Created by egor on 15.07.15.
 */
public interface EngineEventsListener {
    void onGraphicsFrame(float graphicsFPS);
    void onPhysicsFrame(float fps);

    boolean onTouchEvent(MotionEvent e);

    void onSurfaceCreated();
}
