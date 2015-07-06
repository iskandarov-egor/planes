package Scene.Sprite;

import Utils.AndroidCanvas;

/**
 * Created by egor on 01.07.15.
 */
public abstract class Sprite {
    private double width;

    //Картинка кароч
    public Sprite() {

    }

    public abstract void draw(AndroidCanvas canvas, double x, double y);
    public abstract void onFrame();


    public double getWidth() {
        return width;
    }
}
