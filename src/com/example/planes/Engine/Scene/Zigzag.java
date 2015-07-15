package com.example.planes.Engine.Scene;

/**
 * Created by egor on 13.07.15.
 */
public class Zigzag {
    private static Rect rect = new Rect(1, 1);
    private static Rect tiltedRect = new Rect(1, 1);
    private float angle;

    public Zigzag() {
        rect.setColor(0.5f, 0.5f, 1);
        tiltedRect.setColor(0.5f, 0.5f, 1);
    }

    public void setWH(float w, float h) {
        rect.setWH(w, 0.05f);
        tiltedRect.setWH((float) Math.hypot(h, w), 0.05f);
        angle = (float) Math.atan2(h, w);
    }

    public void draw(float x, float y, float[] matrix) {


        rect.draw(x, y + 1, 0, matrix);
        rect.draw(x, y - 1, 0, matrix);

        tiltedRect.draw(x, y, angle, matrix);
    }

}
