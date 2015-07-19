package com.example.planes.Game;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Game.Camera.Camera;
import com.example.planes.Game.Camera.FollowingCamera;
import com.example.planes.Game.Models.Movable;
import com.example.planes.Game.Models.Plane;
import com.example.planes.Game.Models.Cloud;
import com.example.planes.R;
import com.example.planes.Utils.Helper;
import com.example.planes.Utils.MathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 12.07.15.
 */
public class Game implements EngineEventsListener, SceneButtonListener {
    private Engine engine;
    private Camera camera;
    private SceneButton buttonUp;
    private SceneButton buttonDown;
    private Plane plane;
    private List<Movable> movables = new ArrayList<>();


    public Game() {
        Log.d("hey", "Game()");
        //1. create scene
        this.engine = new Engine();
        engine.setGraphicsFPS(GameConfig.FPS);
        engine.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        final Scene scene = engine.getScene();
        float period = GameConfig.worldPeriod;
        scene.setPeriod(period);
        scene.setBackgroundColor(0, 0, 1);

        Spawner spawner = new Spawner(scene);
        // create ground
        SceneObject ground = spawner.createGround();

        //create plane
        plane = spawner.createPlane(-1, 0);
        movables.add(plane);

        //create clouds
        for(int i = 0; i < GameConfig.cloudsMin; i++) movables.add(spawner.createCloud(i));

        //create buttons
        buttonUp = scene.createButton(-Helper.getScreenRatio() + 0.2f, 0.2f);
        buttonUp.setSprite(new StaticSprite(R.drawable.btn_up, 0.2f));
        buttonUp.setBody(0.2f);
        buttonDown = scene.createButton(-Helper.getScreenRatio() + 0.2f, -0.2f);
        buttonDown.setSprite(new StaticSprite(R.drawable.btn_up, 0.2f));
        buttonDown.setAngle(MathHelper.PI);
        buttonDown.setBody(0.2f);
        scene.setButtonEventListner(this);


        engine.setEventsListener(this);
        camera = new FollowingCamera(plane);
        engine.run();
    }


    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onGLInit() {

    }

    @Override
    public void onGraphicsFrame(float graphicsFPS) {
        camera.onFrame(graphicsFPS);
    }

    @Override
    public void onPhysicsFrame(float physicsFPS) {
        for(Movable m : movables) {
            m.onPhysicsFrame(physicsFPS);
        }
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