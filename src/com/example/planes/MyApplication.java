package com.example.planes;

import android.app.Application;
import android.content.Context;

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