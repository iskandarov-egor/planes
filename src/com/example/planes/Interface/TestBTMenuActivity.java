package com.example.planes.Interface;

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
import com.example.planes.R;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by egor on 05.08.15.
 */
public class TestBTMenuActivity extends Activity implements ConnectorListener, MessageListener {
    TestBTMenuActivity that = this;

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

        }
    };

    final View.OnClickListener noBtListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    final View.OnClickListener beServListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("hey", "beServ clicked");


        }
    };




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        //connector.destruct();
    }


    @Override
    public void onAccepted(RemoteAbonent abonent) {

    }

    @Override
    public void onFound(BluetoothDevice device) {

    }

    @Override
    public void onConnected(RemoteAbonent abonent) {

    }

    @Override
    public void onConnectFailed() {

    }

    @Override
    public void onFinishedDiscovery() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void onMessage(ReceivedMessage receivedMessage) {

    }
}
