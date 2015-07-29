package com.example.planes.Communication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.util.Log;
import com.example.planes.MyApplication;

/**
 * Created by egor on 23.07.15.
 */
public class Connector {
    AcceptThread acceptThread;
    ConnectorListener listener;
    BluetoothAdapter adapter;
    ContextWrapper act;
    private BroadcastReceiver foundReceiver = null;

    public Connector(ConnectorListener listener) {
        this.listener = listener;
        act = MyApplication.get();
        adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null) {
            Log.d("whoops", "well shit");
        }
    }

    public void startAccepting() {
        if(isAccepting()) {
            //stopAccepting();
            throw new RuntimeException("already accepting");  //debug
        }
        if(isSearching()) {
            throw  new RuntimeException("was seaching"); //debug
        }

        assertBT();
        getAcceptThread().start();
    }

    private void assertBT() {
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) throw new RuntimeException("bt not enabled");
    }

    public void connectTo (BluetoothDevice device) {
        if(isAccepting()) {
            //stopAccepting();
            throw new RuntimeException("was accepting");  //debug
        }
        if(isSearching()) {
            throw  new RuntimeException("was seaching"); //debug
        }

        assertBT();
        ConnectThread connThread = new ConnectThread(device, this);
        connThread.start();
    }

    private boolean isSearching() {
        return BluetoothAdapter.getDefaultAdapter().isDiscovering();
    }

    public void startSearching() {  //todo app
        if(isAccepting()) {
            throw new RuntimeException("was accepting");
        }
        if(isSearching()) {
            throw  new RuntimeException("was seaching"); //debug
        }
        assertBT();


        BroadcastReceiver mReceiver = getFoundReceiver();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        act.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    private BroadcastReceiver getFoundReceiver() {
        if(foundReceiver == null) {
            foundReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        // Add the name and address to an array adapter to show in a ListView
                        listener.onFound(device);
                    }
                }
            };
        }
        return foundReceiver;
    }

    private AcceptThread getAcceptThread() {
        if(acceptThread == null) {
            acceptThread = new AcceptThread(this);
        }
        return acceptThread;
    }

    private boolean isAccepting() {
        if(acceptThread == null) return false;
        Thread.State state = acceptThread.getState();
        return state != Thread.State.NEW && state != Thread.State.TERMINATED;
    }

    public void stopAccepting() {
        acceptThread.cancel();
    }

    public void stopSearching() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        act.unregisterReceiver(getFoundReceiver());
    }

    public void onAccepted(BluetoothSocket socket) {
        RemoteAbonent abonent = new RemoteAbonent(socket, this);
        listener.onAccepted(abonent);
    }

    public void onConnected(BluetoothSocket socket) {
        RemoteAbonent abonent = new RemoteAbonent(socket, this);
        listener.onConnected(abonent);
    }

    public void onMessage(Message msg, RemoteAbonent remoteAbonent) {
        listener.onMessage(remoteAbonent, msg);
    }
}