package com.example.planes.Communication.Message;

import java.nio.ByteBuffer;

/**
 * Created by egor on 06.08.15.
 */

public class StartGameMessage extends Message {
    private final int yourId;

    public StartGameMessage(int yourId) {
        super(START_GAME_MESSAGE, 4);
        this.yourId = yourId;
        contents.putInt(yourId);

    }

    public StartGameMessage(ByteBuffer buffet) {
        this(buffet.getInt());
    }

    public int getYourId() {
        return yourId;
    }
}
