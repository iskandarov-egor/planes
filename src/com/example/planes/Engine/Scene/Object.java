package com.example.planes.Engine.Scene;


import com.example.planes.Engine.Utils;

/**
 * Created by egor on 01.07.15.
 */
public class Object extends SceneObject {

    protected float vx = 0;
    protected float vy = 0;
    private float angleSpeed = 0;

    public Object(float x, float y) {
        super(x, y);
    }

    public void setSpeed(float vx, float vy) {
        //debug
        if(Math.abs(vx) > 444444) throw new RuntimeException("что то не так");
        if(Math.abs(vy) > 444444) throw new RuntimeException("что то не так");

        this.vx = vx;
        this.vy = vy;
    }


    public void onPhysicsFrame(float horizPeriod, float physicsFPS) {
        //debug
        if(horizPeriod < 0) throw new IllegalArgumentException("period");

        angle = Utils.mod(angle + angleSpeed / physicsFPS, Utils.PI2);

        x += vx / physicsFPS; // todo улучшить
        y += vy / physicsFPS;

        if(horizPeriod != 0) {
            while (x > horizPeriod / 2) x -= horizPeriod;
            while (x < -horizPeriod / 2) x += horizPeriod;
        }
    }



//    public Vector getSpeed() {
//        if(parent != null) return MathHelper.vectorSum(parent.getSpeed(), speed);
//        return speed;
//    }

    public void setAngleSpeed(float angleSpeed) {
        //debug
        if(Math.abs(angleSpeed) > 444) throw new RuntimeException("что то не так");

        this.angleSpeed = angleSpeed;
    }




}
