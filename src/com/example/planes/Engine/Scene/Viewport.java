package com.example.planes.Engine.Scene;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import com.example.planes.Engine.Utils;


/**
 * Created by egor on 13.07.15.
 */
public final class Viewport {
    // для учета вытянутости экрана
    final float[] ratioMatrix = new float[16];

    // то же, но у учетом зума
    final float[] ratioAndZoomMatrix = new float[16];

    // матрица направления камеры
    final private float[] cameraM = new float[16];

    Viewport() {
        Matrix.setLookAtM(cameraM, 0, 0, 0, 1, 0, 0, 0f, 0f, 1.0f, 0.0f);
        updateScreenMatrix();
    }

    float cameraX = 0;
    float cameraY = 0;

    private void updateScreenMatrix() {
        float[] screenM = new float[16];
        float halfWidth = getHalfWidth();
        float halfHeight = getHalfHeight();
        Matrix.frustumM(screenM, 0, -halfWidth, halfWidth, -halfHeight, halfHeight, 1, 2);
        Matrix.multiplyMM(ratioAndZoomMatrix, 0, screenM, 0, cameraM, 0);
        Matrix.frustumM(screenM, 0, -screenRatio, screenRatio, -1f, 1f, 1, 2);
        Matrix.multiplyMM(ratioMatrix, 0, screenM, 0, cameraM, 0);
    }

    float currentZoom = 1;
    float screenRatio = 1;

    // размеры экрана в пикселиях
    int screenWidth;
    int screenHeight;

    // полуширина экрана с учетом зума
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

        this.screenWidth = width;
        this.screenHeight = height;
        GLES20.glViewport(0, 0, width, height);
        screenRatio = (float) width / height;

        updateScreenMatrix();
    }

    public void setPosition(float x, float y) {
        cameraX = x;
        cameraY = y;
    }

    public Utils.FloatPoint screenToEngine(float x, float y) {
        return new Utils.FloatPoint(2*(x / screenHeight - 0.5f*screenRatio),
                2*(0.5f - y / screenHeight));
    }
}
