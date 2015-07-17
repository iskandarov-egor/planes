package com.example.planes.Engine;

import com.example.planes.Engine.Scene.SceneButton;

/**
 * Created by egor on 16.07.15.
 */
public interface SceneButtonListener {
    void onButtonDown(SceneButton btn);
    void onButtonUp(SceneButton btn, boolean pointInside);
}
