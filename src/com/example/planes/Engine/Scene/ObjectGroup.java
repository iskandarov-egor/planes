package com.example.planes.Engine.Scene;


import com.example.planes.Engine.Scene.StaticObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 04.07.15.
 */
public class ObjectGroup {

    private List<StaticObject> list;
    public ObjectGroup() {
        list = new ArrayList<>();
    }

    public void add(StaticObject object) {
        if(list.contains(object)) throw new RuntimeException("already present");
        list.add(object);
    }

    List<StaticObject> getList() {
        return list;
    }
}