package Scene;

import Utils.MathHelper;
import Utils.Vector;

/**
 * Created by egor on 02.07.15.
 */
public class Body {
    // пока что круг
    public double radius; // знаю что публик плохо, если хочешь уберу
    public SceneObject owner;

    public Body(SceneObject owner, double radius) {
        this.owner = owner;
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public boolean intersects(Body body) {
        return MathHelper.dist(owner.getPosition(), body.getOwner().getPosition()) < radius + body.getRadius();
    }

    public SceneObject getOwner() {
        return owner;
    }
}
