package com.example.planes.Config;

import android.graphics.Color;
import android.graphics.Typeface;
import com.example.planes.Activities.MyApplication;

/**
 * Created by egor on 20.08.15.
 */
public class MenuConfig {
    public static final int SKY_COLOR = Color.parseColor("#ffabd5fb");
    public static int DARK_BROWN = Color.parseColor("#423e2c");
    public static Typeface MAIN_FONT = Typeface.createFromAsset(MyApplication.get().getAssets(), "fonts/expressway rg.ttf");
}
