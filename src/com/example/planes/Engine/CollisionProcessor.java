package com.example.planes.Engine;

import com.example.planes.Engine.Scene.SceneObject;

/**
 * Created by egor on 04.07.15.
 */
public interface CollisionProcessor {
    public void process(SceneObject object, SceneObject other);
}
