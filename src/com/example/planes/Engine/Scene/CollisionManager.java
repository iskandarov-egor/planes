package com.example.planes.Engine.Scene;

import com.example.planes.Engine.ObjectGroup;

import java.util.*;

/**
 * Created by egor on 13.07.15.
 */
final class CollisionManager {
    private final Scene scene;

    public CollisionManager(Scene scene) {
        this.scene = scene;
    }


    private Map<ObjectGroup, List<StaticObject>> groupMap = new HashMap<>();
    private List<CollisionListener> colListeners = new ArrayList<>();

    void doCollisions() {
        for(CollisionListener listener : colListeners) {
            List<ObjectGroup> groups = listener.getGroups();
            for(int i = 0; i < groups.size(); i++){
                // todo null pointer
                for(StaticObject object : groupMap.get(groups.get(i))) { // for each object in the group
                    for (int j = i + 1; j < groups.size(); j++) {
                        for (StaticObject other : groupMap.get(groups.get(j))) { // for each object in another group
                            if(object != other) {
                                if(object.intersects(other, scene.getHorizPeriod())) {
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
    }

    public void addCollisionListener(CollisionListener listener) {
        if(colListeners.contains(listener)) throw new RuntimeException("This listener is already present");
        colListeners.add(listener);
    }

    public void addToGroup(ObjectGroup group, StaticObject object) {
        StaticObject impl = object;
        List<StaticObject> contents = groupMap.get(group);
        if(contents == null) contents = new LinkedList<>();

        if(contents.contains(object)) throw new RuntimeException("already present");
        contents.add(impl);
        groupMap.put(group, contents);
    }
}
