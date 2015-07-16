package com.example.planes.Models;

import com.example.planes.Engine.Scene.*;
import com.example.planes.Engine.Scene.Object;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 15.07.15.
 */
public class Plane extends Object {

    private float k = 0.002666f;
    private float acc = 0.025f* k *60*60;
    private float tanDrag = 0.008f*60;
    private float g = 0.015f* k *60*60;
    private float dirVel = 0.027f;
    private float lift = 0.015f*60;
    private float normDrag = 0.008f*60;
    private boolean gazing = true;

    public Plane(float x, float y) {
        super(x, y);
        setSprite(new Triangle());
        setBody(0.1f);
    }

    public Plane(float x, float y, float speed, float angle) {
        this(x, y);
        this.vx = (float) (speed*Math.cos(angle));
        this.vy = (float) (speed*Math.sin(angle));
    }

    public int turning = 0;

    @Override
    public void onPhysicsFrame(float horizPeriod, float physicsFPS) {
        super.onPhysicsFrame(horizPeriod, physicsFPS);
        //float fps2 = physicsFPS*physicsFPS;
        float v = (float) (vx*Math.cos(angle)+vy*Math.sin(angle));
        if(turning == 1) {
            angle -= angleVelFunc(v);
        }
        if(turning == 2) {
            angle += angleVelFunc(v);
        }

        if(gazing) {
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

    }

    private float angleVelFunc(float v) {
        float resist = 0.5f;
        float maxV = acc / tanDrag;
        return this.dirVel*(1-Math.min(resist, resist*(Math.abs(v)/maxV)));
    }
}
