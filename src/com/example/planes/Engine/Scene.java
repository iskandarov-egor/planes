package com.example.planes.Engine;

import android.graphics.Point;

/**
 * Created by egor on 12.07.15.
 */
public interface Scene {
    // Период повторения мира по горизонтали (в экранах). Если равен нулю, то мир не повторяется.
    void setHorizontalPeriod(float period);

    SceneObject createObject(float x, float y, ObjectGroup group);

    // Создать объект, который не зависит от камеры, как бы приклеенный к камере
    Sticker createSticker(float x, float y);

    void setBackgroundColor(float r, float g, float b);

    public Viewport getViewport();
}
