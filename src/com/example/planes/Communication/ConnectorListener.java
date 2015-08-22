package com.example.planes.Communication;

import android.bluetooth.BluetoothDevice;
import com.example.planes.Communication.Message.Message;

/**
 * Created by egor on 24.07.15.
 */
public interface ConnectorListener {
    void onAccepted(RemoteAbonent abonent);
    void onFound(BluetoothDevice device);
    void onConnected(RemoteAbonent abonent);

    void onConnectFailed();

    void onFinishedDiscovery();
}