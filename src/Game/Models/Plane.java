package Game.Models;

import Scene.Scene;
import Scene.SceneObject;
import Scene.Sprite.AnimatedSprite;
import Scene.Sprite.Sprite;
import Scene.Sprite.StaticSprite;
import User.User;
import Utils.Vector;
import config.GameConfig;

/**
 * Created by egor on 02.07.15.
 */
public abstract class Plane extends SceneObject {
    private User player;
    protected double health;
    private SceneObject propeller;
    private boolean dead = false;

    public Plane(Scene scene, User player, double x, double y) {
        super(x, y);
        this.player = player;

        Sprite bodySprite = new StaticSprite(/*блабла.png*/);
        setSprite(bodySprite);
        propeller = new SceneObject(10, 0); // как связать размер картинки в пикселях с координатной системой мира??
        propeller.setSprite(new AnimatedSprite());
        propeller.setParent(this);
    }

    public void fire(Scene scene){
        Vector planeSpeed = getSpeed();
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

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
