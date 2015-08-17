package com.example.planes.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by egor on 14.08.15.
 */
public class EngineTimerManager {
    List<EngineTimer> timers = new ArrayList<>();

    void addTask(Runnable runnable, float timeLeft) {
        iter.add(new EngineTimer(runnable, timeLeft));

        if(iterating) mustCallNext = true; // prevent illegal state ex
    }
    boolean iterating = false;
    boolean mustCallNext = false;

    ListIterator<EngineTimer> iter = timers.listIterator();
    void onPhysicsFrame(float fps) {
        float dt = 1/fps;
        iter = timers.listIterator();
        iterating = true;
        while(iter.hasNext()) {
            EngineTimer timer = iter.next();
            timer.timeLeft -= dt;
            if(timer.timeLeft <= 0) {
                timer.runnable.run();
                if(mustCallNext) {
                    iter.previous();
                    iter.previous();
                    mustCallNext = false;
                }
                iter.remove();
            }
        }
        iterating = false;
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
