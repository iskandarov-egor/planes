package com.example.planes.Communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.util.Log;
import com.example.planes.Interface.MyApplication;
import com.example.planes.Interface.SelectOpponentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by egor on 23.07.15.
 */
public class Connector {
    public static final UUID uuid = UUID.fromString("7ae01e41-3df2-4aa9-97bd-107081d1b773");
    AcceptThread acceptThread;
    ConnectorListener listener;
    BluetoothAdapter adapter;
    ContextWrapper app;
    private BroadcastReceiver foundReceiver = getFoundReceiver();

    private List<RemoteAbonent> remotes = new ArrayList<>(1);

    public Connector(ConnectorListener listener) {
        this.listener = listener;
        app = MyApplication.get();
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
            stopSearching();
        }

        assertBT();
        getAcceptThread().start();
    }

    private void assertBT() {
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) throw new RuntimeException("bt not enabled");
    }

    public void connectTo (BluetoothDevice device) {
        if(isAccepting()) {
            stopAccepting();
        }
        if(isSearching()) {
            stopSearching();
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
            stopAccepting();
        }
        if(isSearching()) {
            throw  new RuntimeException("was seaching"); //debug
        }
        assertBT();



        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        app.registerReceiver(foundReceiver, filter); // Don't forget to unregister during onDestroy
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
                    if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                        listener.onFinishedDiscovery();
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

    public boolean isAccepting() {
        if(acceptThread == null) return false;
        Thread.State state = acceptThread.getState();
        return state != Thread.State.NEW && state != Thread.State.TERMINATED;
    }

    public void stopAcceptingSafe() {
        if(isAccepting()) {
            stopAccepting();
        }
    }

    public void stopSearchingSafe() {
        if(isSearching()) {
            stopSearching();
        }
    }

    public void stopAccepting() {
        acceptThread.cancel();
    }

    public void stopSearching() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        app.unregisterReceiver(getFoundReceiver());
    }

    public void onAccepted(BluetoothSocket socket) {
        RemoteAbonent abonent = new RemoteAbonent(socket);
        listener.onAccepted(abonent);
    }

    public void onConnected(BluetoothSocket socket) {
        RemoteAbonent abonent = new RemoteAbonent(socket);
        listener.onConnected(abonent);
    }

    public void onConnectFailed() {
        listener.onConnectFailed();
    }

    public void destruct() {
        stopAcceptingSafe();
        stopSearchingSafe();
        app.unregisterReceiver(foundReceiver);
    }

    public void setListener(ConnectorListener listener) {
        this.listener = listener;
    }
}