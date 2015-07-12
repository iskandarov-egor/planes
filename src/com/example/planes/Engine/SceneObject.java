package com.example.planes.Engine;

import com.example.planes.Engine.Sprite.Sprite;

/**
 * Created by egor on 11.07.15.
 */
public interface SceneObject {
    void setSpeed(float vx, float vy);
    void setAngleSpeed(float angleSpeed);
    void setSprite(Sprite sprite);
    float getAbsoluteX();
    float getAbsoluteY();

    void setAngle(float angle);
    ObjectImpl getImpl();
    void addBody(float radius);

    void setColor(float r, float g, float b);
}
