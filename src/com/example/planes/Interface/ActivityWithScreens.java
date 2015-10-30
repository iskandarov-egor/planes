package com.example.planes.Interface;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by egor on 23.08.15.
 */
public abstract class ActivityWithScreens extends ActivityWithClouds {
    public ActivityWithScreens(String name) {
        super(name);
    }

    Screen currentScreen;
    Stack<Screen> stack = new Stack<>();
    protected void emptyStack() {
        stack.clear();
    }

    protected void goToScreen(Screen screen) {
        if (currentScreen != null) {
            stack.push(currentScreen);
            Log.d("scrr", "push");
        }
        switchScreen(screen);
    }

    protected void switchScreen(Screen screen) {
        if(currentScreen != null) currentScreen.leave();
        screen.open();
        currentScreen = screen;
        Log.d("scrr", "seitch");
    }

    @Override
    public void onBackPressed() {
        if (stack.isEmpty()) {
            super.onBackPressed();
            Log.d("scrr", "bpr");
        } else {
            goBack();
        }

    }

    protected void goBack(){
        Log.d("scrr", "goback");
        if (stack.isEmpty()) return;
        Screen prev = stack.pop();
        Log.d("scrr", "poped");
        switchScreen(prev);
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
