package com.example.planes.Engine.Scene;

import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;
import com.example.planes.Engine.Engine;
import com.example.planes.Engine.SceneButtonListener;
import com.example.planes.Engine.Utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by egor on 09.07.15.
 */

public final class Scene {

    private final List<SceneObject> objects = new ArrayList<>();
    private final List<Sticker> stickers = new ArrayList<>();
    private final CollisionManager collisionManager = new CollisionManager(this);
    private final ButtonManager buttonManager = new ButtonManager(this);
    private final Viewport viewport = new Viewport();
    // количество экранов в горизонтальном периоде повторения сцены
    private float numberOfScreens = 0;
    private float[] backgroundColor = {0, 0, 0};
    private final Engine engine;

    public Scene(Engine engine) {
        Log.d("hey", "Scene() called");
        this.engine = engine;
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

    public float getWorldWidth(){
        return numberOfScreens /** viewport.screenRatio */* 2;
    }

    public void setPeriod(float numberOfScreens) {
        if(numberOfScreens < 0) throw new IllegalArgumentException("NO");
        this.numberOfScreens = numberOfScreens;
        if(numberOfScreens != 0) {
            // zigzag was updated here
        }
    }

    public void setButtonEventListner(SceneButtonListener listner) {
        buttonManager.setListener(listner);
    }

    public void onGraphicsFrame(float graphicsFPS){

        // очистка экрана
        GLES20.glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //zigzag.draw(-viewport.cameraX, -viewport.cameraY, viewport.ratioAndZoomMatrix);

        float halfHeight = viewport.getHalfHeight();
        float halfWidth = viewport.getHalfWidth();
        for(SceneObject object : objects) {
            object.onGraphicsFrame(graphicsFPS);
            if(!object.getVisible()) continue;
            float horizPeriod = getWorldWidth();
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
            sticker.onGraphicsFrame(graphicsFPS);
            if(sticker.getVisible()) sticker.draw(viewport.ratioMatrix);
        }
    }

    public void onPhysicsFrame(float physicsFPS) {
        setCanModifyObjects(false);
        for(SceneObject object : objects) {
            if(!object.hasParent()) {
                if (false && numberOfScreens != 0) {
                    float horizPeriod = getWorldWidth();
                    float x = object.getX();
                    while (x > horizPeriod / 2) x -= horizPeriod;
                    while (x < -horizPeriod / 2) x += horizPeriod;
                    object.setX(x);
                }
            }
            object.onPhysicsFrame(physicsFPS);
        }
        collisionManager.doCollisions();

        setCanModifyObjects(true);
    }

    boolean getCanModifyObjects() {
        return canModifyObjects;
    }

    public void addSticker(Sticker sticker) {
        if(stickers.contains(sticker)) throw new RuntimeException("sticker already present");
        stickers.add(sticker);
    }

    boolean canModifyObjects = true;

    private Queue<SceneObject> removeQueue = new ArrayDeque<>();

    private void removeObjectNow(SceneObject object) {
        objects.remove(object);

        //debug
        if(objects.contains(object)) throw new RuntimeException("что то не так");

        collisionManager.onObjectRemoved(object);
        //object.setScene(null);
    }

    void removeObject(SceneObject object) {
        //debug
        if(!objects.contains(object)) throw new RuntimeException("no such object");
        if(removeQueue.contains(object)) throw new RuntimeException("already queued for removal");

        if(canModifyObjects) {
            removeObjectNow(object);
        } else {
            removeQueue.add(object);
        }
    }

    public void removeSticker(Sticker sticker) {
        //debug
        if(!stickers.contains(sticker)) throw new RuntimeException("no such sticker");

        stickers.remove(sticker);
    }

    private void setCanModifyObjects(boolean canModifyObjects) {
        this.canModifyObjects = canModifyObjects;
        if(!removeQueue.isEmpty() && canModifyObjects) {
            SceneObject object = removeQueue.poll();
            while(object != null) {
                removeObjectNow(object);
                object = removeQueue.poll();
            }
        }
        if(canModifyObjects) {
            collisionManager.onCanModify();
        }
    }

    public SceneObject createObject(float x, float y, float height) {
        SceneObject object = new SceneObject(x, y, this, height);
        objects.add(object);
        return object;
    }

    public SceneObject addObject(SceneObject object) {
        if(objects.contains(object)) throw new RuntimeException();
        objects.add(object);
        return object;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void onScreenChanged(int width, int height) {
        viewport.onScreenChanged(width, height);
      //  if(numberOfScreens != 0) zigzag.setWH(getWorldWidth(), 2);
    }

//    public void addToGroup(ObjectGroup group, SceneObject object) {
//        collisionManager.addToGroup(group, object);
//    }

    public void addCollisionListener(CollisionListener listener) {
        collisionManager.addCollisionListener(listener);
    }

    public boolean onTouchEvent(int action, float x, float y, int pointerId, int vid) {
        return buttonManager.onTouch(action, x, y, pointerId, vid);
    }

    public boolean contains(SceneObject object) {
        return objects.contains(object);
    }

    public void removeCollisionListener(CollisionListener collisionListener) {
        collisionManager.removeCollisionListener(collisionListener);
    }

    public SceneButton createButton(float x, float y, Sprite sprite, float height) {
        SceneButton btn = new SceneButton(x, y, this, sprite, height);
        addButton(btn);
        return btn;
    }

    public void addButton(SceneButton btn) {
        buttonManager.addButton(btn);
        addSticker(btn);
    }

    public Sticker createSticker(float x, float y, float height) {
        Sticker sticker = new Sticker(x, y, this, height);
        stickers.add(sticker);
        return sticker;
    }

    public Engine getEngine() {
        return engine;
    }
}
