package com.example.planes.Game;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import com.example.planes.Communication.Message.LoadedMessage;
import com.example.planes.Config.BmpConfig;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Game.Camera.Camera;
import com.example.planes.Game.Camera.FollowingCamera;
import com.example.planes.Game.Models.*;
import com.example.planes.Interface.MyActivity;
import com.example.planes.R;
import com.example.planes.Utils.Helper;
import com.example.planes.Utils.Text.StickerText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 12.07.15.
 */
public class Round implements EngineEventsListener {
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
    private final MyActivity activity;


    static {
        Point groundWH = Helper.getDrawableWH(R.drawable.ground);
        float h = 2 * GameConfig.worldPeriod / groundWH.x * groundWH.y;
        groundLevel = -1 + h*(1 - BmpConfig.groundLevel);
    }

    private int numPlayers;

    public BTMessageListener getMessageListener() {
        return messageListener;
    }

    private final int roundNumber;
    private int opponentsLoaded = 0;
    public Round(int playerId, ArrayList<Player> players, int roundNumber, MyActivity activity, boolean theyLoaded) {
        Log.d("hey", "Game()");

        this.activity = activity;
        this.roundNumber = roundNumber;
        numPlayers = players.size();
        if(theyLoaded) {
            opponentsLoaded = players.size() - 1;
        }
        this.players = players;
        myId = playerId;
        messageListener = new BTMessageListener(this, players);
        for(Player player : players) {
            if(player.getAbonent() != null) player.getAbonent().setListener(messageListener);
        }

        if(MyActivity.type == MyActivity.Type.NO_BT) {
            numPlayers = GameConfig.numPlayersTypeNoBt;
            for(int i = 1; i < numPlayers; i++) {
                players.add(new Player(null));
            }
        }

        Engine engine = activity.getEngine();
        final Scene scene = engine.newScene();
        scene.setPeriod(GameConfig.worldPeriod);
        scene.setBackgroundColor(GameConfig.skyColor);
        msgScore = new StickerText(engine.getScene(), 0, 0.5f, 0.1f);


        Spawner spawner = new Spawner(this);
        // create ground
        SceneObject ground = spawner.createGround();

        SceneObject cross = scene.createObject(0, getGroundLevel(), 0.1f);
        cross.setSprite(new StaticSprite(R.drawable.cross));

        //create clouds
        spawner.createClouds();

        //create planes
        planesGroup = new ObjectGroup(scene);
        bulletsGroup = new ObjectGroup(scene);
        for (int i = 0; i < numPlayers; i++) {
            Plane plane = new Plane(scene, 0, 0, 0, 0);
            planes.add(plane);
            scene.addObject(plane);
            planesGroup.add(plane);
            plane.setPlayer(players.get(i));
        }
        myPlane = planes.get(myId);

        msgScore.setVisible(true);

        placePlanes();

        //createOnCollisionStartListener(planesGroup, planesGroup, getPlanesVsPlanes());

        ObjectGroup groundGroup = new ObjectGroup(scene);
        groundGroup.add(ground);
        createOnCollisionStartListener(planesGroup, bulletsGroup, getPlanesVsBullets());
        createOnCollisionListener(planesGroup, groundGroup, getPlanesVsGround());
        camera = new FollowingCamera(myPlane);

        //init controls
        controls = new Controls(this);
        controls.lock();
        scene.setButtonEventListner(controls);

        engine.setEventsListener(this);

        if(theyLoaded || MyActivity.type == MyActivity.Type.NO_BT) {
            startGameWithCountdown();
        } else {

            msgScore.setText("Waiting...");

        }
        messageListener.broadcastMessage(new LoadedMessage());
    }

    public void onLoadedMessage() {
        opponentsLoaded++;
        if(opponentsLoaded == getNumPlayers() - 1) {
            startGameWithCountdown();
        }
    }

    public void startGameWithCountdown() {
        msgScore.setText("Round " + String.valueOf(roundNumber) + "/" + String.valueOf(GameConfig.ROUND_COUNT));
        activity.getEngine().addTimer(new Runnable() {
            @Override
            public void run() {
                setCountdownTimer(3);
            }
        }, 3);
    }

    private void setCountdownTimer(final int seconds) {
        msgScore.setText(String.valueOf(seconds));
        activity.getEngine().addTimer(new Runnable() {
            @Override
            public void run() {
                if (seconds == 1) {
                    controls.unlock();
                    msgScore.setVisible(false);
                    //killPlaneIfAlive(planes.get(0));
                } else {
                    setCountdownTimer(seconds - 1);
                }
            }
        }, 1);
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

    public Scene getScene() {
        return activity.getEngine().getScene();
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
                Plane plane = (Plane) object;
                Bullet bullet  = (Bullet) (other);
                plane.onHit(bullet);

                if(plane.getHealth() <= 0) killPlaneIfAlive((Plane) (object));

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
        Log.d("hey", "Plane " + String.valueOf(planes.indexOf(plane)) + " dead");
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
        getScene().addObject(new Explosion(plane));
        plane.remove();
    }

    private void finalizeRound() {
        Log.d("game", "round over");

            Player winner = null;
            for (Plane otherPlane : planes) {
                if (otherPlane.getAlive()) {
                    if (winner != null) throw new RuntimeException(); //debug
                    winner = otherPlane.getPlayer();
                    winner.onWin();
                }
            }

            msgScore.setText(makeScoreString());
            activity.getEngine().addTimer(new Runnable() {
                @Override
                public void run() {
                    activity.onRoundOver();
                }
            }, 3);
            msgScore.setVisible(true);

    }

    private void setButtonsVisible(boolean visible) {
        controls.setButtonsVisible(visible);
    }

    private void placePlanes() {
        float w = getScene().getWorldWidth();
        float period = w / numPlayers;
        Plane plane = planes.get(0);
        plane.setXY(0, 1);
        plane.stopEngine();
        for (int i = 1; i < numPlayers; i++) {
            plane = planes.get(i);
            plane.setXY(i * period, 1);
            // getGroundLevel()
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

    public void onDisconnected() {
        MyActivity.onDisconnected();
    }
}