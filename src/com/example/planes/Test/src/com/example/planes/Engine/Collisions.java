package com.example.planes.Engine;


import com.example.planes.Engine.Scene.CollisionListener;
import com.example.planes.Engine.Scene.ObjectGroup;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;

/**
 * Created by egor on 17.07.15.
 */
public class Collisions extends junit.framework.TestCase{
    Scene scene;
    SceneObject redApple, redCar, greenApple, greenCar;
    ObjectGroup reds, greens, cars, apples;

    @Override
    protected void setUp() throws Exception {
        super.setUp();


    }

    public void before() {
        scene = new Scene(null);
        apples = new ObjectGroup(scene);
        cars = new ObjectGroup(scene);
        greens = new ObjectGroup(scene);
        reds = new ObjectGroup(scene);
        greenApple = scene.createObject(0, 0, 1);
        redApple = scene.createObject(0, 0, 1);
        redCar = scene.createObject(0, 0, 1);
        greenCar = scene.createObject(0, 0, 1);
        greenApple.setBody(1);
        redApple.setBody(1);
        redCar.setBody(1);
        greenCar.setBody(1);
        apples.add(greenApple);
        apples.add(redApple);
        cars.add(greenCar);
        cars.add(redCar);
    }

    public void testApplesVsApples() {
        before();
        greenApple.setXY(77, 77);
        redApple.setXY(77, 77);



        CollisionListener listener = new CollisionListener(apples, apples);
        final int[] countStart = new int[1];
        final int[] countEach = new int[1];
        countStart[0] = 0;
        countEach[0] = 0;
        listener.setOnCollisionStart(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                countStart[0]++;
            }
        });
        listener.setOnCollision(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                countEach[0]++;
            }
        });
        final int[] countEnd = new int[1];
        countEnd[0] = 0;
        listener.setOnCollisionEnd(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {

                countEnd[0]++;
            }
        });
        scene.addCollisionListener(listener);
        scene.onPhysicsFrame(11);
        scene.onPhysicsFrame(11);
        scene.onPhysicsFrame(11);
        assertEquals(countStart[0], 1);
        assertEquals(countEnd[0], 0);
        assertEquals(countEach[0], 3);
        greenApple.setXY(10, 10);
        scene.onPhysicsFrame(11);
        assertEquals(countStart[0], 1);
        assertEquals(countEnd[0], 1);
    }

    public void testApplesVsCars(){
        before();
        redApple.setXY(77, 77);
        greenApple.setXY(77, 77);
        greenCar.setXY(111, 111);
        redCar.setXY(111, 111);
        CollisionListener listener = new CollisionListener(apples, cars);
        final int[] countStart = new int[1];
        countStart[0] = 0;
        listener.setOnCollisionStart(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                countStart[0]++;
            }
        });
        final int[] countEnd = new int[1];
        countEnd[0] = 0;
        listener.setOnCollisionEnd(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                countEnd[0]++;
            }
        });
        scene.addCollisionListener(listener);
        scene.onPhysicsFrame(11);
        assertEquals(countStart[0], 0);
        assertEquals(countEnd[0], 0);
        greenApple.setXY(111, 111);
        scene.onPhysicsFrame(11);
        assertEquals(countStart[0], 2);
        assertEquals(countEnd[0], 0);
        greenCar.setXY(0, 0);
        scene.onPhysicsFrame(11);
        assertEquals(countStart[0], 2);
        assertEquals(countEnd[0], 1);
        redCar.setXY(77, 77);
        scene.onPhysicsFrame(11);
        assertEquals(countStart[0], 3);
        assertEquals(countEnd[0], 2);
    }

    public void testRemove(){
        before();
        redApple.setXY(111, 111);
        redCar.setXY(1111, 1111);
        CollisionListener listener = new CollisionListener(apples, cars);
        final int[] countStart = new int[1];
        countStart[0] = 0;
        listener.setOnCollisionStart(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                other.remove();
                countStart[0]++;
            }
        });
        final int[] countEnd = new int[1];
        countEnd[0] = 0;
        listener.setOnCollisionEnd(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                countEnd[0]++;
            }
        });
        scene.addCollisionListener(listener);
        scene.onPhysicsFrame(11);
        assertEquals(countStart[0], 1);
        assertEquals(countEnd[0], 1);
    }

    public void testApplesVsCarsVsReds(){
        before();
        greenApple = scene.createObject(77, 77, 1);
        redApple = scene.createObject(77, 77, 1);
        redCar = scene.createObject(111, 111, 1);
        greenCar = scene.createObject(111, 111, 1);
        greenApple.setBody(1);
        redApple.setBody(1);
        redCar.setBody(1);
        greenCar.setBody(1);
        apples.add(greenApple);
        apples.add(redApple);
        cars.add(greenCar);
        cars.add(redCar);
    }



}
