package com.example.planes;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.example.planes.Engine.Scene;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Scene scene;
    Game game;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hey", "activity oncreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        game = new Game();

        scene = game.getScene();
        setContentView(scene.getView(this));


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("hey", "activity onconfigchanged");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("hey", "activity onresume");
        scene.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("hey", "activity onpause");
        scene.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("hey", "activity onstart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("hey", "activity onrestorestate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("hey", "activity onrestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("hey", "activity onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("hey", "activity ondestroy");
    }
}
