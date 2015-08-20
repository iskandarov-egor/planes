package com.example.planes.Activities;

import android.app.Application;
import android.content.Context;

/**
 * Created by egor on 10.07.15.
 */
public class MyApplication extends Application {
    private static MyApplication meApp;

    @Override
    public void onCreate() {
        super.onCreate();
        meApp = this;
        // TODO Put your application initialization code here.
    }

    public static MyApplication get() {
        return meApp;
    }

    public static Context getContext() {
        return meApp.getApplicationContext();
    }
}