package com.example.planes.Engine;

import java.util.*;

/**
 * Created by egor on 13.07.15.
 */
public final class CollisionManager {
    SceneImpl scene;

    public CollisionManager(SceneImpl scene) {
        this.scene = scene;
    }

    private Map<ObjectGroup, List<ObjectImpl>> groupMap = new HashMap<>();
    private List<CollisionListener> colListeners = new ArrayList<>();

    void doCollisions() {


        for(CollisionListener listener : colListeners) {
            List<ObjectGroup> groups = listener.groups;
            for(int i = 0; i < groups.size(); i++){
                for(ObjectImpl object : groupMap.get(groups.get(i))) { // for each object in the group
                    for (int j = i + 1; j < groups.size(); j++) {
                        for (ObjectImpl other : groupMap.get(groups.get(j))) { // for each object in another group
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

    public void addToGroup(ObjectGroup group, SceneObject object) {
        ObjectImpl impl = object.getImpl();
        List<ObjectImpl> contents = groupMap.get(group);
        if(contents == null) contents = new LinkedList<>();

        if(contents.contains(object)) throw new RuntimeException("already present");
        contents.add(impl);
        groupMap.put(group, contents);
    }
}
