package com.example.planes.Engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

/**
 * Created by name on 08.07.15.
 */
final class MyGLSurfaceView extends GLSurfaceView {


    public MyGLSurfaceView(Context context, Scene scene, MyGLRenderer mRenderer){
        super(context);
        Log.d("hey", "MyGLSurfaceView called");
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);


        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

       // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}