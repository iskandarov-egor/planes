package com.example.planes.Engine;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by egor on 18.07.15.
 */
public class TextureManager {
    private static Map<Integer, Integer> intMap = new HashMap<>();
    private static Map<Bitmap, Integer> bmpMap = new HashMap<>();
    public static int getTexture(int fileId) {
        if(intMap.containsKey(fileId)) return intMap.get(fileId);

        int id = GLHelper.createTexture(fileId);
        intMap.put(fileId, id);
        return id;
    }

    public static int getTexture(Bitmap bitmap) {
        if(bmpMap.containsKey(bitmap)) return bmpMap.get(bitmap);

        int id = GLHelper.createTexture(bitmap);
        bmpMap.put(bitmap, id);
        return id;
    }
}
