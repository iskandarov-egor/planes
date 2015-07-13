package com.example.planes.Engine.Sprite;

/**
 * Created by egor on 13.07.15.
 */
public class Zigzag {
    private static Rect rect = new Rect(1, 1);
    private static Rect tiltedRect = new Rect(1, 1);
    private float angle;

    public Zigzag() {
        rect.setColor(0.2f, 0.2f, 0);
        tiltedRect.setColor(0.2f, 0.2f, 0);
    }

    public void setWH(float w, float h) {
//        this.w = w;
//        this.h = h;

        rect.setWH(w / h * 2, 0.1f);
        tiltedRect.setWH(2*(float) Math.hypot(1, w / h), 0.1f);
        angle = (float) Math.atan2(1, w / h);
    }

    public void draw(float x, float y, float[] matrix) {


        rect.draw(x, y + 1, 0, matrix);
        rect.draw(x, y - 1, 0, matrix);

        tiltedRect.draw(x, y, angle, matrix);
    }

}
