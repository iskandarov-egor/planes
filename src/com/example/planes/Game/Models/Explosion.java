package com.example.planes.Game.Models;

import com.example.planes.Config.Config;
import com.example.planes.Engine.Scene.AnimatedSprite;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.R;

/**
 * Created by egor on 17.08.15.
 */
public class Explosion extends SceneObject {

    public Explosion(float x, float y, Scene scene, float height) {
        super(x, y, scene, height);
        setRemoveWhenAnimDone(true);
        setSprite(new AnimatedSprite(this, R.drawable.explosion, 3, 1));
    }

    public Explosion(Plane plane) {
        this(plane.getX(), plane.getY(), plane.getScene(), Config.explolsionHeight);

    }

}
