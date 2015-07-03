package Scene;

import Sprite.Sprite;
import Utils.AndroidCanvas;
import Utils.MathHelper;
import Utils.Vector;

import java.util.List;

/**
 * Created by egor on 01.07.15.
 */
public class SceneObject {
    protected Vector position;
    protected Vector speed;
    
    private double angle = 0;
    private double angleSpeed = 0;
    private Sprite sprite = null;
    private Body body = null;
    private SceneObject parent = null;


    public SceneObject(double x, double y) {
        position.set(x, y);

    }
    
    public void moveBy(double dx, double dy) {
        position.add(dx, dy);
    };
    public void moveTo(int x, int y) {
        position.set(x, y);
    };
    public void onFrame() {
        position.add(speed);
        angle += angleSpeed;
    }
    public void rotate(double angle) {};

    public void setSprite(Sprite sprite){
        sprite = sprite;
    }

    public void removeSprite() {
        sprite = null;
    }

    public void setSpeed(double vx, double vy) {
        speed.set(vx, vy);
    }

    public double getX() {
        if(parent != null) return parent.getX() + position.x;
        return position.x;
    }

    public double getY() {
        if(parent != null) return parent.getY() + position.y;
        return position.y;
    }

    public Vector getSpeed() {
        if(parent != null) return MathHelper.vectorSum(parent.getSpeed(), speed);
        return speed;
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
    }

    public boolean intersects(SceneObject object) {
        if(!hasBody() || !object.hasBody()) return false;
        return body.intersects(object.body);
    }
    
    public boolean hasBody(){
        return body != null;
    }

    public Body getBody() {
        return body;
    }

    public Vector getPosition() {
        if(parent != null) return MathHelper.vectorSum(parent.getPosition(), position);
        return position;
    }

    public void setParent(SceneObject parent) {
        this.parent = parent;
    }

    public SceneObject getParent() {
        return parent;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public double getWidth() {
        if(sprite == null) return 0;
        return sprite.getWidth();
    }

    public void draw(AndroidCanvas canvas, double x, double y) {
        if(sprite != null) sprite.draw(canvas, x, y);
    }
}
