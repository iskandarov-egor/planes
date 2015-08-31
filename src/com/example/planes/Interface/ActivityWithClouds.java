package com.example.planes.Interface;

import android.widget.RelativeLayout;
import com.example.planes.Utils.GameStarter;
import com.example.planes.Utils.Helper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by egor on 23.08.15.
 */
public abstract class ActivityWithClouds extends GameStarter {
    Timer cloudTimer = new Timer("null", true);
    boolean clstopped = true;
    TimerTask cloudTask = null;

    public ActivityWithClouds(String name) {
        super(name);
    }

    private void startCloudLoop() {
        if(!clstopped) throw new RuntimeException();
        clstopped = false;
        cloudTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Cloud cloud : clouds) {
                            cloud.move();
                        }
                    }
                });

            }
        };
        cloudTimer.scheduleAtFixedRate(cloudTask, 50, 33);
    }

    private void stopCloudLoop() {
        if(clstopped) throw new RuntimeException();
        clstopped = true;
        cloudTask.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCloudLoop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCloudLoop();
    }

    protected RelativeLayout layout;

    ArrayList<Cloud> clouds = new ArrayList<>(6);
    private void addCloud(Cloud cloud) {
        clouds.add(cloud);
        layout.addView(cloud);
    }

    protected void makeClouds() {
        int h = Helper.getScreenHeight();
        int w = Helper.getScreenWidth();
        float ch = h * 0.1f;
        for(int i = 0; i < 7; i++) addCloud(new Cloud(this, w, h));
    }
}
