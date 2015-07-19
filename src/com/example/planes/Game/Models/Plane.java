package com.example.planes.Game.Models;

import android.view.MotionEvent;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 15.07.15.
 */
public class Plane implements Movable{
    public SceneObject so;
    private float k = 0.002666f;
    private float acc = 0.025f* k *60*60;
    private float tanDrag = 0.008f*60;
    private float g = 0.015f* k *60*60;
    private float dirVel = 0.027f;
    private float lift = 0.015f*60;
    private float normDrag = 0.008f*60;
    private boolean gazing = true;
    private float vx, vy;
    private float angle;


    public Plane(SceneObject object) {
        so = object;
    }

    public Plane(SceneObject object, float speed, float angle) {
        this(object);
        this.vx = (float) (speed*Math.cos(angle));
        this.vy = (float) (speed*Math.sin(angle));
    }

    public int turning = 0;

    public void onPhysicsFrame(float physicsFPS) {
        float v = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));
        if(turning == 1) {
            angle -= angleVelFunc(v);
            so.setAngle(angle);
        }
        if(turning == 2) {
            angle += angleVelFunc(v);
            so.setAngle(angle);
        }

        if(gazing && so.getY() < GameConfig.worldHeight) {
            float vx1 = (float) (acc*Math.cos(angle)) / physicsFPS;
            float vy1 = (float) (acc*Math.sin(angle)) / physicsFPS;

            vx += vx1;
            vy += vy1;
        }
        vy -= g / physicsFPS;


///////////////////LIFT

        float normV = (float) (vy*Math.cos(angle)-vx*Math.sin(angle));
        float tanV = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));

        float lift = this.lift*(Math.abs(tanV)) / physicsFPS;

        vx = MathHelper.pullToX(vx, (float)Math.abs(Math.sin(angle)*lift), (float) (tanV*Math.cos(angle)));
        vy = MathHelper.pullToX(vy, (float)Math.abs(Math.cos(angle) * lift), (float) (tanV * Math.sin(angle)));

////////////////DRAG
        float vtan = (float) (vx*Math.cos(angle) + vy*Math.sin(angle));
        float vnorm = (float) (vy*Math.cos(angle)-vx*Math.sin(angle));
        vtan = MathHelper.pullToX(vtan, Math.abs(tanDrag  * vtan/ physicsFPS), 0);
        vnorm = MathHelper.pullToX(vnorm, Math.abs(normDrag  * vnorm/ physicsFPS), 0);
        vx = (float) (vtan*Math.cos(angle) - vnorm*Math.sin(angle));
        vy = (float) (vnorm*Math.cos(angle) + vtan*Math.sin(angle));


        so.setXY(so.getX()+vx / physicsFPS, so.getY()+vy / physicsFPS);

    }

    private float angleVelFunc(float v) {
        float resist = 0.5f;
        float maxV = acc / tanDrag;
        return this.dirVel*(1-Math.min(resist, resist*(Math.abs(v)/maxV)));
    }

    public SceneObject getSceneObject() {
        return so;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }
}
