package com.example.planes.Engine;

import android.opengl.GLES20;
import android.util.Log;

import java.util.*;

/**
 * Created by egor on 09.07.15.
 */

final class SceneImpl implements Scene {

    private List<ObjectImpl> objects = new ArrayList<>();
    private List<Sticker> stickers = new ArrayList<>();
    private CollisionManager collisionManager = new CollisionManager(this);
    // Буква Z на фоне для наглядности, показывает границы мира
    private Zigzag zigzag = new Zigzag();
    private Viewport viewport = new Viewport();
    // количество экранов в горизонтальном периоде повторения сцены
    private float numberOfScreens = 0;
    private float[] backgroundColor = {0, 0, 0};

    public SceneImpl() {
        Log.d("hey", "Scene() called");
    }

    public void setBackgroundColor(float R, float G, float B) {
        Utils.assertColor(R, G, B);

        backgroundColor[0] = R;
        backgroundColor[1] = G;
        backgroundColor[2] = B;

    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    float getHorizPeriod(){
        return numberOfScreens * viewport.screenRatio * 2;
    }

    public void setHorizontalPeriod(float numberOfScreens) {
        if(numberOfScreens < 0) throw new IllegalArgumentException("NO");
        this.numberOfScreens = numberOfScreens;
        if(numberOfScreens != 0) zigzag.setWH(getHorizPeriod(), 2);
    }

    public void onGraphicsFrame(float graphicsFPS){

        // очистка экрана
        GLES20.glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        zigzag.draw(-viewport.cameraX, -viewport.cameraY, viewport.ratioAndZoomMatrix);

        float halfHeight = viewport.getHalfHeight();
        float halfWidth = viewport.getHalfWidth();
        for(ObjectImpl object : objects) {
            object.onGraphicsFrame(graphicsFPS);
            float horizPeriod = getHorizPeriod();
            if(horizPeriod < 0) throw new RuntimeException("period"); //debug

            float x = (object.getAbsoluteX() - viewport.cameraX);
            float y = (object.getAbsoluteY() - viewport.cameraY);

            if(horizPeriod != 0) {
                float r = (object.getRadius());
                if (y + r > -halfHeight && y - r < halfHeight) {
                    //тащим х влево экрана
                    while (x + r > horizPeriod - halfWidth) x -= horizPeriod;
                    while (x + r < -halfWidth) x += horizPeriod;
                    //цикл рисования объекта и его горизонтальных образов если они помещаются
                    while (x - r < halfWidth) {
                        object.draw(x, y, viewport.ratioAndZoomMatrix);
                        x += horizPeriod;
                    }
                }
            } else {
                object.draw(x, y, viewport.ratioAndZoomMatrix);
            }
        }

        for(Sticker sticker : stickers) {
            sticker.draw(viewport.ratioMatrix);
        }
    }

    public void onPhysicsFrame(float physicsFPS) {
        setCanRemoveObjects(false);
        for(ObjectImpl object : objects) {
            object.onPhysicsFrame(getHorizPeriod(), physicsFPS);
        }
        collisionManager.doCollisions();
        setCanRemoveObjects(true);
    }

    public SceneObject createObject(float x, float y) {
        ObjectImpl object = new ObjectImpl(x, y);
        objects.add(object);
        return object;
    }

    public Sticker createSticker(float x, float y) {
        Sticker sticker = new Sticker(x, y);
        stickers.add(sticker);
        return sticker;
    }

    boolean canRemoveObjects = true;

    private Queue<ObjectImpl> removeQueue = new ArrayDeque<>();
    public void removeObject(ObjectImpl object) {
        //debug
        if(!objects.contains(object)) throw new RuntimeException("no such object");
        if(removeQueue.contains(object)) throw new RuntimeException("already queued for removal");

        if(canRemoveObjects) {
            objects.remove(object);
        } else {
            removeQueue.add(object);
            object.setRemoved(true);
        }
    }

    public boolean canRemove() {
        return canRemoveObjects;
    }

    public void setCanRemoveObjects(boolean canRemoveObjects) {
        this.canRemoveObjects = canRemoveObjects;
        if(!removeQueue.isEmpty() && canRemoveObjects) {
            ObjectImpl object = removeQueue.remove();
            while(object != null) {
                objects.remove(object);
            }
        }
    }

    public SceneObject createObject(float x, float y, ObjectGroup group) {
        SceneObject object = createObject(x, y);
        collisionManager.addToGroup(group, object);
        return object;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void onScreenChanged(int width, int height) {
        if(numberOfScreens != 0) zigzag.setWH(getHorizPeriod(), 2);
        viewport.onScreenChanged(width, height);
    }
}
