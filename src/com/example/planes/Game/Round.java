package com.example.planes.Game;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
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
    private final Game game;


    static {
        Point groundWH = Helper.getDrawableWH(R.drawable.ground);
        float h = 2 * GameConfig.worldPeriod / groundWH.x * groundWH.y;
        groundLevel = -1 + h*(1 - BmpConfig.groundLevel);
    }

    private int numPlayers;



    public BTMessageListener getMessageListener() {
        return messageListener;
    }

    public int getMyId() {
        return myId;
    }


    public Round(int playerId, ArrayList<RemoteAbonent> otherPlayers, int roundNumber, Game game) {
        Log.d("hey", "Game()");
        this.game = game;
        numPlayers = otherPlayers.size() + 1;
        players = new ArrayList<>(numPlayers);
        myId = playerId;
        messageListener = new BTMessageListener(this, otherPlayers);
        for(RemoteAbonent abonent : otherPlayers) {
            abonent.setListener(messageListener);
        }

        if(GameConfig.type == GameConfig.TYPE_NO_BT) numPlayers = GameConfig.numPlayersTypeNoBt;

        Engine engine = game.getEngine();
        final Scene scene = engine.newScene();
        scene.setPeriod(GameConfig.worldPeriod);
        scene.setBackgroundColor(0, 0, 1);
        msgScore = new StickerText(engine.getScene(), 0, 0.5f, 0.1f);


        Spawner spawner = new Spawner(this);
        // create ground
        SceneObject ground = spawner.createGround();

        SceneObject cross = scene.createObject(0, getGroundLevel(), 0.1f);
        cross.setSprite(new StaticSprite(R.drawable.cross));

        //create clouds
        for (int i = 0; i < GameConfig.cloudsMin; i++) {
            int sprite = (i % 2 == 0)?R.drawable.cloud:R.drawable.fore_cloud;
            Cloud cloud = new Cloud(scene, sprite);
            scene.addObject(cloud);
            if(i % 2 == 1) {
                cloud.setZindex(2);
            }
        }

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
        msgScore.setText("Round " + String.valueOf(roundNumber) + "/" + String.valueOf(GameConfig.ROUND_COUNT));
        msgScore.setVisible(true);

        placePlanes();

        createOnCollisionStartListener(planesGroup, planesGroup, getPlanesVsPlanes());

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

        engine.addTimer(new Runnable() {
            @Override
            public void run() {
                setCountdownTimer(3);
            }
        }, 3);


    }

    private void setCountdownTimer(final int seconds) {
        msgScore.setText(String.valueOf(seconds));
        game.getEngine().addTimer(new Runnable() {
            @Override
            public void run() {
                if (seconds == 1) {
                    controls.unlock();
                    msgScore.setVisible(false);
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
        return game.getEngine().getScene();
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
            game.getEngine().addTimer(new Runnable() {
                @Override
                public void run() {
                    game.onRoundOver();
                }
            }, 3);
            msgScore.setVisible(true);

    }

    private void setButtonsVisible(boolean visible) {
        controls.setButtonsVisible(visible);
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