package com.example.planes.Utils;

/**
 * Created by egor on 02.07.15.
 */
public class Vector {
    public float x, y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vector) {
        x = vector.x;
        y = vector.y;
    }

    public Vector() {
        x = 0; y = 0;
    }


    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector getOrt() {
        float len = getLength();
        return new Vector(x / len, y / len);
    }

    public float getLength() {
        return (float)Math.hypot(x, y);
    }
    public void add(Vector vector) {
        x += vector.x;
        y += vector.y;
    }
    public void multiply(float by) {
        x *= by;
        y *= by;
    }

    public void add(float dx, float dy) {
        x += dx;
        y += dy;
    }
}
