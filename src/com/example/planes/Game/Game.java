package com.example.planes.Game;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Engine;

import java.util.ArrayList;

/**
 * Created by egor on 16.08.15.
 */
public class Game {
    private static Game instance = null;
    private Engine engine;
    private int roundNumber = 0;
    private Round currentRound = null;
    private final int playerId;
    private final ArrayList<RemoteAbonent> otherPlayers;


    public Game(int playerId, ArrayList<RemoteAbonent> otherPlayers) {

        this.engine = new Engine(GameConfig.FPS, GameConfig.PHYSICS_FPS);
        this.playerId = playerId;
        this.otherPlayers = otherPlayers;
        newRound();
        //engine.run();
    }

    public static void NewGame(ArrayList<RemoteAbonent> otherPlayers, int playerId) {
        Log.d("hey", "static NewGame");
        instance = new Game(playerId, otherPlayers);

    }

    public static Game getInstance() {
        return instance;
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

    public View createView(Context context) {
        return engine.createView(context);
    }
}
