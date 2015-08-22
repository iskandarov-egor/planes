package com.example.planes.Interface;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.TextView;
import com.example.planes.R;

/**
 * Created by egor on 22.08.15.
 */
public class SelectOpponentActivity extends Activity {
    TextView tv;
    public static SelectOpponentActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(instance != null) throw new RuntimeException();
        instance = this;

        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Looking for devices...");

    }

    public void onFound(BluetoothDevice device) {

    }


}
