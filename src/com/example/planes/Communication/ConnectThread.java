package com.example.planes.Communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final Connector connector;
    private final BluetoothDevice device;
    public ConnectThread(BluetoothDevice device, Connector conn) {
        this.device = device;
        connector = conn;
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("7ae01e41-3df2-4aa9-97bd-107081d1b773"));
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out

            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        connector.onConnected(mmSocket);
    }

    //private boolean cancelled = false;
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            //cancelled = true;
            mmSocket.close();
        } catch (IOException e) { }
    }
}