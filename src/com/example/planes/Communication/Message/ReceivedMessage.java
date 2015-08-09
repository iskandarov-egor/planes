package com.example.planes.Communication.Message;

import com.example.planes.Communication.RemoteAbonent;

/**
 * Created by egor on 06.08.15.
 */
public class ReceivedMessage {
    Message msg;
    RemoteAbonent sender;

    public ReceivedMessage(Message msg, RemoteAbonent sender) {
        this.msg = msg;
        this.sender = sender;
    }

    public Message getMessage() {
        return msg;
    }

    public RemoteAbonent getSender() {
        return sender;
    }
}
