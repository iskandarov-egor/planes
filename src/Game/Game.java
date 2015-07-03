package Game;
import Game.Models.*;

import Scene.Scene;
import Utils.AndroidCanvas;
import Utils.Helper;
import config.GameConfig;
import User.User;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Scene.SceneObject;
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
    private User localPlayer; // владелец телефона, от него идут события нажатий
    private AndroidCanvas canvas;
    public Game(int playersCount, AndroidCanvas canvas) {
        scene = new Scene(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        base = new Base(this);
        ground = new Ground(scene);
        gameLoop = new GameLoop(this);
        this.playersCount = playersCount;
    }

    public Scene getScene() {
        return scene;
    }

    public void onFrame() {
        scene.onFrame();
        doCollisions();

    }

    public void render() {
        scene.draw(canvas);
    }

    public void startGame() {
        roundNumber = 0;
        startNewRound();
    }

    GameLoop gameLoop;
    private void startNewRound() {

        Plane testPlane = new SmallPlane(scene, localPlayer, GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2);

        gameLoop.run();
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

    private void doCollisions(){
        for(int i = 0; i < planes.size() - 1; i++) {
            Plane plane = planes.get(i);
            SceneObject planeObject = plane.getSceneObject();
            for(int j = i + 1; j < planes.size(); j++) {
                Plane otherPlane = planes.get(j);
                if(planeObject.intersects(otherPlane.getSceneObject())) {
                    killPlane(plane);
                    killPlane(otherPlane);
                }
            }

            if(planeObject.intersects(base)) {
                killPlane(plane);
            }


        }


        for(int i = 0; i < bullets.size(); i++) {
            for(Plane plane : planes) {

                if (plane.getSceneObject().intersects(bullets.get(i))) {
                    plane.hit();
                    if (plane.getHealth() <= 0) killPlane(plane);
                    scene.removeObject(bullets.get(i));
                    bullets.remove(i);
                    i--;
                }
            }
        }
    }



    public void onFireDown(Plane plane){

    }

    public void onFireUp(Plane plane){

    }

    public void processMessages() {
        // тут реагировать на события нажатий, сообщения сервера и т.п.
        // вызывается GameConfig.FPS раз в секунду
    }
}
