package com.example.planes.Game;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.planes.Communication.Connector;
import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.BmpConfig;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Game.Camera.Camera;
import com.example.planes.Game.Camera.FollowingCamera;
import com.example.planes.Game.Models.*;
import com.example.planes.R;
import com.example.planes.Utils.Helper;
import com.example.planes.Utils.Text.StickerText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by egor on 12.07.15.
 */
public class Game implements EngineEventsListener {
    private static Game instance = null;
    private Engine engine;
    private Camera camera;
    private Plane myPlane;
    private List<Prop> props = new ArrayList<>();
    private List<GameObject> gameObjects = new ArrayList<>();
    private List<Plane> planes = new ArrayList<>();
    private int deadCount = 0;
    private int myId;
    private static final float groundLevel;
    private Controls controls;
    private StickerText msgDead;
    ObjectGroup bulletsGroup;
    ObjectGroup planesGroup;
    private BTMessageListener messageListener = null;

    static {
        Point groundWH = Helper.getDrawableWH(R.drawable.ground);
        float h = 2 * GameConfig.worldPeriod / groundWH.x * groundWH.y;
        groundLevel = -1 + h*(1 - BmpConfig.groundLevel);
    }

    private int numPlayers;

    public static void NewGame(ArrayList<RemoteAbonent> otherPlayers, int playerId) {

        instance = new Game(playerId, otherPlayers);

    }

    public BTMessageListener getMessageListener() {
        return messageListener;
    }

    public int getMyId() {
        return myId;
    }


    private Game(int playerId, ArrayList<RemoteAbonent> otherPlayers) {
        Log.d("hey", "Game()");
        myId = playerId;
        messageListener = new BTMessageListener(this, otherPlayers);
        for(RemoteAbonent abonent : otherPlayers) {
            abonent.setListener(messageListener);
        }
        numPlayers = otherPlayers.size() + 1;

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

        SceneObject cross = scene.createObject(0, getGroundLevel());
        cross.setSprite(new StaticSprite(R.drawable.cross, 0.1f));

        //create clouds
        for (int i = 0; i < GameConfig.cloudsMin; i++) props.add(spawner.createCloud(i));

        //create planes
        float w = scene.getWorldWidth();
        planesGroup = new ObjectGroup(scene);
        bulletsGroup = new ObjectGroup(scene);
        for (int i = 0; i < numPlayers; i++) {
            float x = (numPlayers == 1)?0:-w / 2 + w * 0.1f + 0.8f * w * i / (numPlayers - 1);
            Plane plane = new Plane(scene, x, 0, 0, 0);
            plane.setVx(0.15f);
            plane.getSceneObject().setXY(plane.getX(), getGroundLevel()
                    + plane.getSceneObject().getSprite().getH()*0.5f - plane.getSceneObject().getDy());
            plane.stopEngine();
            planes.add(plane);
            gameObjects.add(plane);
            planesGroup.add(plane.getSceneObject());
        }
        myPlane = planes.get(myId);

        createOnCollisionStartListener(planesGroup, planesGroup, getPlanesVsPlanes());
        createOnCollisionStartListener(planesGroup, bulletsGroup, getPlanesVsBullets());
        camera = new FollowingCamera(myPlane);

        //init controls
        controls = new Controls(this);
        scene.setButtonEventListner(controls);

        engine.setEventsListener(this);

        engine.run();
    }

    public static Game getInstance() {
        return instance;
    }

    private void createOnCollisionStartListener(ObjectGroup group1, ObjectGroup group2, CollisionProcessor processor) {
        CollisionListener list = new CollisionListener(group1, group2);
        list.setOnCollisionStart(processor);
        getScene().addCollisionListener(list);
    }

    private Scene getScene() {
        return engine.getScene();
    }

    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onSurfaceCreated() {

    }

    @Override
    public void onGraphicsFrame(float graphicsFPS) {
        camera.onFrame(graphicsFPS);
        messageListener.processMessages();
    }

    @Override
    public void onPhysicsFrame(float fps) {
        Iterator<GameObject> i = gameObjects.iterator();
        while(i.hasNext()) {
            GameObject g = i.next();
            g.onPhysicsFrame(fps);
            if(g.isRemoved()) i.remove();
        }
        for(Prop prop : props) {
            prop.onPhysicsFrame(fps);
        }
        for(Plane plane : planes) {
            if(planeTouchingGround(plane)) {
                if(plane.getAngle() > GameConfig.landingGearAngleLeft &&
                        plane.getAngle() < GameConfig.landingGearAngleRight) {
                    killPlaneIfAlive(plane);
                } else {

                }
            }
        }
    }

    private boolean planeTouchingGround(Plane plane) {
        return plane.isTouchingGround();
        //return plane.getY()-plane.getSceneObject().getRadius() < getGroundLevel();
    }

    public Engine getEngine() {
        return engine;
    }

    public View createView(Context context) {
        return engine.createView(context);
    }

    public CollisionProcessor getPlanesVsPlanes() {
        return new CollisionProcessor() {
                @Override
                public void process(SceneObject object, SceneObject other) {
                    killPlaneIfAlive((Plane) getGameObjectBySO(object));
                    killPlaneIfAlive((Plane) getGameObjectBySO(other));
                }
            };
    }

    public CollisionProcessor getPlanesVsBullets() {
        return new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                killPlaneIfAlive((Plane) getGameObjectBySO(object));
                Bullet bullet  = (Bullet) getGameObjectBySO(other);
                bullet.onHit();
            }
        };
    }

    private void killPlaneIfAlive(Plane plane) {
        if(GameConfig.immortality) return;
        if(!plane.getAlive()) return;
        plane.die();
        deadCount++;
        int numPlayers = getNumPlayers();
        if(numPlayers - deadCount == 1 && numPlayers != 1 || deadCount == 1 && numPlayers == 1) {
            Log.d("game", "round over");
        }
        if (plane == this.myPlane) {
            if(msgDead == null ) {
                msgDead = new StickerText(engine.getScene(), "dead", 0, 0.5f, 0.1f);
            } else {
                msgDead.setVisible(true);
            }
        }
    }

    private GameObject getGameObjectBySO(SceneObject so) {
        for(GameObject g : gameObjects) {
            if(g.getSceneObject() == so) return g;
        }
        throw new RuntimeException("no such obje");
        //return null;
    }

    public static float getGroundLevel() {

        return groundLevel;
    }

    public Plane getMyPlane() {
        return myPlane;
    }

    List<Prop> getProps() {
        return props;
    }

    public ObjectGroup getBulletsGroup() {
        return bulletsGroup;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Plane getPlane(int index) {

        return planes.get(index);
    }

    private int getNumPlayers() {
        return numPlayers;
    }

    public boolean amIServer() {
        return myId == 0;
    }

}