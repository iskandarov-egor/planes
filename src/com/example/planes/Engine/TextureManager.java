package com.example.planes.Engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by egor on 18.07.15.
 */
public class TextureManager {
    private static Map<Integer, Integer> map = new HashMap<>();
    public static int getTexture(int fileId) {
        if(map.containsKey(fileId)) return map.get(fileId);

        int id = GLHelper.createTexture(fileId);
        map.put(fileId, id);
        return id;
    }
}
