package com.example.planes.Communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private final Connector connector;
 
    public AcceptThread(Connector conn) {
        connector = conn;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            tmp = adapter.listenUsingRfcommWithServiceRecord("arbitrary", Connector.uuid);
        } catch (IOException e) {
            Log.d("error", Log.getStackTraceString(e));
        }
        mmServerSocket = tmp;
    }
 
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                if(!cancelled) Log.d("error", Log.getStackTraceString(e));
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                connector.onAccepted(socket);
                // Do work to manage the connection (in a separate thread)
//                try {
//                    mmServerSocket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //break;
            }
        }
    }

    private boolean cancelled = false;
    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            cancelled = true;
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}