package com.example.planes.Engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by name on 08.07.15.
 */
final class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context, MyGLRenderer mRenderer){
        super(context);
        Log.d("hey", "MyGLSurfaceView called");
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

       // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

/*
        float x = e.getX();
        float y = e.getY();

        for(SceneButton button : buttons) {

        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;*/
        return true;
    }

    public void setOnClickListener(ButtonClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private List<SceneButton> buttons = new ArrayList<>();
    ButtonClickListener onClickListener = null;
    public SceneButton createButton(float x, float y) {

        SceneButton button = new SceneButton(x, y);
        buttons.add(button);
        return button;
    }
}