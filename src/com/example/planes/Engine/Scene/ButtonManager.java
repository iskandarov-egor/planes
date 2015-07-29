package com.example.planes.Engine.Scene;

import android.view.MotionEvent;
import com.example.planes.Engine.SceneButtonListener;
import com.example.planes.Engine.Utils;

import java.util.ArrayList;

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

    private SceneButton held = null;

    public boolean onTouch(MotionEvent e) {
        if(listener == null) return false;
        Utils.FloatPoint pos = scene.getViewport().screenToEngine(e.getRawX(), e.getRawY());
        float x = pos.x;
        float y = pos.y;

        canRemove = false;

        if(e.getAction() == MotionEvent.ACTION_UP && held != null) {
            listener.onButtonUp(held, held.isPointInside(x, y));
            held = null;
        }

        for (SceneButton btn : buttons) {
            if (btn.isPointInside(x, y)) {
                switch(e.getAction() ){
                    case MotionEvent.ACTION_DOWN:
                        listener.onButtonDown(btn);
                        held = btn;
                        canRemove = true;
                        return true;
                }

            }
        }

        canRemove = true;
        return false;
    }
}