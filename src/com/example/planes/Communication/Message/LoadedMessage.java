package com.example.planes.Communication.Message;

import java.nio.ByteBuffer;

/**
 * Created by egor on 06.08.15.
 */

public class LoadedMessage extends Message {
    public LoadedMessage() {
        super(LOADED_MESSAGE, 0);
    }
}
