package com.example.planes.Engine.Scene;

import java.util.*;

/**
 * Created by egor on 13.07.15.
 */
final class CollisionManager {
    private final Scene scene;

    public CollisionManager(Scene scene) {
        this.scene = scene;
    }

    //private Map<ObjectGroup, List<SceneObject>> groupMap = new HashMap<>();
    private List<CollisionListener> colListeners = new ArrayList<>();

    void onObjectRemoved(SceneObject object) {
        //debug
        if(!scene.canRemoveObjects) throw new RuntimeException("что то не так");
        if(!canModify) throw new RuntimeException("что то совсем не так");

        for(CollisionListener listener : colListeners) {
            listener.onObjectRemoved(object);
        }
    }

    boolean canModify = true;

    private void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    void doCollisions() {
        setCanModify(false);
        for(CollisionListener listener : colListeners) {
            List<ObjectGroup> groups = listener.getGroups();
            for(int i = 0; i < groups.size(); i++){
                for(SceneObject object : groups.get(i).getList()) { // for each object in the group
                    if(scene.contains(object)) {
                        for (int j = i + 1; j < groups.size(); j++) {
                            for (SceneObject other : groups.get(j).getList()) { // for each object in another group
                                if (scene.contains(other) && object != other) {
                                    if (object.intersects(other, scene.getHorizPeriod())) {
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
        setCanModify(true);
    }

    public void addCollisionListener(CollisionListener listener) {
        if(colListeners.contains(listener)) throw new RuntimeException("This listener is already present");
        if(!canModify) throw new RuntimeException("cannot add collision listener while processing collisions");
        colListeners.add(listener);
    }

    public void removeCollisionListener(CollisionListener collisionListener) {
        if(!canModify) throw new RuntimeException("cannot remove collision listener while processing collisions");
        colListeners.remove(collisionListener);
    }

//    public void addToGroup(ObjectGroup group, SceneObject object) {
//        SceneObject impl = object;
//        List<SceneObject> contents = groupMap.get(group);
//        if(contents == null) contents = new LinkedList<>();
//
//        if(contents.contains(object)) throw new RuntimeException("already present");
//        contents.add(impl);
//        groupMap.put(group, contents);
//    }
}
