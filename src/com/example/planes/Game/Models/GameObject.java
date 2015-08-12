package com.example.planes.Game.Models;

import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Game.Game;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 29.07.15.
 */
public abstract class GameObject extends SceneObject{
    private static final float g = 0.00003999f *60*60;
    private float vx, vy;
    protected float tanDrag = 0.008f*60;
    protected float normDrag = 0.008f*60;
    private float angle = 0;
    private boolean customGroundPhys = false;

    public GameObject(Scene scene, float x, float y, float speed, float angle, float height) {
        super(x, y, scene, height);
        this.vx = (float) (speed*Math.cos(angle));
        this.vy = (float) (speed*Math.sin(angle));
    }
    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public void setCustomGroundPhys(boolean customGroundPhys) {
        this.customGroundPhys = customGroundPhys;
    }

    protected void applyGravity(float physicsFPS) {
        vy -= g / physicsFPS;

    }

    protected void applySpeed(float physicsFPS) {
        setXY(getX()+vx / physicsFPS, getY() + vy / physicsFPS);
    }

    protected void applyDrag(float physicsFPS) {
        float vtan = (float) (vx*Math.cos(angle) + vy*Math.sin(angle));
        float vnorm = (float) (vy*Math.cos(angle)-vx*Math.sin(angle));
        vtan = MathHelper.pullToX(vtan, Math.abs(tanDrag  * vtan/ physicsFPS), 0);
        vnorm = MathHelper.pullToX(vnorm, Math.abs(normDrag  * vnorm/ physicsFPS), 0);
        vx = (float) (vtan*Math.cos(angle) - vnorm*Math.sin(angle));
        vy = (float) (vnorm*Math.cos(angle) + vtan*Math.sin(angle));
    }

    protected void applyGround(float physicsFPS) {
        float y = getY();
        if(!customGroundPhys) {

            float ground = Game.getGroundLevel();

            if (y + vy / physicsFPS - getRadius() < ground) {
                y = ground + getRadius();
                vy = 0;

            }
        }
        setY(y);
    }

    @Override
    public void onPhysicsFrame(float physicsFPS) {
        /////////////////GRAV
        applyGravity(physicsFPS);

        ////////////////DRAG
        applyDrag(physicsFPS);


        ///////////////GROUND
        applyGround(physicsFPS);

        applySpeed(physicsFPS);

    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }


    public boolean isTouchingGround() {
        float ground = Game.getGroundLevel();
        return getY() <= ground + getRadius();
    }


}