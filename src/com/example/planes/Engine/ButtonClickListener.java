package com.example.planes.Engine;
import android.view.MotionEvent;
/**
 * Created by egor on 13.07.15.
 */
public interface ButtonClickListener {
    void onTouchEvent(SceneButton button, MotionEvent e);
}
