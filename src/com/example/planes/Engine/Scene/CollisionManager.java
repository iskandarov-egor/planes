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
        if(!scene.canModifyObjects) throw new RuntimeException("что то не так");
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
        for(CollisionListener listener : colListeners) {  // for each listener
            List<ObjectGroup> groups = listener.getGroups();
            for(int i = 0; i < groups.size(); i++){ // for each group in listener
                ObjectGroup group1 = groups.get(i);
                for(int soi = 0; soi < group1.getList().size(); soi++) { // for each object in the group
                    SceneObject object = group1.getList().get(soi);
                    for (int j = i + 1; j < groups.size(); j++) { // another group in listener
                        ObjectGroup group2= groups.get(j);
                        int soj = 0;
                        if(group1 == group2) soj = soi + 1;
                        for (;soj < group2.getList().size(); soj++) { // for each object in another group
                            SceneObject other = group2.getList().get(soj);
                            if (object != other) {
                                if (object.intersects(other, scene.getWorldWidth())) {
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

    void onCanModify() {
        for(CollisionListener list : colListeners) {
            for(ObjectGroup group : list.getGroups()) {
                group.onCanModify();
            }
        }
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
