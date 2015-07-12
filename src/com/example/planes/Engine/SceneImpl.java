package com.example.planes.Engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;
import com.example.planes.MyActivity;
import com.example.planes.Utils.AndroidCanvas;

import java.util.*;

/**
 * Created by egor on 09.07.15.
 */
public final class SceneImpl implements Scene{
    private MyGLSurfaceView view;
    private List<ObjectImpl> objects;
    private int graphicsFPS = 60;
    private int physicsFPS = 60;
    private boolean playing = false;
    MyGLRenderer gLRenderer;
    private float numberOfScreens = 0;

    public SceneImpl() {
        Log.d("hey", "Scene called");
        gLRenderer = new MyGLRenderer(this);

        objects = new ArrayList<>();
        //init camera
        setCameraPosition(0, 0);
        Matrix.setLookAtM(cameraM, 0, 0, 0, 1, 0, 0, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(transformM, 0, screenM, 0, cameraM, 0);
    }

    public void setBackgroundColor(float R, float G, float B) {
        Utils.assertColor(R, G, B);
        GLES20.glClearColor(R, G, B, 1.0f);
    }

    private float getHorizPeriod(){
        return numberOfScreens * halfWidth * 2 * currentZoom;
    }

    public void setHorizontalPeriod(float numberOfScreens) {
        this.numberOfScreens = numberOfScreens;
    }


    public void run(){
        if(view == null) throw new RuntimeException("run called before view assignment");

        Log.d("hey", "Scene.run called");

        gLRenderer.run();
    }


    private float[] cameraM = new float[16];
    private float[] screenM = new float[16];
    private float[] transformM = new float[16];


    private float cameraX = 0;
    private float cameraY = 0;

    public void onGraphicsFrame(){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        for(ObjectImpl object : objects) {

            float horizPeriod = getHorizPeriod();
            if(horizPeriod < 0) throw new RuntimeException("period"); //debug

            if(horizPeriod != 0) {
                float x = (object.getAbsoluteX() - cameraX);
                float y = (object.getAbsoluteY() - cameraY);
                float r = (object.getRadius());



                if (y + r > -halfHeight && y - r < halfHeight) {
                    //тащим х влево экрана
                    while (x + r > horizPeriod - halfWidth) x -= horizPeriod;
                    while (x + r < -halfWidth) x += horizPeriod;
                    //цикл рисования объекта и его горизонтальных образов если они помещаются
                    while (x - r < halfWidth) {

                            object.draw(x, y, transformM);

                        x += horizPeriod;
                    }
                }
            } else {
                object.draw(object.getAbsoluteX() - cameraX, object.getAbsoluteY() - cameraY, transformM);
            }
        }
    }

    public View getView(Context context) {
        if(view != null) throw new RuntimeException("view was already assigned");

        view = new MyGLSurfaceView(context, this, gLRenderer);
        return view;
    }

    private float halfWidth = 1;
    private float halfHeight = 1;
    private float currentZoom = 1;

    public void onScreenChanged(int width, int height) {
        Log.d("hey", "onScreenChanged called");
        GLES20.glViewport(0, 0, width, height);
        halfWidth = (float) width / height /currentZoom;
        halfHeight = 1f /currentZoom;

        updateScreenMatrix();
    }

    public void setCameraPosition(float x, float y) {
        cameraX = x;
        cameraY = y;
    }


    public void updateScreenMatrix() {
        Matrix.frustumM(screenM, 0, -halfWidth, halfWidth, -halfHeight, halfHeight, 1, 2);
        Matrix.multiplyMM(transformM, 0, screenM, 0, cameraM, 0);
    }


    public void zoom(float zoom) {
        if(zoom <= 0) throw new IllegalArgumentException("zoom");

        currentZoom = zoom;
        updateScreenMatrix();
    }


    public void onPhysicsFrame() {
        setCanRemove(false);
        for(ObjectImpl object : objects) {
            object.onPhysicsFrame(getHorizPeriod());

        }
        doCollisions();
        setCanRemove(true);
    }

    public SceneObject createObject(float x, float y) {
        ObjectImpl object = new ObjectImpl(x, y, this);
        objects.add(object);
        return object;
    }

    public int getPhysicsFPS() {
        return physicsFPS;
    }

/////////////////////////////COLLISIONS

    private Map<ObjectGroup, List<ObjectImpl>> groupMap = new HashMap<>();
    private List<CollisionListener> colListeners = new ArrayList<>();
    boolean canRemove = true;
    private void doCollisions() {


        for(CollisionListener listener : colListeners) {
            List<ObjectGroup> groups = listener.groups;
            for(int i = 0; i < groups.size(); i++){
                for(ObjectImpl object : groupMap.get(groups.get(i))) { // for each object in the group
                    for (int j = i + 1; j < groups.size(); j++) {
                        for (ObjectImpl other : groupMap.get(groups.get(j))) { // for each object in another group
                            if(object != other) {
                                if(object.intersects(other, getHorizPeriod())) {
                                    listener.processCollision(object, other);
                                } else {
                                    listener.processNoCollision(object, other);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private Queue<ObjectImpl> removeQueue = new ArrayDeque<>();
    public void removeObject(ObjectImpl object) {
        //debug
        if(!objects.contains(object)) throw new RuntimeException("no such object");
        if(removeQueue.contains(object)) throw new RuntimeException("no such object");

        if(canRemove) {
            objects.remove(object);
        } else {
            removeQueue.add(object);
            object.setRemoved(true);
        }
    }
    public boolean canRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
        if(!removeQueue.isEmpty() && canRemove) {
            ObjectImpl object = removeQueue.remove();
            while(object != null) {
                objects.remove(object);
            }
        }
    }

    public void addCollisionListener(CollisionListener group) {
        if(colListeners.contains(group)) throw new RuntimeException("This listener is already present");
        colListeners.add(group);
    }

///////////////////////////////GROUPS
    public void addToGroup(ObjectGroup group, SceneObject object) {
        ObjectImpl impl = object.getImpl();
        List<ObjectImpl> contents = groupMap.get(group);
        if(contents == null) contents = new LinkedList<>();

        if(contents.contains(object)) throw new RuntimeException("already present");
        contents.add(impl);
        groupMap.put(group, contents);
    }

    public SceneObject createObject(float x, float y, ObjectGroup group) {
        SceneObject object = createObject(x, y);
        addToGroup(group, object);
        return object;
    }

    public void setGraphicsFPS(int graphicsFPS) {
        if(graphicsFPS <= 0) throw new IllegalArgumentException("fps");
        this.graphicsFPS = graphicsFPS;
    }

    public void setPhysicsFPS(int physicsFPS) {
        if(physicsFPS <= 0) throw new IllegalArgumentException("fps");
        this.graphicsFPS = physicsFPS;
    }


    public int getGraphicsFPS() {
        return graphicsFPS;
    }
}
