package com.example.planes.Interface;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by egor on 23.08.15.
 */
public class LoggedActivity extends Activity {
    final String name;

    public LoggedActivity(String name) {
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hey", name + " onCreate, saved state " + String.valueOf(savedInstanceState));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("hey", name + " onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("hey", name + " onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("hey", name + " onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("hey", name + " onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("hey", name + " onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("hey", name + " ondestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("hey", "activity onrestorestate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("hey", "activity onconfigchanged");
    }
}