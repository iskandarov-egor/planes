package com.example.planes.Engine.Scene;


import java.util.ArrayList;
import java.util.List;

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

    public void add(SceneObject object) {
        if(list.contains(object)) throw new RuntimeException("already present");
        if(!scene.contains(object)) throw new IllegalArgumentException("object is not in scene");
        list.add(object);
    }

    List<SceneObject> getList() {
        return list;
    }
}