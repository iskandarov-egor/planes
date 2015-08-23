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

/**
 * Created by egor on 19.08.15.
 */
public class MenuActivity extends ActivityWithScreens {
    Activity that = this;
    final GameStarter gameStarter = new GameStarter(this);

    ArrayList<Screen> screens = new ArrayList<>(5);
    Screen mainScreen = new Screen();
    Screen modeScreen = new Screen();
    Screen serverScreen = new Screen();
    Screen clientScreen = new Screen();

    TextView tvWait;
    TextView tvPlayServer;
    TextView tvReady;
    RemoteAbonent them = null;

    private enum State {
        CLIENT_CONNECTING, CLIENT_NOT_READY, CLIENT_READY, SERVER_WAITING, SERVER_READY, SERVER_NOT_READY, NONE
    }

    private State state = State.NONE;

    public MenuActivity() {
        super("Menu");
        Log.d("hey", "menu constructor");
    }

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
                setupScreens();
                makeClouds();

                goToScreen(mainScreen);
                //startCloudLoop();
            }

        });
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
        tvPlayServer = new MyTextView(this, 142*scale, 0.728f*h, "Play");
        tvPlayServer.setVisibility(View.GONE);
        tvReady = new MyTextView(this, 142*scale, 0.728f*h, "");

        NEWGAME.setTextColor(Color.WHITE);
        mainScreen.add(ivMain).add(tvPlay).add(tvChangeName);
        modeScreen.add(ivMain).add(tvBeClient).add(tvBeServer);
        serverScreen.add(ivGame).add(NEWGAME).add(selectPlane).add(selectMap).add(tvWait).add(tvPlayServer);
        clientScreen.add(ivGame).add(NEWGAME).add(selectPlane).add(selectMap).add(tvWait).add(tvReady);


        tvBeServer.setOnClickListener(makeBeServerListener());
        tvBeClient.setOnClickListener(makeBeClientListener());
        tvReady.setOnClickListener(makeReadyListener());

        Button playBT = (Button) findViewById(R.id.noBtBtn);
        playBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameStarter.startWithNoBT();
            }
        });

    }

    private View.OnClickListener makeReadyListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(state) {
                    case CLIENT_NOT_READY:
                        state = State.CLIENT_READY;
                        tvReady.setText("Ready");
                        break;
                    case CLIENT_READY:
                        state = State.CLIENT_NOT_READY;
                        tvReady.setText("Not ready");
                        break;
                    default: throw new RuntimeException("wrong state");
                }
            }
        };
    }

    private View.OnClickListener makeBeClientListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state != State.NONE) throw new RuntimeException();
                state = State.CLIENT_CONNECTING;
                gameStarter.connectAsClient();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameStarter.onActivityResult(requestCode, resultCode, data);
    }

    private View.OnClickListener makeBeServerListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state != State.NONE) throw new RuntimeException();
                state = State.SERVER_WAITING;
                goToScreen(serverScreen);
                gameStarter.startServer();
            }
        };
    }

    private void setupScreens() {

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
        if(state != State.SERVER_WAITING) throw new RuntimeException();
        state = State.SERVER_NOT_READY;
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
        gameStarter.destruct();
    }

    public void onConnected(RemoteAbonent abonent) {
        throw new RuntimeException();
    }

    public void onConnectedFromBTList(RemoteAbonent abonent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                goToScreen(clientScreen);
                if(state != State.CLIENT_CONNECTING) throw new RuntimeException();
                state = State.CLIENT_NOT_READY;
                tvReady.setText("Not ready");
            }
        });
    }
}
