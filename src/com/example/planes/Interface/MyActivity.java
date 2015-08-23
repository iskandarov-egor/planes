package com.example.planes.Interface;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Engine;
import com.example.planes.Game.Round;

import java.util.ArrayList;

public class MyActivity extends LoggedActivity {
    public static boolean disconnectedBeforeCreation;
    /**
     * Called when the activity is first created.
     */

    private Engine engine;
    private int roundNumber = 0;
    private Round currentRound = null;
    private static int playerId;
    private static ArrayList<RemoteAbonent> otherPlayers;
    private static MyActivity activity = null;

    public static void NewGame(ArrayList<RemoteAbonent> otherPlayers, int playerId) {
        Log.d("hey", "static NewGame");
//        instance = new Game(playerId, otherPlayers);
        disconnectedBeforeCreation = false;
        MyActivity.playerId = playerId;
        MyActivity.otherPlayers = otherPlayers;

    }

    public MyActivity() {
        super("MyActivity");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        if(disconnectedBeforeCreation) onDisconnected();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.engine = new Engine(GameConfig.FPS, GameConfig.PHYSICS_FPS);


        setContentView(engine.createView(this));
        engine.run();
        newRound();

    }

    @Override
    protected void onResume() {
        super.onResume();
        engine.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        engine.onPause();
    }

    private void newRound() {
        Log.d("hey", "new round()");
        roundNumber++;
        currentRound = new Round(playerId, otherPlayers, roundNumber, this);

    }

    public void onRoundOver() {
        newRound();
    }

    public Engine getEngine() {
        return engine;
    }

    public static void onDisconnected() {
        if(activity == null) {
            MyActivity.disconnectedBeforeCreation = true;
        } else {
            activity.finish();
            DialogHelper.showOKDialog(activity, "", "Connection was lost");
        }
    }
}
