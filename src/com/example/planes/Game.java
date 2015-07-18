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
    SceneButton buttonUp;
    SceneButton buttonDown;
    Plane plane;


    public Game() {
        Log.d("hey", "Game()");
        //1. create scene
        this.engine = new Engine();
        engine.setGraphicsFPS(GameConfig.FPS);
        engine.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        final Scene scene = engine.getScene();
        scene.setHorizontalPeriod(1.6f);
        scene.setBackgroundColor(0, 0, 1);


        //2. create objects
        SceneObject squareObject = scene.createObject(0, 0);

        //square.setColor(1, 1, 1);

        squareObject.setSprite(new TextureSprite(R.drawable.ic_launcher, 0.1f));
        squareObject.setBody(0.1f);

        SceneObject triangleObject = scene.createObject(0.7f, 0 * 0.3f);
        triangleObject.setSprite(new Triangle());
        triangleObject.setAngle(MathHelper.PI);


        triangleObject.setBody(0.1f);


        plane = new Plane(scene, -1, 0, 0.16f, 0);

        plane.so.setSprite(new TextureSprite(R.drawable.plane_stub, 0.1f));

        //3. create buttons
        buttonUp = scene.createButton(-getScreenRatio() + 0.2f, 0.2f);
        buttonUp.setSprite(new TextureSprite(R.drawable.btn_up, 0.2f));
        buttonUp.setBody(0.2f);
        buttonDown = scene.createButton(-getScreenRatio() + 0.2f, -0.2f);
        buttonDown.setSprite(new TextureSprite(R.drawable.btn_up, 0.2f));
        buttonDown.setAngle(MathHelper.PI);
        buttonDown.setBody(0.2f);
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

    private int droidId = 0;
    @Override
    public void onGlInit() {
       // droid.loadTexture();
       // pin.loadTexture();
       // but.loadTexture();
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
        if(btn == buttonUp) {
            plane.turning = 2;
        }
        if(btn == buttonDown) {
            plane.turning = 1;
        }
    }

    @Override
    public void onButtonUp(SceneButton btn, boolean pointInside) {
        if(btn == buttonUp || btn == buttonDown) {
            plane.turning = 0;
        }
    }
}