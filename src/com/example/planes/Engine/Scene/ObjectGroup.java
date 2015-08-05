package com.example.planes.Engine.Scene;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by egor on 04.07.15.
 */
public class ObjectGroup {

    private List<SceneObject> list;
    private Scene scene;
    public ObjectGroup(Scene scene) {
        list = new ArrayList<>();
        this.scene = scene;
    }

    private Queue<SceneObject> removeQueue = new ArrayDeque<>();
    private Queue<SceneObject> addQueue = new ArrayDeque<>();

    public void add(SceneObject object) {
        if(list.contains(object)) throw new RuntimeException("already present");
        if(!scene.contains(object)) throw new IllegalArgumentException("object is not in scene");

        if(scene.getCanModifyObjects()) {
            list.add(object);
            object.onAddedToGroup(this);
        } else {
            addQueue.add(object);
        }

    }

    List<SceneObject> getList() {
        return list;
    }

    public Scene getScene() {
        return scene;
    }

    void onObjectRemoved(SceneObject object) {
        if(!list.contains(object)) throw new RuntimeException("wasnt here");
        if(scene.getCanModifyObjects()) {
            list.remove(object);
        } else {
            removeQueue.add(object);
        }
    }

    void onCanModify() {
        SceneObject object = removeQueue.poll();
        while(object != null) {
            list.remove(object);
            object = removeQueue.poll();
        }
        object = addQueue.poll();
        while(object != null) {
            list.add(object);
            object.onAddedToGroup(this);
            object = addQueue.poll();
        }
    }
}