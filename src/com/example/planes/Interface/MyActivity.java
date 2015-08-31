package com.example.planes.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Engine;
import com.example.planes.Game.Models.Player;
import com.example.planes.Game.Round;

import java.util.ArrayList;

public class MyActivity extends LoggedActivity {
    public static boolean disconnectedBeforeCreation;
    public static boolean opponentLoadedEarlier;

    private Engine engine;
    private int roundNumber = 0;
    private Round currentRound = null;
    private static int playerId;
    private static ArrayList<Player> players = new ArrayList<>();
    private static MyActivity activity = null;
    public enum Type {
        NO_BT, CLIENT, SERVER, NOT_SET
    }

    public static Type type = Type.NOT_SET;


    public static void NewGame(ArrayList<RemoteAbonent> otherPlayers, int playerId, Type type) {
        Log.d("hey", "static NewGame");
        MyActivity.type = type;
        disconnectedBeforeCreation = false;
        opponentLoadedEarlier = false;
        MyActivity.playerId = playerId;
        players.clear();
        for(RemoteAbonent abonent : otherPlayers) {
            Player player = new Player(abonent);
            players.add(player);
        }
        players.add(playerId, new Player(null));
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
        boolean opponentReady = true;
        if(roundNumber == 1)  {
            opponentReady = opponentLoadedEarlier;
            Log.d("hey", "this is first round and opp loaded earlier");
        }
        currentRound = new Round(playerId, players, roundNumber, this, opponentReady);
    }

    public void onRoundOver() {
        if(roundNumber < GameConfig.ROUND_COUNT) {
            newRound();
        } else {
            Intent intent = new Intent();
            for(int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                intent.putExtra(String.valueOf(i), player.getWins());
            }

            setResult(RESULT_OK, intent);
            finish();
        }
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
