package Scene;

import Utils.AndroidCanvas;

import java.util.*;

/**
 * Created by egor on 01.07.15.
 */
public class Scene {
    private List<SceneObject> objects;

    private Map<ObjectGroup, List<SceneObject>> groupMap = new HashMap<>();
    private Camera camera = new Camera();
    private int screenWidth = 444;
    private int screenHeight = 444;
    private List<CollisionListener> colListeners = null;
    private Runnable onPhysicsFrame = null;
    private Runnable onGraphicsFrame = null;

    private GameLoop gameLoop;
    public void play(){
        gameLoop.run();
    }
    public void pause(){
        gameLoop.pause();
    }
    public void draw(AndroidCanvas canvas) {
        for(SceneObject object : objects) {
            double x = unitsToPixels(object.getX() * camera.zoom - camera.x);
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
        Runnable onPhysicsFrameRunnable = this::onPhysicsFrame;
        Runnable onGraphicsFrameRunnable = this::onGraphicsFrame;
        gameLoop = new GameLoop(onPhysicsFrameRunnable, onGraphicsFrameRunnable);

    }

    private void onPhysicsFrame(){



    }

    private void onGraphicsFrame() {

    }

    private double pixelsToUnits(double x) {
        return x * width / screenWidth;
    }

    private double unitsToPixels(double x) {
        return x * screenWidth / width;
    }


    private void removeObjectNow(SceneObject object) {
        objects.remove(object);
        while(groupMap.values().remove(object));
    }


    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public void addObject(SceneObject object, ObjectGroup group) {
        addObject(object);
        addToGroup(group, object);
    }

    boolean canRemove = true;
    public void onFrame() {
        setCanRemove(false);
        for(SceneObject object : objects) {
            object.onFrame();
        }

        for(CollisionListener listener : colListeners) {
            List<ObjectGroup> groups = listener.groups;
            for(int i = 0; i < groups.size(); i++){
                for(SceneObject object : groupMap.get(groups.get(i))) { // for each object in the group
                    for (int j = i + 1; i < groups.size(); j++) {
                        for (SceneObject other : groupMap.get(groups.get(j))) { // for each object in another group
                            if(object != other) {
                                if(object.intersects(other)) {
                                    listener.processCollision(object, other);
                                } else {
                                    listener.processNoCollision(object, other);
                                }
                            }
                        }
                    }
                }
            }
        }
        setCanRemove(true);
    }

    private Queue<SceneObject> removeQueue = new ArrayDeque<>();
    public void removeObject(SceneObject object) {
        if(canRemove) {
            objects.remove(object);
        } else {
            removeQueue.add(object);
            object.setRemoved(true);
        }
    }
    public boolean canRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
        if(canRemove) {
            SceneObject object = removeQueue.remove();
            while(object != null) {
                objects.remove(object);
            }
        }
    }



    public void setOnPhysicsFrame(Runnable onPhysicsFrame) {
        this.onPhysicsFrame = onPhysicsFrame;
    }

    public void setOnGraphicsFrame(Runnable onGraphicsFrame) {
        this.onGraphicsFrame = onGraphicsFrame;
    }


    public void pauseAndClear() {
        pause();
        objects = null;
        colListeners = null;
    }

    public void addCollisionListener(CollisionListener group) {
        colListeners.add(group);
    }

    public void addToGroup(ObjectGroup group, SceneObject object) {
        List<SceneObject> contents = groupMap.get(object);
        if(contents == null) contents = new LinkedList<>();
        contents.add(object);
        groupMap.put(group, contents);
    }
}
