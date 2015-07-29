package com.example.planes.Game.Models;

import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Engine.Utils;
import com.example.planes.R;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 15.07.15.
 */
public class Plane extends FullPhysicsObject {
    private float k = 0.002666f;
    private float acc = 0.00006665f*60*60;
    private float dirVel = 0.027f;
    private float lift = 0.015f*60;
    private boolean gazing = true;
    private boolean alive = true;

    public Plane(Scene scene, float x, float y, float speed, float angle) {
        super(scene, x, y, speed, angle);
        tanDrag = 0.008f*60;
        normDrag = 0.008f*60;
        so.setSprite(new StaticSprite(R.drawable.plane_stub, 0.1f));
        so.setBody(0.1f);
    }

    private enum Direction {
        LEFT, RIGHT, STRAIGHT
    }

    public Direction dir = Direction.STRAIGHT;

    public void onPhysicsFrame(float physicsFPS) {
        float vx = getVx();
        float vy = getVy();
        float angle = getAngle();
        float v = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));
        if(dir == Direction.RIGHT && alive) {
            angle -= angleVelFunc(v);
            so.setAngle(angle);
        }
        if(dir == Direction.LEFT && alive) {
            angle += angleVelFunc(v);
            so.setAngle(angle);
        }

        if(alive && gazing && so.getY() < GameConfig.worldCeiling) {
            float vx1 = (float) (acc*Math.cos(angle)) / physicsFPS;
            float vy1 = (float) (acc*Math.sin(angle)) / physicsFPS;

            vx += vx1;
            vy += vy1;
        }

///////////////////LIFT

        float normV = (float) (vy*Math.cos(angle)-vx*Math.sin(angle));
        float tanV = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));

        float lift = this.lift*(Math.abs(tanV)) / physicsFPS;

        vx = MathHelper.pullToX(vx, (float)Math.abs(Math.sin(angle)*lift), (float) (tanV*Math.cos(angle)));
        vy = MathHelper.pullToX(vy, (float)Math.abs(Math.cos(angle) * lift), (float) (tanV * Math.sin(angle)));

        setVx(vx);
        setVy(vy);
        super.onPhysicsFrame(physicsFPS);

    }

    private float angleVelFunc(float v) {
        float resist = 0.5f;
        float maxV = acc / tanDrag;
        return this.dirVel*(1-Math.min(resist, resist*(Math.abs(v)/maxV)));
    }

    public SceneObject getSceneObject() {
        return so;
    }



    public void goLeft() {
        dir = Direction.LEFT;
    }

    public void goRight() {
        dir = Direction.RIGHT;
    }

    public void goStraight() {
        dir = Direction.STRAIGHT;
    }

    public void die() {
        alive = false;
    }

    public float getY() {
        return so.getY();
    }


    public void startEngine() {
        gazing = true;
    }

    public void stopEngine() {
        gazing = false;
    }

    public Bullet fire() {
        Utils.FloatPoint nose = getNose();
        float v = (float) Math.hypot(getVx(), getVy()) + GameConfig.bulletSpeed;
        Bullet bullet = new Bullet(so.getScene(), nose.x, nose.y, v, getAngle());

        return bullet;
    }


    public Utils.FloatPoint getNose() {
        Utils.FloatPoint nose = new Utils.FloatPoint(0, 0);
        nose.x = (float) (so.getX() + so.getRadius() * Math.cos(getAngle()));
        nose.y = (float) (so.getY() + so.getRadius() * Math.sin(getAngle()));
        return nose;
    }
}
