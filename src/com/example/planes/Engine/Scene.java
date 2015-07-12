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

    // Период повторения мира по горизонтали (в экранах). Если равен нулю, то мир не повторяется.
    void setHorizontalPeriod(float period);

    void zoom(float zoom);

    SceneObject createObject(float x, float y, ObjectGroup group);

    // Создать объект, который не зависит от камеры, как бы приклеенный к камере
    Sticker createSticker(float x, float y);

    void setBackgroundColor(float r, float g, float b);

    void addCollisionListener(CollisionListener listener);

    // для SetContentView в Activity
    View getView(Context context);

    void run();

    void pause();

    //void onPause();

    //void onResume();
}
