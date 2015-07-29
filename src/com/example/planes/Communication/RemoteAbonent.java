package com.example.planes.Communication;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

/**
 * Created by egor on 23.07.15.
 */
public class RemoteAbonent {
    private final Connector connector;
    //private BluetoothDevice device;
    private BluetoothSocket socket;
    private ConnectedThread thread;

    public RemoteAbonent(BluetoothSocket socket, Connector connector) {
        this.socket = socket;
        this.connector = connector;
        thread = new ConnectedThread(socket, this);
        thread.start();
    }

    public void sendMessage(Message msg) {
        thread.write(msg.getBytes());
    }

    public void onMessage(byte[] buffer, int bytes) {
        Message message = Message.getMessage(buffer, bytes);
        connector.onMessage(message, this);
    }

    private final RemoteAbonent that = this;

}
