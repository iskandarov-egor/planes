package com.example.planes.Interface;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.planes.Config.MenuConfig;
import com.example.planes.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by egor on 19.08.15.
 */
public class MenuActivity extends Activity {
    Activity that = this;

    RelativeLayout layout;
    Screen currentScreen;
    ArrayList<Screen> screens = new ArrayList<>(5);
    Screen mainScreen = new Screen();
    Screen modeScreen = new Screen();
    Screen gameScreen = new Screen();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);


        layout = (RelativeLayout) findViewById(R.id.relativelayout);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = layout.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                setupViews();
                makeClouds();
                setupScreens();
                goToScreen(mainScreen);
                //startCloudLoop();
            }

        });
    }


    Timer cloudTimer = new Timer("null", true);
    boolean clstopped = true;
    private void startCloudLoop() {
        if(!clstopped) throw new RuntimeException();
        clstopped = false;
        cloudTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (final Cloud cloud : clouds) {
                    cloud.post(new Runnable() {
                        @Override
                        public void run() {
                            cloud.move();
                        }
                    });
                }
            }
        }, 50, 50);

    }

    private void stopCloudLoop() {
        if(clstopped) throw new RuntimeException();
        cloudTimer.cancel();

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

    private void setupViews() {
        int h = layout.getBottom(); // get height
        float scale = h / 720f;

        layout.setBackgroundColor(MenuConfig.SKY_COLOR);
        TextView tvPlay = new MyTextView(this, (142f*scale), (0.49f*h), "Play");
        TextView tvChangeName = new MyTextView(this, scale * 46, 0.78f * h, "Change name");
        TextView tvBeServer = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.53f * h, "Create");
        TextView tvBeClient = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.7f * h, "Connect");

        tvPlay.setOnClickListener(makePlayClickListener());
        tvBeServer.setOnClickListener(makeServerClickListener());
        ImageView ivMain = new MainImageView(this, R.drawable.menu_planes);
        ImageView ivGame = new MainImageView(this, R.drawable.manu_game);
        TextView NEWGAME = new MyTextView(this, scale * 94, 0.046f * h, "NEW GAME");
        TextView selectPlane = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.32f * h, "Select plane");
        TextView selectMap = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.49f * h, "Select map");
        TextView tvWait = new MyTextView(this, scale * MenuConfig.FONT_SIZE_SMALL, 0.728f * h, "Waiting for " +
                "another\nplayer to connect");
        NEWGAME.setTextColor(Color.WHITE);
        mainScreen.add(ivMain).add(tvPlay).add(tvChangeName);
        modeScreen.add(ivMain).add(tvBeClient).add(tvBeServer);
        gameScreen.add(ivGame).add(NEWGAME).add(selectPlane).add(selectMap).add(tvWait);
    }

    ArrayList<Cloud> clouds = new ArrayList<>(6);
    private void makeClouds() {
        Cloud cloud = new Cloud(this, 0, 200, 1, 50);
        clouds.add(cloud);
        layout.addView(cloud);
    }

    private View.OnClickListener makeServerClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToScreen(gameScreen);
            }
        };
    }

    private void setupScreens() {

    }

    private void goToScreen(Screen screen) {
        if(currentScreen != null) currentScreen.leave();
        screen.open();
        currentScreen = screen;
    }

    private View.OnClickListener makePlayClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToScreen(modeScreen);
            }
        };
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        super.onBackPressed();
    }

    private class Screen {
        private final ArrayList<View> views = new ArrayList<>(4);



        public Screen add(View view) {
            views.add(view);
            return this;
        }

        public void open() {
            for(View view : views) {
                layout.addView(view);
            }
        }

        public void leave() {
            for(View view : views) {
                layout.removeView(view);
            }
        }
    }
}
