package com.example.planes.Engine.Scene;


import android.graphics.Bitmap;

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
    private final AbstractSceneObject so;

    private AnimatedSprite(int numFrames, float frameTime, AbstractSceneObject so, float aspectRatio) {
        super(aspectRatio);

        this.numFrames = numFrames;
        this.frameTime = frameTime;
        this.so = so;
        initUvs();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    public AnimatedSprite(AbstractSceneObject so, Bitmap bmp, int numFrames, float frameTime) {
        this(numFrames, frameTime, so, getAspectRatioBy(bmp));
        if(bmp == null) throw new NullPointerException();
        bitmap = bmp;
    }

    public AnimatedSprite(AbstractSceneObject so, int fileId, int numFrames, float frameTime) {
        this(numFrames, frameTime, so, getAspectRatioBy(fileId));
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
                if(so.isRemoveWhenAnimDone()) {
                    so.remove();
                    return;
                } else {
                    resetUvs();
                }
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
