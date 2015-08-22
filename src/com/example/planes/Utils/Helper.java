package com.example.planes.Utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.example.planes.Interface.MyApplication;

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
        DisplayMetrics m = new DisplayMetrics();
        display.getMetrics(m);
        /////if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        float r =  ((float)m.widthPixels) / m.heightPixels;
        return (r > 1)?r:1f/r;
    }

    public static int getScreenHeight(){
        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        //Point size = new Point();
        //display.getSize(size);
        DisplayMetrics m = new DisplayMetrics();
        display.getMetrics(m);// todo
        return (m.heightPixels < m.widthPixels)?m.heightPixels:m.widthPixels;
    }

    public static Point getDrawableWH(int id) {
        Drawable d = MyApplication.getContext().getResources().getDrawable(id);
        return new Point(d.getIntrinsicWidth(), d.getIntrinsicHeight());
    }

    public static float getDrawableRatio(int id) {
        Point p = getDrawableWH(id);
        return (float)p.x / p.y;
    }

    public static void setY(View view, int height) {
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        param.topMargin = height;
        view.setLayoutParams(param);
    }

    public static int _getScreenWidth() {
        WindowManager wm = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        display.getMetrics(m);// todo
        return (m.heightPixels > m.widthPixels)?m.heightPixels:m.widthPixels;
    }

    static private int screenWidth = -1;
    public static int getScreenWidth() {
        if(screenWidth == -1) screenWidth = _getScreenWidth();
        return screenWidth;
    }

    public static String cutString(String str, int max) {
        int len = str.length();

        if(len > max) {
            String newstr = str.substring(0, max - 3) + "...";
            return newstr;
        }
        return str;
    }

}
