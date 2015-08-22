package com.example.planes.Interface;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.planes.Communication.Connector;
import com.example.planes.Communication.ConnectorListener;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.R;
import com.example.planes.Utils.GameStarter;

import java.util.ArrayList;

/**
 * Created by egor on 22.08.15.
 */
public class SelectOpponentActivity extends Activity implements ConnectorListener{
    TextView tv;
    ArrayAdapter<Device> adapter;
    ArrayList<Device> list = new ArrayList<>();
    ListView lv;
    Button bRepeat;
    //public static SelectOpponentActivity instance;
    public static OnCreateListener onCreateListener;
    public static GameStarter starter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(instance != null) throw new RuntimeException();
//        instance = this;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        setContentView(R.layout.bt_list);

        lv = (ListView) findViewById(R.id.listView);
        tv = (TextView) findViewById(R.id.textView);
        notFound = (TextView) findViewById(R.id.tv_not_found);
        bRepeat = (Button) findViewById(R.id.search_repeat);
        tv.setText("Discovering devices...");
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(makeListItemListener());
        notFound.setVisibility(View.GONE);
        bRepeat.setVisibility(View.GONE);
        if(onCreateListener == null) throw new RuntimeException();


        bRepeat.setOnClickListener(makeRepeatListener());

        if(starter != null) throw new RuntimeException("case not implemented yet");
        onCreateListener.onCreate(this);
        if(starter == null) throw new RuntimeException("set starter in oncreate!");
    }

    private AdapterView.OnItemClickListener makeListItemListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                starter.getConnector().setListener(starter);
                starter.getConnector().connectTo(adapter.getItem(i).device);
            }
        };
    }

    private View.OnClickListener makeRepeatListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bRepeat.setVisibility(View.GONE);
                adapter.clear();
                starter.getConnector().startSearching();
                tv.setText("Discovering devices...");
                notFound.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onAccepted(RemoteAbonent abonent) {
        throw new RuntimeException();
    }

    public void onFound(BluetoothDevice device) {
        adapter.add(new Device(device, device.getName()));
    }

    @Override
    public void onConnected(RemoteAbonent abonent) {
        throw new RuntimeException();
    }

    @Override
    public void onConnectFailed() {
        throw new RuntimeException();
    }


    TextView notFound;
    public void onFinishedDiscovery() {
        tv.setText("Discovery finished.");

        if(adapter.isEmpty()) {
            notFound.setVisibility(View.VISIBLE);
        }
        bRepeat.setVisibility(View.VISIBLE);
    }

    public static interface OnCreateListener {
        void onCreate(SelectOpponentActivity act);
    }

    private static class Device {
        String name;
        BluetoothDevice device;

        public Device(BluetoothDevice device, String name) {
            this.device = device;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
