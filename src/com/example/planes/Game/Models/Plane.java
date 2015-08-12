package com.example.planes.Game.Models;

import android.util.Log;
import com.example.planes.Config.BmpConfig;
import com.example.planes.Config.Config;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Engine.Utils;
import com.example.planes.Game.Game;
import com.example.planes.R;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 15.07.15.
 */
public class Plane extends GameObject {
    private float k = 0.002666f;
    private float acc = 0.00006665f*60*60;
    private float dirVel = 0.027f;
    private float lift = 0.015f*60;
    private boolean engineOn = false;
    private boolean alive = true;

    private static final Wheel frontWheel;
    private static final Wheel backWheel;

    static {
        float frontWheelDx = Config.planeHeight * (BmpConfig.frontWheelX);
        float frontWheelDy = Config.planeHeight * (BmpConfig.frontWheelY);
        float backWheelDx = Config.planeHeight * (BmpConfig.backWheelX);
        float backWheelDy = Config.planeHeight * (BmpConfig.backWheelY);
        frontWheel = new Wheel(frontWheelDx, frontWheelDy);
        backWheel = new Wheel(backWheelDx, backWheelDy);
    }

    public Plane(Scene scene, float x, float y, float speed, float angle) {
        super(scene, x, y, speed, angle, Config.planeHeight);

        tanDrag = 0.008f*60;
        normDrag = 0.008f*60;
        setSprite(new StaticSprite(R.drawable.plane_stub));
        setBody(Config.planeHeight);
        setCustomGroundPhys(true);
    }

    public void resurrect() {
        alive = true;
    }


    public boolean getAlive() {
        return alive;
    }

    public boolean isEngineOn() {
        return engineOn;
    }

    private enum Direction {
        LEFT, RIGHT, STRAIGHT
    }

    public Direction dir = Direction.STRAIGHT;

    public void onPhysicsFrame(float fps) {
        float vx = getVx();
        float vy = getVy();
        float angle = getAngle();
        float v = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));
        if(dir == Direction.RIGHT && alive) {
            angle -= angleVelFunc(v);
            setAngle(angle);
        }
        if(dir == Direction.LEFT && alive) {
            angle += angleVelFunc(v);
            setAngle(angle);
        }

        if(alive && engineOn && getY() < GameConfig.worldCeiling) {
            float vx1 = (float) (acc *  Math.cos(angle)) / fps;
            float vy1 = (float) (acc * Math.sin(angle)) / fps;

            vx += vx1;
            vy += vy1;
        }

///////////////////LIFT

        float normV = (float) (vy*Math.cos(angle)-vx*Math.sin(angle));
        float tanV = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));

        float lift = this.lift*(Math.abs(tanV)) / fps;

        vx = MathHelper.pullToX(vx, (float)Math.abs(Math.sin(angle) * lift), (float) (tanV*Math.cos(angle)));
        vy = MathHelper.pullToX(vy, (float)Math.abs(Math.cos(angle) * lift), (float) (tanV * Math.sin(angle)));

        setVx(vx);
        setVy(vy);

        applyDrag(fps);

////////////////////// GEAR vs GROUND
        applyGround(fps);

        applySpeed(fps);
    }

    @Override
    protected void applyGround(float fps) {
        boolean frontWheelTouching = frontWheel.isTouchingGround(this);
        boolean backWheelTouching = backWheel.isTouchingGround(this);
        if(frontWheelTouching && !backWheelTouching) {
            frontWheel.correctPlane(this);
        }
        if(!frontWheelTouching && backWheelTouching) {
            backWheel.correctPlane(this);
        }
        if(frontWheelTouching && backWheelTouching) {
            setVy(0);
        }
        if(!frontWheelTouching && !backWheelTouching) {
            applyGravity(fps);
        }
    }


    private static int shots = 0;

    private float angleVelFunc(float v) {
        float resist = 0.5f;
        float maxV2 = 0.5f * acc / tanDrag;
        if(v < maxV2) {
            return dirVel*(Math.min(1, (Math.abs(v)/maxV2)));
        } else {
            return dirVel*(1 - Math.min(resist, resist * (Math.abs(v - maxV2) / maxV2)));
        }
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


    public void startEngine() {
        engineOn = true;
    }

    public void stopEngine() {
        engineOn = false;
    }

    public Bullet fire(ObjectGroup bulletsGroup) {
        Utils.FloatPoint nose = getNose();
        float v = (float) Math.hypot(getVx(), getVy()) + GameConfig.bulletSpeed;
        Bullet bullet = new Bullet(getScene(), nose.x, nose.y, v, getAngle());
        getScene().addObject(bullet);
        Log.d("shots fired", String.valueOf(shots));
        shots++;
        bulletsGroup.add(bullet);
        return bullet;
    }

    public Utils.FloatPoint getNose() {
        Utils.FloatPoint nose = new Utils.FloatPoint(0, 0);
        nose.x = (float) (getX() + (getRadius()*1.5f) * Math.cos(getAngle()));
        nose.y = (float) (getY() + getRadius()*1.5f * Math.sin(getAngle()));
        return nose;
    }

    private static class Wheel {
        final float dx, dy;
        final float z;
        final float d;

        public Wheel(float dx, float dy) {
            this.dx = dx;
            this.dy = dy;
            z = MathHelper.modpi2((float) Math.atan2(Config.planeDy - dy, Config.planeDx + dx));
            d = (float) Math.hypot(dx, dy);
        }

        boolean isTouchingGround(Plane plane) {
            float y = plane.getY() + d * (float)Math.sin(z + plane.getAngle());
            return y < Game.getGroundLevel();
        }

        void correctPlane(Plane plane) {
            SceneObject so = plane;
            float o = MathHelper.modpi2(z + plane.getAngle());

            float s = MathHelper.modpi2((float) Math.asin((Game.getGroundLevel() - so.getY()) / d));
            if(s < MathHelper.PI) throw new RuntimeException("level > center");
            if(o < MathHelper.PI) throw new RuntimeException("wheel > center");
            if(o > MathHelper.PI * 1.5f) {
                so.setAngle(s - z);
            } else {
                so.setAngle(MathHelper.PI - s - z);
            }
        }
    }
}
