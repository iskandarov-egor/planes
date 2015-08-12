package com.example.planes.Engine.Scene;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.util.Log;
import com.example.planes.Engine.MyGLRenderer;
import com.example.planes.Engine.TextureManager;
import com.example.planes.MyApplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by egor on 01.07.15.
 */
public class AnimatedSprite extends StaticSprite {
    private final float frameTime;

    private int numFrames;
    float[] uvs = new float[] {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    private AnimatedSprite(float height, float width, int numFrames, float frameTime) {
        super();
        this.numFrames = numFrames;
        this.frameTime = frameTime;

        initUvs();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }



    public AnimatedSprite(Bitmap bmp, float height, int numFrames, float frameTime) {
        this(height, getWidthBy(height, bmp), numFrames, frameTime);
        if(bmp == null) throw new NullPointerException();
        bitmap = bmp;
    }

    public AnimatedSprite(int fileId, float height, int numFrames, float frameTime) {
        this(height, getWidthBy(height, fileId), numFrames, frameTime);
        if(fileId == 0) throw new IllegalArgumentException("0 id");
        this.fileId = fileId;
    }

    private float atlasStep;

    private void initUvs(){
        resetUvs();
    }

    private void resetUvs() {
        float correct = 0;
        uvs[0] = correct;
        uvs[2] = uvs[0];
        uvs[4] = 1f / numFrames - correct;
        uvs[6] = uvs[4] - correct;
        atlasStep = 1f / numFrames;
    }
    private float timeSinceLastFrame = 0;

    @Override
    public void onFrame(float fps) {
        float dt = 1f / fps;
        timeSinceLastFrame += dt;
        while(timeSinceLastFrame > frameTime) {
            timeSinceLastFrame -= frameTime;

            if (uvs[0] + atlasStep >= 1) {
                resetUvs();
            } else {
                for (int i = 0; i < 8; i += 2) {
                    uvs[i] += atlasStep;
                }
            }
        }
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }
}
