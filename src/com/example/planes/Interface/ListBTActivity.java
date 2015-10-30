package com.example.planes.Interface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.planes.Communication.ConnectorListener;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.R;
import com.example.planes.Utils.GameStarter;

import java.util.ArrayList;

/**
 * Created by egor on 22.08.15.
 */
public class ListBTActivity extends LoggedActivity implements ConnectorListener{
    TextView tv;
    ArrayAdapter<Device> adapter;
    ArrayList<Device> list = new ArrayList<>();
    ListView lv;
    Button bRepeat;
    //public static OnCreateListener onCreateListener;
    public static GameStarter starter = null;

    public ListBTActivity() {
        super("ListBT");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
//        if(onCreateListener == null) throw new RuntimeException();


        bRepeat.setOnClickListener(makeRepeatListener());

        //if(starter != null) throw new RuntimeException("case not implemented yet");
//        onCreateListener.onCreate(this);
        starter.getConnector().setListener(this);
        if(starter == null) throw new RuntimeException("set starter in oncreate!");
    }

    private ListBTActivity that = this;
    ProgressDialog progressDialog = null;
    private AdapterView.OnItemClickListener makeListItemListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //starter.getConnector().setListener(starter);
                starter.getConnector().stopSearchingSafe();
                starter.getConnector().connectTo(adapter.getItem(i).device);
                chosenName = adapter.getItem(i).name;
                progressDialog = DialogHelper.showProgressDialog(that, "", "Connecting, please wait...",
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                startDiscovery();
                            }
                        });
            }
        };
    }

    private void startDiscovery() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bRepeat.setVisibility(View.GONE);
                adapter.clear();
                starter.getConnector().startSearching();
                tv.setText("Discovering devices...");
                notFound.setVisibility(View.GONE);
            }
        });
    }

    private View.OnClickListener makeRepeatListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDiscovery();
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
        progressDialog.dismiss();
        starter.onConnectedFromBTList(abonent);
        finish();

    }

    String chosenName = null; //debug

    @Override
    public void onConnectFailed() {
        progressDialog.dismiss();
        DialogHelper.showOKDialog(this, "Connection failed", "Could not connect to " + chosenName);
        startDiscovery();
    }

    TextView notFound;
    public void onFinishedDiscovery() {
        if(adapter.isEmpty()) {
            tv.setText("No devices found.");
        } else {
            tv.setText("Choose an opponent.");
        }
        bRepeat.setVisibility(View.VISIBLE);
    }

    public static interface OnCreateListener {
        void onCreate(ListBTActivity act);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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