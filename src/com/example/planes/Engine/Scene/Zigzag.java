package com.example.planes.Engine.Scene;

/**
 * Created by egor on 13.07.15.
 */
public class Zigzag {
    private static Rect rect = new Rect();
    private static Rect tiltedRect = new Rect();
    private float angle;
    private float w, h;

    public Zigzag() {
        rect.setColor(0.5f, 0.5f, 1);
        tiltedRect.setColor(0.5f, 0.5f, 1);
    }

    public void setWH(float w, float h) {
        rect.setWH(w, 0.05f);
        tiltedRect.setWH((float) Math.hypot(h, w), 0.05f);
        angle = (float) Math.atan2(h, w);
        this.w = w;
        this.h = h;
    }

    public void draw(float x, float y, float[] matrix) {


        rect.draw(x, y + h/2, 0, matrix);
        rect.draw(x, y - h/2, 0, matrix);

        tiltedRect.draw(x, y, angle, matrix);
    }

}
