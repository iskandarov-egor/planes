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
import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.Message.ReadyMessage;
import com.example.planes.Communication.Message.ReceivedMessage;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.BmpConfig;
import com.example.planes.Config.MenuConfig;
import com.example.planes.R;
import com.example.planes.Utils.Helper;

/**
 * Created by egor on 19.08.15.
 */
public class MenuActivity extends ActivityWithScreens {
    Activity that = this;
    //final GameStarter gameStarter = new GameStarter(this);

    Screen mainScreen = new Screen();
    Screen modeScreen = new Screen();
    Screen serverScreen = new Screen();
    Screen clientScreen = new Screen();
    Screen resultScreen = new Screen();
    Screen victoryScreen = new Screen();
    Screen defeatScreen = new Screen();

    TextView tvWait;
    TextView tvPlayServer;
    TextView tvReady;
    TextView tvResultTitle;
    TextView tvResultScore;
    TextView tvOK;
    RemoteAbonent them = null;

    public void onReadyMessage(final boolean ready) {
        if(state != State.SERVER_NOT_READY && state != State.SERVER_READY) throw new RuntimeException();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ready) {
                    state = State.SERVER_READY;
                    tvPlayServer.setVisibility(View.VISIBLE);
                    tvWait.setVisibility(View.GONE);
                } else {
                    state = State.SERVER_NOT_READY;
                    tvPlayServer.setVisibility(View.GONE);
                    tvWait.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onDisconnected() {
        if(state == State.NONE) throw new RuntimeException();
        state = State.NONE;
        DialogHelper.showOKDialog(this, "", "Connection was lost");
        ///goToScreen(mainScreen);
        goBack();
    }

    private enum State {
        CLIENT_NOT_READY, CLIENT_READY, SERVER_WAITING, SERVER_READY, SERVER_NOT_READY, NONE,
        SERVER_RESULTS, CLIENT_RESULTS, SERVER_GAME, CLIENT_GAME
    }

    private State state = State.NONE;

    public MenuActivity() {
        super("Menu");
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
        TextView tvPlay = new MyTextView(this, (MenuConfig.FONT_SIZE_HUGE *scale), (0.49f*h), "Play");
        TextView tvChangeName = new MyTextView(this, scale * MenuConfig.FONT_SIZE_SMALL, 0.78f * h, "Change name");
        TextView tvBeServer = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.53f * h, "Create");
        TextView tvBeClient = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.7f * h, "Connect");

        tvPlay.setOnClickListener(makePlayClickListener());


        ImageView ivMain = new MainImageView(this, R.drawable.menu_planes);
        ImageView ivGame = new MainImageView(this, R.drawable.manu_game);
        TextView NEWGAME = new MyTextView(this, scale * BmpConfig.menuHeaderH, 0.046f * h, "NEW GAME");
        TextView selectPlane = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.32f * h, "Select plane");
        TextView selectMap = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.49f * h, "Select map");
        tvWait = new MyTextView(this, scale * MenuConfig.FONT_SIZE_SMALL, 0.728f * h, "Waiting for " +
                "another\nplayer to connect");
        tvPlayServer = new MyTextView(this, MenuConfig.FONT_SIZE_BIG * scale, 0.728f*h, "Play");
        tvPlayServer.setVisibility(View.GONE);
        tvReady = new MyTextView(this, MenuConfig.FONT_SIZE_BIG * scale, 0.728f * h, "");
        tvResultTitle = new MyTextView(this, scale * MenuConfig.FONT_SIZE_BIG, 0.046f * h, "");
        tvOK = new MyTextView(this, MenuConfig.FONT_SIZE_BIG * scale, 0.728f*h, "OK");
        tvResultScore = new MyTextView(this, scale * MenuConfig.FONT_SIZE_MID, 0.32f * h, "");


        NEWGAME.setTextColor(Color.WHITE);
        tvResultTitle.setTextColor(Color.WHITE);

        mainScreen.add(ivMain).add(tvPlay).add(tvChangeName);
        modeScreen.add(ivMain).add(tvBeClient).add(tvBeServer);
        serverScreen.add(ivGame).add(NEWGAME).add(selectPlane).add(selectMap).add(tvWait).add(tvPlayServer);
        clientScreen.add(ivGame).add(NEWGAME).add(selectPlane).add(selectMap).add(tvReady);
        victoryScreen.add(ivGame).add(tvResultTitle).add(tvResultScore).add(tvOK);
        defeatScreen.add(ivGame).add(tvResultTitle).add(tvResultScore).add(tvOK);


        tvBeServer.setOnClickListener(makeBeServerListener());
        tvBeClient.setOnClickListener(makeBeClientListener());
        tvReady.setOnClickListener(makeReadyListener());
        tvPlayServer.setOnClickListener(makePlayServerListener());
        tvOK.setOnClickListener(makeOKListener());

        Button playBT = (Button) findViewById(R.id.noBtBtn);
        playBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWithNoBT();

            }
        });
    }

    private View.OnClickListener makeOKListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state != State.SERVER_RESULTS && state == State.CLIENT_RESULTS) {
                    throw new IllegalStateException(String.valueOf(state));
                } else {
                    emptyStack();
                    goToScreen(mainScreen);
                }
            }
        };
    }

    private View.OnClickListener makePlayServerListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state != State.SERVER_READY) throw new RuntimeException();

                startAsServerWith(them);
                state = State.SERVER_GAME;
            }
        };
    }

    private View.OnClickListener makeReadyListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(state) {
                    case CLIENT_NOT_READY:
                        state = State.CLIENT_READY;
                        tvReady.setText("Ready");
                        them.sendMessage(new ReadyMessage(true));
                        break;
                    case CLIENT_READY:
                        state = State.CLIENT_NOT_READY;
                        tvReady.setText("Not ready");
                        them.sendMessage(new ReadyMessage(false));
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
                ///state = State.CLIENT_CONNECTING;
                startAsClient();
            }
        };
    }

    @Override
    public void onMessage(ReceivedMessage rmsg) {
        if(rmsg.getMessage().getType() == Message.START_GAME_MESSAGE) {
            if(state != State.CLIENT_READY) throw new IllegalStateException();
            state = State.CLIENT_GAME;

        }
        super.onMessage(rmsg);
        if(rmsg.getMessage().getType() == Message.READY_MESSAGE) {
            ReadyMessage msg = (ReadyMessage) rmsg.getMessage();
            onReadyMessage(msg.getReady());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDefeat(final String scoreString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResultTitle.setText("DEFEAT");
                tvResultScore.setText(scoreString);
                goToScreen(defeatScreen);
                if(state == State.CLIENT_GAME) {
                    state = State.CLIENT_RESULTS;
                } else if (state == State.SERVER_GAME) {
                    state = State.SERVER_RESULTS;
                } else throw new IllegalStateException();
            }
        });
    }

    @Override
    protected void onVictory(final String scoreString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResultTitle.setText("VICTORY");
                tvResultScore.setText(scoreString);
                goToScreen(victoryScreen);
                if(state == State.CLIENT_GAME) {
                    state = State.CLIENT_RESULTS;
                } else if (state == State.SERVER_GAME) {
                    state = State.SERVER_RESULTS;
                } else throw new IllegalStateException();
            }
        });
    }

    @Override
    protected void onTie(final String scoreString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResultTitle.setText("TIE");
                tvResultScore.setText(scoreString);
                goToScreen(resultScreen);
                if(state == State.CLIENT_GAME) {
                    state = State.CLIENT_RESULTS;
                } else if (state == State.SERVER_GAME) {
                    state = State.SERVER_RESULTS;
                } else throw new IllegalStateException();
            }
        });
    }

    @Override
    protected void onLeftGame() {
        state = State.NONE;
        emptyStack();
        goToScreen(mainScreen);
    }



    private View.OnClickListener makeBeServerListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state != State.NONE) throw new RuntimeException();
                state = State.SERVER_WAITING;
                tvWait.setText("Waiting for another\nplayer to connect");
                tvPlayServer.setVisibility(View.GONE);
                tvWait.setVisibility(View.VISIBLE);
                goToScreen(serverScreen);
                startAsServer();
            }
        };
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
        if (them != null) them.disconnect();
        state = State.NONE;
        super.onBackPressed();
    }

    @Override
    public void onAccepted(final RemoteAbonent abonent) {
        super.onAccepted(abonent);
        Log.d("hey", "menu onAccepted");
        startRoomAsServerWith(abonent);
    }

    private void startRoomAsServerWith(final RemoteAbonent abonent) {
        if(state != State.SERVER_WAITING && state != State.SERVER_RESULTS) throw new RuntimeException();
        //goToScreen(serverScreen);
        state = State.SERVER_NOT_READY;
        tvWait.post(new Runnable() {
            @Override
            public void run() {
                tvWait.setText(Helper.cutString(abonent.getDevice().getName(), 8) + "\nis not ready");
            }
        });
        them = abonent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destruct();
    }


    @Override
    public void onConnectedFromBTList(RemoteAbonent abonent) {
        super.onConnectedFromBTList(abonent);
        startRoomAsClientWith(abonent);
    }

    private void startRoomAsClientWith(RemoteAbonent abonent) {
        them = abonent;
        goToScreen(clientScreen);
        if (state != State.NONE && state != State.CLIENT_RESULTS) throw new RuntimeException();
        state = State.CLIENT_NOT_READY;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvReady.setText("Not ready");
            }
        });
    }
}
