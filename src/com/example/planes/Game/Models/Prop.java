package com.example.planes.Game.Models;

import com.example.planes.Engine.Scene.SceneObject;

/**
 * Created by egor on 19.07.15.
 */
public interface Prop {
    void onPhysicsFrame(float fps);
    //boolean isRemoved();
    //SceneObject getSceneObject();
}
