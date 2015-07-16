package com.example.planes.Engine.Scene;

import android.opengl.GLES20;
import android.util.Log;
import com.example.planes.Engine.Utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by egor on 09.07.15.
 */

public final class Scene {

    private final List<StaticObject> objects = new ArrayList<>();
    private final List<Sticker> stickers = new ArrayList<>();
    private final CollisionManager collisionManager = new CollisionManager(this);
    // Буква Z на фоне для наглядности, показывает границы мира
    private final Zigzag zigzag = new Zigzag();
    private final Viewport viewport = new Viewport();
    // количество экранов в горизонтальном периоде повторения сцены
    private float numberOfScreens = 0;
    private float[] backgroundColor = {0, 0, 0};

    public Scene() {
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
        for(StaticObject object : objects) {
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
        for(StaticObject object : objects) {
            object.onPhysicsFrame(getHorizPeriod(), physicsFPS);
        }
        collisionManager.doCollisions();
        setCanRemoveObjects(true);
    }

    public void addObject(StaticObject object) {
        if(objects.contains(object)) throw new RuntimeException("object already present");

        objects.add(object);

    }

    public void addSticker(Sticker sticker) {
        if(stickers.contains(sticker)) throw new RuntimeException("sticker already present");
        stickers.add(sticker);
    }

    boolean canRemoveObjects = true;

    private Queue<StaticObject> removeQueue = new ArrayDeque<>();

    private void removeObjectNow(StaticObject object) {
        objects.remove(object);

        //debug
        if(objects.contains(object)) throw new RuntimeException("что то не так");

        collisionManager.onObjectRemoved(object);
    }

    public void removeObject(StaticObject object) {
        //debug
        if(!objects.contains(object)) throw new RuntimeException("no such object");
        if(removeQueue.contains(object)) throw new RuntimeException("already queued for removal");

        if(canRemoveObjects) {
            removeObjectNow(object);
        } else {
            removeQueue.add(object);
        }
    }

    private void setCanRemoveObjects(boolean canRemoveObjects) {
        this.canRemoveObjects = canRemoveObjects;
        if(!removeQueue.isEmpty() && canRemoveObjects) {
            StaticObject object = removeQueue.remove();
            while(object != null) {
                removeObjectNow(object);
            }
        }
    }

//    public SceneObject createObject(float x, float y, ObjectGroup group) {
//        SceneObject object = createObject(x, y);
//        collisionManager.addToGroup(group, object);
//        return object;
//    }

    public Viewport getViewport() {
        return viewport;
    }

    public void onScreenChanged(int width, int height) {
        viewport.onScreenChanged(width, height);
        if(numberOfScreens != 0) zigzag.setWH(getHorizPeriod(), 2);
    }

//    public void addToGroup(ObjectGroup group, StaticObject object) {
//        collisionManager.addToGroup(group, object);
//    }

    public void addCollisionListener(CollisionListener listener) {
        collisionManager.addCollisionListener(listener);
    }
}
