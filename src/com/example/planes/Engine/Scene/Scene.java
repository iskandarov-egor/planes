package com.example.planes.Engine.Scene;

import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;
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

    public float getPeriod(){
        return numberOfScreens * viewport.screenRatio * 2;
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
            float horizPeriod = getPeriod();
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
        for(SceneObject object : objects) {
            if(!object.hasParent()) {
                if (false && numberOfScreens != 0) {
                    float horizPeriod = getPeriod();
                    float x = object.getX();
                    while (x > horizPeriod / 2) x -= horizPeriod;
                    while (x < -horizPeriod / 2) x += horizPeriod;
                    object.setX(x);
                }
            }
        }
        collisionManager.doCollisions();
        setCanRemoveObjects(true);
    }

    public void addSticker(Sticker sticker) {
        if(stickers.contains(sticker)) throw new RuntimeException("sticker already present");
        stickers.add(sticker);
    }

    boolean canRemoveObjects = true;

    private Queue<SceneObject> removeQueue = new ArrayDeque<>();

    private void removeObjectNow(SceneObject object) {
        objects.remove(object);

        //debug
        if(objects.contains(object)) throw new RuntimeException("что то не так");

        collisionManager.onObjectRemoved(object);
        //object.setScene(null);
    }

    public void removeObject(SceneObject object) {
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
            SceneObject object = removeQueue.poll();
            while(object != null) {
                removeObjectNow(object);
                object = removeQueue.poll();
            }
        }
    }

    public SceneObject createObject(float x, float y) {
        SceneObject object = new SceneObject(x, y, this);
        objects.add(object);
        return object;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void onScreenChanged(int width, int height) {
        viewport.onScreenChanged(width, height);
      //  if(numberOfScreens != 0) zigzag.setWH(getPeriod(), 2);
    }

//    public void addToGroup(ObjectGroup group, SceneObject object) {
//        collisionManager.addToGroup(group, object);
//    }

    public void addCollisionListener(CollisionListener listener) {
        collisionManager.addCollisionListener(listener);
    }

    public boolean onTouchEvent(MotionEvent e) {
        return buttonManager.onTouch(e);
    }

    public boolean contains(SceneObject object) {
        return objects.contains(object);
    }

    public void removeCollisionListener(CollisionListener collisionListener) {
        collisionManager.removeCollisionListener(collisionListener);
    }

    public SceneButton createButton(float x, float y) {
        SceneButton btn = new SceneButton(x, y, this);
        buttonManager.addButton(btn);
        stickers.add(btn);
        return btn;
    }

    public Sticker createSticker(float x, float y) {
        Sticker sticker = new Sticker(x, y, this);
        stickers.add(sticker);
        return sticker;
    }
}
