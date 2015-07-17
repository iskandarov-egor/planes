package com.example.planes;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Engine.Scene.Object;
import com.example.planes.Models.Plane;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 12.07.15.
 */
public class Game implements EngineEventsListener, SceneButtonListener {
    Engine engine;
    ObjectGroup squaresGroup;
    ObjectGroup trianglesGroup;
    StupidTestCamera camera;
    SceneButton button;
    Plane plane;

    public Game() {
        Log.d("hey", "Game()");
        //1. create scene
        this.engine = new Engine();
        engine.setGraphicsFPS(GameConfig.FPS);
        engine.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        final Scene scene = engine.getScene();
        scene.setHorizontalPeriod(1.5f);
        scene.setBackgroundColor(0, 0, 1);


        //2. create objects
        SceneObject squareObject = scene.createObject(0, 0);
        Square square = new Square();
        square.setColor(1, 1, 1);

        squareObject.setSprite(square);
        squareObject.setBody(0.1f);

        SceneObject triangleObject = scene.createObject(0.7f, 0 * 0.3f);
        triangleObject.setSprite(new Triangle());
        triangleObject.setAngle(MathHelper.PI);


        triangleObject.setBody(0.1f);


        plane = new Plane(scene, -1, 0, 0.16f, 0);


        //3. create buttons
        button = new SceneButton(-getScreenRatio() + 0.2f, 1 - 0.2f);
        scene.addButton(button);
        button.setSprite(new Square());
        button.setBody(0.1f);
        scene.setButtonEventListner(this);

        //4. create collision listeners
        trianglesGroup = new ObjectGroup(scene);
        squaresGroup = new ObjectGroup(scene);
        CollisionListener listener = new CollisionListener(squaresGroup, trianglesGroup);

        trianglesGroup.add(triangleObject);
        squaresGroup.add(squareObject);
        listener.setOnCollisionStart(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                scene.setBackgroundColor(1, 0, 0);
            }
        });
        listener.setOnCollisionEnd(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                scene.setBackgroundColor(0, 0, 1);
            }
        });
        scene.addCollisionListener(listener);

        engine.setEventsListener(this);
        camera = new StupidTestCamera(scene);
        engine.run();
    }


    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    private float getScreenRatio() {
        WindowManager wm = (WindowManager) MyApplication.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        return (float)size.x / size.y;
    }

    private int getScreenHeight(){
        WindowManager wm = (WindowManager) MyApplication.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        return size.y;
    }

    @Override
    public void onGraphicsFrame(float graphicsFPS) {
        camera.onFrame();
    }

    @Override
    public void onPhysicsFrame(float physicsFPS) {
        plane.onPhysicsFrame(physicsFPS);
    }

    public Engine getEngine() {
        return engine;
    }

    public View createView(Context context) {
        return engine.createView(context);
    }

    @Override
    public void onButtonDown(SceneButton btn) {
        if(btn == button) {
            plane.turning = 2;
        }
    }

    @Override
    public void onButtonUp(SceneButton btn, boolean pointInside) {
        if(btn == button) {
            plane.turning = 0;
        }
    }
}