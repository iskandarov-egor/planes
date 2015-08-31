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
        //setRenderMode(RENDERMODE_WHEN_DIRTY);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }
    private int id = 0;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        id++;
        log(e.getAction() & MotionEvent.ACTION_MASK, id);
        return engine.onTouchEvent(e, id);
    }

    private void log(int action, int vid) {
        int type = action & MotionEvent.ACTION_MASK;
        switch (type) {
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("multitouch madness", "v pt down" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_DOWN:
                Log.d("multitouch madness", "v down" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("multitouch madness", "v up" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("multitouch madness", "v pt up" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_HOVER_ENTER:
                Log.d("multitouch madness", "v hov enter" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                Log.d("multitouch madness", "v hov exit" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                Log.d("multitouch madness", "v hov move" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d("multitouch madness", "v outside" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("multitouch madness", "v cancel" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("multitouch madness", "v move" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_SCROLL:
                Log.d("multitouch madness", "v scroll" + String.valueOf(vid));
                break;
            default:
                Log.d("multitouch madness", "v something else" + String.valueOf(vid));
        }
    }
}