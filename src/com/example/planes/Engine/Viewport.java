package com.example.planes.Engine;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by egor on 13.07.15.
 */
public final class Viewport {
    private float[] cameraM = new float[16];

    float[] transformM = new float[16];
    float[] stickerTransformM = new float[16];

    Viewport() {
        Matrix.setLookAtM(cameraM, 0, 0, 0, 1, 0, 0, 0f, 0f, 1.0f, 0.0f);
        updateScreenMatrix();
    }

    float cameraX = 0;
    float cameraY = 0;

    void updateScreenMatrix() {
        float[] screenM = new float[16];
        float halfWidth = getHalfWidth();
        float halfHeight = getHalfHeight();
        Matrix.frustumM(screenM, 0, -halfWidth, halfWidth, -halfHeight, halfHeight, 1, 2);
        Matrix.multiplyMM(transformM, 0, screenM, 0, cameraM, 0);
        Matrix.frustumM(screenM, 0, -screenRatio, screenRatio, -1f, 1f, 1, 2);
        Matrix.multiplyMM(stickerTransformM, 0, screenM, 0, cameraM, 0);
    }

    private float currentZoom = 1;
    float screenRatio = 1;

    float getHalfWidth(){
        return screenRatio / currentZoom;
    }

    float getHalfHeight(){
        return 1f/currentZoom;
    }

    public void setZoom(float zoom) {
        if(zoom <= 0) throw new IllegalArgumentException("zoom");
        currentZoom = zoom;
        updateScreenMatrix();

    }

    void onScreenChanged(int width, int height) {
        Log.d("hey", "onScreenChanged called");
        GLES20.glViewport(0, 0, width, height);
        screenRatio = (float) width / height;
        updateScreenMatrix();

    }


    public void setPosition(float x, float y) {
        cameraX = x;
        cameraY = y;
    }
}
