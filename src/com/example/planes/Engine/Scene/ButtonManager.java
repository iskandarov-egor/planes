package com.example.planes.Engine.Scene;

import android.util.Log;
import android.view.MotionEvent;
import com.example.planes.Engine.SceneButtonListener;
import com.example.planes.Engine.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by egor on 16.07.15.
 */
class ButtonManager {
    SceneButtonListener listener = null;
    ArrayList<SceneButton> buttons = new ArrayList<>();
    final Scene scene;

    public ButtonManager(Scene scene) {
        this.scene = scene;
    }

    public void addButton(SceneButton btn) {
        if(btn == null) throw new NullPointerException("button");
        if(buttons.contains(btn)) throw new RuntimeException("already present");

        buttons.add(btn);
    }

    private boolean canRemove = true;

    public void setListener(SceneButtonListener listener) {
        this.listener = listener;
    }

    private Map<Integer, SceneButton> held = new HashMap<>();

    private int lol = 0;
    private static int slol = 0;

    public boolean onTouch(int action, float x, float y, int pointerId, int vid) {
        if(listener == null) return false;

        canRemove = false;


        int type = action & MotionEvent.ACTION_MASK;
        if(type == MotionEvent.ACTION_SCROLL) throw new RuntimeException("SCROLL!");
        if(type == MotionEvent.ACTION_HOVER_ENTER) throw new RuntimeException("HOVER!");
        if(type == MotionEvent.ACTION_OUTSIDE) throw new RuntimeException("OUTSIDE!");
        if(type != MotionEvent.ACTION_POINTER_DOWN && type != MotionEvent.ACTION_DOWN &&
                type != MotionEvent.ACTION_UP && type != MotionEvent.ACTION_POINTER_UP
                && type != MotionEvent.ACTION_CANCEL) return false;



        log(type, vid);
        Log.d("multitouch madness", String.valueOf(lol)+ " " + String.valueOf(slol));
        lol++;
        slol++;

        Utils.FloatPoint pos = scene.getViewport().screenToEngine(x, y);
        float ex = pos.x;
        float ey = pos.y;

        if(type == MotionEvent.ACTION_POINTER_UP || type == MotionEvent.ACTION_UP || type == MotionEvent.ACTION_CANCEL) {
            SceneButton btn = held.get(pointerId);
            Log.d("multitouch madness", "it is an UP");
            if(btn != null) {
                Log.d("multitouch madness", ".. and there's a button, fire event and remove from held");
                held.remove(pointerId);
                listener.onButtonUp(btn, btn.isPointInside(ex, ey));
                canRemove = true;
                return true;
            }
            Log.d("multitouch madness", ".. and there's nmo button mayan! you missed or something?");
            return false;
        }
        Log.d("multitouch madness", "looks like it is a DOWN or something");
        for (SceneButton btn : buttons) {
            if (btn.isPointInside(ex, ey) && btn.getVisible()) {

                if(type == MotionEvent.ACTION_DOWN || type == MotionEvent.ACTION_POINTER_DOWN) {
                    Log.d("multitouch madness", ".. found button, fire and put in held");
                    listener.onButtonDown(btn);
                    held.put(pointerId, btn);
                    canRemove = true;
                    return true;
                }

            }
        }
        Log.d("multitouch madness", ".. didn't find button, you missed or something?");

        canRemove = true;
        return false;
    }

    private void log(int action, int vid) {
        int type = action & MotionEvent.ACTION_MASK;
        switch (type) {
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("multitouch madness", "pt down" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_DOWN:
                Log.d("multitouch madness", "down" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("multitouch madness", "up" + String.valueOf(vid));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("multitouch madness", "pt up" + String.valueOf(vid));
                break;
            default:
                Log.d("multitouch madness", "something else" + String.valueOf(vid));
        }
    }

    private class Pair {
        int pid;
        SceneButton btn;

        public Pair(SceneButton btn, int pid) {
            this.btn = btn;
            this.pid = pid;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof Pair) {
                Pair p = (Pair) o;
                return pid == p.pid && btn == p.btn;
            } else return super.equals(o);
        }

        @Override
        public int hashCode() {
            return pid;
        }
    }
}