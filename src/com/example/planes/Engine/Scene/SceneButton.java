package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Circle;

/**
 * Created by egor on 16.07.15.
 */
public class SceneButton extends Sticker {
    SceneButton(float x, float y, Scene scene, Sprite sprite, float height) {
        super(x, y, scene, height);
        setSprite(sprite);
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
        setBody(sprite.getRadius());
    }

}
