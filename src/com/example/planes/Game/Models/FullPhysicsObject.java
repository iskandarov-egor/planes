package com.example.planes.Game.Models;

import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Game.Game;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 29.07.15.
 */
public abstract class FullPhysicsObject implements Movable{
    private static final float g = 0.00003999f *60*60;
    private float vx, vy;
    protected float tanDrag = 0.008f*60;
    protected float normDrag = 0.008f*60;
    protected SceneObject so;
    private float angle = 0;



    public FullPhysicsObject(Scene scene, float x, float y, float speed, float angle) {
        so = scene.createObject(x, y);
        this.vx = (float) (speed*Math.cos(angle));
        this.vy = (float) (speed*Math.sin(angle));
    }
    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public void onPhysicsFrame(float physicsFPS) {
        /////////////////GRAV
        vy -= g / physicsFPS;

        ////////////////DRAG
        float vtan = (float) (vx*Math.cos(angle) + vy*Math.sin(angle));
        float vnorm = (float) (vy*Math.cos(angle)-vx*Math.sin(angle));
        vtan = MathHelper.pullToX(vtan, Math.abs(tanDrag  * vtan/ physicsFPS), 0);
        vnorm = MathHelper.pullToX(vnorm, Math.abs(normDrag  * vnorm/ physicsFPS), 0);
        vx = (float) (vtan*Math.cos(angle) - vnorm*Math.sin(angle));
        vy = (float) (vnorm*Math.cos(angle) + vtan*Math.sin(angle));

        ///////////////GROUND
        float y = so.getY();
        float ground = Game.getGroundLevel();

        if(y + vy/physicsFPS < ground) {
            y = ground;
            vy = 0;
        }

        so.setXY(so.getX()+vx / physicsFPS, y + vy / physicsFPS);

    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public float getAngle() {
        return so.getAngle();
    }
}
