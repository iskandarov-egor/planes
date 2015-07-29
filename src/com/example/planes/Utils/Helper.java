package com.example.planes.Utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;
import com.example.planes.MyApplication;

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

    public static float getScreenRatio() {
        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        return (float)size.x / size.y;
    }

    public static int getScreenHeight(){
        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        return size.y;
    }

    public static Point getDrawableWH(int id) {
        Drawable d = MyApplication.getContext().getResources().getDrawable(id);
        return new Point(d.getIntrinsicWidth(), d.getIntrinsicHeight());
    }
}
