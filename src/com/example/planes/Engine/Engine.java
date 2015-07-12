package com.example.planes.Engine;

/**
 * Created by egor on 12.07.15.
 */
public class Engine {
    static SceneImpl scene = new SceneImpl();

    public static Scene getScene() {
        return scene;
    }
}
