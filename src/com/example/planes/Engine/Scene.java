package com.example.planes.Engine;

import android.content.Context;
import android.view.View;
import com.example.planes.MyActivity;

/**
 * Created by egor on 12.07.15.
 */
public interface Scene {

    void setGraphicsFPS(int fps);

    void setPhysicsFPS(int fps);

    void setHorizontalPeriod(float period);

    void zoom(float zoom);

    SceneObject createObject(float x, float y, ObjectGroup group);
    Sticker createSticker(float x, float y);

    void setBackgroundColor(float r, float g, float b);

    void addCollisionListener(CollisionListener listener);

    View getView(Context context);

    void run();
}
