package com.example.planes.Engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by egor on 04.07.15.
 */
public final class CollisionListener {
    List<ObjectGroup> groups = new ArrayList<>();
    private CollisionProcessor onCollisionStart;
    private CollisionProcessor onCollision;
    private CollisionProcessor onCollisionEnd;

    public void addGroup(ObjectGroup group) {
        //debug
        if(group == null) throw new NullPointerException("group");

        groups.add(group);
    }

    public CollisionListener(ObjectGroup group1, ObjectGroup group2) {
        addGroup(group1);
        addGroup(group2);
    }

    private int counter = 0; //debug

    void processCollision(SceneObject object, SceneObject other) {
        //debug
        if(object == null) throw new NullPointerException("object");
        if(other == null) throw new NullPointerException("other");

        if(onCollisionStart != null && !areColliding(object, other)) {
            startCollision(object, other);
            onCollisionStart.process(object, other);
        }
        if(onCollision != null) onCollision.process(object, other);
    }

    void processNoCollision(SceneObject object, SceneObject other) {
        if(areColliding(object, other)) {
            stopCollision(object, other);
            onCollisionEnd.process(object, other);
        }
    }

    public void setOnCollisionEnd(CollisionProcessor onCollisionEnd) {
        //debug
        if(onCollisionEnd == null) throw new NullPointerException("processor");

        this.onCollisionEnd = onCollisionEnd;
    }

    private final static class Pair {
        public SceneObject object;
        public SceneObject other;

        public Pair(SceneObject object, SceneObject other) {
            this.object = object;
            this.other = other;
        }
        @Override
        public boolean equals(Object o){
            if(o instanceof Pair) {
                Pair p = (Pair)o;
                return (object == p.object && other == p.other) || (object == p.other && other == p.object);
            } else return this == o;
        }

    }
    private List<Pair> colliding = new LinkedList<>(); // array maybe?
    public boolean areColliding(SceneObject object, SceneObject other) {
        //debug
        if(object == null) throw new NullPointerException("object");
        if(other == null) throw new NullPointerException("other");

        Pair pair = new Pair(object, other);
        return colliding.contains(pair);
    }

    public void setOnCollisionStart(CollisionProcessor onCollisionStart) {
        //debug
        if(onCollisionStart == null) throw new NullPointerException("processor");

        this.onCollisionStart = onCollisionStart;
    }

    public void setOnCollision(CollisionProcessor onCollision) {
        //debug
        if(onCollisionEnd == null) throw new NullPointerException("processor");

        this.onCollision = onCollision;
    }

    private void startCollision(SceneObject object, SceneObject other) {
        //debug
        if(object == null) throw new NullPointerException("object");
        if(other == null) throw new NullPointerException("other");
        if(colliding.contains(new Pair(object, other))) throw new RuntimeException("already colliding!");
        if(colliding.size() != counter) throw new RuntimeException("whoops");
        counter++;

        colliding.add(new Pair(object, other));

    }

    private void stopCollision(SceneObject object, SceneObject other) {
        //debug
        if(object == null) throw new NullPointerException("object");
        if(other == null) throw new NullPointerException("other");
        if(!colliding.contains(new Pair(object, other))) throw new RuntimeException("were not colliding!");
        if(colliding.size() != counter) throw new RuntimeException("whoops");
        if(!colliding.contains(new Pair(object, other))) throw new RuntimeException("whoops");

        while(colliding.remove(new Pair(object, other))) counter--;
    }
}

