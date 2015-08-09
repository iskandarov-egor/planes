package com.example.planes.Communication.Message;

import java.nio.ByteBuffer;

/**
 * Created by egor on 23.07.15.
 */
public abstract class Message {
    public static final int STUPID_MESSAGE = 0;
    public static final int TURN_MESSAGE = 1;
    public static final int START_GAME_MESSAGE = 2;
    protected final int type;
    protected ByteBuffer contents;
    private int yourId;

    Message(int type, int capacity) {
        this.type = type;
        contents = ByteBuffer.allocate(capacity + 4);
        contents.putInt(type);
    }


    public byte[] getBytes() {
        return contents.array();
    }

    public static Message getMessage(byte[] buffet, int bytes) {
        ByteBuffer bb = ByteBuffer.wrap(buffet, 0, bytes);
        int type = bb.getInt();

        switch(type) {
            case STUPID_MESSAGE:  return new StupidMessage(bb);
            case TURN_MESSAGE: return new TurnMessage(bb);
            case START_GAME_MESSAGE: return new StartGameMessage(bb);
        }
        throw new RuntimeException("bad msg type");
    }

    public int getType() {
        return type;
    }



}
