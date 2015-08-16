package com.example.planes.Engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

/**
 * Created by egor on 14.08.15.
 */
public class EngineTimerManager {
    List<EngineTimer> timers = new ArrayList<>();

    void addTask(Runnable runnable, float timeLeft) {
        timers.add(new EngineTimer(runnable, timeLeft));
    }

    void onPhysicsFrame(float fps) {
        float dt = 1/fps;
        Iterator<EngineTimer> i = timers.iterator();
        while(i.hasNext()) {
            EngineTimer timer = i.next();
            timer.timeLeft -= dt;
            if(timer.timeLeft <= 0) {
                timer.runnable.run();
                i.remove();
            }
        }
    }

    private static class EngineTimer {
        Runnable runnable;
        float timeLeft;

        public EngineTimer(Runnable runnable, float timeLeft) {
            this.runnable = runnable;
            this.timeLeft = timeLeft;
        }


    }
}
