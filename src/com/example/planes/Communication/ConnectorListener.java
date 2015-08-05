package com.example.planes.Communication;

import android.bluetooth.BluetoothDevice;

/**
 * Created by egor on 24.07.15.
 */
public interface ConnectorListener {
    void onAccepted(RemoteAbonent abonent);
    void onMessage(RemoteAbonent abonent, Message msg);
    void onFound(BluetoothDevice device);
    void onConnected(RemoteAbonent abonent);

    void onConnectFailed();
}