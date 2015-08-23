package com.example.planes.Interface;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by egor on 23.08.15.
 */
public abstract class ActivityWithScreens extends ActivityWithClouds {
    public ActivityWithScreens(String name) {
        super(name);
    }

    Screen currentScreen;

    protected void goToScreen(Screen screen) {
        if(currentScreen != null) currentScreen.leave();
        screen.open();
        currentScreen = screen;
    }

    protected class Screen {
        private final ArrayList<View> views = new ArrayList<>(4);

        public Screen add(View view) {
            views.add(view);
            return this;
        }

        public void open() {
            runOnUiThread(openRunnable);
        }

        Runnable openRunnable = new Runnable() {
            @Override
            public void run() {
                for(View view : views) {
                    layout.addView(view);
                }
            }
        };

        Runnable leaveRunnable = new Runnable() {
            @Override
            public void run() {
                for(View view : views) {
                    layout.removeView(view);
                }
            }
        };

        public void leave() {
            runOnUiThread(leaveRunnable);

        }
    }
}
