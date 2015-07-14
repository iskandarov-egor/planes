package com.example.planes.Engine.Body;

import com.example.planes.Engine.SceneObject;

/**
 * Created by egor on 02.07.15.
 */
public abstract class Body {


    public Body() {
    }


    // пересекается ли это тело с телом body, которое смещено на (dx, dy) относительно этого тела.
    // если period != 0 то считается что мир горизонтально повторяется с периодом period
    public abstract boolean intersects(Body body, float dx, float dy, float period);

    // находится ли точка внутри данного тела
    public abstract boolean isPointInside(float dx, float dy);


}
