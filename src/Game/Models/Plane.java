package Game.Models;

import Sprite.Sprite;
import User.User;
import Scene.SceneObject;
import Scene.Scene;
import Sprite.StaticSprite;
import Sprite.AnimatedSprite;
import Game.Game;
import Utils.Vector;
import config.GameConfig;

/**
 * Created by egor on 02.07.15.
 */
public abstract class Plane {
    private User player;
    protected double health;
    private SceneObject body;
    private SceneObject propeller;
    private boolean dead = false;

    public Plane(Scene scene, User player, double x, double y) {

        this.player = player;
        body = new SceneObject(x, y);
        Sprite bodySprite = new StaticSprite(/*блабла.png*/);
        body.setSprite(bodySprite);
        propeller = new SceneObject(10, 0); // как связать размер картинки в пикселях с координатной системой мира??
        propeller.setSprite(new AnimatedSprite());
        propeller.setParent(body);
    }

    public void fire(Scene scene){
        Vector planeSpeed = body.getSpeed();
        Vector bulletSpeed = new Vector(planeSpeed.getOrt());
        bulletSpeed.multiply(GameConfig.bulletSpeed);
        bulletSpeed.add(planeSpeed);
        Bullet bullet = new Bullet(propeller.getX(), propeller.getY());
        bullet.setSpeed(bulletSpeed);
        scene.addObject(bullet);
    }

    public void onFrame() {

        //применяем физику самолета
    }


    // USER CONTROL EVENTS
    public void onLeftDown(){}
    public void onRightDown(){}
    public void onLeftUp(){}
    public void onRightUp(){}

    public void onStopDown(){}
    public void onStopUp(){}

    public void hit() {
        health -= GameConfig.bulletDamage;
    }

    public double getHealth() {
        return health;
    }

    public SceneObject getSceneObject() {
        return body;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
