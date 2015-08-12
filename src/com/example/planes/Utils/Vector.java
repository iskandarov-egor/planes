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

    public void rotate(float centerX, float centerY, float angle) {
        float x1 = x - centerX;
        float y1 = y - centerY;
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        x = x1 * cos - y1 * sin + centerX;
        y = x1 * sin + y1 * cos + centerY;
    }

    public void rotate(float angle) {
        rotate(0, 0, angle);
    }

    public static float vecProduct(Vector v1, Vector v2) {
        return v1.x*v2.y - v2.x*v1.y;
    }
}
