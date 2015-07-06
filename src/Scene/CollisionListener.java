package Scene;

import java.util.List;

/**
 * Created by egor on 04.07.15.
 */
public class CollisionListener {
    public List<ObjectGroup> groups;
    public collisionProcessor onCollisionStart;
    public collisionProcessor onCollision;

    public void addGroup(ObjectGroup group) {
        groups.add(group);
    }

    public CollisionListener(ObjectGroup group1, ObjectGroup group2) {
        groups.add(group1);
        groups.add(group2);
    }

    public void processCollision(SceneObject object, SceneObject other) {
        if(onCollisionStart != null && !areColliding(object, other)) {
            onCollisionStart.process(object, other);
        }
        if(onCollision != null) onCollision.process(object, other);
    }

    public void processNoCollision(SceneObject object, SceneObject other) {
        if(!areColliding(object, other)) {
            stopCollision(object, other);
        }

    }

    private static class Pair {
        public SceneObject object;
        public SceneObject other;

        public Pair(SceneObject object, SceneObject other) {
            this.object = object;
            this.other = other;

        }
        public boolean equals(Pair p){
            return (object == p.object && other == p.other) || (object == p.other && other == p.object);
        }
    }
    private List<Pair> colliding;
    public boolean areColliding(SceneObject object, SceneObject other) {

        return colliding.contains(new Pair(object, other));
    }

    public void setOnCollisionStart(collisionProcessor onCollisionStart) {
        this.onCollisionStart = onCollisionStart;
    }

    public void setOnCollision(collisionProcessor onCollision) {
        this.onCollision = onCollision;
    }

    public void startCollision(SceneObject object, SceneObject other) {
        colliding.add(new Pair(object, other));
    }

    public void stopCollision(SceneObject object, SceneObject other) {
        while(colliding.remove(new Pair(object, other))) {};
    }
}

