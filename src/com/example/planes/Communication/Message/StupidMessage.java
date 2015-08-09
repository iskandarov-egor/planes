package com.example.planes.Communication.Message;

import com.example.planes.Communication.Message.Message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by egor on 23.07.15.
 */
public class StupidMessage extends Message {
    private String string;
    private static String utf = "UTF-8";

    public StupidMessage(String string) {
        super(STUPID_MESSAGE, string.getBytes().length);
        byte[] bytes = null;
        try {
            bytes = string.getBytes(utf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        contents.put(bytes);
    }

    public StupidMessage(ByteBuffer bb) {
        super(STUPID_MESSAGE, bb.array().length - bb.position());
        try {
            string = new String(bb.array(), bb.position(), bb.array().length - bb.position(), utf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        contents.put(string.getBytes());
    }

    public String getString() {
        return string;
    }

}
