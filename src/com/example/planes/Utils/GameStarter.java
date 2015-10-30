package com.example.planes.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.planes.Communication.Connector;
import com.example.planes.Communication.ConnectorListener;
import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.Message.ReadyMessage;
import com.example.planes.Communication.Message.ReceivedMessage;
import com.example.planes.Communication.Message.StartGameMessage;
import com.example.planes.Communication.MessageListener;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.GameConfig;
import com.example.planes.Interface.*;
import com.example.planes.R;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by egor on 21.08.15.
 */
public abstract class GameStarter extends LoggedActivity implements ConnectorListener, MessageListener {

    private Queue<Runnable> btRunnables = new ArrayDeque<>(4);
    private static final int REQUEST_ENABLE_BT = 2147048222;
    private int myId = -1;

    public GameStarter(String loggedName) {
        super(loggedName);
        //this.activity = activity;
        connector = new Connector(this);
    }

    private void doWithBT(Runnable r, Runnable ifCancelled) {
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            btRunnables.add(r);
            btRunnables.add(ifCancelled);
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            r.run();
        }
    }

    final Connector connector;
    final int REQUEST_OPPONENT = 4354665;
    ListBTActivity listBTActivity = null;
    GameStarter that = this;
    public void startAsClient() {
        doWithBT(new Runnable() {
            @Override
            public void run() {
                //if(listBTActivity != null) throw new RuntimeException();
                listBTActivity = null;
                onFinishedDiscovery = false;
                foundDevices.clear();
                ListBTActivity.starter = that;
//                ListBTActivity.onCreateListener = new ListBTActivity.OnCreateListener() {
//                    @Override
//                    public void onCreate(ListBTActivity act) {
//                        listBTActivity = act;
//                        connector.setListener(act);
//
//                        if(!foundDevices.isEmpty()) {
//                            for(BluetoothDevice device : foundDevices) {
//                                listBTActivity.onFound(device);
//                            }
//                        }
//                        if(onFinishedDiscovery) {
//                            listBTActivity.onFinishedDiscovery();
//                        }
//                    }
//                };
                themChosen = false;
                Intent i = new Intent(that, ListBTActivity.class);
                startActivityForResult(i, REQUEST_OPPONENT);
                connector.startSearching();
            }
        }, new Runnable() {
            @Override
            public void run() {
                // cancelled
            }
        });
    }

    public void startWithNoBT() {
        Log.d("hey", "testBT button clicked");
        ArrayList<RemoteAbonent> list = new ArrayList<RemoteAbonent>(0);
        MyActivity.NewGame(list, 0, MyActivity.Type.NO_BT);
        Intent intent = new Intent(this, MyActivity.class);
        Log.d("hey", "testBT button - intent");
        startActivity(intent);
        Log.d("hey", "testBT button - start");
    }

    public void startAsServer() {
        doWithBT(new Runnable() {
            @Override
            public void run() {
                connector.startAccepting();

            }
        },new Runnable() {
            @Override
            public void run() {
                //cancelled
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_OPPONENT) {
            connector.stopSearchingSafe();
        }
        if(requestCode == REQUEST_GAME) {
            int status = data.getIntExtra(MyActivity.GAME_END_STATUS, -1);
            if (status == MyActivity.END_STATUS_DISCONNECTED) {
                onDisconnected();
            } else if (status == MyActivity.END_STATUS_LEFT) {
                onLeftGame();
            } else {
                int firstWins = data.getIntExtra("0", -1);
                int secondWins = data.getIntExtra("1", -1);
                if (firstWins == -1 || secondWins == -1) throw new RuntimeException();
                int winnerId = (firstWins > secondWins) ? 0 : 1;
                String scoreString = String.valueOf(firstWins) + " / " + String.valueOf(secondWins);
                if (firstWins == secondWins) {
                    onTie(scoreString);
                } else {
                    if (myId == winnerId) onVictory(scoreString);
                    else onDefeat(scoreString);
                }
            }
        }
        if(requestCode == REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                Runnable r = btRunnables.poll();
                while (r != null) {
                    r.run();
                    if(btRunnables.poll() == null) throw new RuntimeException("must contain even number of elem");
                    r = btRunnables.poll();
                }
            } else {
                btRunnables.poll();
                Runnable r = btRunnables.poll();
                while (r != null) {
                    r.run();
                    Runnable t = btRunnables.poll();
                    r = btRunnables.poll();
                    if(r == null && t != null) throw new RuntimeException("must contain even number of elem");
                }
            }
        }
    }

    protected abstract void onDefeat(String scoreString);

    protected abstract void onVictory(String scoreString);

    protected abstract void onTie(String scoreString);

    protected abstract void onLeftGame();

    public void destruct() {
        connector.destruct();
    }

    @Override
    public void onAccepted(RemoteAbonent abonent) {
        Log.d("hey", "onaccepted");
        connector.stopAcceptingSafe();
        abonent.setListener(this);
    }

    @Override
    public void onMessage(final ReceivedMessage rmsg) {
        final Message msg = rmsg.getMessage();
        RemoteAbonent abonent = rmsg.getSender();
        switch (msg.getType()) {
            case Message.READY_MESSAGE:

                break;
            case Message.START_GAME_MESSAGE:
                startGame(abonent, ((StartGameMessage) msg).getYourId());
                break;
            case Message.LOADED_MESSAGE:
                Log.d("hey", "got loadedMessage in starter");
                MyActivity.opponentLoadedEarlier = true;
        }
    }

    @Override
    public  abstract  void onDisconnected();

    public void startAsServerWith(RemoteAbonent abonent) {
        abonent.sendMessage(new StartGameMessage(1));
        startGame(abonent, 0);
    }

    public static int REQUEST_GAME = 8473;

    private void startGame(RemoteAbonent abonent, int myId) {
        this.myId = myId;


        ArrayList<RemoteAbonent> them = new ArrayList<>(1);
        them.add(abonent);


        MyActivity.NewGame(them, myId, (myId == 0) ? MyActivity.Type.SERVER : MyActivity.Type.CLIENT);

        Intent intent = new Intent(this, MyActivity.class);

        startActivityForResult(intent, REQUEST_GAME);
    }

    ArrayList<BluetoothDevice> foundDevices = new ArrayList<>(0);

    @Override
    public void onFound(BluetoothDevice device) {
        Log.d("hey", "onFound bt device");
        if(listBTActivity == null) {
            Log.d("debug", "sel opp act was null");
            foundDevices.add(device);
        } else {
            //listBTActivity.onFound(device);
            throw new RuntimeException();
        }
    }

    @Override
    public void onConnected(RemoteAbonent abonent) {

    }

    @Override
    public void onConnectFailed() {
        Log.d("hey", "on conn failed");
    }

    boolean onFinishedDiscovery = false;

    @Override
    public void onFinishedDiscovery() {
        if(onFinishedDiscovery) throw new RuntimeException();
        onFinishedDiscovery = true;
        if(listBTActivity != null) {
            //listBTActivity.onFinishedDiscovery();
            throw new RuntimeException();
        }
    }

    public Connector getConnector() {
        return connector;
    }

    boolean themChosen = false;
    public void onConnectedFromBTList(RemoteAbonent abonent) {
        themChosen = true;
        abonent.setListener(this);
    }


}
