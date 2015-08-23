package com.example.planes.Communication;

import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.Message.ReceivedMessage;
import com.example.planes.Communication.RemoteAbonent;

/**
 * Created by egor on 06.08.15.
 */
public interface MessageListener {


    void onMessage(ReceivedMessage receivedMessage);

    void onDisconnected();
}
