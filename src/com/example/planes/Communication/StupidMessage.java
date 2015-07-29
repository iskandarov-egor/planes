package com.example.planes.Communication;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by egor on 23.07.15.
 */
public class StupidMessage extends Message {
    private String string;
    private static String utf = "UTF-8";

    public StupidMessage(String string) {
        type = TYPE_STUPID_MESSAGE;
        byte[] bytes = new byte[0];
        try {
            bytes = string.getBytes(utf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteBuffer ruff = ByteBuffer.allocate(4 + bytes.length);
        ruff.putInt(TYPE_STUPID_MESSAGE);
        ruff.put(bytes);
        this.bytes = ruff.array();
    }

    public StupidMessage(byte[] bytes, int offset, int howmany) {
        try {
            string = new String(bytes, offset, howmany, utf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getString() {
        return string;
    }
}
