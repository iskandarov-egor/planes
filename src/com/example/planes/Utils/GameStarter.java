package com.example.planes.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import com.example.planes.Communication.Connector;
import com.example.planes.Communication.ConnectorListener;
import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.Message.ReceivedMessage;
import com.example.planes.Communication.Message.StartGameMessage;
import com.example.planes.Communication.MessageListener;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.GameConfig;
import com.example.planes.Game.Game;
import com.example.planes.Interface.MenuActivity;
import com.example.planes.Interface.MyActivity;
import com.example.planes.Interface.ListBTActivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by egor on 21.08.15.
 */
public class GameStarter implements ConnectorListener, MessageListener {
    private final MenuActivity activity;

    private Queue<Runnable> btRunnables = new ArrayDeque<>(4);
    private static final int REQUEST_ENABLE_BT = 2147048222;

    public GameStarter(MenuActivity activity) {
        this.activity = activity;
        connector = new Connector(this);
    }

    private void doWithBT(Runnable r, Runnable ifCancelled) {
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            btRunnables.add(r);
            btRunnables.add(ifCancelled);
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            r.run();
        }
    }

    final Connector connector;
    final int REQUEST_OPPONENT = 4354665;
    ListBTActivity listBTActivity = null;
    GameStarter that = this;
    public void connectAsClient() {
        doWithBT(new Runnable() {
            @Override
            public void run() {
                //if(listBTActivity != null) throw new RuntimeException();
                listBTActivity = null;
                onFinishedDiscovery = false;
                foundDevices.clear();
                ListBTActivity.onCreateListener = new ListBTActivity.OnCreateListener() {
                    @Override
                    public void onCreate(ListBTActivity act) {
                        listBTActivity = act;
                        connector.setListener(act);
                        ListBTActivity.starter = that;
                        if(!foundDevices.isEmpty()) {
                            for(BluetoothDevice device : foundDevices) {
                                listBTActivity.onFound(device);
                            }
                        }
                        if(onFinishedDiscovery) {
                            listBTActivity.onFinishedDiscovery();
                        }
                    }
                };
                Intent i = new Intent(activity, ListBTActivity.class);
                activity.startActivityForResult(i, REQUEST_OPPONENT);
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
        GameConfig.type = GameConfig.TYPE_NO_BT;
        Log.d("hey", "testBT button clicked");
        ArrayList<RemoteAbonent> list = new ArrayList<RemoteAbonent>(0);
        Game.NewGame(list, 0);
        Intent intent = new Intent(activity, MyActivity.class);
        Log.d("hey", "testBT button - intent");
        activity.startActivity(intent);
        Log.d("hey", "testBT button - start");
    }

    public void startServer() {
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_OPPONENT) {

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

    public void destruct() {
        connector.destruct();
    }

    @Override
    public void onAccepted(RemoteAbonent abonent) {
        Log.d("hey", "onaccepted");
        connector.stopAcceptingSafe();
        activity.onAccepted(abonent);
        //abonent.sendMessage(new StartGameMessage(1));
        //startGame(abonent, 0);
    }

    @Override
    public void onMessage(final ReceivedMessage rmsg) {
        final Message msg = rmsg.getMessage();
        RemoteAbonent abonent = rmsg.getSender();

        if(msg.getType() == Message.START_GAME_MESSAGE) {
            startGame(abonent, ((StartGameMessage) msg).getYourId());
        }
    }


    private void startGame(RemoteAbonent abonent, int myId) {
        GameConfig.type = GameConfig.TYPE_BT;


        ArrayList<RemoteAbonent> them = new ArrayList<>(1);
        them.add(abonent);
        Game.NewGame(them, myId);

        Intent intent = new Intent(activity, MyActivity.class);

        activity.startActivity(intent);
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
//        String name = device.getName();
//        if(name.equals( "planesbt")) {
//            Log.d("hey", "onFound bt device name matched");
//            connector.connectTo(device);
//        }
    }

    @Override
    public void onConnected(RemoteAbonent abonent) {
        Log.d("hey", "on connected");
        abonent.setListener(this);
        activity.onConnected(abonent);
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

    public void onConnectedFromBTList(RemoteAbonent abonent) {
        activity.onConnectedFromBTList(abonent);
    }
}
