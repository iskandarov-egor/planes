package Scene;

import Utils.AndroidCanvas;

import java.util.List;

/**
 * Created by egor on 01.07.15.
 */
public class Scene {
    private List<SceneObject> objects;

    private Camera camera;
    private int screenWidth;
    private int screenHeight;

    public void draw(AndroidCanvas canvas) {
        for(SceneObject object : objects) {
            double x = unitsToPixels(object.getX()*camera.zoom - camera.x);
            double y = unitsToPixels(object.getY() * camera.zoom - camera.y);
            double r = unitsToPixels(camera.zoom * object.getWidth() / 2);


            if(y + r > 0 && y - r < screenHeight) {
                //тащим х влево экрана
                while (x + r > 0) x -= screenWidth;
                //цикл рисования объекта и его горизонтальных образов если они помещаются
                while (x - r < screenWidth) {
                    x += screenWidth;
                    if (x + r > 0 && x - r < screenWidth) {
                        object.draw(canvas, x, y);
                    }
                }
            }
        }
    }
    private double width, height;

    public Scene(double width, double height) {
        this.width = width;
        this.height = height;
    }



    private double pixelsToUnits(double x) {
        return x * width / screenWidth;
    }

    private double unitsToPixels(double x) {
        return x * screenWidth / width;
    }

    public void removeObject(SceneObject object) {
        objects.remove(object);
    }

    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public void onFrame() {
        for(SceneObject object : objects) {
            object.onFrame();
        }
    }
}
