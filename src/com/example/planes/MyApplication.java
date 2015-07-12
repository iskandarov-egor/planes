package com.example.planes;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Sprite.Square;
import com.example.planes.Engine.Sprite.Triangle;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 10.07.15.
 */
public class MyApplication extends Application {


    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // TODO Put your application initialization code here.

    }



}