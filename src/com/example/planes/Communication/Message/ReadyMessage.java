package com.example.planes.Communication.Message;

import java.nio.ByteBuffer;

/**
 * Created by egor on 06.08.15.
 */

public class ReadyMessage extends Message {


    private final EnumBoolean ready;

    private ReadyMessage(EnumBoolean ready) {
        super(READY_MESSAGE, 4);
        this.ready = ready;
        contents.putInt(ready.ordinal());
    }

    public ReadyMessage(boolean ready) {
        this((ready)?EnumBoolean.TRUE:EnumBoolean.FALSE);
    }

    public ReadyMessage(ByteBuffer buffet) throws ArrayIndexOutOfBoundsException //todo
    {
        this(EnumBoolean.values()[buffet.getInt()]);
    }

    public boolean getReady() {
        return (ready == EnumBoolean.TRUE)?true:false;
    }
}
