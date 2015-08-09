package com.example.planes;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.planes.Communication.*;
import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.Message.ReceivedMessage;
import com.example.planes.Communication.Message.StartGameMessage;
import com.example.planes.Communication.Message.StupidMessage;
import com.example.planes.Config.GameConfig;
import com.example.planes.Game.Game;
import com.example.planes.Communication.MessageListener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by egor on 05.08.15.
 */
public class TestBTMenuActivity extends Activity implements ConnectorListener, MessageListener {
    TestBTMenuActivity that = this;
    final Connector connector = new Connector(this);
    Button noBtBtn;
    Button beServBtn;
    Button connBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("hey", "testBTactivity oncreate");
        setContentView(R.layout.test_bt);
        noBtBtn = (Button) findViewById(R.id.no_bt_btn);
        noBtBtn.setOnClickListener(noBtListener);
        beServBtn = (Button) findViewById(R.id.be_serv_btn);
        beServBtn.setOnClickListener(beServListener);
        connBtn = (Button) findViewById(R.id.conn_btn);
        connBtn.setOnClickListener(connListener);
    }

    final View.OnClickListener connListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("hey", "conn button clicked");
            doWithBT(new Runnable() {
                @Override
                public void run() {
                    connector.startSearching();
                }
            }, new Runnable() {
                @Override
                public void run() {
                    connBtn.setText("fuck you too");
                }
            });
        }
    };

    final View.OnClickListener noBtListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GameConfig.type = GameConfig.TYPE_NO_BT;
            Log.d("hey", "testBT button clicked");
            Intent intent = new Intent(that, MyActivity.class);
            Log.d("hey", "testBT button - intent");
            startActivity(intent);
            Log.d("hey", "testBT button - start");
        }
    };

    final View.OnClickListener beServListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("hey", "beServ clicked");

            doWithBT(new Runnable() {
                         @Override
                         public void run() {
                             connector.startAccepting();
                             beServBtn.setText("serving");
                         }
                     },new Runnable() {
                        @Override
                        public void run() {
                            beServBtn.setText("oh come on man!");
                        }
                    });
        }
    };

    private Queue<Runnable> btRunnables = new ArrayDeque<>(4);
    private static final int REQUEST_ENABLE_BT = 2147048222;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT) {
            if(resultCode == RESULT_OK) {
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("hey", "testBTactivity onconfigchanged");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("hey", "testBTactivity onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("hey", "testBTactivity onpause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("hey", "testBTactivity onstart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("hey", "testBTactivity onrestorestate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("hey", "testBTactivity onrestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("hey", "testBTactivity onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("hey", "testBTactivity ondestroy");
        connector.destruct();
    }

    @Override
    public void onAccepted(RemoteAbonent abonent) {
        Log.d("hey", "onaccepted");
        connector.stopAcceptingSafe();
        abonent.sendMessage(new StartGameMessage(1));
        startGame(abonent, 0);
    }

    @Override
    public void onMessage(final ReceivedMessage rmsg) {
        final Message msg = rmsg.getMessage();
        RemoteAbonent abonent = rmsg.getSender();
        if(msg.getType() == Message.STUPID_MESSAGE) {
            beServBtn.post(new Runnable() {
                @Override
                public void run() {
                    beServBtn.setText(((StupidMessage)msg).getString());
                }
            });
            if(((StupidMessage)msg).getString().equals("you are a nigger")) {
                abonent.sendMessage(new StupidMessage("no, you are"));
            }
        }
        if(msg.getType() == Message.START_GAME_MESSAGE) {
            startGame(abonent, ((StartGameMessage) msg).getYourId());
        }
    }


    private void startGame(RemoteAbonent abonent, int myId) {
        GameConfig.type = GameConfig.TYPE_BT;


        ArrayList<RemoteAbonent> them = new ArrayList<>(1);
        them.add(abonent);
        Game.NewGame(them, myId);

        Intent intent = new Intent(that, MyActivity.class);

        startActivity(intent);
    }

    @Override
    public void onFound(BluetoothDevice device) {
        Log.d("hey", "onFound bt device");
        String name = device.getName();
            if(name.equals( "planesbt")) {
                Log.d("hey", "onFound bt device name matched");
                connector.connectTo(device);
            }
    }

    @Override
    public void onConnected(RemoteAbonent abonent) {
        Log.d("hey", "on connected");
        abonent.setListener(this);
        abonent.sendMessage(new StupidMessage("you are a nigger"));
    }

    @Override
    public void onConnectFailed() {
        Log.d("hey", "on conn failed");
    }


}
