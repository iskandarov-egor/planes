package com.example.planes.Communication;

import java.nio.ByteBuffer;

/**
 * Created by egor on 23.07.15.
 */
public abstract class Message {
    public static final int TYPE_STUPID_MESSAGE = 0;
    protected int type;
    protected byte[] bytes;


    public byte[] getBytes() {
        return bytes;
    }

    public static Message getMessage(byte[] buffet, int bytes) {
        ByteBuffer bb = ByteBuffer.wrap(buffet);
        int type = bb.getInt();
        if(type == TYPE_STUPID_MESSAGE) return new StupidMessage(buffet, 4, bytes - 4);
        else return null;
    }

    public int getType() {
        return type;
    }
}
