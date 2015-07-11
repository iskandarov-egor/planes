package com.example.planes.Utils;

/**
 * Created by egor on 02.07.15.
 */
public class Helper {
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
