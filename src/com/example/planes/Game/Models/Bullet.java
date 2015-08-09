package com.example.planes.Game.Models;

import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.R;

/**
 * Created by egor on 29.07.15.
 */
public class Bullet extends GameObject {
    public Bullet(Scene scene, float x, float y, float speed, float angle) {
        super(scene, x, y, speed, angle);
        setSprite(new StaticSprite(R.drawable.but, 0.05f));
        setBody(0.1f);
        normDrag = 0.004f*60;
        tanDrag = 0.004f*60;
    }

    private float distTravelled = 0;


    @Override
    public void onPhysicsFrame(float physicsFPS) {
        super.onPhysicsFrame(physicsFPS);
        distTravelled += Math.hypot(getVx(), getVy()) / physicsFPS; // opt
        if(distTravelled > GameConfig.bulletPath) {
            remove();
        }
    }

    public float getDistTravelled() {
        return distTravelled;
    }

    public void onHit() {
        remove();
    }
}
