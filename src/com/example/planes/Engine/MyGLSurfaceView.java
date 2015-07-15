package com.example.planes.Engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by name on 08.07.15.
 */
final class MyGLSurfaceView extends GLSurfaceView {
    private final Engine engine;

    public MyGLSurfaceView(Context context, MyGLRenderer mRenderer, Engine engine){
        super(context);
        this.engine = engine;
        Log.d("hey", "MyGLSurfaceView called");
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return engine.onTouchEvent(e);
    }
}