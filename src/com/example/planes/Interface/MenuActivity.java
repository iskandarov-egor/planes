package com.example.planes.Interface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.MenuConfig;
import com.example.planes.R;
import com.example.planes.Utils.GameStarter;
import com.example.planes.Utils.Helper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by egor on 19.08.15.
 */
public class MenuActivity extends Activity {
    Activity that = this;
    final GameStarter gameStarter = new GameStarter(this);
    RelativeLayout layout;
    Screen currentScreen;
    ArrayList<Screen> screens = new ArrayList<>(5);
    Screen mainScreen = new Screen();
    Screen modeScreen = new Screen();
    Screen gameScreen = new Screen();

    TextView tvWait;
    TextView tvPlayReady;

    public MenuActivity() {
        super();
        Log.d("hey", "menu constructor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hey", "menu oncreate");
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
                setupScreens();
                makeClouds();

                goToScreen(mainScreen);
                //startCloudLoop();
            }

        });
    }


    Timer cloudTimer = new Timer("null", true);
    boolean clstopped = true;
    TimerTask cloudTask = null;
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
        Log.d("hey", "menu onpause");
        stopCloudLoop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("hey", "menu onresume");
        startCloudLoop();
    }

    private void setupViews() {
        int h = layout.getHeight(); // get height
        float scale = h / 720f;

        layout.setBackgroundColor(MenuConfig.SKY_COLOR);
        TextView tvPlay = new MyTextView(this, (142f*scale), (0.49f*h), "Play");
        TextView tvChangeName = new MyTextView(this, scale * 46, 0.78f * h, "Change name");
        TextView tvBeServer = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.53f * h, "Create");
        TextView tvBeClient = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.7f * h, "Connect");

        tvPlay.setOnClickListener(makePlayClickListener());

        ImageView ivMain = new MainImageView(this, R.drawable.menu_planes);
        ImageView ivGame = new MainImageView(this, R.drawable.manu_game);
        TextView NEWGAME = new MyTextView(this, scale * 94, 0.046f * h, "NEW GAME");
        TextView selectPlane = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.32f * h, "Select plane");
        TextView selectMap = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.49f * h, "Select map");
        tvWait = new MyTextView(this, scale * MenuConfig.FONT_SIZE_SMALL, 0.728f * h, "Waiting for " +
                "another\nplayer to connect");
        NEWGAME.setTextColor(Color.WHITE);
        mainScreen.add(ivMain).add(tvPlay).add(tvChangeName);
        modeScreen.add(ivMain).add(tvBeClient).add(tvBeServer);
        gameScreen.add(ivGame).add(NEWGAME).add(selectPlane).add(selectMap).add(tvWait);
        tvPlayReady = new MyTextView(this, 142*scale, 0.728f*h, "Not ready");

        tvBeServer.setOnClickListener(makeBeServerListener());
        tvBeClient.setOnClickListener(makeBeClientListener());
        Button playBT = (Button) findViewById(R.id.noBtBtn);
        playBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameStarter.startWithNoBT();
            }
        });

    }



    private View.OnClickListener makeBeClientListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameStarter.connectAsClient();
                //goToScreen(gameScreen);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameStarter.onActivityResult(requestCode, resultCode, data);

    }

    ArrayList<Cloud> clouds = new ArrayList<>(6);
    private void addCloud(Cloud cloud) {
        clouds.add(cloud);
        layout.addView(cloud);
    }

    private void makeClouds() {
        int h = Helper.getScreenHeight();
        int w = Helper.getScreenWidth();
        float ch = h * 0.1f;
        for(int i = 0; i < 7; i++) addCloud(new Cloud(this, w, h));
//        addCloud(new Cloud(this, 0, h * 0.5f, 1.1f, ch));
//        addCloud(new Cloud(this, 0.2f * w, h * 0.5f, 1.15f, ch));
//        addCloud(new Cloud(this, 0.4f * w, h * 0.3f, 0.95f, ch*1.15f));
//        addCloud(new Cloud(this, 0.7f * w, h * 0.1f, 1.33f, ch*1.4f));
//        addCloud(new Cloud(this, 0.1f * w, h * 0.25f, 1.3f, ch*1.3f));
//        addCloud(new Cloud(this, 0.89f * w, h * 0.77f, 0.8f, ch*0.8f));
    }

    private View.OnClickListener makeBeServerListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToScreen(gameScreen);
                gameStarter.startServer();
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

    public void onAccepted(final RemoteAbonent name) {
        Log.d("hey", "menu onAccepted");
        float h = layout.getHeight();
        tvWait.post(new Runnable() {
            @Override
            public void run() {
                tvWait.setText(Helper.cutString(name.getDevice().getName(), 7) + "\nis not ready");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("hey", "menu ondestroy");
        gameStarter.destruct();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("hey", "menu onrestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("hey", "menu onstart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("hey", "menu onstop");
    }

    public void onConnected(RemoteAbonent abonent) {
        goToScreen(gameScreen);
        tvPlayReady.setText("Not ready"); // todo check if has open game

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

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for(View view : views) {
                    layout.removeView(view);
                }
            }
        };

        public void leave() {
            runOnUiThread(runnable);

        }
    }
}
