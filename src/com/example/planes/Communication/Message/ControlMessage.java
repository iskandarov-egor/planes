package com.example.planes.Communication.Message;

import com.example.planes.Game.Models.Plane;

import java.nio.ByteBuffer;

/**
 * Created by egor on 08.08.15.
 */
public abstract class ControlMessage extends Message {
    private final float vx, vy;
    private final float x, y;
    private final float angle;

    ControlMessage(int type, int capacity, Plane plane) {
        super(type, 5*4 + capacity);
        vx = plane.getVx();
        vy = plane.getVy();
        x = plane.getX();
        y = plane.getY();
        angle = plane.getAngle();
        contents.putFloat(vx).putFloat(vy).putFloat(x).putFloat(y).putFloat(angle);
    }


    public ControlMessage(int type, int capacity, ByteBuffer buffet) {
        super(type, 5*4+capacity);
        vx = buffet.getFloat();
        vy = buffet.getFloat();
        x = buffet.getFloat();
        y = buffet.getFloat();
        angle = buffet.getFloat();
    }

    public float getAngle() {
        return angle;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
