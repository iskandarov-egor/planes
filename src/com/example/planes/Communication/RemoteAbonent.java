package com.example.planes.Communication;

import android.bluetooth.BluetoothSocket;
import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.Message.ReceivedMessage;

/**
 * Created by egor on 23.07.15.
 */
public class RemoteAbonent {
   // private final Connector connector;
    //private BluetoothDevice device;
    private BluetoothSocket socket;
    private ConnectedThread thread;
    private MessageListener listener;

    public RemoteAbonent(BluetoothSocket socket) {
        this.socket = socket;

        thread = new ConnectedThread(socket, this);
        thread.start();
    }

    public void sendMessage(Message msg) {
        thread.write(msg.getBytes());
    }

    public void onMessage(byte[] buffer, int bytes) {
        if(listener != null) {
            Message message = Message.getMessage(buffer, bytes);
            listener.onMessage(new ReceivedMessage(message, this));
        }
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    private final RemoteAbonent that = this;

    public void onDestruct() {
        if(thread == null) throw new RuntimeException("что то не так");
        thread.cancel();
        thread = null;
    }

}
