package com.example.planes.Engine;

import com.example.planes.Engine.Scene.StaticObject;

/**
 * Created by egor on 04.07.15.
 */
public interface CollisionProcessor {
    public void process(StaticObject object, StaticObject other);
}
