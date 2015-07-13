package com.example.planes.Engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;
import com.example.planes.Engine.Sprite.StaticSprite;
import com.example.planes.Engine.Sprite.Zigzag;

import java.util.*;

/**
 * Created by egor on 09.07.15.
 */
final class SceneImpl implements Scene{

    private List<ObjectImpl> objects = new ArrayList<>();
    private List<Sticker> stickers = new ArrayList<>();
    CollisionManager collisionManager = new CollisionManager(this);

    private boolean playing = false;

    Viewport viewport = new Viewport();
    private float numberOfScreens = 0;

    Zigzag background = new Zigzag();
    public SceneImpl() {
        Log.d("hey", "Scene called");

        //init camera
        setCameraPosition(0, 0);

        //Matrix.multiplyMM(transformM, 0, screenM, 0, cameraM, 0);

    }

    public void setBackgroundColor(float R, float G, float B) {
        Utils.assertColor(R, G, B);
        GLES20.glClearColor(R, G, B, 1.0f);
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public float getHorizPeriod(){
        return numberOfScreens * viewport.screenRatio * 2;
    }

    public void setHorizontalPeriod(float numberOfScreens) {
        this.numberOfScreens = numberOfScreens;
    }

    @Override
    public SceneButton createButton(float x, float y) {
        return null;//view.createButton(x, y);
    }

    public void setOnClickListener(ButtonClickListener onClickListener) {
       // view.setOnClickListener(onClickListener);
    }

    public void onGraphicsFrame(){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        background.draw(-viewport.cameraX, -viewport.cameraY, viewport.transformM);

        float halfHeight = viewport.getHalfHeight();
        float halfWidth = viewport.getHalfWidth();
        for(ObjectImpl object : objects) {

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

                            object.draw(x, y, viewport.transformM);

                        x += horizPeriod;
                    }
                }
            } else {
                object.draw(x, y, viewport.transformM);
            }
        }

        for(Sticker sticker : stickers) {
            sticker.draw(viewport.stickerTransformM);
        }
    }



    public void setCameraPosition(float x, float y){
        viewport.cameraX = x;
        viewport.cameraY = y;
    }

    public void onPhysicsFrame() {
        setCanRemoveObjects(false);
        for(ObjectImpl object : objects) {
            object.onPhysicsFrame(getHorizPeriod());

        }
        collisionManager.doCollisions();
        setCanRemoveObjects(true);
    }

    public SceneObject createObject(float x, float y) {
        ObjectImpl object = new ObjectImpl(x, y, this);
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
        if(removeQueue.contains(object)) throw new RuntimeException("no such object");

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
        background.setWH(width, height);
    }
}
