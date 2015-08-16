package com.example.planes.Engine;

import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.Viewport;

/**
 * Created by egor on 17.07.15.
 */
public class SceneTest extends junit.framework.TestCase {
    Scene scene;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        scene = new Scene(null);

    }

    public void testObjects(){
        SceneObject car = scene.createObject(10, 0, 1);
        SceneObject bike = scene.createObject(20, 0, 1);
        SceneObject apple = scene.createObject(30, 0, 1);
        SceneObject tank = scene.createObject(40, 0, 1);
        apple.remove();
        assertTrue(scene.contains(car));
        assertTrue(scene.contains(bike));
        assertTrue(scene.contains(tank));
    }

    private void assertFloat(float x, float y, float eps) {
        assertTrue(Math.abs(x - y) < eps);
    }

    public void testViewport() {

        scene.onScreenChanged(200, 100);
        Viewport view = scene.getViewport();
        view.setPosition(0, 0);
        view.setZoom(1);
        Utils.FloatPoint p = view.screenToEngine(200, 100);
        float eps = 0.01f;
        assertFloat(p.x, 2, eps);
        assertFloat(p.y, -1, eps);

    }
}
