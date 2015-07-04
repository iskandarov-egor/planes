package Game;

import Game.Models.*;
import Scene.CollisionListener;
import Scene.ObjectGroup;
import Scene.Scene;
import User.User;
import Utils.AndroidCanvas;
import Utils.Helper;
import config.GameConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * Created by egor on 02.07.15.
 */
public class Game {
    private Scene scene;
    private List<Plane> planes = new ArrayList<Plane>();
    private List<Bullet> bullets = new LinkedList<Bullet>();
    private Base base;
    private Ground ground;
    private int roundNumber = 0;
    private User localPlayer;
    private AndroidCanvas canvas;
    public Game(int playersCount, AndroidCanvas canvas) {
        scene = new Scene(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        base = new Base(this);
        ground = new Ground(scene);

        this.playersCount = playersCount;

        scene.setOnGraphicsFrame(() -> {
            processMessages();
        });
    }

    ObjectGroup planesGroup = new ObjectGroup();
    ObjectGroup bulletsGroup = new ObjectGroup();
    private void setCollisionRules() {


        CollisionListener planesAndPlanes = new CollisionListener(planesGroup, planesGroup);
        planesAndPlanes.setOnCollisionStart((object, other) -> {
            killPlane((Plane) object);
            killPlane((Plane) other);
        });
        scene.addCollisionListener(planesAndPlanes);

        CollisionListener planesAndBullets = new CollisionListener(planesGroup, bulletsGroup);
        planesAndBullets.setOnCollisionStart((object, other) -> {
            Plane plane = (Plane) object;

            plane.hit();
            if (plane.getHealth() <= 0) killPlane(plane);
            scene.removeObject(other);
            bullets.remove(other);
        });
    }

    public Scene getScene() {
        return scene;
    }

    private void syncWithServer() {

    }

    public void sendUpdatesToClient() {

    }

    public void startNewGame() {
        roundNumber = 0;
        scene.pauseAndClear();
        startNewRound();
    }


    private void startNewRound() {

        Plane testPlane = new SmallPlane(scene, localPlayer, GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2);
        scene.addObject(testPlane, planesGroup);
        setCollisionRules();
    }

    private int planesDead = 0;
    private int playersCount;
    private void killPlane(Plane plane) {
        // может быть убит несколько раз!
        plane.setDead(true);
        planesDead++;
        if(planesDead == playersCount - 1) waitAndStartNewRound();
    }

    private void waitAndStartNewRound() {
        Helper.sleep(1000);
        startNewRound();
    }


    public void onFireDown(Plane plane){

    }

    public void onFireUp(Plane plane){

    }

    public void processMessages() {
        // тут реагировать на события нажатий, сообщения сервера и т.п.
    }
}
