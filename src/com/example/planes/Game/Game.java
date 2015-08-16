package com.example.planes.Game;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import java.util.List;

/**
 * Created by egor on 12.07.15.
 */
public class Game implements EngineEventsListener {
    private static Game instance = null;
    private Engine engine;
    private Camera camera;
    private Plane myPlane;
    private List<Plane> planes = new ArrayList<>();
    private int deadCount = 0;
    private int myId;
    private static final float groundLevel;
    private Controls controls;
    ObjectGroup bulletsGroup;
    ObjectGroup planesGroup;
    private List<Player> players;
    private BTMessageListener messageListener = null;
    private int roundNumber = 0;

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
        numPlayers = otherPlayers.size() + 1;
        players = new ArrayList<>(numPlayers);
        myId = playerId;
        messageListener = new BTMessageListener(this, otherPlayers);
        for(RemoteAbonent abonent : otherPlayers) {
            abonent.setListener(messageListener);
        }

        if(GameConfig.type == GameConfig.TYPE_NO_BT) numPlayers = 2;

        //1. create scene
        this.engine = new Engine();
        engine.setGraphicsFPS(GameConfig.FPS);
        engine.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        final Scene scene = engine.getScene();
        scene.setPeriod(GameConfig.worldPeriod);
        scene.setBackgroundColor(0, 0, 1);
        msgScore = new StickerText(engine.getScene(), 0, 0.5f, 0.1f);


        Spawner spawner = new Spawner(this);
        // create ground
        SceneObject ground = spawner.createGround();

        SceneObject cross = scene.createObject(0, getGroundLevel(), 0.1f);
        cross.setSprite(new StaticSprite(R.drawable.cross));

        //create clouds
        for (int i = 0; i < GameConfig.cloudsMin; i++) spawner.createCloud(i);

        //create planes
        planesGroup = new ObjectGroup(scene);
        bulletsGroup = new ObjectGroup(scene);
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
            Plane plane = new Plane(scene, 0, 0, 0, 0);
            planes.add(plane);
            scene.addObject(plane);
            planesGroup.add(plane);
            plane.setPlayer(players.get(i));
        }
        myPlane = planes.get(myId);
        startNewRound();

        createOnCollisionStartListener(planesGroup, planesGroup, getPlanesVsPlanes());

        ObjectGroup groundGroup = new ObjectGroup(scene);
        groundGroup.add(ground);
        createOnCollisionStartListener(planesGroup, bulletsGroup, getPlanesVsBullets());
        createOnCollisionListener(planesGroup, groundGroup, getPlanesVsGround());
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

    private void createOnCollisionListener(ObjectGroup group1, ObjectGroup group2, CollisionProcessor processor) {
        CollisionListener list = new CollisionListener(group1, group2);
        list.setOnCollision(processor);
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
    public void onScreenChanged(int width, int height) {


       msgScore.onScreenChanged();

    }

    @Override
    public void onEngineReady() {

    }

    @Override
    public void onGraphicsFrame(float graphicsFPS) {
        camera.onFrame(graphicsFPS);
        messageListener.processMessages();
    }

    @Override
    public void onPhysicsFrame(float fps) {

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
                    killPlaneIfAlive((Plane) (object));
                    killPlaneIfAlive((Plane) (other));
                }
            };
    }

    public CollisionProcessor getPlanesVsBullets() {
        return new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                killPlaneIfAlive((Plane) (object));
                Bullet bullet  = (Bullet) (other);
                bullet.onHit();
            }
        };
    }

    public CollisionProcessor getPlanesVsGround() {
        return new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                Plane plane = (Plane) object;

                plane.onTouchingGround();
                if(plane.isCrashed()) {
                    killPlaneIfAlive(plane);
                }

            }
        };
    }

    StickerText msgScore;

    private void killPlaneIfAlive(Plane plane) {
        if(GameConfig.immortality) return;
        if(!plane.getAlive()) return;
        plane.die();
        deadCount++;
        int numPlayers = getNumPlayers();
        boolean oneLeft = numPlayers - deadCount == 1;

        if(oneLeft && numPlayers != 1 || deadCount == 1 && numPlayers == 1) {
            finalizeRound();
        }
        if (plane == this.myPlane) {
            for(Plane otherPlane : planes) {
                if(otherPlane != myPlane && otherPlane.getAlive()) {
                    ((FollowingCamera)camera).setPlane(otherPlane);
                    break;
                }
            }
        }
    }

    private void finalizeRound() {
        Log.d("game", "round over");
        if(roundNumber < GameConfig.ROUND_COUNT) {

            Player winner = null;
            for (Plane otherPlane : planes) {
                if (otherPlane.getAlive()) {
                    if (winner != null) throw new RuntimeException(); //debug
                    winner = otherPlane.getPlayer();
                    winner.onWin();
                }
            }
            msgScore.setText(makeScoreString());
            engine.addTimer(new Runnable() {
                @Override
                public void run() {
                    msgScore.setVisible(false);
                    startNewRound();
                }
            }, 2);
            msgScore.setVisible(true);
        } else {

        }
    }

    private void setButtonsVisible(boolean visible) {
        controls.setButtonsVisible(visible);
    }


    private void startNewRound() {
        roundNumber++;

        msgScore.setText("Round " + String.valueOf(roundNumber) + "/" + String.valueOf(GameConfig.ROUND_COUNT));
        msgScore.setVisible(true);

        placePlanes();
    }

    private void placePlanes() {
        float w = getScene().getWorldWidth();
        for (int i = 0; i < numPlayers; i++) {
            float x = (numPlayers == 1)?0:-w / 2 + w * 0.1f + 0.8f * w * i / (numPlayers - 1);
            Plane plane = planes.get(i);
            plane.setXY(x, 0);// getGroundLevel()
            //+ plane.getH() * 0.5f - plane.getDy());
            plane.stopEngine();
        }
    }

    private String makeScoreString() {
        return "Round over";
    }

    public static float getGroundLevel() {

        return groundLevel;
    }

    public Plane getMyPlane() {
        return myPlane;
    }

    public ObjectGroup getBulletsGroup() {
        return bulletsGroup;
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