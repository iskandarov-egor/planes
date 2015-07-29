package com.example.planes.Game.Models;

import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.R;

/**
 * Created by egor on 29.07.15.
 */
public class Bullet extends FullPhysicsObject {


    public Bullet(Scene scene, float x, float y, float speed, float angle) {
        super(scene, x, y, speed, angle);
        so.setSprite(new StaticSprite(R.drawable.but, 0.05f));
        so.setBody(0.1f);
        normDrag = 0.004f*60;
        tanDrag = 0.004f*60;
    }
}
