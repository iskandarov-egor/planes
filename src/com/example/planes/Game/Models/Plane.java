package com.example.planes.Game.Models;

import android.util.Log;
import com.example.planes.Config.BmpConfig;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Engine.Utils;
import com.example.planes.Game.Round;
import com.example.planes.R;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 15.07.15.
 */
public class Plane extends GameObject {

    private float acc = k*0.050f*60*60;
    private float dirVel = 0.033f * 60;
    private float lift = 0.015f*60;
    private float velolim = 5f*60;
    private boolean engineOn = false;
    private boolean alive = true;

    private static final Wheel frontWheel;
    private static final Wheel backWheel;

    static {
        float frontWheelDx = BmpConfig.planeHeight * (BmpConfig.frontWheelX);
        float frontWheelDy = BmpConfig.planeHeight * (BmpConfig.frontWheelY);
        float backWheelDx = BmpConfig.planeHeight * (BmpConfig.backWheelX);
        float backWheelDy = BmpConfig.planeHeight * (BmpConfig.backWheelY);
        frontWheel = new Wheel(frontWheelDx, frontWheelDy);
        backWheel = new Wheel(backWheelDx, backWheelDy);
    }

    private boolean crashed = false;
    private Player player;
    private float health = GameConfig.planeHealth;

    public Plane(Scene scene, float x, float y, float speed, float angle) {
        super(scene, x, y, speed, angle, BmpConfig.planeHeight);

        tanDrag = 2*0.008f*60;
        normDrag = 4*0.008f*60;
        setSprite(new StaticSprite(R.drawable.plane_stub));

//        ComplexPolygon poly = new ComplexPolygon(Config.planePolyX[0], Config.planePolyY[0]);
//        for(int i = 1; i < Config.planePolyX.length; i++) {
//            poly.addSimplePolygon(Config.planePolyX[i], Config.planePolyY[i]);
//        }
        //setBody(poly);
        setBody(0.1f);
        setCustomGroundPhys(true);
        SceneObject propeller = new SceneObject(0.25f, 0, scene, 0.1f);
        propeller.setParent(this);
        propeller.setSprite(new AnimatedSprite(R.drawable.propeller, 8, 0.05f));
        scene.addObject(propeller);
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

    public void onTouchingGround() {
        if(!frontWheel.isTouchingGround(this) && !backWheel.isTouchingGround(this) && !GameConfig.immortality) {
            crashed = true;
        }
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void onHit(Bullet bullet) {
        health -= 1;
    }

    public float getHealth() {
        return health;
    }

    private enum Direction {
        LEFT, RIGHT, STRAIGHT
    }

    public Direction dir = Direction.STRAIGHT;

    public void onPhysicsFrame(float fps) {
        applySpeed(fps);
        float angle = getAngle();
        float v = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));
        if(dir == Direction.RIGHT && alive) {
            angle -= angleVelFunc(v, fps);
            setAngle(angle);
        }
        if(dir == Direction.LEFT && alive) {
            angle += angleVelFunc(v, fps);
            setAngle(angle);
        }

        float vx1 = 0;
        float vy1 = 0;
        if(alive && engineOn && getY() < GameConfig.worldCeiling) {
            float k = 1 - v / velolim;
            vx1 = (float) (k*acc *  Math.cos(angle)) / fps;
            vy1 = (float) (k*acc * Math.sin(angle)) / fps;


        }
//        setVx(vx);
//        setVy(vy);
        applyGear(fps);
///////////////////LIFT

        float normV = (float) (vy*Math.cos(angle)-vx*Math.sin(angle));
        float tanV = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));
        float speed = (float) Math.hypot(vx, vy);

        float lift = this.lift*(Math.abs(tanV)) / fps;

        vx = MathHelper.pullToX(vx, (float)Math.abs(Math.sin(angle) * lift), (float) (speed * Math.cos(angle)));
        vy = MathHelper.pullToX(vy, (float)Math.abs(Math.cos(angle) * lift), (float) (speed * Math.sin(angle)));
//        setVx(vx);
//        setVy(vy);


        applyDrag(fps);
        vx += vx1;
        vy += vy1;

////////////////////// GEAR vs GROUND

        if(crashed) {
            if(getVy() < 0) {
                //setVy(0);
                vy = 0;
            }
        }


    }


    protected void applyGear(float fps) {
        boolean frontWheelTouching = frontWheel.isTouchingGround(this);
        boolean backWheelTouching = backWheel.isTouchingGround(this);
        if(frontWheelTouching && !backWheelTouching) {
            frontWheel.correctPlane(this);
        }
        if(!frontWheelTouching && backWheelTouching) {
            backWheel.correctPlane(this);
        }
        if(frontWheelTouching && backWheelTouching) {
            vy = 0;
        }
        if(!frontWheelTouching && !backWheelTouching) {
            applyGravity(fps);
        }
    }


    private static int shots = 0;

    private float angleVelFunc(float v, float fps) {
        float resist = 0.3f;
        float maxV = 1 / (1 / this.velolim + this.tanDrag / this.acc);
        return dirVel*(1-Math.min(resist, resist*(Math.abs(v)/maxV))) / fps;
    }

    private float angleVelFunc2(float v, float fps) {
        float resist = 0.3f;
        float maxV2 = 0.5f / (1 / this.velolim + this.tanDrag / this.acc);
        if(v < maxV2) {
            return dirVel*(Math.min(1, (Math.abs(v)/maxV2))) / fps;
        } else {
            return dirVel*(1 - Math.min(resist, resist * (Math.abs(v - maxV2) / maxV2))) / fps;
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
            z = MathHelper.modpi2((float) Math.atan2(BmpConfig.planeDy - dy, BmpConfig.planeDx + dx));
            d = (float) Math.hypot(dx, dy);
        }

        boolean isTouchingGround(Plane plane) {
            float y = plane.getY() + d * (float)Math.sin(z + plane.getAngle());
            return y < Round.getGroundLevel();
        }

        void correctPlane(Plane plane) {
            SceneObject so = plane;
            float o = MathHelper.modpi2(z + plane.getAngle());

            float s = MathHelper.modpi2((float) Math.asin((Round.getGroundLevel() - so.getY()) / d));
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