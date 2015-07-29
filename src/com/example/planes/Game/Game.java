package com.example.planes.Game;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.Config;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Game.Camera.Camera;
import com.example.planes.Game.Camera.FollowingCamera;
import com.example.planes.Game.Models.Movable;
import com.example.planes.Game.Models.Plane;
import com.example.planes.R;
import com.example.planes.Utils.Helper;
import com.example.planes.Utils.MathHelper;
import com.example.planes.Utils.Text.StickerText;

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
    private SceneButton buttonStopGo;
    private SceneButton buttonFire;
    private Plane plane;
    private List<Movable> movables = new ArrayList<>();
    private List<Plane> planes = new ArrayList<>();
    private int deadCount = 0;
    private int playerId;
    private int numPlayers;
    private CollisionProcessor planesVsPlanes;
    private static final float groundLevel;
    private StaticSprite stopSprite, goSprite;
    private static Game instance;
    static {
        Point groundWH = Helper.getDrawableWH(R.drawable.ground);
        groundLevel = -1 + Helper.getScreenRatio() * GameConfig.worldPeriod / groundWH.x * groundWH.y;
    }


    public Game(List<RemoteAbonent> them, int numPlayers, int playerId) {
        this.playerId = playerId;
        this.numPlayers = numPlayers;
        Log.d("hey", "Game()");
        //1. create scene
        this.engine = new Engine();
        engine.setGraphicsFPS(GameConfig.FPS);
        engine.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        final Scene scene = engine.getScene();
        scene.setPeriod(GameConfig.worldPeriod);
        scene.setBackgroundColor(0, 0, 1);

        Spawner spawner = new Spawner(this);
        // create ground
        SceneObject ground = spawner.createGround();

        //create planes
        float w = scene.getWorldWidth();
        ObjectGroup planesGroup = new ObjectGroup(scene);
        for (int i = 0; i < numPlayers; i++) {
            Plane plane = spawner.createPlane(-w / 2 + w * 0.1f + 0.8f * w * i / (numPlayers - 1), 0);
            //ground.getY() + ground.getSprite().getH());
            planes.add(plane);
            movables.add(plane);
            planesGroup.add(plane.getSceneObject());
        }
        plane = planes.get(playerId);

        CollisionListener planesVsPlanesListener = new CollisionListener(planesGroup, planesGroup);
        planesVsPlanesListener.setOnCollisionStart(getPlanesVsPlanes());
        scene.addCollisionListener(planesVsPlanesListener);

        //create clouds
        for (int i = 0; i < GameConfig.cloudsMin; i++) movables.add(spawner.createCloud(i));



        //create buttons

        buttonDown = spawner.createButtonDown();
        buttonUp = spawner.createButtonUp();
        buttonFire = spawner.createButtonFire();
        buttonStopGo = scene.createButton(Helper.getScreenRatio() - Config.btnMargin, -Config.btnMargin);
        stopSprite = new StaticSprite(R.drawable.btn_stop, Config.btnRadius);
        goSprite = new StaticSprite(R.drawable.btn_go, Config.btnRadius);
        buttonStopGo.setSprite(stopSprite);
        buttonStopGo.setBody(Config.btnRadius);
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
        for(Plane plane : planes) {
            if(planeTouchingGround(plane)) {
                if(plane.getAngle() > GameConfig.landingGearAngleLeft &&
                        plane.getAngle() < GameConfig.landingGearAngleRight) {
                    killPlane(plane);
                } else {

                }
            }
        }
    }

    private boolean planeTouchingGround(Plane plane) {
        return plane.getY()-plane.getSceneObject().getRadius() < getGroundLevel();
    }

    public Engine getEngine() {
        return engine;
    }

    public View createView(Context context) {
        return engine.createView(context);
    }

    private boolean leftDown = false;
    private boolean rightDown = false;
    private boolean goin = true;

    @Override
    public void onButtonDown(SceneButton btn) {
        if(btn == buttonUp) {
            leftDown = true;
            plane.goLeft();
        }
        if(btn == buttonDown) {
            rightDown = true;
            plane.goRight();
        }
        if(btn == buttonStopGo) {
            goin = !goin;
            if(goin){
                buttonStopGo.setSprite(stopSprite);
                plane.startEngine();
            } else {
                buttonStopGo.setSprite(goSprite);
                plane.stopEngine();
            }
        }
        if(btn == buttonFire) {
            movables.add(plane.fire());
        }
    }

    @Override
    public void onButtonUp(SceneButton btn, boolean pointInside) {
        if(btn == buttonUp) {
            leftDown = false;
        }
        if(btn == buttonDown) {
            rightDown = false;
        }
        if(btn == buttonUp || btn == buttonDown) {
            if (rightDown) plane.goRight();
            else if (leftDown) plane.goLeft();
            else plane.goStraight();
        }
    }

    public CollisionProcessor getPlanesVsPlanes() {
        if(planesVsPlanes == null) {
            planesVsPlanes = new CollisionProcessor() {
                @Override
                public void process(SceneObject object, SceneObject other) {
                    killPlane(getPlaneBySO(object));
                    killPlane(getPlaneBySO(other));
                }
            };
        }
        return planesVsPlanes;
    }

    private void killPlane(Plane plane) {
        plane.die();
        deadCount++;
        if(numPlayers - deadCount == 1 && numPlayers != 1 || deadCount == 1 && numPlayers == 1) {
            Log.d("game", "round over");
        }
    }

    private Plane getPlaneBySO(SceneObject so) {
        for(Plane plane : planes) {
            if(plane.getSceneObject() == so) return plane;
        }
        throw new RuntimeException("no such plane");
        //return null;
    }

    public static float getGroundLevel() {
        return groundLevel;
    }
}